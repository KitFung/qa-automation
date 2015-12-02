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
    /*logStartMsg = "create topic: share text";
    logPassMsg = "pass: text topic created";
    testCreateTextTopic("Automation Test");*/
    
    logStartMsg = "create topic: share image";
    logPassMsg = "pass: image topic created";
    testCreateImageTopic();
    
    logStartMsg = "create topic: share link";
    logPassMsg = "pass: link topic created";
    testCreateLinkTopic();
}
afterTest();
// end smoke test
/*------------------------------------------------------------------*/

// test: create text topic
function testCreateTextTopic(textTopic) {
    UIALogger.logStart(logStartMsg);
    try {
        // action: share a text topic
        target.frontMostApp().mainWindow().buttons()["share text btn"].tap();
        target.frontMostApp().keyboard().typeString(textTopic);
        // also test the cross button
        target.frontMostApp().mainWindow().buttons()["green cross"].tap();
        target.frontMostApp().mainWindow().buttons()["share text btn"].tap();
        target.frontMostApp().keyboard().typeString(textTopic);
        target.frontMostApp().mainWindow().buttons()["Share"].tap();
        target.delay(1);
        
        // result: first enter Lobby, then enter the topic room
        var lobbyName = target.frontMostApp().navigationBar().name();
        if (lobbyName !== "LobbyView") {
            throw new Error("cannot enter lobby");
        }
        target.frontMostApp().mainWindow().tableViews()[0].cells()[screenname].tap();
        var findTopic = target.frontMostApp().mainWindow().scrollViews()[0].staticTexts().firstWithPredicate("name contains \""+textTopic+"\"");
        if (!findTopic.isValid()) {
            throw new Error("cannot find topic");
        }
        
        // pass
        UIALogger.logPass(logPassMsg);
    }
    catch (err) {
        // fail
        UIALogger.logFail("fail: " + err.message);
        target.logElementTree();
        throw err;
    }
}

// test: create image topic
function testCreateImageTopic() {
    UIALogger.logStart(logStartMsg);
    try {
        // action: share an image topic
        target.frontMostApp().mainWindow().buttons()["share camera btn"].tap();
        target.frontMostApp().actionSheet().collectionViews()[0].cells()["From Gallery"].buttons()["From Gallery"].tap();
        // define the photo to choose for each testing device
        if (deviceNo == -2) {
            target.frontMostApp().mainWindow().tableViews()[0].cells()["Moments"].tap();
            target.frontMostApp().mainWindow().collectionViews()[0].cells()["Photo, Landscape, August 09, 2012, 5:55 AM"].tap();
        }
        // also test the cross button
        target.frontMostApp().mainWindow().buttons()["blue cross"].tap();
        target.frontMostApp().mainWindow().buttons()["share camera btn"].tap();
        target.frontMostApp().actionSheet().collectionViews()[0].cells()["From Gallery"].buttons()["From Gallery"].tap();
        // define the photo to choose for each testing device
        if (deviceNo == -2) {
            target.frontMostApp().mainWindow().tableViews()[0].cells()["Camera Roll"].tap();
            target.frontMostApp().mainWindow().collectionViews()[0].cells()["Photo, Landscape, March 13, 2011, 8:17 AM"].tap();
        }
        target.frontMostApp().mainWindow().buttons()["Share"].tap();
        target.delay(1);
        
        // result: first enter Lobby, then enter the topic room
        var lobbyName = target.frontMostApp().navigationBar().name();
        if (lobbyName !== "LobbyView") {
            throw new Error("cannot enter lobby");
        }
        target.frontMostApp().mainWindow().tableViews()[0].cells()[screenname].tap();
        var findTopic = target.frontMostApp().mainWindow().scrollViews()[0].staticTexts().firstWithPredicate("name contains \"View Photo\"");
        if (!findTopic.isValid()) {
            throw new Error("cannot find topic");
        }
        else {
            // tap to enlarge the image
            target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["View Photo"].tap();
            target.delay(4);
            target.frontMostApp().mainWindow().scrollViews()[0].images()[0].tap();
            target.delay(1);
            target.frontMostApp().navigationBar().rightButton().tap();
        }
        
        // pass
        UIALogger.logPass(logPassMsg);
    }
    catch (err) {
        // fail
        UIALogger.logFail("fail: " + err.message);
        target.logElementTree();
        throw err;
    }
}

// test: create link topic
function testCreateLinkTopic() {
    UIALogger.logStart(logStartMsg);
    try {
        
        
        // pass
        UIALogger.logPass(logPassMsg);
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
}

function afterTest() {
    target.frontMostApp().mainWindow().scrollViews()[0].buttons()["nav up"].tap();
    target.frontMostApp().mainWindow().buttons()["@"+username.toLowerCase()].tap();
    target.frontMostApp().mainWindow().buttons()["Log Out"].tap();
}

/*------------------------------------------------------------------*/
