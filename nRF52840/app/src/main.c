/*
 * Copyright (c) 2021 Nordic Semiconductor ASA
 *
 * SPDX-License-Identifier: LicenseRef-Nordic-5-Clause
 */

#include <stdbool.h>
#include <stdint.h>
#include <nrfx_saadc.h>
#include <zephyr/kernel.h>
#include <nrfx_timer.h>

#include "my_gpio.h"
 
#define SAADC_CHANNEL_COUNT   1
#define SAADC_SAMPLE_INTERVAL_MS 250


#define VDD_CHANNEL 0
#define LIM_L 4000//2275  // 2.0V TODO define in terms of res,refV, gain
#define LIM_H INT16_MAX // No uper limit

static volatile bool is_ready = true;
static nrf_saadc_value_t samples[SAADC_CHANNEL_COUNT];
static nrfx_saadc_channel_t channels[SAADC_CHANNEL_COUNT] = {NRFX_SAADC_DEFAULT_CHANNEL_SE(NRF_SAADC_INPUT_VDD, VDD_CHANNEL)};

bool limflag = false;

 
static void event_handler(nrfx_saadc_evt_t const * p_event)
{
    if (p_event->type == NRFX_SAADC_EVT_DONE)
    {
        for(int i = 0; i < p_event->data.done.size; i++)
        {
            p_event->data.done.p_buffer[i];
        }
        set_led_on(LED_INT_GREEN);
    }
}

// void saadc_config(){
//     int err_code = nrfx_saadc_init(7);
    

// 	#if defined(__ZEPHYR__)
//         IRQ_DIRECT_CONNECT(NRFX_IRQ_NUMBER_GET(NRF_SAADC), IRQ_PRIO_LOWEST, nrfx_saadc_irq_handler, 0);
//     #endif

 
//     channels[0].channel_config.gain = NRF_SAADC_GAIN1_6;
    
    
//     err_code = nrfx_saadc_channels_config(channels, SAADC_CHANNEL_COUNT);
    

//     const nrfx_saadc_adv_config_t saadc_adv_config =  {.oversampling = NRF_SAADC_OVERSAMPLE_DISABLED,
//                                                        .burst = NRF_SAADC_BURST_DISABLED,
//                                                        .internal_timer_cc = 0,
//                                                        .start_on_end=true};

// 	err_code = nrfx_saadc_simple_mode_set((1<<0),
// 											NRF_SAADC_RESOLUTION_12BIT,
// 											NRF_SAADC_OVERSAMPLE_DISABLED,
// 											event_handler);

	
// 	err_code = nrfx_saadc_buffer_set(samples, SAADC_CHANNEL_COUNT);


// 	err_code = nrfx_saadc_mode_trigger();
// }

void my_timer_handler(struct k_timer *dummy)
{
    set_led_on(LED_INT_RGB_BLUE);
    int err_code = nrfx_saadc_mode_trigger();
}
 
int main(void)
{
    int err_code;

    err_code = leds_init();
    //saadc_config();
    err_code = nrfx_saadc_init(7);
    

	#if defined(__ZEPHYR__)
        IRQ_DIRECT_CONNECT(NRFX_IRQ_NUMBER_GET(NRF_SAADC), IRQ_PRIO_LOWEST, nrfx_saadc_irq_handler, 0);
    #endif

 
    channels[0].channel_config.gain = NRF_SAADC_GAIN1_6;
    
    
    err_code = nrfx_saadc_channels_config(channels, SAADC_CHANNEL_COUNT);
    

    // const nrfx_saadc_adv_config_t saadc_adv_config =  {.oversampling = NRF_SAADC_OVERSAMPLE_DISABLED,
    //                                                    .burst = NRF_SAADC_BURST_DISABLED,
    //                                                    .internal_timer_cc = 0,
    //                                                    .start_on_end=true};

    err_code = nrfx_saadc_simple_mode_set((1<<0),
											NRF_SAADC_RESOLUTION_12BIT,
											NRF_SAADC_OVERSAMPLE_DISABLED,
											event_handler);

	
	err_code = nrfx_saadc_buffer_set(samples, SAADC_CHANNEL_COUNT);

    struct k_timer my_timer;
    k_timer_init (&my_timer,my_timer_handler, NULL);
	k_timer_start(&my_timer, K_SECONDS(5), K_SECONDS(5));



	while(1){
        set_led_on(LED_INT_RGB_RED);
		k_sleep(K_MSEC(1000));
        set_led_off(LED_INT_RGB_RED);
        set_led_off(LED_INT_RGB_BLUE);
        set_led_off(LED_INT_GREEN);
        k_sleep(K_MSEC(1000));
	}
    
}


