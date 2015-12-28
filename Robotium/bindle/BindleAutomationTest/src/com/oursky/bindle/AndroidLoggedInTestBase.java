package com.oursky.bindle;

import android.widget.EditText;
import android.widget.TextView;

public class AndroidLoggedInTestBase extends AndroidTestBase{
	public static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.oursky.bindle.WelcomeActivity";
	public String[] allUserName = {"TestAc2", "TestAc3", "TestAc4"};
	public String[] allUserPassword = {"TestPassword2", "TestPassword3", "TestPassword4"};

	public AndroidLoggedInTestBase() throws ClassNotFoundException {
        super(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
    }
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		logIn();
	}
	
	@Override
	public void tearDown() throws Exception {
		logOut();
		super.tearDown();
	}

	public void switchAccountToTestAc2() {
		logOut();
		logInToDefaultAccount(0);
	}

	public void switchAccountToTestAc3() {
		logOut();
		logInToDefaultAccount(1);
	}
	
	public void switchAccountToTestAc4() {
		logOut();
		logInToDefaultAccount(2);
	}
	
	public void logIn() {
		logInToDefaultAccount(0);
	}
	
	public void logInToDefaultAccount(int idx) {
		String userName = allUserName[idx]; 
		String password = allUserPassword[idx];
		logIn(userName, password);
	}
	
	public void logIn(String userName, String password) {
		device().waitForActivity("WelcomeActivity");
    	device().clickOnButton("Log in");
    	device().waitForActivity("LoginActivity");
    	device().clearEditText((EditText)device().getView("id/email"));
    	device().enterText((EditText) device().getView("id/email"), userName);
        device().clearEditText((EditText)device().getView("id/password"));
    	device().enterText((EditText) device().getView("id/password"), password);
        device().clickOnView((TextView) device().getView("id/action_done"));
        device().waitForActivity("LobbyActivity");
	}
	
	public void logOut() {
		device().goBackToActivity("LobbyActivity");
        device().clickOnView((TextView) device().getView("action_settings"));
        device().waitForActivity("SettingsActivity");
    	device().clickOnText("Log out");
    }

}
