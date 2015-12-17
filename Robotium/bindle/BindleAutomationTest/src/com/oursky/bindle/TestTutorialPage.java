package com.oursky.bindle;

import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;
import android.widget.TextView;


/**
 * Created by kitfung_Oursky on 23/11/15.
 */
public class TestTutorialPage extends AndroidTestBase {

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.oursky.bindle.TutorialActivity";
    
	public TestTutorialPage() throws ClassNotFoundException {
        super(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
    }
	
	@Smoke
    public void testTutorialPageExist() {
		Log.d(TAG, ">>> Test: testTutorialPageExist   -- Start -- <<<");
		Log.d(TAG, "check tutorial page exist");
    	device().waitForActivity("TutorialActivity");
        assertTrue("Tutorial Page haven't display", device().searchText("Welcome"));
        Log.d(TAG, "checked tutorial page exist");
        Log.d(TAG, ">>> Test: testTutorialPageExist   -- End -- <<<");
    }
    
	@SmallTest
    public void testNextPageOfTutorialPage() {
		Log.d(TAG, ">>> Test: testNextPageOfTutorialPage   -- Start -- <<<");
		
		Log.d(TAG, "load tutorial page");
    	device().waitForActivity("TutorialActivity");
    	TextView NextBtn = (TextView) device().getView("action_done");
    	Log.d(TAG, "loaded tutorial page");

    	Log.d(TAG, "Go to the next page of the tutorial page");
    	device().clickOnView(NextBtn);
        device().waitForActivity("WelcomeActivity");
        Log.d(TAG, "Successfully go to the next page of the tutorial page");

        Log.d(TAG, "Checking the welcome page component");
        device().assertCurrentActivity("Next Page of tutorial page is welcome page", "WelcomeActivity");
        assertTrue("Welcome Page don't have sign up button.", device().searchButton("Sign up"));
        assertTrue("Welcome Page don't have log in button.", device().searchButton("Log in"));
        Log.d(TAG, "Checked the welcome page component");

        Log.d(TAG, ">>> Test: testNextPageOfTutorialPage   -- End -- <<<");
	}
    
}