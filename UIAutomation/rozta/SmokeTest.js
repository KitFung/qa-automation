var deviceNo = -2;

var username = "robot17@test.com";

var target = UIATarget.localTarget();

// Alert handler
UIATarget.onAlert = function onAlert(alert) {
    var title = alert.name();
    UIALogger.logDebug("Alert: " + title);
    if (title == "Logout") {
        target.frontMostApp().alert().buttons()["Logout"].tap();
        target.delay(1);
        return true;
    }
    return false;
}

target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);

// Sign up
target.frontMostApp().mainWindow().buttons()["Sign up"].tap();
target.frontMostApp().keyboard().typeString(username);
target.frontMostApp().mainWindow().scrollViews()[0].secureTextFields()[0].tap();
target.frontMostApp().keyboard().typeString("qwertyui\n");
target.frontMostApp().keyboard().typeString("Tester A");
target.frontMostApp().mainWindow().scrollViews()[0].buttons()["profile img"].tap();
if (deviceNo > 0) {
    target.frontMostApp().actionSheet().collectionViews()[0].cells()["Take Photo"].buttons()["Take Photo"].tap();
    target.frontMostApp().mainWindow().buttons()["FrontBackFacingCameraChooser"].tap();
    target.delay(2);
    target.frontMostApp().mainWindow().buttons()["PhotoCapture"].tap();
    target.frontMostApp().mainWindow().buttons()["Use Photo"].tap();
}
else {
    target.frontMostApp().actionSheet().collectionViews()[0].cells()["Library"].buttons()["Library"].tap();
    target.frontMostApp().mainWindow().tableViews()[0].cells()["Moments"].tap();
    target.frontMostApp().mainWindow().collectionViews()[0].cells()["Photo, Landscape, August 08, 2012, 2:55 PM"].tap();
}
target.delay(1);
target.frontMostApp().navigationBar().buttons()["forward btn"].tap();
target.delay(2);

// Edit profile
target.frontMostApp().navigationBar().buttons()[1].tap();
target.frontMostApp().mainWindow().tableViews()[0].cells()["Your Name"].tap();
target.frontMostApp().keyboard().keys()["delete"].tap();
target.frontMostApp().keyboard().typeString("B");
target.frontMostApp().mainWindow().tableViews()[0].cells()["Password"].tap();
target.frontMostApp().mainWindow().tableViews()[0].cells()[0].secureTextFields()[0].tap();
target.frontMostApp().keyboard().typeString("qwertyui\n12345678\n12345678");
target.frontMostApp().navigationBar().buttons()["Submit"].tap();
target.delay(7);

// Log out
target.frontMostApp().mainWindow().tableViews()[0].cells()["Logout"].tap();

// Log in
target.frontMostApp().mainWindow().buttons()["Login"].tap();
target.frontMostApp().keyboard().typeString(username+"\n12345678");
target.frontMostApp().navigationBar().buttons()["forward btn"].tap();

