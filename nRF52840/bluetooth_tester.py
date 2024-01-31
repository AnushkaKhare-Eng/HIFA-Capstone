import asyncio
from bleak import BleakScanner
from bleak import BleakClient
from time import sleep


MODEL_NBR_UUID = "2A24"

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
    

    try:
        async with BleakClient(devices[int(user_resp)].address) as client:
            if client.is_connected():
                print("Successfully connected")
                sleep(5)
            else:
                print("Failed to connect")
    except Exception as error:
        print("Failed to connect with Exception:", type(error).__name__) # An exception occurred: division by zero


asyncio.run(main())