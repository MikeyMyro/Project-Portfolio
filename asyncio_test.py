#Jack Beneigh
#Breaking Code
import asyncio
import machine, neopixel
import time
from Robot import bot

# Notes with their frequencies (in Hz)
notes = {
    "C4": 262,
    "D4": 294,
    "E4": 330,
    "F4": 349,
    "G4": 392,
    "A4": 440,
    "B4": 494,
    "C5": 523,
    "REST": 0,  # Silence
}

# Twinkle, Twinkle Little Star (Extended Version)
# Each tuple contains (note, duration in seconds)
song = [
    ("C4", 0.5), ("C4", 0.5), ("G4", 0.5), ("G4", 0.5),  # Twinkle, twinkle
    ("A4", 0.5), ("A4", 0.5), ("G4", 1.0),              # Little star
    ("F4", 0.5), ("F4", 0.5), ("E4", 0.5), ("E4", 0.5), # How I wonder
    ("D4", 0.5), ("D4", 0.5), ("C4", 1.0),              # What you are

    ("G4", 0.5), ("G4", 0.5), ("F4", 0.5), ("F4", 0.5), # Up above the
    ("E4", 0.5), ("E4", 0.5), ("D4", 1.0),              # World so high
    ("G4", 0.5), ("G4", 0.5), ("F4", 0.5), ("F4", 0.5), # Like a diamond
    ("E4", 0.5), ("E4", 0.5), ("D4", 1.0), ("REST", 1.0),    # In the sky

    # Repeat with variations
    ("C4", 0.5), ("C4", 0.5), ("G4", 0.5), ("G4", 0.5), 
    ("A4", 0.5), ("A4", 0.5), ("G4", 1.0), ("REST", 1.0),
    ("F4", 0.5), ("F4", 0.5), ("E4", 0.5), ("E4", 0.5), 
    ("D4", 0.5), ("D4", 0.5), ("C4", 1.0), ("REST", 1.0),

    # Add a slower ending to fill time
    ("C4", 1.0), ("G4", 1.0), ("A4", 1.0)
]

    # Define colors corresponding to notes (R, G, B values for each note)
colors = {
    'C4': (255, 0, 0),   # Red
    'D4': (0, 255, 0),   # Green        
    'E4': (0, 0, 255),   # Blue
    'F4': (255, 255, 0), # Yellow        
    'G4': (0, 255, 255), # Cyan
    'A4': (255, 0, 255), # Magenta
    'B4': (255, 128, 0), # Orange
    'C5': (128, 0, 255), # Purple
    'REST': (0, 0, 0)    # Off
}

np = neopixel.NeoPixel(machine.Pin(18), 2)  # Assuming 2 NeoPixels on GPIO 18
   # Initialize PWM for sound output
buzzer = machine.PWM(machine.Pin(22))  # Use GPIO 19 for the buzzer
buzzer.duty_u16(0)  # Start with no sound

# Setup the button GPIO pin
button_pin = machine.Pin(20, machine.Pin.IN, machine.Pin.PULL_UP)  # GPIO 14 for the button


# Initialize robot
conf = {
    "M1A": 8,
    "M1B": 9,
    "M2A": 10,
    "M2B": 11,
    "left": 2,
    "right": 3,
    "A": 20,
    "B": 21
}

b = bot(**conf)

# Ensure robot is in a safe state
b.stop()

async def play_note_with_leds(note, duration):
    color = colors.get(note, (0, 0, 0))
    frequency = notes.get(note, 0)

    # Set LEDs
    np[0] = color
    np[1] = color
    np.write()

    # Set buzzer frequency
    if frequency > 0:
        buzzer.freq(frequency)
        buzzer.duty_u16(16384)  # Sound on
    else:
        buzzer.duty_u16(0)  # Rest

    await asyncio.sleep(duration)  # Non-blocking delay

    # Turn off buzzer and LEDs
    buzzer.duty_u16(0)
    np[0] = (0, 0, 0)
    np[1] = (0, 0, 0)
    np.write()

# Define asynchronous movement functions
async def move_forward(duration, speed=1.5):
    b.fwd(speed=speed)
    await asyncio.sleep(duration)  # Non-blocking delay
    b.stop()

async def move_backward(duration, speed=1.5):
    b.reverse(speed=speed)
    await asyncio.sleep(duration)  # Non-blocking delay
    b.stop()

async def rotate(duration, speed=0.3):
    b.rotate(speed=speed)  # Call the rotate method on the robot instance
    await asyncio.sleep(duration)  # Allow rotation for the specified duration
    b.stop()  # Stop the robot after the duration

async def play_song():
    for note, duration in song:
        await play_note_with_leds(note, duration)

# Countdown function
async def countdown():
    print("Get ready!")
    for i in range(3, 0, -1):  # Countdown from 3 to 1
        print(i)
        await asyncio.sleep(1)
    print("Go!")

# Check if the button is pressed
def wait_for_button_press():
    while button_pin.value() == 1:  # Wait until the button is pressed (button_pin.value() == 0 when pressed)
        time.sleep(0.1)  # Small delay to debounce the button press

# Main testing routine
async def main():
    print("Press the button to start the countdown...")
    wait_for_button_press()  # Wait for the button press

    # Run countdown
    await countdown()

    # Start the music and lights concurrently with movement
    music_task = asyncio.create_task(play_song())
    runs1 = 0
    runs2 = 0
    while runs1 < 3: 

        # First Run
        print("Moving forward...")
        await move_forward(1, speed=0.3)  # 1 second

        print("Moving backward...")
        await move_backward(1, speed=0.3)  # 1 seconds

        print("Moving forward...")
        await move_forward(1, speed=0.3)  # 1 second

        print("Moving backward...")
        await move_backward(1, speed=0.3)  # 1 seconds

        print("rotate")
        await rotate(3, speed=0.3)  # 3 seconds
        runs1 += 1

    while runs2 < 1: 

        print("Moving backward...")
        await move_backward(0.5, speed=2.5)  # 0.5 seconds

        print("Moving forward...")
        await move_forward(0.5, speed=2.5)  # 0l5 second

        print("Moving backward...")
        await move_backward(0.5, speed=2.5)  # 0.5 seconds

        print("Moving forward...")
        await move_forward(0.5, speed=2.5)  # 0.5 second

        print("Moving backward...")
        await move_backward(3, speed=0.3)  # 3 seconds

        print("rotate")
        await rotate(4, speed=0.3)  # 3 seconds

        runs2 += 1


    print("Stopping robot.")
    b.stop()

    # Wait for the music task to complete
    await music_task

# Run the main routine
asyncio.run(main())

