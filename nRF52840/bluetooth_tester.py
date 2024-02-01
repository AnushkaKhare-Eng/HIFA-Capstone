import asyncio
from bleak import BleakScanner
from bleak import BleakClient
from time import sleep

UUID = "0000180a-0000-1000-8000-00805f9b34fb"

def notification_handler(sender, data):
    """Simple notification handler which prints the data received."""
    print(data)
    print("hi")

async def list_client_services(client):
    await client.get_services()
    for s in client.services:
            print(s.description)
            for c in s.characteristics:
                print(f"\tDescription: {c.description}, UUID: {c.uuid}")

async def main():
   
    print("Scanning for devices...",end=" ")
    devices = await BleakScanner.discover()
    print("Found devices:")
    i = 0
    for d in devices:
        print(f"\t{i} - {d}")
        i += 1
    user_resp = input("Select device number to connect, or 'q' to quit: ")

    if ('q' in user_resp):
        return
    
    print(f"Attempting to connect to {devices[int(user_resp)]}")
    

    async with BleakClient(devices[int(user_resp)].address) as client:
        if not client.is_connected():
             raise Exception("Could not connect to client")
        
        await client.start_notify("6e400003-b5a3-f393-e0a9-e50e24dcca9e",notification_handler)
        await asyncio.sleep(60.0)
        await client.stop_notify("6e400003-b5a3-f393-e0a9-e50e24dcca9e")


asyncio.run(main())