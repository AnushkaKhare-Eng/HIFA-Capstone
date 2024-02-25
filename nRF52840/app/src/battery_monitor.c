#include <nrfx_saadc.h>
#include <nrfx_log.h>
#include "my_gpio.h"

#define VDD_PIN 9
#define VDD_CHANNEL 0
#define LIM_L 3640  // 2.0V (by linear interpolation)
#define LIM_H INT16_MAX // No uper limit

#define SAMPLING_ITERATIONS 8

nrfx_saadc_channel_t m_single_channel = NRFX_SAADC_DEFAULT_CHANNEL_SE(VDD_PIN, VDD_CHANNEL);
 
static nrf_saadc_value_t m_samples_buffer[1];

static bool m_saadc_ready;

static void saadc_handler(nrfx_saadc_evt_t const * p_event)
{
    nrfx_err_t status;
    (void)status;

    uint16_t samples_number;

    switch (p_event->type)
    {
        case NRFX_SAADC_EVT_DONE:
            NRFX_LOG_INFO("SAADC event: DONE");

            samples_number = p_event->data.done.size;
            for (uint16_t i = 0; i < samples_number; i++)
            {
                NRFX_LOG_INFO("[Sample %d] value == %d", i, p_event->data.done.p_buffer[i]);
            }

            m_saadc_ready = true;
            break;

        case NRFX_SAADC_EVT_CALIBRATEDONE:
            NRFX_LOG_INFO("SAADC event: CALIBRATEDONE");
            status = nrfx_saadc_mode_trigger();
            NRFX_ASSERT(status == NRFX_SUCCESS);
            break;

        case NRFX_SAADC_EVT_LIMIT:
            NRFX_LOG_INFO("SAADC event: LIMIT");
            set_led_on(LED_INT_RGB_RED);
            break;

        default:
            break;
    }
}


int init_saadc(void)
{
    // Initialize SAADC driver
    nrfx_err_t status = nrfx_saadc_init(NRFX_SAADC_DEFAULT_CONFIG_IRQ_PRIORITY);
    NRFX_ASSERT(status == NRFX_SUCCESS);

    // calibrate
    status = nrfx_saadc_offset_calibrate(NULL);
    NRFX_ASSERT(status == NRFX_SUCCESS);

    // #if defined(__ZEPHYR__)
    //     IRQ_DIRECT_CONNECT(NRFX_IRQ_NUMBER_GET(NRF_SAADC), IRQ_PRIO_LOWEST, nrfx_saadc_irq_handler, 0);
    // #endif

    // configure channel
    uint32_t channels_mask = nrfx_saadc_channels_configured_get();
    status = nrfx_saadc_simple_mode_set(channels_mask,
                                        NRF_SAADC_RESOLUTION_8BIT,
                                        NRF_SAADC_OVERSAMPLE_DISABLED,
                                        saadc_handler);
    NRFX_ASSERT(status == NRFX_SUCCESS);

    nrfx_saadc_limits_set(VDD_CHANNEL, LIM_L, LIM_H);
    NRFX_ASSERT(status == NRFX_SUCCESS);

    m_single_channel.channel_config.gain = NRF_SAADC_GAIN1_6;
    status = nrfx_saadc_channel_config(&m_single_channel);
    NRFX_ASSERT(status == NRFX_SUCCESS);

    status = nrfx_saadc_buffer_set(m_samples_buffer, 1);
    NRFX_ASSERT(status == NRFX_SUCCESS);

    return 0;
}

