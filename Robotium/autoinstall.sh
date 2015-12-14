#!/bin/bash

TARGET_APK_PATH="$1"
TARGET_PACKAGE_NAME="$2"
TEST_CASE_APK_PATH="$3"
TEST_CASE_PACKAGE_NAME="$4"

adb start-server
adb get-serialno > tmp

while read line
    do 
        echo "Uninstall target apk in device: "${line}
        adb -s $line uninstall ${TARGET_PACKAGE_NAME}
        echo "Pushing target apk to device: "${line}
        adb -s $line push ${TARGET_APK_PATH} "/data/local/tmp"
        echo "Install target apk in device: "${line}
        adb -s $line install ${TARGET_APK_PATH}
        echo "Uninstall test apk in device: "${line}
        adb -s $line uninstall ${TEST_CASE_PACKAGE_NAME}
        echo "Pushing test apk in device: "${line}
        adb -s $line push ${TEST_CASE_APK_PATH} "/data/local/tmp"
        echo "Install test apk in device: "${line}
        adb -s $line install ${TEST_CASE_APK_PATH}
done < tmp


echo "  --  Done  --  "
echo " ========================== "
echo " ========Command=========== "

while read line
    do 
        echo "adb -s "${line}" shell am instrument -w "${TEST_CASE_PACKAGE_NAME}"/android.test.InstrumentationTestRunner"
done < tmp
echo " ========================== "

rm tmp
