#include <zephyr/bluetooth/bluetooth.h>
#include <zephyr/bluetooth/conn.h>
#include <zephyr/bluetooth/uuid.h>
#include <bluetooth/services/nus.h>

#include "ble_manager.h"
#include "my_gpio.h"
#include "button_monitor.h"

#define NON_CONNECTABLE_ADV_IDX 0
#define CONNECTABLE_ADV_IDX     1

#define UART_BUF_SIZE 5

bool okayed_by_user = false;

struct bt_conn *most_recent_conn = NULL;

static void advertising_work_handle(struct k_work *work);

static K_WORK_DEFINE(advertising_work, advertising_work_handle);

static struct bt_le_ext_adv *ext_adv[CONFIG_BT_EXT_ADV_MAX_ADV_SET];

static const struct bt_le_adv_param *connectable_adv_param =
	BT_LE_ADV_PARAM(BT_LE_ADV_OPT_CONNECTABLE | BT_LE_ADV_OPT_USE_NAME | BT_LE_ADV_OPT_EXT_ADV | BT_LE_ADV_OPT_CODED,
			BT_GAP_ADV_FAST_INT_MIN_2, /* 100 ms */
			BT_GAP_ADV_FAST_INT_MAX_2, /* 150 ms */
			NULL);

static const struct bt_data connectable_data[] = {
	BT_DATA_BYTES(BT_DATA_FLAGS, BT_LE_AD_GENERAL | BT_LE_AD_NO_BREDR),
	BT_DATA_BYTES(BT_DATA_UUID16_ALL, BT_UUID_16_ENCODE(BT_UUID_DIS_VAL))
};

#define BT_UUID_MYID_SERV_VAL \
BT_UUID_128_ENCODE(0x6e4073a1, 0xb5a3, 0xf393, 0xe0a9, 0xe50e24dcca9e)
#define BT_UUID_MYID_SERV BT_UUID_DECLARE_128(BT_UUID_MYID_SERV_VAL)

#define BT_UUID_MYID_CHAR_VAL \
BT_UUID_128_ENCODE(0x6e4003a1, 0xb5a3, 0xf393, 0xe0a9, 0xe50e24dcca9e)
#define BT_UUID_MYID_CHAR BT_UUID_DECLARE_128(BT_UUID_MYID_CHAR_VAL)

uint64_t myid = 0x0000000000000000;

char state = 'U';

static void adv_connected_cb(struct bt_le_ext_adv *adv,
			     struct bt_le_ext_adv_connected_info *info)
{
	printk("Advertiser[%d] %p connected conn %p\n", bt_le_ext_adv_get_index(adv),
		adv, info->conn);
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

	printk("Connected %s\n", addr);

	set_led_on(LED_INT_RGB_BLUE);

	err = bt_conn_le_phy_update(conn, BT_CONN_LE_PHY_PARAM_CODED);
	if (err) {
		printk("Phy update request failed: %d",  err);
	}
	else {
		printk("Phy update request succeeded");
		set_led_on(LED_INT_RGB_GREEN);
	}

	state = 'C';
	most_recent_conn = conn;
	k_sem_give(&new_cnx);
}

static void disconnected(struct bt_conn *conn, uint8_t reason)
{
	set_led_off(LED_INT_RGB_BLUE);
	set_led_off(LED_INT_RGB_GREEN);
	char addr[BT_ADDR_LE_STR_LEN];

	bt_addr_le_to_str(bt_conn_get_dst(conn), addr, sizeof(addr));

	printk("Disconnected: %s (reason %u)\n", addr, reason);

	/* Process the disconnect logic in the workqueue so that
	 * the BLE stack is finished with the connection bookkeeping
	 * logic and advertising is possible.
	 */
	k_work_submit(&advertising_work);

	state = 'U';
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
	set_led(LED_INT_RGB_GREEN, 1);
}

static struct bt_nus_cb nus_cb = {
	.received = bt_receive_cb,
	.send_enabled = BT_NUS_SEND_STATUS_ENABLED,
};


static ssize_t read_vnd(struct bt_conn *conn, const struct bt_gatt_attr *attr,
   void *buf, uint16_t len, uint16_t offset){

 return bt_gatt_attr_read(conn, attr, buf, len, offset, &myid,
     8);
}

BT_GATT_SERVICE_DEFINE(myid_svc,
  BT_GATT_PRIMARY_SERVICE(BT_UUID_MYID_SERV),
  BT_GATT_CHARACTERISTIC(BT_UUID_MYID_CHAR,
			       BT_GATT_CHRC_BROADCAST | BT_GATT_CHRC_READ,
			       BT_GATT_PERM_READ,
				   read_vnd, NULL, NULL),
);


int setup_ble(void){
	int err = bt_enable(NULL); // enabling BT done synchronously
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

	// get device's unique id
	uint32_t a = NRF_FICR->DEVICEID[0];
	uint32_t b = NRF_FICR->DEVICEID[1] & 0x0000FFFF;
	myid = (((uint64_t) b) << 32) | a;

	err = connectable_adv_create();
	if (err) {
		printk("Failed to create connectable advertising (err %d)\n", err);
		return 1;
	}
	return 0;
}

// TODO - in future support reaching out to "strangers" if we cant connect to user 
bool send_msg(uint8_t *data,uint16_t len){
	int64_t beg_time = k_uptime_get();
	while (bt_nus_send(NULL, data, len)){
		if (k_uptime_get() - beg_time > 5000){
			printk("Failed to send message\n");
			return false;
		}
	}
	return true;
}	


void connection_request_monitor(void){
	for (;;) {
		k_sem_take(&new_cnx, K_FOREVER);

		int countdown = 10;
		okayed_by_user = false;
		while(!okayed_by_user){
			set_led_on(LED_INT_RGB_BLUE);
			k_sleep(K_MSEC(1000));
			set_led_off(LED_INT_RGB_BLUE);
			k_sleep(K_MSEC(1000));
			
			if (countdown-- == 0){
				bt_conn_disconnect(most_recent_conn, BT_HCI_ERR_REMOTE_USER_TERM_CONN);
				break;
			}
		}
	}
}

