#include <zephyr/bluetooth/bluetooth.h>
#include <zephyr/bluetooth/conn.h>
#include <zephyr/bluetooth/uuid.h>
#include <dk_buttons_and_leds.h>
#include <bluetooth/services/nus.h>

#include "ble_manager.h"

#define NON_CONNECTABLE_ADV_IDX 0
#define CONNECTABLE_ADV_IDX     1

#define UART_BUF_SIZE 8


#define SINGLE_GREEN  DK_LED1

#define MULTI_RED     DK_LED2
#define MULTI_GREEN	  DK_LED3
#define MULTI_BLUE	  DK_LED4

static void advertising_work_handle(struct k_work *work);

static K_WORK_DEFINE(advertising_work, advertising_work_handle);

static struct bt_le_ext_adv *ext_adv[CONFIG_BT_EXT_ADV_MAX_ADV_SET];

static const struct bt_le_adv_param *connectable_adv_param =
	BT_LE_ADV_PARAM(BT_LE_ADV_OPT_CONNECTABLE | BT_LE_ADV_OPT_USE_NAME,
			BT_GAP_ADV_FAST_INT_MIN_2, /* 100 ms */
			BT_GAP_ADV_FAST_INT_MAX_2, /* 150 ms */
			NULL);

static const struct bt_data connectable_data[] = {
	BT_DATA_BYTES(BT_DATA_FLAGS, BT_LE_AD_GENERAL | BT_LE_AD_NO_BREDR),
	BT_DATA_BYTES(BT_DATA_UUID16_ALL, BT_UUID_16_ENCODE(BT_UUID_DIS_VAL))
};

static void adv_connected_cb(struct bt_le_ext_adv *adv,
			     struct bt_le_ext_adv_connected_info *info)
{
	printk("Advertiser[%d] %p connected conn %p\n", bt_le_ext_adv_get_index(adv),
		adv, info->conn);
	dk_set_led_on(MULTI_BLUE);
}

static const struct bt_le_ext_adv_cb adv_cb = {
	.connected = adv_connected_cb
};

static void connectable_adv_start(void)
{
	int err;

	err = bt_le_ext_adv_start(ext_adv[CONNECTABLE_ADV_IDX], BT_LE_EXT_ADV_START_DEFAULT);
	if (err) {
		printk("Failed to start connectable advertising (err %d)\n", err);
	}
}

static void advertising_work_handle(struct k_work *work)
{
	connectable_adv_start();
}




static void connected(struct bt_conn *conn, uint8_t err)
{
	char addr[BT_ADDR_LE_STR_LEN];

	if (err) {
		printk("Connection failed (err %u)\n", err);
		return;
	}

	bt_addr_le_to_str(bt_conn_get_dst(conn), addr, sizeof(addr));

	// dk_set_led_on(CON_STATUS_LED);
	// dk_set_led_off(SEND_LED);

	printk("Connected %s\n", addr);
}

static void disconnected(struct bt_conn *conn, uint8_t reason)
{
	dk_set_led_off(MULTI_BLUE);
	char addr[BT_ADDR_LE_STR_LEN];

	bt_addr_le_to_str(bt_conn_get_dst(conn), addr, sizeof(addr));

	printk("Disconnected: %s (reason %u)\n", addr, reason);

	/* Process the disconnect logic in the workqueue so that
	 * the BLE stack is finished with the connection bookkeeping
	 * logic and advertising is possible.
	 */
	k_work_submit(&advertising_work);
}

BT_CONN_CB_DEFINE(conn_callbacks) = {
	.connected = connected,
	.disconnected = disconnected,
};

static int advertising_set_create(struct bt_le_ext_adv **adv,
				  const struct bt_le_adv_param *param,
				  const struct bt_data *ad, size_t ad_len)
{
	int err;
	struct bt_le_ext_adv *adv_set;

	err = bt_le_ext_adv_create(param, &adv_cb,
				   adv);
	if (err) {
		return err;
	}

	adv_set = *adv;

	printk("Created adv: %p\n", adv_set);

	err = bt_le_ext_adv_set_data(adv_set, ad, ad_len,
				     NULL, 0);
	if (err) {
		printk("Failed to set advertising data (err %d)\n", err);
		return err;
	}

	return bt_le_ext_adv_start(adv_set, BT_LE_EXT_ADV_START_DEFAULT);
}

// creates connectable advertisement
static int connectable_adv_create(void)
{
	int err;

	err = bt_set_name(CONFIG_BT_DEVICE_NAME);
	if (err) {
		printk("Failed to set device name (err %d)\n", err);
		return err;
	}

	err = advertising_set_create(&ext_adv[CONNECTABLE_ADV_IDX], connectable_adv_param,
				     connectable_data, ARRAY_SIZE(connectable_data));
	if (err) {
		printk("Failed to create a connectable advertising set (err %d)\n", err);
	}

	return err;
}

static void bt_receive_cb(struct bt_conn *conn, const uint8_t *const data,
			  uint16_t len)
{
	dk_set_led(SINGLE_GREEN, 1);
}

static struct bt_nus_cb nus_cb = {
	.received = bt_receive_cb,
};






int setup(void){
	int err = bt_enable(NULL); // NULL -> no callback to notify completion -> enabling BT done synchronously
	if (err) {
		printk("Bluetooth init failed (err %d)\n", err);
		return 1;
	}
	printk("Bluetooth initialized\n");

	err = bt_nus_init(&nus_cb);
	if (err) {
		printk("Failed to initialize UART service (err %d)\n", err);
		return 1;
	}
	printk("UART service initialized\n");

	err = connectable_adv_create();
	if (err) {
		printk("Failed to create connectable advertising (err %d)\n", err);
		return 1;
	}
}

void on_press_send(){
	dk_set_led_on(SINGLE_GREEN);
	uint8_t data[UART_BUF_SIZE];
	data[0] = 'h';
	data[1] = 'e';
	data[2] = 'l';
	data[3] = 'p';
	data[4] = '!';
	uint16_t len = UART_BUF_SIZE;

	while (bt_nus_send(NULL, data, len)){
	}
	dk_set_led_off(SINGLE_GREEN);
}	
