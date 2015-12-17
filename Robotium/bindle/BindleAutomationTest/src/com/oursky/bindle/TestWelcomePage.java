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
		Log.d(TAG, ">>> Test: testSignUpBtn   -- Start -- <<<");

		Log.d(TAG, "Searching the signup button");
    	device().waitForActivity("WelcomeActivity");
        assertTrue("Sign up button not exist", device().searchButton("Sign up"));
        Log.d(TAG, "Found the signup button");
        
        Log.d(TAG, "Checking the redirect of the signup button");
        device().clickOnButton("Sign up");
        device().waitForActivity("SignUpActivity");
        device().assertCurrentActivity("The Sign Up Button haven't redirect the user to sign up page",
        		"SignUpActivity");
        Log.d(TAG, "Checked the redirect of the signup button");
        
        Log.d(TAG, ">>> Test: testSignUpBtn   -- End -- <<<");
	}
	 
	@Smoke
	public void testLoginBtn() {
		Log.d(TAG, ">>> Test: testLoginBtn   -- Start -- <<<");
		
		Log.d(TAG, "Searching the login button");
    	device().waitForActivity("WelcomeActivity");
        assertTrue("Log in button exist", device().searchButton("Log in"));
        Log.d(TAG, "Found the login button");
        
        Log.d(TAG, "Checking the redirect of the login button");
        device().clickOnButton("Log in");
        device().waitForActivity("LoginActivity");
        Log.i("1200120", device().getCurrentActivity().toString());
        device().assertCurrentActivity("The Log in Button haven't redirect the user to log in page",
        		"LoginActivity");
        Log.d(TAG, "Checked the redirect of the login button");

        Log.d(TAG, ">>> Test: testLoginBtn   -- End -- <<<");
	}
}
