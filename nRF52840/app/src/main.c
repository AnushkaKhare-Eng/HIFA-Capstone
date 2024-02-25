/*
 * Copyright (c) 2021 Nordic Semiconductor ASA
 *
 * SPDX-License-Identifier: LicenseRef-Nordic-5-Clause
 */

#include <zephyr/kernel.h>
#include <zephyr/sys/printk.h>
#include <zephyr/settings/settings.h>
#include "my_gpio.h"
#include "button_monitor.h"
#include "ble_manager.h"



int main(void)
{
	int err;
	printk("ND begining initialization procedures...\n");
	
	err = leds_init();
	if (err) {
		printk("LEDs init failed (err %d)\n", err);
		return 1;
	}
	printk("LEDs initialized\n");

	err = setup();
	if(err){
		// error msg printed in setup function
		return 1;
	}

	dk_set_led_on(LED_INT_RGB_RED);

	

	printk("Connectable advertising started\n");
	dk_buttons_init(on_press_send);
	
	err = buttons_init(handler);
	if (err) {
		printk("Buttons init failed (err %d)\n", err);
		return 1;
	}

	set_led_on(LED_INT_RGB_BLUE);

	set_led_on(LED_INT_GREEN);
	
	while (1) {
		k_sleep(K_MSEC(1000));
		set_led_off(LED_INT_GREEN);
		k_sleep(K_MSEC(1000));
		set_led_on(LED_INT_GREEN);
	}
}

K_THREAD_DEFINE(hpm, 1024, hold_press_monitor, NULL, NULL,
		NULL, 7, 0, 0);

K_THREAD_DEFINE(tpm, 1024, triple_press_monitor, NULL, NULL,
	NULL, 7, 0, 0);


