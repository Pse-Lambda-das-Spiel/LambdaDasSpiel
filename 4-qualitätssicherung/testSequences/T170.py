from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
import os
import time

# Prerequisites:
# - Logged in with profile that has at least 30 coins.
# - In the main menu
# - german profile (other languages may change the size of some dialogs)

# Tested with 1280x720 resolution

def calcWidth(width):
    return int(int(displayWidth) * int(width) / int(1280))

def calcHeight(height):
    return int(int(displayHeight) * int(height) / int(720))

print "Test sequence T170"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Click Shop-Button"
device.touch(calcWidth(1100), calcHeight(125), "DOWN_AND_UP")
time.sleep(1)

print "Open Music-DropDownMenu"
device.touch(calcWidth(630), calcHeight(130), "DOWN_AND_UP")
time.sleep(1)

print "Click on first Music-Item"
device.touch(calcWidth(630), calcHeight(330), "DOWN_AND_UP")
time.sleep(1)

print "Confirm Buy"
device.touch(calcWidth(610), calcHeight(450), "DOWN_AND_UP")
time.sleep(1)

print "Click on second Music-Item"
device.touch(calcWidth(630), calcHeight(430), "DOWN_AND_UP")
time.sleep(1)

print "Confirm Buy"
device.touch(calcWidth(610), calcHeight(450), "DOWN_AND_UP")
time.sleep(1)

print "Click on second Music-Item"
device.touch(calcWidth(630), calcHeight(430), "DOWN_AND_UP")
time.sleep(1)

print "Activate"
device.touch(calcWidth(610), calcHeight(410), "DOWN_AND_UP")
time.sleep(1)

print "Click on first Music-Item"
device.touch(calcWidth(630), calcHeight(330), "DOWN_AND_UP")
time.sleep(1)

print "Activate"
device.touch(calcWidth(610), calcHeight(410), "DOWN_AND_UP")
time.sleep(1)

print "End of T170"