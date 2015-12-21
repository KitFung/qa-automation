Basic Flow
----

1. Download the apk for our target apps.
2. Resign the apk.
	- You may use this [tools](https://code.google.com/p/apk-resigner/) to resign
	- OR re-sign it manually by the below step
		- 1. Rename the apk from {name}.apk to {name}.zip
		- 2. unzip it and DELETE the META-INF folder
		- 3. Re-zip the apk file
		- 4. Rename it to {name}.apk
		- 5. Type this command `jarsigner -keystore ~/.android/debug.keystore -storepass android -keypass
android {name}.apk androiddebugkey`
		- 6. Type this command `zipalign 4 {name}.apk {temp name}.apk`
		- 7. Type this command `mv {temp name}.apk {name}.apk`
3. Create our test project.
	- You can take the bindle test as a example.
	- Remember to set the instrumentation in AndroidManifest.xml
	```
	<instrumentation
        android:name="android.test.InstrumentationTestRunner"
        android:targetPackage="com.bindlechat.Bindle" />
    ```
	- ** IMPORTANT ** 
		- Download [robotium](https://github.com/RobotiumTech/robotium/wiki/Downloads) and import to your project.
		- Remember to export it.
			- Eclipse -> Java Build Bath -> Order and Export -> Select robotium and let it to the top.
4. Install it in to the device.
	- Suggested you use eclipse to run the test in each device first. Than you can skip the painful step below.
	- 1. `adb -s {device id} push {name}.apk /data/local/tmp`
	- 2. `adb -s {device id} install {name}.apk`
	- 3. `adb -s {device id} push {test package}.apk /data/local/tmp`
	- 4. `adb -s {device id} install {test package}.apk`
	- The `{test package}.apk` can be found in `{path to your test project}/bin/`
5. Run the test.
	- See the instruction below.

You can get more detail from:

[https://robotium.googlecode.com/files/RobotiumForBeginners.pdf](https://robotium.googlecode.com/files/RobotiumForBeginners.pdf)

Check testing process
---
1. Open logcat
2. Using "bindle-test" to filter the message 

Run Test on One Device
---
It is recommanded to using eclipse to run the test.

1. Select the test case that you want to test
2. Select "Run as"
3. Select "Android Junit Test"
4. Click "OK"


Run Test on Multiple Device
---
1. First run `adb devices`
2. You will get something like that

```
List of devices attached 
4df1b6e228519ffb	device
emulator-5554	device

```
3. Record all the device id
   -  e.g. 4df1b6e228519ffb, emulator-5554
4. Open multiple command line window(one window per device)
5. Input the following command for each device (one command per window)
```
adb -s {device id} shell am instrument -w com.oursky.bindle.test/android.test.InstrumentationTestRunner
```
6. Press enter



Optional
----

- smoke test only

```
adb -s {device id} shell am instrument -w -e annotation android.test.suitebuilder.annotation.Smoke com.oursky.bindle.test/android.test.InstrumentationTestRunner

```

- small test

```
adb -s {device id} shell am instrument -w -e size small com.oursky.bindle.test/android.test.InstrumentationTestRunner 

```

- medium test

```
adb -s {device id} shell am instrument -w -e size medium com.oursky.bindle.test/android.test.InstrumentationTestRunner 

```

- large test

```
adb -s {device id} shell am instrument -w -e size large com.oursky.bindle.test/android.test.InstrumentationTestRunner 

```

There may have more option, but seldomly used. You can check it from [HERE](http://developer.android.com/reference/android/test/InstrumentationTestRunner.html)

Remark
---
1. Remember to update the apk and the testing apk if any changed.
	- Eclipse will help you to update automatically.
	- Manaully test need to update it by your self.

	
autoinstall.sh
----
A bash script for install the apk to all the connected device automatically.

It take four parameters.

```
source autoinstall.sh {p1} {p2} {p3} {p4}
```

Parameters| Meaning/Required value
-------- | --------
p1 | target apk location
p2 | target apk package name
p3 | testing case apk location
p4 | testing case apk package name

Example

```
source autoinstall.sh ~/Desktop/selendroid/app-prod-release.apk com.bindlechat.BindleTest  ~/AndroidStudioProjects/qa-automation/Robotium/bindle/BindleAutomationTest/bin/BindleAutomationTest.apk com.oursky.bindle.test

```

