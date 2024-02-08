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

#define SINGLE_GREEN  LED_INT_GREEN

#define MULTI_RED     LED_INT_RGB_RED
#define MULTI_GREEN	  LED_INT_RGB_GREEN
#define MULTI_BLUE	  LED_INT_RGB_BLUE

#define EXT_RED_A     LED_EXT_RED_A
#define EXT_RED_B     LED_EXT_RED_B

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
	
	err = buttons_init(handler);
	if (err) {
		printk("Buttons init failed (err %d)\n", err);
		return 1;
	}

	set_led_on(MULTI_BLUE);

	set_led_on(SINGLE_GREEN);
	
	while (1) {
		k_sleep(K_MSEC(1000));
		set_led_off(SINGLE_GREEN);
		k_sleep(K_MSEC(1000));
		set_led_on(SINGLE_GREEN);
	}
}

K_THREAD_DEFINE(hpm, 1024, hold_press_monitor, NULL, NULL,
		NULL, 7, 0, 0);

K_THREAD_DEFINE(tpm, 1024, triple_press_monitor, NULL, NULL,
	NULL, 7, 0, 0);


