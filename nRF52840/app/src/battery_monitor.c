#include <nrfx_saadc.h>
#include <zephyr/kernel.h>
#include "my_gpio.h"

#define SAADC_CHANNEL_COUNT   1

#define VDD_CHANNEL 0
#define LIM_L  3200 // 2275  // 2.0V TODO define in terms of res,refV, gain

static nrfx_saadc_channel_t channels[SAADC_CHANNEL_COUNT] = {NRFX_SAADC_DEFAULT_CHANNEL_SE(NRF_SAADC_INPUT_VDD, VDD_CHANNEL)};
static nrf_saadc_value_t samples[SAADC_CHANNEL_COUNT];


static void saadc_handler(nrfx_saadc_evt_t const * p_event)
{
    if (p_event->type == NRFX_SAADC_EVT_DONE)
    {
        if (p_event->data.done.p_buffer[0] < LIM_L)
        {
            set_led_on(LED_INT_RGB_RED);
        }
        else
        {
            set_led_off(LED_INT_RGB_RED);
        }
    }
}

void my_timer_handler(struct k_timer *dummy)
{
    int err_code = nrfx_saadc_mode_trigger();
}

int init_saadc(struct k_timer *reading_timer)
{
    int err_code;

    err_code = nrfx_saadc_init(7);
    

	#if defined(__ZEPHYR__)
        IRQ_DIRECT_CONNECT(NRFX_IRQ_NUMBER_GET(NRF_SAADC), IRQ_PRIO_LOWEST, nrfx_saadc_irq_handler, 0);
    #endif

 
    channels[0].channel_config.gain = NRF_SAADC_GAIN1_6;
    
    
    err_code = nrfx_saadc_channels_config(channels, SAADC_CHANNEL_COUNT);
    
    err_code = nrfx_saadc_simple_mode_set((1<<0),
											NRF_SAADC_RESOLUTION_12BIT,
											NRF_SAADC_OVERSAMPLE_DISABLED,
											saadc_handler);

	
	err_code = nrfx_saadc_buffer_set(samples, SAADC_CHANNEL_COUNT);

   
    k_timer_init (reading_timer,my_timer_handler, NULL);
	k_timer_start(reading_timer, K_SECONDS(5), K_SECONDS(5));
    
    return 0;
}

