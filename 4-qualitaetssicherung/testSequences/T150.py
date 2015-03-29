from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
import os
import time

# Prerequisites:
# - Logged in with a Profile that has unlocked Level 6.
# - Be in the LevelSelection-Screen with Level 6 visible.

# Tested with 1280x720 resolution

def calcWidth(width):
    return int(int(displayWidth) * int(width) / int(1280))

def calcHeight(height):
    return int(int(displayHeight) * int(height) / int(720))

print "Test sequence T150"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Select level 6"
device.touch(calcWidth(830), calcHeight(460), "DOWN_AND_UP")
time.sleep(1)

print "Close Levelgoal-Dialog"
device.touch(calcWidth(640), calcHeight(460), "DOWN_AND_UP")
time.sleep(1)

print "Open Hint-Dialog"
device.touch(calcWidth(60), calcHeight(190), "DOWN_AND_UP")
time.sleep(1)

print "Close Hint-Dialog"
device.touch(calcWidth(640), calcHeight(460), "DOWN_AND_UP")
time.sleep(1)

print "Place white gem"
device.drag((510,630),(530,190),3.0,50)
time.sleep(1)

print "Select white gem"
device.touch(calcWidth(600), calcHeight(190), "DOWN_AND_UP")
time.sleep(1)

print "Choose color red"
device.touch(calcWidth(630), calcHeight(320), "DOWN_AND_UP")
time.sleep(1)

print "Try to drag blue lamb"
device.drag((340,200),(630,360),3.0,50)
time.sleep(1)

print "Click Reduction-Button"
device.touch(calcWidth(1200), calcHeight(630), "DOWN_AND_UP")
time.sleep(1)

print "Click OneStepForward-Button"
device.touch(calcWidth(760), calcHeight(630), "DOWN_AND_UP")
time.sleep(7)

print "Click Play-Button"
device.touch(calcWidth(630), calcHeight(630), "DOWN_AND_UP")
print "Levelfinish-Dialog should show a success once the reduction is complete"
time.sleep(5)

print "End of T150"