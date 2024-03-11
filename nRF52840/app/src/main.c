#include "my_gpio.h"
#include "battery_monitor.h"
  
int main(void)
{
    leds_init();

    struct k_timer my_timer;
    init_saadc(&my_timer);

	while(1){
		k_sleep(K_MSEC(1000));
        set_led_on(LED_INT_GREEN);
        k_sleep(K_MSEC(1000));
        set_led_off(LED_INT_GREEN);
	}    
}
