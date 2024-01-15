/*
 * Copyright (c) 2021 Nordic Semiconductor ASA
 *
 * SPDX-License-Identifier: LicenseRef-Nordic-5-Clause
 */

/** @file
 *  @brief Nordic Distance Measurement sample
 */


#include <dk_buttons_and_leds.h>
#include <zephyr/kernel.h>


#define DEVICE_NAME             CONFIG_BT_DEVICE_NAME
#define DEVICE_NAME_LEN         (sizeof(DEVICE_NAME) - 1)

#define LED_A          DK_LED1
#define LED_B          DK_LED2
#define RUN_LED_BLINK_INTERVAL  1000 // delay between blinks in ms


int blinky_test(void)
{
	int err;
	uint32_t blink_status = 0;

	err = dk_leds_init();
	if (err) {
		return 0;
	}

	for (;;) {
		dk_set_led(LED_A, (blink_status++) % 2);
		dk_set_led(LED_B,(blink_status) % 2);
		k_sleep(K_MSEC(RUN_LED_BLINK_INTERVAL));
	}
}
