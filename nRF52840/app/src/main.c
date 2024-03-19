#include "my_gpio.h"
#include "ble_manager.h"
#include "battery_monitor.h"
#include "button_monitor.h"

const int battery_check_period_sec = 3600;

int main(void)
{
	// Initialize GPIO
    leds_init();
	buttons_init(handler);

	// Initialize BLE
	setup_ble();

	// Initialize battery monitor
    struct k_timer my_timer;
    init_saadc(&my_timer,battery_check_period_sec);

	while(1){
		k_sleep(K_MSEC(1000));
        set_led_on(LED_INT_GREEN);
        k_sleep(K_MSEC(1000));
        set_led_off(LED_INT_GREEN);
	}    
}

// Handle detected sequences of button presses
K_THREAD_DEFINE(hpm, 1024, hold_press_monitor, NULL, NULL,
		NULL, 7, 0, 0);
K_THREAD_DEFINE(tpm, 1024, triple_press_monitor, NULL, NULL,
	NULL, 7, 0, 0);
