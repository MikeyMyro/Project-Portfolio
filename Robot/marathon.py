#Mikey Myro
#Marathon Code
import machine
import time


class MarathonBot:
    def __init__(self, **kwargs):
        # Initialize the bot with the provided pin configuration
        
        # Setup DC Motor pins for controlling the motors
        self.M1A = machine.PWM(machine.Pin(kwargs["M1A"]), freq=50, duty_u16=0)  # Left motor forward
        self.M1B = machine.PWM(machine.Pin(kwargs["M1B"]), freq=50, duty_u16=0)  # Left motor backward
        self.M2A = machine.PWM(machine.Pin(kwargs["M2A"]), freq=50, duty_u16=0)  # Right motor forward
        self.M2B = machine.PWM(machine.Pin(kwargs["M2B"]), freq=50, duty_u16=0)  # Right motor backward

        # Setup sensor pins for line detection
        self.left = machine.Pin(kwargs["left"], machine.Pin.IN)  # Left line sensor
        self.right = machine.Pin(kwargs["right"], machine.Pin.IN)  # Right line sensor

        # Setup start button pin
        self.start_button = machine.Pin(kwargs["start_button"], machine.Pin.IN, machine.Pin.PULL_UP)

    # Read the state of the line sensors
    def read_line(self):
        return self.left.value(), self.right.value()

    # Set motor speeds
    def set_motor_speed(self, left_speed, right_speed):
        """
        left_speed and right_speed:
        Positive values move the motor forward; negative values move it backward.
        """
        # Set left motor speed
        self.M1A.duty_u16(0)
        self.M1B.duty_u16(int(max(0, left_speed) * 65535))  # Forward
        self.M1A.duty_u16(int(max(0, -left_speed) * 65535))  # Backward
        
        # Set right motor speed
        self.M2A.duty_u16(0)
        self.M2B.duty_u16(int(max(0, right_speed) * 65535))  # Forward
        self.M2A.duty_u16(int(max(0, -right_speed) * 65535))  # Backward

    # Stop both motors
    def stop(self):
        self.set_motor_speed(0, 0)


def marathon():
    # Define pin configuration
    conf = {
        "M1A": 8,  # Left motor forward
        "M1B": 9,  # Left motor backward
        "M2A": 10,  # Right motor forward
        "M2B": 11,  # Right motor backward
        "left": 2,  # Left line sensor
        "right": 3,  # Right line sensor
        "start_button": 20  # Start button
    }

    # Initialize the bot with the pin configuration
    bot = MarathonBot(**conf)

    # Wait for the user to press the start button
    print("Press the start button to begin...")
    while bot.start_button.value() == 1:  # Button not pressed
        time.sleep_ms(100)

    # Countdown sequence before starting
    print("Starting in...")
    for i in range(3, 0, -1):
        print(i)
        time.sleep(1)

    print("Go!")
    bot.stop()  # Ensure motors are stopped initially

    # Main loop for line following
    while True:
        line = bot.read_line()  # Read line sensor values

        if line == (0, 1):  # Line detected by the right sensor
            print("Adjusting LEFT: Left wheel back, Right wheel forward")
            bot.set_motor_speed(-0.5, 0.5)  # Reverse left, forward right
        elif line == (1, 0):  # Line detected by the left sensor
            print("Adjusting RIGHT: Right wheel back, Left wheel forward")
            bot.set_motor_speed(0.5, -0.5)  # Forward left, reverse right
        elif line == (1, 1):  # Both sensors detect the line
            print("Staying STRAIGHT")
            bot.set_motor_speed(0.4, 0.4)  # Move forward
        elif line == (0, 0):  # No sensors detect the line (centered)
            print("Moving FORWARD steadily")
            bot.set_motor_speed(0.4, 0.4)  # Move forward steadily
        else:  # Unexpected case
            print("Lost line! Searching...")
            bot.set_motor_speed(0.4, 0.4)  # Keep moving forward to find the line
            time.sleep_ms(100)

        time.sleep_ms(50)  # Short delay to prevent rapid sensor readings


# Exception handling for safe bot stop in case of error
try:
    marathon()
except Exception as e:
    print("Emergency stop. Stopping motors.")
    conf = {
        "M1A": 8,
        "M1B": 9,
        "M2A": 10,
        "M2B": 11,
        "left": 2,
        "right": 3,
        "start_button": 20
    }
    bot = MarathonBot(**conf)
    bot.stop()  # Stop motors in case of an error
    raise e
