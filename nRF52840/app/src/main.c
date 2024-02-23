/*
 * Copyright (c) 2021 Nordic Semiconductor ASA
 *
 * SPDX-License-Identifier: LicenseRef-Nordic-5-Clause
 */

#include <zephyr/kernel.h>
#include <zephyr/sys/printk.h>

#include <zephyr/settings/settings.h>

#include <dk_buttons_and_leds.h>
#include <bluetooth/services/nus.h>

#include "ble_manager.h"

#define SINGLE_GREEN  DK_LED1

#define MULTI_RED     DK_LED2
#define MULTI_GREEN	  DK_LED3
#define MULTI_BLUE	  DK_LED4

int main(void)
{
	int err;
	printk("ND begining initialization procedures...\n");
	
	err = dk_leds_init();
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

	dk_set_led_on(MULTI_RED);

	

	printk("Connectable advertising started\n");
	dk_buttons_init(on_press_send);
	
	return 0;
}
