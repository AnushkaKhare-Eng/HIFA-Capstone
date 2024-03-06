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

        is_ready = true;
    }
    if (p_event->type == NRFX_SAADC_EVT_READY)
    if (p_event->type == NRFX_SAADC_EVT_LIMIT)
    {
        // low battery light
        limflag = true;
    }
}

 
int main(void)
{
    int err_code;


    
    err_code = nrfx_saadc_init(7);
    

	#if defined(__ZEPHYR__)
        IRQ_DIRECT_CONNECT(NRFX_IRQ_NUMBER_GET(NRF_SAADC), IRQ_PRIO_LOWEST, nrfx_saadc_irq_handler, 0);
    #endif

 
    channels[0].channel_config.gain = NRF_SAADC_GAIN1_6;
    
    
    err_code = nrfx_saadc_channels_config(channels, SAADC_CHANNEL_COUNT);
    

    const nrfx_saadc_adv_config_t saadc_adv_config =  {.oversampling = NRF_SAADC_OVERSAMPLE_DISABLED,
                                                       .burst = NRF_SAADC_BURST_DISABLED,
                                                       .internal_timer_cc = 1,
                                                       .start_on_end=true};

	err_code = nrfx_saadc_advanced_mode_set((1<<0),
											NRF_SAADC_RESOLUTION_12BIT,
											&saadc_adv_config,
											event_handler);

    // this must be set after seting the mode, any change to mode will reset the limits
    // Only non-blocking conversions can be monitored -> 
    err_code = nrfx_saadc_limits_set(VDD_CHANNEL, LIM_L, LIM_H);

	
	err_code = nrfx_saadc_buffer_set(samples, SAADC_CHANNEL_COUNT);


	err_code = nrfx_saadc_mode_trigger();

   



	while(1){
		k_sleep(K_MSEC(100));
		if(is_ready){
			is_ready = false;
		}
	}
    
}


