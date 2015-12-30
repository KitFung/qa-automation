var deviceNo = -3;

var username = "hahaha4@test.com";

var target = UIATarget.localTarget();

// Alert handler
UIATarget.onAlert = function onAlert(alert) {
    var title = alert.name();
    UIALogger.logDebug("Alert: " + title);
    var alertHandler = {
        "Logout": function(){
            target.frontMostApp().alert().buttons()["Logout"].tap();
            target.delay(1);
            return true;
        },
        "Add to Calendar": function() {
            target.frontMostApp().alert().buttons()["OK"].tap();
            target.delay(1);
            return true;
        },
        "Delete activity": function() {
            target.frontMostApp().alert().buttons()["Delete"].tap();
            target.delay(1);
            return true;
        },
    }
    if(alertHandler[title])
        return alertHandler[title]();
    else
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

//Create Activity
target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
target.delay(2);
target.frontMostApp().mainWindow().tableViews()[0].cells()[0].tapWithOptions({tapOffset:{x:0.50, y:0.70}});
target.frontMostApp().mainWindow().collectionViews()[0].cells()["Dining"].buttons()[0].tap();
target.frontMostApp().mainWindow().collectionViews()[0].cells()["Activity Name"].textFields()[0].textFields()[0].tap();
target.frontMostApp().keyboard().keys()["delete"].touchAndHold(2.0);
target.frontMostApp().keyboard().typeString("qwe");

target.frontMostApp().mainWindow().collectionViews()[0].cells()[2].textFields()[0].textFields()[0].tap();
target.frontMostApp().windows()[3].buttons()["arrow right"].tap();
target.delay(1);
target.frontMostApp().windows()[3].buttons()[35].tap();
target.frontMostApp().mainWindow().collectionViews()[0].cells()[3].textFields()[0].textFields()[0].tap();
target.frontMostApp().windows()[3].pickers()[3].wheels()[0].tapWithOptions({tapOffset:{x:0.45, y:0.59}});
target.frontMostApp().mainWindow().buttons()["Next"].tap();
target.frontMostApp().mainWindow().buttons()["Done"].tap();
target.frontMostApp().mainWindow().buttons()[3].tapWithOptions({tapCount:3});
target.delay(1);
target.frontMostApp().mainWindow().buttons()["Done"].tap();
target.delay(2);

//Edit a column
target.frontMostApp().mainWindow().collectionViews()[0].cells()[0].textFields()[0].textFields()[0].tap();
target.delay(1);
for(var i=0; i< 3; i ++) {target.tap({x:347.00, y:491.67});
    target.frontMostApp().windows()[3].pickers()[0].wheels()[0].tapWithOptions({tapOffset:{x:0.50, y:0.76}});
}
target.frontMostApp().windows()[1].buttons()["Done"].tap();

//Add a Row
target.frontMostApp().mainWindow().buttons()["activity plus btn"].tap();
target.frontMostApp().keyboard().typeString("Tester A");
target.frontMostApp().windows()[1].buttons()["Done"].tap();
target.frontMostApp().mainWindow().collectionViews()[0].cells()[4].textFields()[0].textFields()[0].tap();
target.delay(1);
for(var i=0; i< 5; i ++) {
    target.frontMostApp().windows()[3].pickers()[0].wheels()[0].tapWithOptions({tapOffset:{x:0.50, y:0.76}});
}
target.frontMostApp().windows()[1].buttons()["Done"].tap();

//Add to caldender
target.frontMostApp().mainWindow().buttons()["activity calendar"].tap();
target.delay(5);

//Edit activity 
target.frontMostApp().mainWindow().buttons()["activity info inactive"].tap();
target.frontMostApp().mainWindow().buttons()["Edit Activity"].tap();
target.delay(2);
target.frontMostApp().mainWindow().collectionViews()[0].cells()[2].textFields()[0].textFields()[0].tap();
target.delay(1);
target.frontMostApp().windows()[3].buttons()["arrow right"].tap();
target.delay(1);
target.frontMostApp().windows()[3].buttons()[10].tap();
target.frontMostApp().mainWindow().buttons()["Next"].tap();
target.frontMostApp().mainWindow().tableViews()[0].tap();
target.frontMostApp().mainWindow().tableViews()[0].cells()["# children"].tap();
target.frontMostApp().mainWindow().buttons()["Done"].tap();

//Comment a activity
target.frontMostApp().mainWindow().buttons()["Participants"].tap();
target.frontMostApp().mainWindow().buttons()["Comments"].tap();
target.delay(1);
target.frontMostApp().mainWindow().tableViews()["Add Comment"].textViews()[0].staticTexts()["Add Comment"].tap();
target.frontMostApp().keyboard().typeString("Hi");
target.frontMostApp().mainWindow().tableViews()["Empty list"].buttons()["activity comment post sign"].tap();

//Delete activity
target.frontMostApp().mainWindow().buttons()["More Info"].tap();
target.frontMostApp().mainWindow().buttons()["Delete Activity"].tap();
target.delay(5);

//Log Out
target.frontMostApp().navigationBar().buttons()[1].tap();
target.delay(1);
target.frontMostApp().mainWindow().tableViews()[0].cells()["Logout"].tap();
target.delay(5);





