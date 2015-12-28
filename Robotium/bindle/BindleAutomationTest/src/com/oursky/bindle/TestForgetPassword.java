package com.oursky.bindle;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/**
 * It will only focus on the apps reaction, this will not really check the mailbox. 
 */

public class TestForgetPassword extends AndroidTestBase{
	
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.oursky.bindle.WelcomeActivity";
	private String name = "TestAc2";
	private String email = "TestAc2@TestAc.com";
	
	public TestForgetPassword() throws ClassNotFoundException {
		super(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		goToForgetPasswordPage();
		checkTitle();
	}
	
	public void testForgetPasswordWithAcName() {
		Log.d(TAG, ">>> Test: testForgetPasswordWithAcName   -- Start -- <<<");
		
		Log.d(TAG, "Filling the field and send request");
		sendForgetPasswordRequest(name);
		Log.d(TAG, "Sent");
		
		Log.d(TAG, "Checking the response");
		checkSendRequestSuccess();
		Log.d(TAG, "Checked");
		
		Log.d(TAG, ">>> Test: testForgetPasswordWithAcName   -- End -- <<<");
	}
	
	public void testForgetPasswordWithEmail() {
		Log.d(TAG, ">>> Test: testForgetPasswordWithEmail   -- Start -- <<<");
		
		Log.d(TAG, "Filling the field and send request");
		sendForgetPasswordRequest(email);
		Log.d(TAG, "Sent");
		
		Log.d(TAG, "Checking the response");
		checkSendRequestSuccess();
		Log.d(TAG, "Checked");

		Log.d(TAG, ">>> Test: testForgetPasswordWithEmail   -- End -- <<<");
	}
	
	public void testForgetPasswordWithNotExistEmail(){ 
		Log.d(TAG, ">>> Test: testForgetPasswordWithNotExistEmail   -- Start -- <<<");
		
		String notExistEmail = "notexistssss123@notexist.com";
		
		Log.d(TAG, "Filling the field and send request");
		sendForgetPasswordRequest(notExistEmail);
		Log.d(TAG, "Sent");

		Log.d(TAG, "Checking the response");
		checkSendRequestFailureByEmail(notExistEmail);
		Log.d(TAG, "Checked");

		Log.d(TAG, ">>> Test: testForgetPasswordWithNotExistEmail   -- End -- <<<");
	}
	
	public void testForgetPasswordWithNotExistUserName(){ 
		Log.d(TAG, ">>> Test: testForgetPasswordWithNotExistEmail   -- Start -- <<<");
		
		String notExistEmail = "notexistsaSCCs23";
		
		Log.d(TAG, "Filling the field and send request");
		sendForgetPasswordRequest(notExistEmail);
		Log.d(TAG, "Sent");

		Log.d(TAG, "Checking the response");
		checkSendRequestFailureByUserName();
		Log.d(TAG, "Checked");

		Log.d(TAG, ">>> Test: testForgetPasswordWithNotExistEmail   -- End -- <<<");
	}
	
//	public void testForgetPasswordWithWrongCase(){
//		String[] wrongCase = {
//				email.toLowerCase(), email.toUpperCase(),
//				name.toLowerCase(), name.toUpperCase(),
//				};
//		for(String s:wrongCase) {
//			sendForgetPasswordRequest(s);
//		}
//	}
	
	public void checkTitle() {
		assertTrue("The forget password page title is wrong",
				device().waitForText("Forgot Password"));
	}
	
	public void checkSendRequestSuccess() {
		assertTrue("Cannot find the success message",
				device().waitForText("Email sent! Please check your inbox."));
		device().clickOnButton("OK");
		device().assertCurrentActivity("It haven't redirect to the login page", "LoginActivity");
	}
	
	public void checkSendRequestFailureByEmail(String email) {
		Log.d(TAG, device().getText("no user found").toString());
		Log.d(TAG, device().getText("no user found").getText().toString());
		assertTrue("Cannot find the warning message",
				device().waitForText(String.format("no user found with email %s", email)));
		device().assertCurrentActivity("It should stay in current page", "ForgotPasswordActivity");
	}
	
	public void checkSendRequestFailureByUserName() {
		assertTrue("Cannot find the warning message",
				device().waitForText("Incorrect username."));
		device().assertCurrentActivity("It should stay in current page", "ForgotPasswordActivity");
	}
	
	public void sendForgetPasswordRequest(String value) {
		device().clearEditText((EditText)device().getView("id/email"));
    	device().enterText((EditText) device().getView("id/email"), value);
    	device().clickOnView((TextView) device().getView("id/action_send"));
	}
	
	public void goToForgetPasswordPage() {
		device().waitForActivity("WelcomeActivity");
    	device().clickOnButton("Log in");
    	device().waitForActivity("LoginActivity");
    	device().clickOnButton("Forgot password?");
    	device().waitForActivity("ForgotPasswordActivity");
	}
	
}
