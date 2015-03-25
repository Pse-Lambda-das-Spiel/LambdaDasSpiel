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

print "Test sequence T130"

device = MonkeyRunner.waitForConnection()
displayWidth = device.getProperty("display.width")
displayHeight = device.getProperty("display.height")

print "Open Config-Dialog of the first profile"
device.touch(calcWidth(1000), calcHeight(125), "DOWN_AND_UP")
time.sleep(1)

print "Choose the configuration option"
device.touch(calcWidth(560), calcHeight(390), "DOWN_AND_UP")
time.sleep(1)

print "Change Language"

print "Clicking 'rightLanguage' Button"
device.touch(calcWidth(1050), calcHeight(270), "DOWN_AND_UP")
time.sleep(1)

print "Language changed"

print "Clicking 'right' Button"
device.touch(calcWidth(1150), calcHeight(600), "DOWN_AND_UP")
time.sleep(1)

print "Clicking on the 'enterName' TextField"
device.touch(calcWidth(640), calcHeight(360), "DOWN_AND_UP")
time.sleep(1)

print "Change Name"
#Without onescreen keyboard because of different keyboard layouts etc.
device.type("Delta\n")
time.sleep(1)

print "Clicking 'right' Button"
device.touch(calcWidth(1150), calcHeight(600), "DOWN_AND_UP")
time.sleep(1)

print "Change Avatar"

print "Clicking 'rightAvatar' Button"
device.touch(calcWidth(1050), calcHeight(370), "DOWN_AND_UP")
time.sleep(1)

print "Avatar changed"

print "Clicking 'accept' Button"
device.touch(calcWidth(1150), calcHeight(600), "DOWN_AND_UP")
print "Profile edited"
time.sleep(1)
print "Should be in main menu now"

print "End of T130"