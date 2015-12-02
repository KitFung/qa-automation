// test run variables
var deviceNo = -2;
// +ve: real device, -ve:simulator
// 1: iPhone6S, 2:iPhone6S+
var doSmokeTest = true;
var doEdgeTestSignup = true;
var doEdgeTestLogin = true;

// user input variables
var accountNo = 1; // tester1-4 signed up in staging
// remark: if you run smoke test and edge test together, accountNo i+1 will also be signed up
var username = "tester" + accountNo;
var screenname = "Tester" + accountNo;
var password = "123456";
var existingUser = "tester1";

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
/*------------------------------------------------------------------*/
// begin smoke test
if (doSmokeTest) {
    logStartMsg = "landing: enter sign up and log in screens";
    logPassMsg = "pass: all buttons in landing pressed";
    testLanding();
    
    logStartMsg = "sign up: enter correct info of new account";
    logPassMsg = "pass: " + username + " sign up success";
    testSignUp(username, screenname, password, password);
    
    logStartMsg = "log in: enter correct info of existing account";
    logPassMsg = "pass: " + username + " log in success";
    testLogIn(username, password);
}
// end smoke test
/*------------------------------------------------------------------*/
// begin edge test: sign up
if (doEdgeTestSignup) {
    if (deviceNo > 0) {
        logStartMsg = "sign up: offline";
        logPassMsg = "pass: invalid sign up fail";
        alertMessage = "The Internet connection appears to be offline";
        testSignUp(username, screenname, password, password, false, true, true);
    }
    
    logStartMsg = "sign up: empty username";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Missing username";  alertButton = "Dismiss";
    testSignUp("", screenname, password, password, false);
    
    logStartMsg = "sign up: empty display name";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Missing display name";  alertButton = "Dismiss";
    testSignUp(username, "", password, password, false);
    
    logStartMsg = "sign up: empty password";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Missing password";  alertButton = "Dismiss";
    testSignUp(username, screenname, "", password, false);
    
    logStartMsg = "sign up: empty confirm password";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Missing confirm password";  alertButton = "Dismiss";
    testSignUp(username, screenname, password, "", false);
    
    logStartMsg = "sign up: wrong confirm password";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Password does not match";  alertButton = "Dismiss";
    testSignUp(username, screenname, password, "111111", false);
    
    logStartMsg = "sign up: wrong username format";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Invalid username format";
    testSignUp("~!@#$%^&*\\\"/", screenname, password, password, false, true);
    
    logStartMsg = "sign up: username with space";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Invalid username format";
    testSignUp("test er", screenname, password, password, false, true);
    
    logStartMsg = "sign up: password too short";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Password length should be longer than";
    testSignUp(username, screenname, "12345", "12345", false, true);
    
    logStartMsg = "sign up: existing account";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Username is already taken";
    testSignUp(existingUser, screenname, password, password, false, true);
    
    logStartMsg = "sign up: existing account (upper case)";
    logPassMsg = "pass: invalid sign up fail";
    alertMessage = "Username is already taken";
    testSignUp(existingUser.toUpperCase(), screenname, password, password, false, true);
    
    logStartMsg = "sign up: correct info + landscape";
    logPassMsg = "pass: " + username + " sign up success";
    if (doSmokeTest) {
        username = "tester" + (accountNo+1);
        screenname = "Tester" + (accountNo+1);
    }
    testSignUp(username, screenname, password, password, true, false, false, true);
}
// end edge test: sign up
/*------------------------------------------------------------------*/
// begin edge test: log in
if (doEdgeTestLogin) {
    if (deviceNo > 0) {
        logStartMsg = "log in: offline";
        logPassMsg = "pass: invalid log in fail";
        alertMessage = "The Internet connection appears to be offline";
        testLogIn(username, password, false, true, true);
    }
    
    logStartMsg = "log in: empty username";
    logPassMsg = "pass: invalid log in fail";
    alertMessage = "Missing username";  alertButton = "Dismiss";
    testLogIn("", password, false);
    
    logStartMsg = "log in: empty password";
    logPassMsg = "pass: invalid log in fail";
    alertMessage = "Missing password";  alertButton = "Dismiss";
    testLogIn(username, "", false);
    
    logStartMsg = "log in: user not exist";
    logPassMsg = "pass: invalid log in fail";
    alertMessage = "Wrong username or password";
    testLogIn("tttttester", password, false, true);
    
    logStartMsg = "log in: username in upper case";
    logPassMsg = "pass: " + username + " log in success";
    testLogIn(username.toUpperCase(), password);
    
    logStartMsg = "log in: correct info + landscape";
    logPassMsg = "pass: " + username + " log in success";
    testLogIn(username, password, true, false, false, true);
}
// end edge test: log in
/*------------------------------------------------------------------*/

// test: landing
function testLanding() {
    UIALogger.logStart(logStartMsg);
    try {        
        // action: press buttons
        target.frontMostApp().mainWindow().buttons()["Sign Up"].tap();
        target.frontMostApp().navigationBar().leftButton().tap();
        target.frontMostApp().mainWindow().buttons()["Log In"].tap();
        target.frontMostApp().navigationBar().leftButton().tap();
        
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

// test: sign up
function testSignUp(username, screenname, password, password2, isValid, isHUD, isOffline, isLandscape) {
    if (isValid === undefined)
        isValid = true;
    if (isHUD === undefined)
        isHUD = false;
    if (isOffline === undefined)
        isOffline = false;
    if (isLandscape === undefined)
        isLandscape = false;
    
    UIALogger.logStart(logStartMsg);
    try {
        // action: sign up
        target.frontMostApp().mainWindow().buttons()["Sign Up"].tap();
        target.frontMostApp().keyboard().typeString(username);
        target.frontMostApp().mainWindow().tableViews()[0].cells()[1].textFields()[0].tap();
        target.frontMostApp().keyboard().typeString(screenname);
        target.frontMostApp().mainWindow().tableViews()[0].cells()[2].secureTextFields()[0].tap();
        target.frontMostApp().keyboard().typeString(password);
        target.frontMostApp().mainWindow().tableViews()[0].cells()[3].secureTextFields()[0].tap();
        target.frontMostApp().keyboard().typeString(password2);
        
        continueSignupLogin(username, isValid, isHUD, isOffline, isLandscape);
    }
    catch (err) {
        // fail
        UIALogger.logFail("fail: " + err.message);
        target.logElementTree();
        throw err;
    }
}

// test: log in
function testLogIn(username, password, isValid, isHUD, isOffline, isLandscape) {
    if (isValid === undefined)
        isValid = true;
    if (isHUD === undefined)
        isHUD = false;
    if (isOffline === undefined)
        isOffline = false;
    if (isLandscape === undefined)
        isLandscape = false;
    
    UIALogger.logStart(logStartMsg);
    try {
        // action: log in
        target.frontMostApp().mainWindow().buttons()["Log In"].tap();
        target.frontMostApp().keyboard().typeString(username);
        target.frontMostApp().mainWindow().tableViews()[0].cells()[1].secureTextFields()[0].tap();
        target.frontMostApp().keyboard().typeString(password);
        
        continueSignupLogin(username, isValid, isHUD, isOffline, isLandscape);
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
function continueSignupLogin(username, isValid, isHUD, isOffline, isLandscape) {    
    // do interruption if needed
    if (isOffline) {
        toggleAirplaneMode();
    }
    if (isLandscape) {
        target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_LANDSCAPELEFT);
    }
    
    // proceed to next step
    target.frontMostApp().navigationBar().rightButton().tap();
    target.delay(1);
    
    // check result
    if (isValid) {
        checkEnterCreateTopic(username);
    }
    else if (isHUD) {
        checkCorrectHUD(isOffline);
    }
    else {
        checkCorrectAlert();
    }
    
    // reverse interruption states
    if (isLandscape) {
        target.delay(1);
        target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
    }
}

function checkEnterCreateTopic(username) {
    // result: enter create topic
    var findTitle = target.frontMostApp().mainWindow().staticTexts().firstWithPredicate("name contains \"What's Up?\"");
    if (!findTitle.isValid()) {
        throw new Error("cannot find What's Up?");
    }
    UIALogger.logDebug("Enter Create Topic");
    // after action: logout
    target.frontMostApp().mainWindow().buttons()["Talk"].tap();
    target.frontMostApp().mainWindow().buttons()["@"+username.toLowerCase()].tap();
    target.frontMostApp().mainWindow().buttons()["Log Out"].tap();
    // log pass
    UIALogger.logPass(logPassMsg);
}

/*------------------------------------------------------------------*/
