#Jack Beneigh
#Curling Code
import machine
import uasyncio as asyncio  # Use uasyncio for non-blocking behavior
import neopixel
from distance_sensor import DistanceSensor  # Import the DistanceSensor class

class Bot:
    def __init__(self, **kwargs):
        # Setup DC Motor pins
        self.M1A = machine.PWM(machine.Pin(kwargs["M1A"]), freq=50, duty_u16=0)
        self.M1B = machine.PWM(machine.Pin(kwargs["M1B"]), freq=50, duty_u16=0)
        self.M2A = machine.PWM(machine.Pin(kwargs["M2A"]), freq=50, duty_u16=0)
        self.M2B = machine.PWM(machine.Pin(kwargs["M2B"]), freq=50, duty_u16=0)

        self.left = machine.Pin(kwargs["left"], machine.Pin.IN)
        self.right = machine.Pin(kwargs["right"], machine.Pin.IN)

        self.A = machine.Pin(kwargs["A"], machine.Pin.IN)
        self.B = machine.Pin(kwargs["B"], machine.Pin.IN)

        # NeoPixel setup
        self.np = neopixel.NeoPixel(machine.Pin(kwargs["neopixel"]), 2)

        # Distance sensor
        self.distance_sensor = DistanceSensor(kwargs["trig"], kwargs["echo"])

    def fwd(self, speed=0.5):
        right_speed = speed *1.043
        left_speed = speed
        self.M1A.duty_u16(0)
        self.M1B.duty_u16(int(left_speed * 65535))  # Left motor forward
        self.M2A.duty_u16(0)
        self.M2B.duty_u16(int(right_speed * 65535))  # Right motor forward

    def stop(self):
        self.M1A.duty_u16(0)
        self.M1B.duty_u16(0)
        self.M2A.duty_u16(0)
        self.M2B.duty_u16(0)

    def light_up(self, color):
        for i in range(len(self.np)):
            self.np[i] = color
        self.np.write()

# Setup the button GPIO pin
button_pin = machine.Pin(20, machine.Pin.IN, machine.Pin.PULL_UP)  # GPIO 20 for the button

async def wait_for_button_press():
    """Non-blocking function to wait for the button press."""
    while button_pin.value() == 1:  # Wait until the button is pressed (button_pin.value() == 0 when pressed)
        await asyncio.sleep(0.1)  # Non-blocking delay to allow other tasks to run
    print("Button pressed!")

async def countdown():
    """Asynchronous countdown function."""
    print("Get ready!")
    for i in range(3, 0, -1):  # Countdown from 3 to 1
        print(i)
        await asyncio.sleep(1)  # Non-blocking delay
    print("Go!")

import machine
import uasyncio as asyncio  # Use uasyncio for non-blocking behavior
import neopixel
from distance_sensor import DistanceSensor  # Import the DistanceSensor class

# Other class definitions remain the same...

async def curling_run():
    """Main function for the curling bot."""
    conf = {
        "M1A": 8,
        "M1B": 9,
        "M2A": 10,
        "M2B": 11,
        "left": 2,
        "right": 3,
        "A": 20,
        "B": 21,
        "trig": 4,
        "echo": 5,
        "neopixel": 18
    }
    b = Bot(**conf)
    threshold_distance = 45  # Threshold for obstacle detection in cm

    while True:
        # Wait for the button press asynchronously
        print("Press the button to start the countdown...")
        await wait_for_button_press()

        # Start the countdown asynchronously
        await countdown()

        # Start the bot
        print("Starting curling bot...")
        b.fwd(speed=0.5)

        while True:
            try:
                # Measure distance asynchronously
                distance = b.distance_sensor.read_distance()
                # print(f"Distance: {distance:.2f} cm")

                if distance <= threshold_distance:
                    print("Obstacle detected! Stopping the bot.")
                    b.stop()
                    b.light_up((255, 0, 0))  # Red color to indicate stop
                    break  # Exit the loop when an obstacle is detected
                else:
                    print("still green")
                    b.light_up((0, 255, 0))  # Green color to indicate clear path

                await asyncio.sleep(0.1)  # Non-blocking delay for updating
            except Exception as e:
                print(f"Error occurred: {e}")
                b.stop()
                b.light_up((255, 255, 0))  # Yellow color for error state
                await asyncio.sleep(1)  # Wait before retrying

async def main():
    """Main entry point with initialization delay."""
    print("Initializing system...")
    await asyncio.sleep(1)  # Ensure hardware setup completes properly
    try:
        await curling_run()
    except Exception as e:
        print(f"Unhandled exception: {e}")
        # Optionally stop everything or show a status

# Run the bot
asyncio.run(main())


# async def curling_run():
#     """Main function for the curling bot."""
#     conf = {
#         "M1A": 8,
#         "M1B": 9,
#         "M2A": 10,
#         "M2B": 11,
#         "left": 2,
#         "right": 3,
#         "A": 20,
#         "B": 21,
#         "trig": 4,
#         "echo": 5,
#         "neopixel": 18
#     }
#     b = Bot(**conf)
#     threshold_distance = 45  # Threshold for obstacle detection in cm

#     while True:
#         # Wait for the button press asynchronously
#         print("Press the button to start the countdown...")
#         await wait_for_button_press()

#         # Start the countdown asynchronously
#         await countdown()

#         # Start the bot
#         print("Starting curling bot...")
#         b.fwd(speed=0.5)

#         while True:
#             # Measure distance asynchronously
#             distance = b.distance_sensor.read_distance()
#             # print(f"Distance: {distance:.2f} cm")

#             if distance <= threshold_distance:
#                 print("Obstacle detected! Stopping the bot.")
#                 b.stop()
#                 b.light_up((255, 0, 0))  # Red color to indicate stop
#                 break  # Exit the loop when an obstacle is detected
#             else:
#                 print("still green")
#                 b.light_up((0, 255, 0))  # Green color to indicate clear path

#             await asyncio.sleep(0.1)  # Non-blocking delay for updating

# # Run the curling bot
# asyncio.run(curling_run())


# import machine
# import asyncio
# import time
# import neopixel
# from distance_sensor import DistanceSensor  # Import the DistanceSensor class

# class Bot:
#     def __init__(self, **kwargs):
#         # Setup DC Motor pins
#         self.M1A = machine.PWM(machine.Pin(kwargs["M1A"]), freq=50, duty_u16=0)
#         self.M1B = machine.PWM(machine.Pin(kwargs["M1B"]), freq=50, duty_u16=0)
#         self.M2A = machine.PWM(machine.Pin(kwargs["M2A"]), freq=50, duty_u16=0)
#         self.M2B = machine.PWM(machine.Pin(kwargs["M2B"]), freq=50, duty_u16=0)

#         self.left = machine.Pin(kwargs["left"], machine.Pin.IN)
#         self.right = machine.Pin(kwargs["right"], machine.Pin.IN)

#         self.A = machine.Pin(kwargs["A"], machine.Pin.IN)
#         self.B = machine.Pin(kwargs["B"], machine.Pin.IN)

#         # NeoPixel setup
#         self.np = neopixel.NeoPixel(machine.Pin(kwargs["neopixel"]), 2)

#         # Distance sensor
#         self.distance_sensor = DistanceSensor(kwargs["trig"], kwargs["echo"])

#     def fwd(self, speed=0.5):
#         right_speed = speed * 1.065
#         left_speed = speed
#         self.M1A.duty_u16(0)
#         self.M1B.duty_u16(int(left_speed * 65535))  # Left motor forward
#         self.M2A.duty_u16(0)
#         self.M2B.duty_u16(int(right_speed * 65535))  # Right motor forward

#     def stop(self):
#         self.M1A.duty_u16(0)
#         self.M1B.duty_u16(0)
#         self.M2A.duty_u16(0)
#         self.M2B.duty_u16(0)

#     def light_up(self, color):
#         for i in range(len(self.np)):
#             self.np[i] = color
#         self.np.write()

# # Setup the button GPIO pin
# button_pin = machine.Pin(20, machine.Pin.IN, machine.Pin.PULL_UP)  # GPIO 20 for the button

# # Check if the button is pressed
# def wait_for_button_press():
#     if button_pin.value() == 1:  # Wait until the button is pressed (button_pin.value() == 0 when pressed)
#         print("Get ready!")
#     else:
#         print("Pressed!")
#         for i in range(3, 0, -1):  # Countdown from 3 to 1
#             print(i)
#             print("Go!")
#             time.sleep(0.1)  # Small delay to debounce the button press

# def curling_run():
#     conf = {
#         "M1A": 8,
#         "M1B": 9,
#         "M2A": 10,
#         "M2B": 11,
#         "left": 2,
#         "right": 3,
#         "A": 20,
#         "B": 21,
#         "trig": 4,
#         "echo": 5,
#         "neopixel": 18
#     }
#     b = Bot(**conf)
#     threshold_distance = 45  # Threshold for obstacle detection in cm

#     print("Starting curling bot...")
#     b.fwd(speed=0.5)  # Start moving forward

#     while True:

#         print("Press the button to start the countdown...")
#         wait_for_button_press()  # Wait for the button press

#         # Measure distance
#         distance = b.distance_sensor.read_distance()
#         print(f"Distance: {distance:.2f} cm")

#         if distance <= threshold_distance:
#             print("Obstacle detected! Stopping the bot.")
#             b.stop()
#             b.light_up((255, 0, 0))  # Red color to indicate stop
#             break  # Stop the loop when an obstacle is detected
#         else:
#             b.light_up((0, 255, 0))  # Green color to indicate clear path

#         time.sleep_ms(100)  # Update frequency


# curling_run()
