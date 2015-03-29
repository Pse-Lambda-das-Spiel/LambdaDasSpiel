from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
import os
import time

# Prerequisites:
# - Application started (->currently in the ProfileSelection-Screen.). One profile has to exist.
# The first profile should be the one from the T110 test sequence, but doesn't really matter as long as its name is TestName or has similar length.

# Tested with 1280x720 resolution

def calcWidth(width):
    return int(int(displayWidth) * int(width) / int(1280))

def calcHeight(height):
    return int(int(displayHeight) * int(height) / int(720))

print "Test sequence T140"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Open Config-Dialog of the first profile"
device.touch(calcWidth(1000), calcHeight(125), "DOWN_AND_UP")
time.sleep(1)

print "Choose the delete option"
device.touch(calcWidth(700), calcHeight(390), "DOWN_AND_UP")
time.sleep(1)

print "Confirm deleting the profile"
device.touch(calcWidth(560), calcHeight(390), "DOWN_AND_UP")
time.sleep(1)

print "Profile should have been deleted."
print "End of T140"