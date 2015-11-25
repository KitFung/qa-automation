package com.oursky.bindle;

import java.util.Locale;

import com.robotium.solo.Solo;

import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;
import android.widget.EditText;
import android.widget.TextView;

public class TestLoginPage extends AndroidTestBase {

	// This will testing the login function, therefore it need to start from welcome page.
    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.oursky.bindle.WelcomeActivity";
	
	public TestLoginPage() throws ClassNotFoundException {
        super(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
    }
    
// Part 1. Login Success with SMS verify message
// This part don't need specific test, just test whether it can login and logout
// ===========================================================
	
	// This account haven't finish S.M.S. verify
    private String emailWithSmsVertify = "TestAcc@TestAcc.com";
	private String passwordWithSmsVertify = "TestPassword"; 
	
	//Login with email
	@SmallTest
    public void testLoginWithEmailWithSms() {
        goToLoginPage();
    	login(emailWithSmsVertify, passwordWithSmsVertify);
    	successLoginCheckingWithSms();
    	logOutInSMSPage();
    }
    
    public void successLoginCheckingWithSms() {
    	device().waitForActivity("SMSVerificationActivity");
        device().assertCurrentActivity("Valid Login failed(with sms)", "SMSVerificationActivity");
    }

    public void logOutInSMSPage() {
    	device().clickOnText("Login with existing account");
    }

// Part 2. Login Success without SMS verify message
// ===========================================================

    private String email = "TestAcc2@TestAcc.com";
    private String userName = "TestAc2"; 
	private String password = "TestPassword2";
	
	
	//Login with email
	@Smoke
    public void testLoginWithEmail() {
        goToLoginPage();
    	login(email, password);
    	successLoginChecking(email);
        logOut();
    }
    
    //Login with user name
	@SmallTest
    public void testLoginWithUserName() {
    	goToLoginPage();
    	login(userName, password);
    	successLoginChecking(userName);
        logOut();
    }
    
    //Login using uppercase account name
	@SmallTest
    public void testLoginWithUpperCaseUserName() {
    	goToLoginPage();
    	login(userName.toUpperCase(Locale.ENGLISH), password);
    	successLoginChecking(userName.toUpperCase(Locale.ENGLISH));
        logOut();
    }
    
    //Login in landscape with correct info
	@SmallTest
    public void testLoginInLandscape() {
    	device().setActivityOrientation(Solo.LANDSCAPE);
    	
    	goToLoginPage();
		login(userName, password);
		successLoginChecking(userName);
        logOut();
    	
    	device().setActivityOrientation(Solo.PORTRAIT);
    }
    
    public void successLoginChecking(String loginAccount) {
    	device().waitForActivity("LobbyActivity");
    	String checkingMsg = String.format("Failed Login to account: %s", loginAccount);
        device().assertCurrentActivity(checkingMsg, "LobbyActivity");
    }
    
    public void logOut() {
    	TextView settingBtn = (TextView) device().getView("action_settings");
        device().clickOnView(settingBtn);
        device().waitForActivity("SettingsActivity");
    	device().clickOnText("Log out");
    }
    
// Part 3. Login Failure
// ===========================================================
    
    //Login with wrong password
    @SmallTest
    public void testLoginWithWrongPassword() {
    	goToLoginPage();
    	login(userName, "wrong");
    	assertTrue("Invalid log in fail (Wrong password)",
    			device().searchText("That seems to be the wrong password. Try again."));
    }
    
    //Login without user account and password
    @SmallTest
    public void testLoginMissingAccountAndPassword() {
    	goToLoginPage();
    	login("", "");
        assertTrue("Invalid log in fail (Empty info)",
        		device().searchText("Please provide your email or screen name."));
    }
    
    //Login without user account
    @SmallTest
    public void testLoginMissingAccount() {
    	goToLoginPage();
    	login("", password);
    	assertTrue("Invalid log in fail (Empty account)",
    			device().searchText("Please provide your email or screen name."));
    }
    
    //Login without password()
    @SmallTest
    public void testLoginMissingPassword() {
    	goToLoginPage();
    	login(userName, "");
    	assertTrue("Invalid log in fail (Empty password)",
    			device().searchText("Please provide your password."));
    }

//===============================
    
    public void login(String account, String password) {
    	device().clearEditText((EditText)device().getView("id/email"));
    	device().enterText((EditText) device().getView("id/email"), account);
        device().clearEditText((EditText)device().getView("id/password"));
    	device().enterText((EditText) device().getView("id/password"), password);
    	TextView nextBtn = (TextView) device().getView("id/action_done");
        device().clickOnView(nextBtn);
    }
    
    public void goToLoginPage() {
    	device().waitForActivity("WelcomeActivity");
    	device().clickOnButton("Log in");
    	device().waitForActivity("LoginActivity");
    }

}
