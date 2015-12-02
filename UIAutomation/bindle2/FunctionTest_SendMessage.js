// test run variables
var deviceNo = -2;
// +ve: real device, -ve:simulator
// 1: iPhone6S, 2:iPhone6S+
var doSmokeTest = true;

// user input variables
var username = "tester1";
var screenname = "Tester1";
var password = "123456";

// strings
var alertMessage = "";
var alertButton = "";
var logStartMsg = "";
var logPassMsg = "";

// position values
var dragNativeMenuStartX = 0;
var dragNativeMenuStartY = 0;
var dragNativeMenuEndX = 0;
var dragNativeMenuEndY = 0;

if (Math.abs(deviceNo) == 1) {
    dragNativeMenuStartX = 210;
    dragNativeMenuStartY = 730;
    dragNativeMenuEndX = 200;
    dragNativeMenuEndY = 130;
} else if (Math.abs(deviceNo) == 2) {
    dragNativeMenuStartX = 180;
    dragNativeMenuStartY = 665;
    dragNativeMenuEndX = 150;
    dragNativeMenuEndY = 130;
}

// define app target
var target = UIATarget.localTarget();
target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);

// preload alert handler
UIATarget.onAlert = function onAlert(alert) {
    try {
        var findAlert = alert.scrollViews()[0].staticTexts().firstWithPredicate("name contains \"" + alertMessage + "\"");
        if (!findAlert.isValid()) {
            throw new Error("cannot find correct alert");
        }
        UIALogger.logDebug("alert: " + alert.scrollViews()[0].staticTexts()[1].name());
        alert.buttons()[alertButton].tap();
        target.delay(1);
        return true;
    }
    catch (err) {
        // fail
        UIALogger.logFail("fail: " + err.message);
        alert.logElementTree();
        throw err;
    }
    return false;
}

// main test run
beforeTest();
/*------------------------------------------------------------------*/
// begin smoke test
if (doSmokeTest) {
    logStartMsg = "add comment: send text message";
    logPassMsg = "pass: text message comment sent";
    testSendText("Automation Test");
}
afterTest();
// end smoke test
/*------------------------------------------------------------------*/

// test: send text message comment
function testSendText(textTopic) {
    
    UIALogger.logStart(logStartMsg);
    try {
        // action: add a text comment
        target.frontMostApp().mainWindow().tableViews()[0].cells()[screenname].tap();
        target.delay(1);
        target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].staticTexts()["Add your thoughts..."].tap();
        target.frontMostApp().keyboard().typeString(textTopic);
        target.frontMostApp().keyboard().typeString("\n");
        target.delay(1);
        
        // result: find the comment appearing in the room
        var findUser = target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[0].cells()[0].staticTexts().firstWithPredicate("name contains \"@"+username+"\"");
        var findComment = target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[0].cells()[0].staticTexts().firstWithPredicate("name contains \""+textTopic+"\"");
        if (!findUser.isValid() || !findComment.isValid()) {
            throw new Error("cannot find comment");
        }
        
        // pass
        UIALogger.logPass(logPassMsg);
        afterUnitTest();
    }
    catch (err) {
        // fail
        UIALogger.logFail("fail: " + err.message);
        target.logElementTree();
        throw err;
    }
}

/*------------------------------------------------------------------*/

// common functions
function toggleAirplaneMode() {
    target.delay(1);
    target.flickFromTo({x:dragNativeMenuStartX, y:dragNativeMenuStartY}, {x:dragNativeMenuEndX, y:dragNativeMenuEndY});
    target.frontMostApp().mainWindow().scrollViews()[0].elements()["Airplane Mode"].tap();
    target.tap({x:dragNativeMenuEndX, y:dragNativeMenuEndY});
}

function checkCorrectHUD(isOffline) {
    if (isOffline === undefined)
        isOffline = false;
    
    // result: show alert in HUD form
    var str = target.frontMostApp().mainWindow().elements()["SVProgressHUD"].label();
    if (str.indexOf(alertMessage) < 0) {
        throw new Error("cannot detect alert");
    }
    UIALogger.logDebug("alert: " + str);
    
    // after action: turn on network, click back button
    if (isOffline) {
        toggleAirplaneMode();
    }
    target.delay(2);
    target.frontMostApp().navigationBar().leftButton().tap();
    
    // log pass
    UIALogger.logPass(logPassMsg);
    target.delay(1);
    
}

function checkCorrectAlert() {
    // result: show alert (detect in handler)
    target.delay(1);
    // after action: back
    target.frontMostApp().navigationBar().leftButton().tap();            
    // log pass
    UIALogger.logPass(logPassMsg);
    target.delay(1);
}

/*------------------------------------------------------------------*/

// specific functions
function beforeTest() {
    target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
    target.frontMostApp().mainWindow().buttons()["Log In"].tap();
    target.frontMostApp().keyboard().typeString(username);
    target.frontMostApp().mainWindow().tableViews()[0].cells()[1].secureTextFields()[0].tap();
    target.frontMostApp().keyboard().typeString(password);
    target.frontMostApp().navigationBar().rightButton().tap();
    target.frontMostApp().mainWindow().buttons()["Talk"].tap();
}

function afterTest() {
    target.frontMostApp().mainWindow().buttons()["@"+username.toLowerCase()].tap();
    target.frontMostApp().mainWindow().buttons()["Log Out"].tap();
}

function afterUnitTest() {
    target.frontMostApp().mainWindow().scrollViews()[0].buttons()["nav up"].tap();
}

/*------------------------------------------------------------------*/
