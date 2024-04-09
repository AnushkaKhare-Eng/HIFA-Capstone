#include "button_monitor.h"
#include "ble_manager.h"
#include "my_gpio.h"

int press_count = 0;
int press_start_time = 0;

char tp_msg[] = {'T','\0'};
char hp_msg[] = {'H','\0'};


void handler (uint32_t button_state, uint32_t has_changed) {
	int64_t current_time = k_uptime_get();

	if(has_changed & BTN_INT_MSK) { // on board button has changed states
		current_time = k_uptime_get();

		if(button_state & BTN_INT_MSK) { // pressed
			okayed_by_user = true;
			if (current_time - press_start_time > TRIPLE_PRESS_MAX_TIME) {
				press_count = 0;
			}
			++press_count;
			if(press_count == 1) {
				press_start_time = current_time;
			} else if (press_count == 3) {
				press_count = 0;
				if (current_time - press_start_time < TRIPLE_PRESS_MAX_TIME)
				k_sem_give(&triple_press);
			}
			
		} else { // released
			if (press_count == 1 && (current_time - press_start_time) > HOLD_PRESS_MIN_TIME && (current_time - press_start_time) < HOLD_PRESS_MAX_TIME){
				k_sem_give(&hold_press);
			}
		}
	} else if (has_changed & BTN_EXT_A_MSK) { // on board button has changed states
		current_time = k_uptime_get();

		if(button_state & BTN_EXT_A_MSK) { // pressed
			if (current_time - press_start_time > TRIPLE_PRESS_MAX_TIME) {
				press_count = 0;
			}
			++press_count;
			if(press_count == 1) {
				press_start_time = current_time;
			} else if (press_count == 3) {
				press_count = 0;
				if (current_time - press_start_time < TRIPLE_PRESS_MAX_TIME)
				k_sem_give(&triple_press);
			}
			
		} else { // released
			if (press_count == 1 && (current_time - press_start_time) > HOLD_PRESS_MIN_TIME && (current_time - press_start_time) < HOLD_PRESS_MAX_TIME){
				k_sem_give(&hold_press);
			}
		}
	}
}

void hold_press_monitor(void){
	for (;;) {
		k_sem_take(&hold_press, K_FOREVER);

		if (send_msg(hp_msg,sizeof(hp_msg))){
			success_lights();
		}
	}
}

void triple_press_monitor(void){
	for (;;) {
		k_sem_take(&triple_press, K_FOREVER);

		if(send_msg(tp_msg, sizeof(tp_msg))){
			success_lights();
		}
	}
}