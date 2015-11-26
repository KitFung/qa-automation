package com.oursky.bindle;

import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;
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
    	device().waitForActivity("TutorialActivity");
        assertTrue("Tutorial Page haven't display", device().searchText("Welcome"));
    }
    
	@SmallTest
    public void testNextPageOfTutorialPage() {
    	device().waitForActivity("TutorialActivity");
    	
    	TextView NextBtn = (TextView) device().getView("action_done");
        device().clickOnView(NextBtn);
        
        device().waitForActivity("WelcomeActivity");
        
        device().assertCurrentActivity("Next Page of tutorial page is welcome page", "WelcomeActivity");
        
        assertTrue("Welcome Page don't have sign up button.", device().searchButton("Sign up"));
        assertTrue("Welcome Page don't have log in button.", device().searchButton("Log in"));
	}
    
}