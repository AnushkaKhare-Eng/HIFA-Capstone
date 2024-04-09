#ifndef BUTTON_MONITOR_H__
#define BUTTON_MONITOR_H__

#include <zephyr/kernel.h>
#include "my_gpio.h"

// in milliseconds
#define TRIPLE_PRESS_MAX_TIME 2000
#define HOLD_PRESS_MIN_TIME   3000
#define HOLD_PRESS_MAX_TIME   6000

static K_SEM_DEFINE(hold_press, 0, 1);
static K_SEM_DEFINE(triple_press, 0, 1);

extern bool okayed_by_user;

void handler (uint32_t button_state, uint32_t has_changed);
void hold_press_monitor(void);
void triple_press_monitor(void);

#endif // BUTTON_MONITOR_H__