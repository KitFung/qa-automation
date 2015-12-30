package com.oursky.bindle;

import java.util.Calendar;

import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by kitfung_Oursky on 23/11/15.
 */
public class TestSignUpPage extends AndroidTestBase {

	public static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.oursky.bindle.WelcomeActivity";
    
    private final Calendar bDate;
    private final Calendar wrongBDate;
    private final String registeredEmail = "TestAc@TestAc.com";
    private final String email = "TestAc2048@TestAc.com";
    private final String password = "TestAc2048";
    private final String magicPhoneNo = "61234567";
    
    // **** Suggest you to set the new email and screen name everytime you run this test ****
    private final String newScreenName = "JvhomMASc";
    
    private final String registeredAccountMsg = "That email address is already in use!";
    private final String wrongEmailMsg = "Please provide correct Email address.";
    private final String emptyPasswordMsg = "Please provide your password.";
    private final String wrongPasswordMsg = "Please use a minimum of 6 characters in your password.";
    private final String emptyBDateMsg = "Please select your birthday.";
    private final String wrongBDateMsg = "The date doesn't look right.";
    private final String arbitraryVerifyCode = "1234";
    private final String regionCodeHK = "+852";
    
    private final String wrongScreenNameReminder = "Letters and numbers only.";
    
	public TestSignUpPage() throws ClassNotFoundException {
        super(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        bDate = Calendar.getInstance();
        bDate.set(2000, 1, 1);
        
        wrongBDate = Calendar.getInstance();
        wrongBDate.set(2046, 12, 21);
    }
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		device().waitForActivity("WelcomeActivity");
    	device().clickOnButton("Sign up");
    	device().waitForActivity("SignUpActivity");
	}

// Part 1. Success Sign up
//==============================================
	@Smoke
	public void testSignUp() {
		Log.d(TAG, ">>> Test: testSignUp   -- Start -- <<<");

		device().waitForActivity("SignUpActivity");
		String tmpScreenName = newScreenName;
		int i = 0;
		Log.d(TAG, "Signing up");
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
		Log.d(TAG, "Signed up");
		
		Log.d(TAG, "Check sms page appear");
		device().assertCurrentActivity("Valid sign up fail", "SMSVerificationActivity");
        assertTrue("The sms alert message haven't appera after sign up", device().searchText("Login with existing account"));
        Log.d(TAG, "Checked sms page");
        device().clickOnText("Login with existing account");

        Log.d(TAG, ">>> Test: testSignUp   -- End -- <<<");
	}
	
	@Smoke
	public void testSignUpAndAutoCreateRoom() {
		Log.d(TAG, ">>> Test: testSignUpAndAutoCreateRoom   -- Start -- <<<");

		device().waitForActivity("SignUpActivity");
		String tmpScreenName = newScreenName;
		int i = 0;
		Log.d(TAG, "Signing up");
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
		Log.d(TAG, "Signed up");
		
		Log.d(TAG, "Check sms page appear");
		device().assertCurrentActivity("Valid sign up fail", "SMSVerificationActivity");
        assertTrue("The sms alert message haven't appera after sign up", device().searchText("Login with existing account"));
        Log.d(TAG, "Checked sms page");
        
        Log.d(TAG, "Finishing the sms verification");
        finishSMSVerify(magicPhoneNo, arbitraryVerifyCode, regionCodeHK);
        Log.d(TAG, "Finished");
        
        device().waitForActivity("LobbyActivity");
        
        Log.d(TAG, "Checking the first alert box");
        assertTrue("The first alert message don't not exist", 
        		device().waitForText("We made your first chat for you. Check it out!"));
        device().clickOnButton(0);
        Log.d(TAG, "Checked");
        
        Log.d(TAG, "Checking the auto-create chat room");
        ListAdapter la = ((ListView) device().getView("id/channelListView")).getAdapter();
        int sizeOfList = ((ListView) device().getView("id/channelListView")).getAdapter().getCount();
        assertNull("The number of auto-create chat room is wrong", la.getItem(1));
        assertEquals("The number of auto-create chat room is wrong", 2, sizeOfList);
        Log.d(TAG, "Checked");
        
        logOut();
        
        Log.d(TAG, ">>> Test: testSignUpAndAutoCreateRoom   -- End -- <<<");
	}

// Part 2. Failed Sign up
//==============================================

	//When sign up with a registered email
	@SmallTest
	public void testSignUpWithRegisteredEmail() {
		Log.d(TAG, ">>> Test: testSignUpWithRegisteredEmail   -- Start -- <<<");

		device().waitForActivity("SignUpActivity");
		
		Log.d(TAG, "Checking the response when try to sign up with a used account");
		signUp(registeredEmail, password, bDate);
		device().assertCurrentActivity("Invalid sign up fail (Registered account)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Registered account)", device().searchText(registeredAccountMsg));
        Log.d(TAG, "Checked");

        device().clickOnText("OK");
        
        Log.d(TAG, ">>> Test: testSignUpWithRegisteredEmail   -- End -- <<<");
	}
	
    //When user input nothing and press Next
	@SmallTest
    public void testSignUpWithoutEnterAnyInfo() {
		Log.d(TAG, ">>> Test: testSignUpWithoutEnterAnyInfo   -- Start -- <<<");
		
    	device().waitForActivity("SignUpActivity");
    	
    	Log.d(TAG, "Check response when sign up without any info");
    	signUp("", "", null);
        device().assertCurrentActivity("Invalid sign up fail (Empty info)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Empty info)", device().searchText(wrongEmailMsg));
        Log.d(TAG, "Checked");
        
        Log.d(TAG, ">>> Test: testSignUpWithoutEnterAnyInfo   -- End -- <<<");
	}
	
	//When user input wrong email format
		@SmallTest
	    public void testSignUpWithWrongEmailFormat() {
			Log.d(TAG, ">>> Test: testSignUpWithWrongEmailFormat   -- Start -- <<<");

	    	device().waitForActivity("SignUpActivity");
	        
	    	Log.d(TAG, "Check response when sign up with wrong email format");
	    	signUp("test", password, bDate);
	    	device().assertCurrentActivity("Invalid sign up fail (Wrong Email Format)", "SignUpActivity");
	        assertTrue("Wrong toast displayed (Wrong Email Format)", device().searchText(wrongEmailMsg));
	        Log.d(TAG, "Checked");

	        Log.d(TAG, ">>> Test: testSignUpWithWrongEmailFormat   -- End -- <<<");
		}
    
	//When user have not input the email
	@SmallTest
    public void testSignUpMissingEmail() {
		Log.d(TAG, ">>> Test: testSignUpMissingEmail   -- Start -- <<<");

    	device().waitForActivity("SignUpActivity");
        
    	Log.d(TAG, "Check response when sign up with missing email");
    	signUp("", password, bDate);
    	device().assertCurrentActivity("Invalid sign up fail (Missing Email)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing Email)", device().searchText(wrongEmailMsg));
        Log.d(TAG, "Checked");

        Log.d(TAG, ">>> Test: testSignUpMissingEmail   -- End -- <<<");
	}	
	
	//When user have not input the password
	@SmallTest
    public void testSignUpMissingPassword() {
		Log.d(TAG, ">>> Test: testSignUpMissingPassword   -- Start -- <<<");

    	device().waitForActivity("SignUpActivity");
        
    	Log.d(TAG, "Check response when sign up with missing password");
    	signUp(email, "", bDate);
    	device().assertCurrentActivity("Invalid sign up fail (Missing Password)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing Password)", device().searchText(emptyPasswordMsg));
        Log.d(TAG, "Checked");
        
        Log.d(TAG, ">>> Test: testSignUpMissingPassword   -- End -- <<<");
    }
	
	//When user input wrong password format
	@SmallTest
    public void testSignUpWrongPasswordFormat() {
		Log.d(TAG, ">>> Test: testSignUpWrongPasswordFormat   -- Start -- <<<");

    	device().waitForActivity("SignUpActivity");
        
    	Log.d(TAG, "Check response when sign up with wrong password format");
    	signUp(email, "12345", bDate);
    	device().assertCurrentActivity("Invalid sign up fail (Wrong Password Format)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Wrong Password Format)", device().searchText(wrongPasswordMsg));
        Log.d(TAG, "Checked");
        
        Log.d(TAG, ">>> Test: testSignUpWrongPasswordFormat   -- End -- <<<");
    }
	
	//When user haven't input birthday
	@SmallTest
	public void testMissingBDate() {
		Log.d(TAG, ">>> Test: testMissingBDate   -- Start -- <<<");

		device().waitForActivity("SignUpActivity");
        
		Log.d(TAG, "Check response when sign up without brithday");
    	signUp(email, password, null);
    	device().assertCurrentActivity("Invalid sign up fail (Missing birthday)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing birthday)", device().searchText(emptyBDateMsg));
        Log.d(TAG, "Checked");
	
        Log.d(TAG, ">>> Test: testMissingBDate   -- End -- <<<");
	}
	
	//When user input wrong birthday
	@SmallTest
	public void testWrongBDate() {
		Log.d(TAG, ">>> Test: testWrongBDate   -- Start -- <<<");
	
		device().waitForActivity("SignUpActivity");
        
		Log.d(TAG, "Check response when sign up with wrong brithday");
    	signUp(email, password, wrongBDate);
    	device().assertCurrentActivity("Invalid sign up fail (Missing birthday)", "SignUpActivity");
        assertTrue("Wrong toast displayed (Missing birthday)", device().searchText(wrongBDateMsg));
        Log.d(TAG, "Checked");
        
        Log.d(TAG, ">>> Test: testWrongBDate   -- End -- <<<");
	}
	
	//When user have not input screen name with space
	@SmallTest
    public void testSignUpScreenNameWithSpace() {
		Log.d(TAG, ">>> Test: testSignUpScreenNameWithSpace   -- Start -- <<<");

    	device().waitForActivity("SignUpActivity");
        
    	Log.d(TAG, "Check response when sign up with screen name with space");
    	signUp(email, password, bDate);
    	continueSignUp("t t");
    	device().assertCurrentActivity("Invalid sign up fail (Wrong screen name format)", "CreateScreenNameActivity");
        assertTrue("Wrong reminder displayed (Wrong screen name format)", device().searchText(wrongScreenNameReminder));
        Log.d(TAG, "Checked");
        
        Log.d(TAG, ">>> Test: testSignUpScreenNameWithSpace   -- End -- <<<");
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
		device().clearEditText((EditText) device().getView("id/screen_name_edit_text"));
		device().enterText((EditText) device().getView("id/screen_name_edit_text"), screenName);
		TextView doneBtn = (TextView) device().getView("id/action_done");
    	device().clickOnView(doneBtn);
    	device().waitForActivity("SMSVerificationActivity");
	}
	
	public void finishSMSVerify(String phoneNo, String verifyCode, String regionCode) {
		device().clearEditText((EditText)device().getView("id/region_code"));
    	device().enterText((EditText) device().getView("id/region_code"), regionCode);
        device().clearEditText((EditText)device().getView("id/phone_number"));
    	device().enterText((EditText) device().getView("id/phone_number"), phoneNo);
    	device().clickOnButton("Send Code");
    	
    	device().sleep(2000);
    	do {
    		this.getInstrumentation().sendStringSync(verifyCode);
    		device().sleep(2000);
    		device().clickOnView(device().getView("id/container"));
    		device().clickOnView(device().getView("id/container"));
    	} while (device().waitForText("Oops"));
		device().clickOnButton("Done");
	}
	
	public void logOut() {
		device().goBackToActivity("LobbyActivity");
        device().clickOnView((TextView) device().getView("action_settings"));
        device().waitForActivity("SettingsActivity");
    	device().clickOnText("Log out");
    }
}