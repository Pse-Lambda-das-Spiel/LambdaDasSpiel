from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
import os
import time

# Prerequisites:
# - Application in main menu

# Tested with 1280x720 resolution

def calcWidth(width):
    return int(int(displayWidth) * int(width) / int(1280))

def calcHeight(height):
    return int(int(displayHeight) * int(height) / int(720))

print "Test sequence T180"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Click Music-Checkbox"
device.touch(calcWidth(1170), calcHeight(630), "DOWN_AND_UP")
time.sleep(1)

print "Click Music-Checkbox again"
device.touch(calcWidth(1170), calcHeight(630), "DOWN_AND_UP")
time.sleep(1)

print "Click Settings-Button"
device.touch(calcWidth(90), calcHeight(630), "DOWN_AND_UP")
print "Should be in Settings-Menu"
time.sleep(1)

print "Change music volume"
device.drag((300,240),(800,240),1.0,50)
time.sleep(1)

print "Change sound volume"
device.drag((300,380),(800,380),1.0,50)
time.sleep(1)

print "Click Back-Button"
device.touch(calcWidth(120), calcHeight(600), "DOWN_AND_UP")
print "Should be in the main menu again"
time.sleep(1)

print "End of T180"