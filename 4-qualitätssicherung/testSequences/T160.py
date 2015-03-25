from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
import os
import time

# Prerequisites:
# - Logged in with a profile that has level 1 completed, level 2 unlocked, rest locked.
# - in main menu

# Tested with 1280x720 resolution

def calcWidth(width):
    return int(int(displayWidth) * int(width) / int(1280))

def calcHeight(height):
    return int(int(displayHeight) * int(height) / int(720))

print "Test sequence T160"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Click LevelSelection-Button"
device.touch(calcWidth(520), calcHeight(470), "DOWN_AND_UP")
time.sleep(1)

print "Selecting Level"

print "Click nextLevelPage-Button"
device.touch(calcWidth(1050), calcHeight(360), "DOWN_AND_UP")
time.sleep(1)

print "Click nextLevelPage-Button"
device.touch(calcWidth(1050), calcHeight(360), "DOWN_AND_UP")
time.sleep(1)

print "Click nextLevelPage-Button"
device.touch(calcWidth(1050), calcHeight(360), "DOWN_AND_UP")
time.sleep(1)

print "Click prevLevelPage-Button"
device.touch(calcWidth(250), calcHeight(390), "DOWN_AND_UP")
time.sleep(1)

print "Click prevLevelPage-Button"
device.touch(calcWidth(250), calcHeight(390), "DOWN_AND_UP")
time.sleep(1)

print "Click prevLevelPage-Button"
device.touch(calcWidth(250), calcHeight(390), "DOWN_AND_UP")
time.sleep(1)

print "Select level 1"
device.touch(calcWidth(450), calcHeight(260), "DOWN_AND_UP")
print "Level selected"
time.sleep(1)

print "Should have started level 1"
print "End of T160"