#ifndef _H_BATTERY_MONITOR__
#define _H_BATTERY_MONITOR__
#include <zephyr/kernel.h>

int init_saadc(struct k_timer *reading_timer);

#endif // _H_BATTERY_MONITOR__