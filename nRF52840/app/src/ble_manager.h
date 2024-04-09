#ifndef BLE_MANAGER_H__
#define BLE_MANAGER_H__
#include <zephyr/kernel.h>

static K_SEM_DEFINE(new_cnx, 0, 1);
void connection_request_monitor(void);

bool send_msg(uint8_t *data, uint16_t len);
int setup_ble(void);
#endif // BLE_MANAGER_H__s