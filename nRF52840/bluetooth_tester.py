import asyncio
from bleak import BleakScanner
from bleak import BleakClient
from time import sleep

PERI_TX_UUID = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"
PERI_RX_UUID = "6e400002-b5a3-f393-e0a9-e50e24dcca9e"

def notification_handler(sender, data):
    # print received data
    print(f"Received: {data.decode()}")

async def list_client_services(client):
    await client.get_services()
    for s in client.services:
            print(s.description)
            for c in s.characteristics:
                print(f"\tDescription: {c.description}, UUID: {c.uuid}")
                # if (c.description ==  "PnP ID"):
                #     print(f"\t\tValue: {bytes(c.v)}")

async def main():
   
    print("Scanning for devices...",end=" ")
    devices = await BleakScanner.discover()
    dev = None
    for d in devices:
        if (d.name) == "WAPA":
             dev = d
    if not dev:
         print("Could not find WAPA device")
         return
    
    print(f"Attempting to connect to {dev}... ",end="")
    async with BleakClient(dev.address) as client:
        if not client.is_connected():
             print("Failed")
             raise Exception("Could not connect to client")
        print("Success")

        print("Getting services...",end="")
        await list_client_services(client)
        print("Got services")


        
        # Receiving from ND
        await client.start_notify(PERI_TX_UUID,notification_handler)
        await asyncio.sleep(60.0)
        await client.stop_notify(PERI_TX_UUID)

        ## Sending to ND
        # await asyncio.sleep(2.0)
        # await client.write_gatt_char(PERI_RX_UUID, b'Hello World!')
        # await asyncio.sleep(2.0)

        

asyncio.run(main())