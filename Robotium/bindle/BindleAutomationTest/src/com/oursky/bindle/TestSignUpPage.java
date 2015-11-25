package com.oursky.bindle;

import java.util.Calendar;

import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kitfung_Oursky on 23/11/15.
 */
public class TestSignUpPage extends AndroidTestBase {

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.oursky.bindle.SignUpActivity";
    
    private final Calendar bDate;
    private final Calendar wrongBDate;
    private final String registeredEmail = "TestAc@TestAc.com";
    private final String email = "TestAc2048@TestAc.com";
    private final String password = "TestAc2048";
    
    // **** Suggest you to set the new email and screen name everytime you run this test ****
    private final String newScreenName = "JvhJHBnxlASc";
    
    private final String registeredAccountMsg = "That email address is already in use!";
    private final String wrongEmailMsg = "Please provide correct Email address.";
    private final String emptyPasswordMsg = "Please provide your password.";
    private final String wrongPasswordMsg = "Please use a minimum of 6 characters in your password.";
    private final String emptyBDateMsg = "Please select your birthday.";
    private final String wrongBDateMsg = "The date doesn't look right.";
    
    private final String wrongScreenNameReminder = "Letters and numbers only.";
    
	public TestSignUpPage() throws ClassNotFoundException {
        super(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        bDate = Calendar.getInstance();
        bDate.set(2000, 1, 1);
        
        wrongBDate = Calendar.getInstance();
        wrongBDate.set(2046, 12, 21);
    }

// Part 1. Success Sign up
//==============================================
	@Smoke
	public void testSignUp() {
		device().waitForActivity("SignUpActivity");
		String tmpScreenName = newScreenName;
		int i = 0;
		while(true) {
			tmpScreenName = newScreenName + i;
			signUp(tmpScreenName + "@testing.com", password, bDate);
			if (device().searchText(registeredAccountMsg)) {
				device().clickOnText("OK");
			} else {
				break;
			}
			i++;
		}
		continueSignUp(tmpScreenName);
		device().assertCurrentActivity("Valid sign up fail", "SMSVerificationActivity");
        assertTrue("The sms alert message haven't appera after sign up", device().searchText("Login with existing account"));
        device().clickOnText("Login with existing account");
	}

// Part 2. Failed Sign up
//==============================================

	//When sign up with a registered email
	@SmallTest
	public void testSignUpWithRegisteredEmail() {
		device().waitForActivity("SignUpActivity");
		
		signUp(registeredEmail, password, bDate);
		
		device().assertCurrentActivity("Invalid sign up fail (Registered account)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Registered account)", device().searchText(registeredAccountMsg));
        device().clickOnText("OK");
	}
	
    //When user input nothing and press Next
	@SmallTest
    public void testSignUpWithoutEnterAnyInfo() {
    	device().waitForActivity("SignUpActivity");
        
    	signUp("", "", null);

        device().assertCurrentActivity("Invalid sign up fail (Empty info)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Empty info)", device().searchText(wrongEmailMsg));
    }
	
	//When user input wrong email format
		@SmallTest
	    public void testSignUpWithWrongEmailFormat() {
	    	device().waitForActivity("SignUpActivity");
	        
	    	signUp("test", password, bDate);

	    	device().assertCurrentActivity("Invalid sign up fail (Wrong Email Format)", "SignUpActivity");
	        assertTrue("Wrong toast displayed (Wrong Email Format)", device().searchText(wrongEmailMsg));
	    }
    
	//When user have not input the email
	@SmallTest
    public void testSignUpMissingEmail() {
    	device().waitForActivity("SignUpActivity");
        
    	signUp("", password, bDate);

    	device().assertCurrentActivity("Invalid sign up fail (Missing Email)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing Email)", device().searchText(wrongEmailMsg));
    }	
	
	//When user have not input the password
	@SmallTest
    public void testSignUpMissingPassword() {
    	device().waitForActivity("SignUpActivity");
        
    	signUp(email, "", bDate);

    	device().assertCurrentActivity("Invalid sign up fail (Missing Password)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing Password)", device().searchText(emptyPasswordMsg));
    }
	
	//When user input wrong password format
	@SmallTest
    public void testSignUpWrongPasswordFormat() {
    	device().waitForActivity("SignUpActivity");
        
    	signUp(email, "12345", bDate);

    	device().assertCurrentActivity("Invalid sign up fail (Wrong Password Format)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Wrong Password Format)", device().searchText(wrongPasswordMsg));
    }
	
	//When user haven't input birthday
	@SmallTest
	public void testMissingBDate() {
		device().waitForActivity("SignUpActivity");
        
    	signUp(email, password, null);
    	
    	device().assertCurrentActivity("Invalid sign up fail (Missing birthday)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing birthday)", device().searchText(emptyBDateMsg));
	}
	
	//When user input wrong birthday
	@SmallTest
	public void testWrongBDate() {
		device().waitForActivity("SignUpActivity");
        
    	signUp(email, password, wrongBDate);
    	
    	device().assertCurrentActivity("Invalid sign up fail (Missing birthday)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing birthday)", device().searchText(wrongBDateMsg));
	}
	
	//When user have not input screen name with space
	@SmallTest
    public void testSignUpScreenNameWithSpace() {
    	device().waitForActivity("SignUpActivity");
        
    	signUp(email, password, bDate);
    	continueSignUp("t t");
    	
    	device().assertCurrentActivity("Invalid sign up fail (Wrong screen name format)", "CreateScreenNameActivity");
        assertTrue("Wrong reminder displayed (Wrong screen name format)", device().searchText(wrongScreenNameReminder));
    }
	
//=============================

	public void signUp(String email, String password, Calendar bDate) {
		device().clearEditText((EditText)device().getView("id/email"));
    	device().enterText((EditText) device().getView("id/email"), email);
        device().clearEditText((EditText)device().getView("id/password"));
    	device().enterText((EditText) device().getView("id/password"), password);
    	
    	if(bDate != null) {
	    	LinearLayout bDateRow = (LinearLayout) device().getView("id/birthday_view");
	    	device().clickOnView(bDateRow);
	    	device().setDatePicker(0, bDate.get(Calendar.YEAR), bDate.get(Calendar.MONTH), bDate.get(Calendar.DAY_OF_MONTH));
	    	
	    	TextView enterBtn = (TextView) device().getView("id/button1");
	    	device().clickOnView(enterBtn);
    	}
    	
    	TextView nextBtn = (TextView) device().getView("id/action_next");
    	device().clickOnView(nextBtn);

    	Log.v("testing", device().getCurrentActivity().toString());
	}
	
	public void continueSignUp(String screenName) {
		device().waitForActivity("CreateScreenNameActivity");
		device().enterText((EditText) device().getView("id/screen_name_edit_text"), screenName);
		TextView doneBtn = (TextView) device().getView("id/action_done");
    	device().clickOnView(doneBtn);
	}
}