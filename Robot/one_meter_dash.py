#Mikey Myro
#1 Meter Dash Code
import asyncio
import machine
import time
import neopixel


class Bot:
    def __init__(self, **kwargs):
        # Initialize the bot with the given pin configuration
        
        # Setup DC Motor pins for controlling the movement
        self.M1A = machine.PWM(machine.Pin(kwargs["M1A"]), freq=50, duty_u16=0)
        self.M1B = machine.PWM(machine.Pin(kwargs["M1B"]), freq=50, duty_u16=0)
        self.M2A = machine.PWM(machine.Pin(kwargs["M2A"]), freq=50, duty_u16=0)
        self.M2B = machine.PWM(machine.Pin(kwargs["M2B"]), freq=50, duty_u16=0)

        # Line sensor pins (for line following behavior)
        self.left = machine.Pin(kwargs["left"], machine.Pin.IN)
        self.right = machine.Pin(kwargs["right"], machine.Pin.IN)

        # Button pins for user interaction
        self.A = machine.Pin(kwargs["A"], machine.Pin.IN)
        self.B = machine.Pin(kwargs["B"], machine.Pin.IN)

        # Ultrasonic distance sensor pins
        self.trig = machine.Pin(kwargs["trig_pin"], machine.Pin.OUT)
        self.echo = machine.Pin(kwargs["echo_pin"], machine.Pin.IN)

        # Neopixel setup for LED feedback
        self.np = neopixel.NeoPixel(machine.Pin(kwargs["neopixel"]), 2)

    # Move forward at the given speed (default 50%)
    def fwd(self, speed=0.5):
        right_speed = speed * 1.065  # Adjusted for motor differences
        left_speed = speed
        self.M1A.duty_u16(0)
        self.M1B.duty_u16(int(left_speed * 65535))  # Left motor forward
        self.M2A.duty_u16(0)
        self.M2B.duty_u16(int(right_speed * 65535))  # Right motor forward

    # Turn left by slowing down the left motor
    def turnleft(self, speed=0.3):
        left_speed = speed * 0.5  # Reduced speed for the left motor
        right_speed = speed  # Full speed for the right motor
        self.M1A.duty_u16(0)
        self.M1B.duty_u16(int(left_speed * 65535))  # Slower left motor
        self.M2A.duty_u16(0)
        self.M2B.duty_u16(int(right_speed * 65535))  # Right motor forward

    # Turn right by slowing down the right motor
    def turnright(self, speed=0.3):
        left_speed = speed  # Full speed for the left motor
        right_speed = speed * 0.5  # Reduced speed for the right motor
        self.M1A.duty_u16(0)
        self.M1B.duty_u16(int(left_speed * 65535))  # Left motor forward
        self.M2A.duty_u16(0)
        self.M2B.duty_u16(int(right_speed * 65535))  # Slower right motor

    # Stop the motors
    def stop(self):
        self.M1A.duty_u16(0)
        self.M1B.duty_u16(0)
        self.M2A.duty_u16(0)
        self.M2B.duty_u16(0)

    # Read line sensor values (left, right)
    def read_line(self):
        return self.left.value(), self.right.value()

    # Measure distance using the ultrasonic sensor
    def read_distance(self, timeout=100):
        # Send a short pulse to trigger the ultrasonic sensor
        self.trig.value(0)
        time.sleep_us(2)
        self.trig.value(1)
        time.sleep_us(10)
        self.trig.value(0)

        start_time = time.ticks_us()
        while self.echo.value() == 0:
            signaloff = time.ticks_us()
            if time.ticks_diff(signaloff, start_time) > timeout * 1000:
                return None

        start_time = time.ticks_us()
        while self.echo.value() == 1:
            signalon = time.ticks_us()
            if time.ticks_diff(signalon, start_time) > timeout * 1000:
                return None

        # Calculate distance based on the duration of the pulse
        pulse_time = time.ticks_diff(signalon, signaloff)
        distance = (pulse_time * 0.0343) / 2  # Convert to cm
        return distance


# Main asynchronous function to control the bot
async def main():
    conf = {
        "M1A": 8,
        "M1B": 9,
        "M2A": 10,
        "M2B": 11,
        "left": 2,
        "right": 3,
        "A": 20,
        "B": 21,
        "trig_pin": 4,
        "echo_pin": 5,
        "neopixel": 18,
    }
    b = Bot(**conf)  # Initialize bot with configuration
    button_pin = machine.Pin(20, machine.Pin.IN, machine.Pin.PULL_UP)

    async def wait_for_button_press():
        # Wait for the user to press a button
        while button_pin.value() == 1:
            await asyncio.sleep(0.1)
        print("Button pressed!")

    async def countdown():
        # Countdown sequence before starting
        print("Get ready!")
        for i in range(3, 0, -1):
            print(i)
            await asyncio.sleep(1)
        print("Go!")

    while True:
        print("Press the button to start the countdown...")
        await wait_for_button_press()
        await countdown()

        state = 0  # Initial state
        start_time = None

        while True:
            line = b.read_line()  # Read line sensor values
            distance = b.read_distance()  # Measure distance

            if state == 0:
                # Waiting for readiness
                if line == (0, 0):
                    print("Ready!")
                    state = 1
            elif state == 1:
                # Ensure bot is on the line and wait for user action
                if line != (0, 0):
                    print("Not ready")
                    state = 0
                elif b.A.value() == 0:
                    while b.A.value() == 0:  # Wait for button release
                        await asyncio.sleep(0.01)
                    print("Starting!")
                    state = 2
                    start_time = time.ticks_ms()
                    b.fwd(speed=0.5)  # Start moving forward
            elif state == 2:
                # Check for obstacles and handle line adjustments
                if distance is not None and distance <= 10:
                    print("Barrier detected! Stopping.")
                    b.stop()
                    while b.read_distance() <= 10:
                        await asyncio.sleep(0.1)
                    print("Clear! Moving forward.")
                    b.fwd(speed=0.5)

                if line != (0, 0):
                    state = 3
            elif state == 3:  # Adjusting based on line detection
                # Distance checking
                distance = b.read_distance()
                if distance is not None and distance <= 10:
                    print("Obstacle detected! Stopping the car.")
                    b.stop()
                    while distance <= 10:  # Keep checking until the obstacle is cleared
                        print(f"Distance: {distance} cm. Waiting for clearance.")
                        distance = b.read_distance()
                        await asyncio.sleep(0.1)  # Non-blocking wait

                    print("Obstacle cleared. Resuming movement.")
                    b.fwd(speed=0.5)

                # Line detection adjustments
                elif line == (1, 0):  # Left sensor detects line
                    print("Adjusting RIGHT")
                    b.turnright(speed=0.3)
                elif line == (0, 1):  # Right sensor detects line
                    print("Adjusting LEFT")
                    b.turnleft(speed=0.3)
                elif line == (0, 0):  # Both sensors off the line
                    print("Moving FORWARD")
                    b.fwd(speed=0.5)
                elif line == (1, 1):  # Both sensors detect the line
                    print("Centered on line")
                    b.fwd(speed=0.5)
                await asyncio.sleep(0.1)  # Avoid busy looping


# Handle exceptions and stop the bot if an error occurs
try:
    asyncio.run(main())
except Exception as e:
    conf = {
        "M1A": 8,
        "M1B": 9,
        "M2A": 10,
        "M2B": 11,
        "left": 2,
        "right": 3,
        "A": 20,
        "B": 21,
        "trig_pin": 4,
        "echo_pin": 5,
        "neopixel": 18,
    }
    b = Bot(**conf)
    b.stop()  # Emergency stop
    print("Emergency stop.")
    raise e
