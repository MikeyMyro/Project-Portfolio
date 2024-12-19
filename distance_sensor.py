#Jack Beneigh
#Program for Distance Sensor
import machine
import uasyncio as asyncio  # Use uasyncio for non-blocking behavior
import time

class DistanceSensor:
    def __init__(self, trig_pin, echo_pin):
        self.trig = machine.Pin(trig_pin, machine.Pin.OUT)
        self.echo = machine.Pin(echo_pin, machine.Pin.IN)

    def read_distance(self, timeout=30000):
        # Send a 10us pulse to trigger
        self.trig.value(0)
        time.sleep_us(2)
        self.trig.value(1)
        time.sleep_us(10)
        self.trig.value(0)

        # Wait for the echo pin to go high
        start_time = time.ticks_us()
        while self.echo.value() == 0:
            if time.ticks_diff(time.ticks_us(), start_time) > timeout:
                # Timeout waiting for echo to start
                print("Timeout: No response from sensor")
                return float('inf')  # Return a very high value indicating no obstacle

        # Record the start time
        pulse_start = time.ticks_us()

        # Wait for the echo pin to go low
        while self.echo.value() == 1:
            if time.ticks_diff(time.ticks_us(), pulse_start) > timeout:
                # Timeout waiting for echo to stop
                print("Timeout: Echo signal too long")
                return float('inf')  # Return a very high value indicating no obstacle

        # Record the end time
        pulse_end = time.ticks_us()

        # Calculate pulse duration
        pulse_duration = time.ticks_diff(pulse_end, pulse_start)

        # Calculate distance in cm
        distance = (pulse_duration * 0.0343) / 2  # Speed of sound: 343 m/s
        return distance


