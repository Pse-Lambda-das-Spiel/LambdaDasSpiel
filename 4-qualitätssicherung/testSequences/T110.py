from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import commands
import sys
import os
import time

# Prerequisites:
# - Application in its initial state -> no profiles exists

# Tested with 1280x720 resolution

def calcWidth(width):
    return int(int(displayWidth) * int(width) / int(1280))

def calcHeight(height):
    return int(int(displayHeight) * int(height) / int(720))

print "Test sequence T110"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Select Language"

print "Clicking 'rightLanguage' Button"
device.touch(calcWidth(1050), calcHeight(270), "DOWN_AND_UP")
time.sleep(1)

print "Clicking 'leftLanguage' Button"
device.touch(calcWidth(250), calcHeight(270), "DOWN_AND_UP")
time.sleep(1)

print "Language selected"

print "Clicking 'right' Button"
device.touch(calcWidth(1150), calcHeight(600), "DOWN_AND_UP")
time.sleep(1)

print "Clicking on the 'enterName' TextField"
device.touch(calcWidth(640), calcHeight(360), "DOWN_AND_UP")
time.sleep(1)

print "Enter Name"
#Without onescreen keyboard because of different keyboard layouts etc.
device.type("TestName\n")
time.sleep(1)

print "Clicking 'right' Button"
device.touch(calcWidth(1150), calcHeight(600), "DOWN_AND_UP")
time.sleep(1)

print "Select Avatar"

print "Clicking 'rightAvatar' Button"
device.touch(calcWidth(1050), calcHeight(370), "DOWN_AND_UP")
time.sleep(1)

print "Clicking 'leftAvatar' Button"
device.touch(calcWidth(250), calcHeight(370), "DOWN_AND_UP")
time.sleep(1)

print "Avatar selected"

print "Clicking 'accept' Button"
device.touch(calcWidth(1150), calcHeight(600), "DOWN_AND_UP")
print "Profile created"
time.sleep(3)
print "Should be in main menu now"

print "End of T110"