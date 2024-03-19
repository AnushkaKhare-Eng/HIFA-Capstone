/*
 * Copyright (c) 2018 Nordic Semiconductor ASA
 *
 * SPDX-License-Identifier: LicenseRef-Nordic-5-Clause
 */

#ifndef MY_GPIO_H__
#define MY_GPIO_H__

#include <zephyr/types.h>
#include <zephyr/sys/slist.h>

// ------------------ \ LEDs / ------------------
#define LED_INT_GREEN       0
#define LED_INT_RGB_RED     1
#define LED_INT_RGB_GREEN   2
#define LED_INT_RGB_BLUE    3
#define LED_EXT_RED_A       4
#define LED_EXT_RED_B       5

#define LED_INT_GREEN_MSK          BIT(LED_INT_GREEN)
#define LED_INT_RGB_RED_MSK        BIT(LED_INT_RGB_RED)
#define LED_INT_RGB_GREEN_MSK      BIT(LED_INT_RGB_GREEN)
#define LED_INT_RGB_BLUE_MSK       BIT(LED_INT_RGB_BLUE)
#define LED_EXT_RED_A_MSK          BIT(LED_EXT_RED_A)
#define LED_EXT_RED_B_MSK          BIT(LED_EXT_RED_B)

#define NO_LEDS_MSK    (0)
#define ALL_LEDS_MSK   (LED_INT_GREEN_MSK |\
                        LED_INT_RGB_RED_MSK | LED_INT_RGB_GREEN_MSK | LED_INT_RGB_BLUE_MSK | \
                        LED_EXT_RED_A_MSK | LED_EXT_RED_B_MSK)
// ------------------ / LEDs \ ------------------

// ------------------ \ BTNs / ------------------
#define BTN_INT          0
#define BTN_EXT_A        1

#define BTN_INT_MSK      BIT(BTN_INT)
#define BTN_EXT_A_MSK    BIT(BTN_EXT_A)

#define NO_BTNS_MSK   (0)
#define ALL_BTNS_MSK  (BTN_INT_MSK | BTN_EXT_A_MSK)
// ------------------ / BTNs \ ------------------

/**
 * @typedef button_handler_t
 * @brief Callback that is executed when a button state change is detected.
 *
 * @param button_state Bitmask of button states.
 * @param has_changed Bitmask that shows which buttons have changed.
 */
typedef void (*button_handler_t)(uint32_t button_state, uint32_t has_changed);

/** Button handler list entry. */
struct btn_handler {
	button_handler_t cb; /**< Callback function. */
	sys_snode_t node; /**< Linked list node, for internal use. */
};

/** @brief Initialize the library to control the LEDs.
 *
 *  @retval 0           If the operation was successful.
 *                      Otherwise, a (negative) error code is returned.
 */
int leds_init(void);

/** @brief Initialize the library to read the button state.
 *
 *  @param  btn_handler Callback handler for button state changes.
 *
 *  @retval 0           If the operation was successful.
 *                      Otherwise, a (negative) error code is returned.
 */
int buttons_init(button_handler_t button_handler);

void check_error(int err_code);

/** @brief Add a dynamic button handler callback.
 *
 * In addition to the button handler function passed to
 * @ref dk_buttons_init, any number of button handlers can be added and removed
 * at runtime.
 *
 * @param[in] handler Handler structure. Must point to statically allocated
 * memory.
 */
void button_handler_add(struct btn_handler *handler);

/** @brief Remove a dynamic button handler callback.
 *
 * @param[in] handler Handler to remove.
 *
 * @retval 0 Successfully removed the handler.
 * @retval -ENOENT This button handler was not present.
 */
int button_handler_remove(struct btn_handler *handler);

/** @brief Read current button states.
 *
 *  @param button_state Bitmask of button states.
 *  @param has_changed Bitmask that shows which buttons have changed.
 */
void read_buttons(uint32_t *button_state, uint32_t *has_changed);

/** @brief Get current button state from internal variable.
 *
 *  @return Bitmask of button states.
 */
uint32_t retrieve_button_states(void);

/** @brief Set value of LED pins as specified in one bitmask.
 *
 *  @param  leds Bitmask that defines which LEDs to turn on and off.
 *
 *  @retval 0           If the operation was successful.
 *                      Otherwise, a (negative) error code is returned.
 */
int set_leds(uint32_t leds);


/** @brief Set value of LED pins as specified in two bitmasks.
 *
 *  @param  leds_on_mask  Bitmask that defines which LEDs to turn on.
 *                        If this bitmask overlaps with @p leds_off_mask,
 *                        @p leds_on_mask has priority.
 *
 *  @param  leds_off_mask Bitmask that defines which LEDs to turn off.
 *                        If this bitmask overlaps with @p leds_on_mask,
 *                        @p leds_on_mask has priority.
 *
 *  @retval 0           If the operation was successful.
 *                      Otherwise, a (negative) error code is returned.
 */
int set_leds_state(uint32_t leds_on_mask, uint32_t leds_off_mask);

/** @brief Set a single LED value.
 *
 *  This function turns a single LED on or off.
 *
 *  @param led_idx Index of the LED.
 *  @param val     Value for the LED: 1 - turn on, 0 - turn off
 *
 *  @retval 0           If the operation was successful.
 *                      Otherwise, a (negative) error code is returned.
 *
 *  @sa dk_set_led_on, dk_set_led_off
 */
int set_led(uint8_t led_idx, uint32_t val);

/** @brief Turn a single LED on.
 *
 *  @param led_idx Index of the LED.
 *
 *  @retval 0           If the operation was successful.
 *                      Otherwise, a (negative) error code is returned.
 */
int set_led_on(uint8_t led_idx);

/** @brief Turn a single LED off.
 *
 *  @param led_idx Index of the LED.
 *
 *  @retval 0           If the operation was successful.
 *                      Otherwise, a (negative) error code is returned.
 */
int set_led_off(uint8_t led_idx);



#endif /* MY_GPIO_H__ */
