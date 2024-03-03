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
	
	err = buttons_init(handler);
	if (err) {
		printk("Buttons init failed (err %d)\n", err);
		return 1;
	}

	printk("Setupcomplete\n");
	set_led_on(LED_INT_RGB_RED);
	
	while (1) {
		// Sleep is important to allow monitor threads to run
		k_sleep(K_MSEC(1000));
	}
}

K_THREAD_DEFINE(hpm, 1024, hold_press_monitor, NULL, NULL,
		NULL, 7, 0, 0);

K_THREAD_DEFINE(tpm, 1024, triple_press_monitor, NULL, NULL,
	NULL, 7, 0, 0);


