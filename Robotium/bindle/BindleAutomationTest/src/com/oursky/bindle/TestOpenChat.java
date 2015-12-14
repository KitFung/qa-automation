package com.oursky.bindle;

import java.util.UUID;

import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;
import android.widget.TextView;

public class TestOpenChat extends AndroidLoggedInTestBase{

	private String openChatRoomName = "Test";
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
		findOpenChat();
		chatAction.talkNormal(s);
		chatAction.sendFirstGIF();
		device().sleep(2000);

		chatAction.emoteText(s);
		device().waitForText(s);
		device().sleep(2000);
		
		chatAction.whisperUser("", s); // sendWhisperWithOutTarget
		checkWhisperWithOutTarget(s);
		device().sleep(2000);
		
		chatAction.whisperUser("chat", s); // sendWhisperToAll
		checkWhisperToAll(s);

		chatAction.leaveChat();
	}
	
	//===============================================

	public void findOpenChat() {
		device().clickOnView(device().getView("id/join_chat_view"));
		device().waitForActivity("JoinChatRoomActivity");
		device().clearEditText((EditText)device().getView("id/search_edit_text"));
    	device().enterText((EditText) device().getView("id/search_edit_text"), openChatRoomName);
    	device().clickOnView((TextView) device().getView("id/action_search"));
    	while(device().searchText("searching")) {
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
