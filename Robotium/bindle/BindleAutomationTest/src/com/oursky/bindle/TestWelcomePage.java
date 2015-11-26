package com.oursky.bindle;

import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;

public class TestWelcomePage extends AndroidTestBase{
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.oursky.bindle.WelcomeActivity";
	    
	public TestWelcomePage() throws ClassNotFoundException {
        super(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
    }

	@Smoke
	public void testSignUpBtn() {
    	device().waitForActivity("WelcomeActivity");
        assertTrue("Sign up button not exist", device().searchButton("Sign up"));
        
        device().clickOnButton("Sign up");
        device().waitForActivity("SignUpActivity");
        device().assertCurrentActivity("The Sign Up Button haven't redirect the user to sign up page",
        		"SignUpActivity");
	}
	 
	@Smoke
	public void testLoginBtn() {
    	device().waitForActivity("WelcomeActivity");
        assertTrue("Log in button exist", device().searchButton("Log in"));
    
        device().clickOnButton("Log in");
        device().waitForActivity("LoginActivity");
        Log.i("1200120", device().getCurrentActivity().toString());
        device().assertCurrentActivity("The Log in Button haven't redirect the user to log in page",
        		"LoginActivity");
        
	}
}
