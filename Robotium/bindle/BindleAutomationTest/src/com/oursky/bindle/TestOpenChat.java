package com.oursky.bindle;

import java.util.UUID;

import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class TestOpenChat extends AndroidLoggedInTestBase{

	private String openChatRoomName = "openchat";  // "Test" in production
	private String s;
	private OperationInChat chatAction;

	public TestOpenChat() throws ClassNotFoundException {
		super();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
		s = UUID.randomUUID().toString();
	}

	/**
	 * Checklist:
	 * 		1. can talk in open chat
	 * 		2. the fail case in whisper - none user, all user
	 * 		3. leave chat
	 */
	
	@LargeTest
	public void testOpenChat(){
		Log.d(TAG, ">>> Test: testOpenChat   -- Start -- <<<");

		Log.d(TAG, "Finding the open chat room");
		findOpenChat();
		Log.d(TAG, "Found the open chat room");
		
		Log.d(TAG, "Sending normal message");
		chatAction.talkNormal(s);
		Log.d(TAG, "Sent normal message");

		Log.d(TAG, "Sending GIF");
		chatAction.sendFirstGIF();
		Log.d(TAG, "Sent GIF");

		device().sleep(2000);

		Log.d(TAG, "Sending emote");
		chatAction.emoteText(s);
		Log.d(TAG, "Sent emote");

		device().sleep(2000);
		
		Log.d(TAG, "Checking response when whisper with no target");
		chatAction.whisperUser("", s); // sendWhisperWithOutTarget
		checkWhisperWithOutTarget(s);
		Log.d(TAG, "Checked");

		device().sleep(2000);
		
		Log.d(TAG, "Checking response when whisper to @chat");
		chatAction.whisperUser("chat", s); // sendWhisperToAll
		checkWhisperToAll(s);
		Log.d(TAG, "Checked");

		chatAction.leaveChat();

		Log.d(TAG, ">>> Test: testOpenChat   -- End -- <<<");
	}
	
	//===============================================

	public void findOpenChat() {
		device().clickOnView(device().getView("id/join_chat_view"));
		device().waitForActivity("JoinChatRoomActivity");
		device().clearEditText((EditText)device().getView("id/search_edit_text"));
    	device().enterText((EditText) device().getView("id/search_edit_text"), openChatRoomName);
    	device().clickOnView((TextView) device().getView("id/action_search"));
    	while(device().waitForText("searching")) {
    		device().sleep(2000);
    	}
    	device().clickOnText("Open Chat");
    	device().waitForActivity("ChatRoomActivity");
	}
	
	public void checkWhisperToAll(String s) {	
    	device().searchText("You can't whisper to entire chat!");
    	device().clickOnButton("OK");	
    }
	
	public void checkWhisperWithOutTarget(String s) {
    	device().searchText("Don't forget to @ Mention the user you want to Whisper to.");
    	device().clickOnButton("OK");	
    }

}
