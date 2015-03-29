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

print "Test sequence T190"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Select Language"

print "Click Achievement-Button"
device.touch(calcWidth(750), calcHeight(470), "DOWN_AND_UP")
time.sleep(1)

print "Watch achievements"
device.drag((940,600),(940,100),2.0,50)
time.sleep(1)

print "Click Back-Button"
device.touch(calcWidth(80), calcHeight(630), "DOWN_AND_UP")
time.sleep(1)

print "Click Settings-Button"
device.touch(calcWidth(90), calcHeight(630), "DOWN_AND_UP")
time.sleep(1)

print "Click Statistics-Button"
device.touch(calcWidth(630), calcHeight(530), "DOWN_AND_UP")
time.sleep(1)

print "Watch statistics"
device.drag((940,600),(940,100),2.0,50)
time.sleep(1)

print "Click Back-Button"
device.touch(calcWidth(120), calcHeight(600), "DOWN_AND_UP")
time.sleep(1)

print "Click Back-Button"
device.touch(calcWidth(120), calcHeight(600), "DOWN_AND_UP")
time.sleep(1)

print "End of T190"