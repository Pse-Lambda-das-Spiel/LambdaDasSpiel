from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
import os
import time

# Prerequisites:
# - Application started (->currently in the ProfileSelection-Screen.) One profile has to exist.

# Tested with 1280x720 resolution

def calcWidth(width):
    return int(int(displayWidth) * int(width) / int(1280))

def calcHeight(height):
    return int(int(displayHeight) * int(height) / int(720))

print "Test sequence T120"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Select first profile"
device.touch(calcWidth(600), calcHeight(130), "DOWN_AND_UP")
time.sleep(1)

print "Should be in main menu now"

print "End of T120"