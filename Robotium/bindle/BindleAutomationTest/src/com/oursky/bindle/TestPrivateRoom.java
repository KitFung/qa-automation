package com.oursky.bindle;

import java.util.Arrays;
import java.util.UUID;

import junit.framework.AssertionFailedError;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TestPrivateRoom extends AndroidLoggedInTestBase{

	private String whisperText = "whisperText";
	private String normalText = "normalText";
	private String chatAllText = "chatAllText";
	private String chatSomeoneText = "chatSomeoneText";
	private String emoteText = "emoteText";
	private OperationInChat chatAction;
	
	public TestPrivateRoom() throws ClassNotFoundException {
		super();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
	}
	
	/**
	 * Flow:
	 * User A create new room,
	 * 		invite user B and C come to this room
	 * 		send 1 normal message
	 * 		1 whisper to user B
	 * 		1 chat all message
	 * 		1 emote message
	 * Switch to User B
	 * 		Check normal message is exist?
	 * 		Check whisper is exist?
	 * 		Check chat all message is exist?
	 * 		Check emote message is exist?
	 * Switch to User C 
	 * 		Check normal message is exist?
	 * 		Check whisper is not exist?
	 * 		Check chat all message is exist?
	 * 		Check emote message is exist?
	 * Switch back to User A
	 * 		Delete chat room.
	 * 
	 */
	
	@LargeTest
	public void testSendDifferentTypeOfMessage() {
		String roomName = createChatRoom();
		String sender = allUserName[0];
		String receiver = allUserName[1];
		
		// User A
		addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		chatAction.talkNormal(normalText);
		checkNormalTextExist(sender);
		device().sleep(2000);

		chatAction.whisperUser(receiver, whisperText);
		checkWhisperTextExist("You", receiver, true);
		device().sleep(2000);
		
		chatAction.emoteText(emoteText);
		checkEmoteTextExist(sender);
		device().sleep(2000);
		
		chatAction.mentionSomeone("chat", chatAllText); // it mean mention all
		device().waitForText(chatAllText);
		device().sleep(2000);

		chatAction.backToLobby();

		// User B
		switchAccountToTestAc3();
		goChatRoom(roomName);
		checkWhisperTextExist(sender, receiver, true);
		checkNormalTextExist(sender);
		checkChatAllTextExist(sender);
		checkEmoteTextExist(sender);
		chatAction.backToLobby();
		
		
		// User C
		switchAccountToTestAc4();
		receiver = allUserName[2];
		goChatRoom(roomName);
		checkWhisperTextExist(sender, receiver, false);
		checkNormalTextExist(sender);
		checkChatAllTextExist(sender);
		checkEmoteTextExist(sender);
		chatAction.backToLobby();
		
		// User A
		switchAccountToTestAc2();
		goChatRoom(roomName);
		chatAction.deleteChatRoom();
	}
	
	/**
	 * Flow:
	 * User A create new room,
	 * 		invite user B and C come to this room
	 * 		mention to user B
	 * Switch to User B
	 * 		Check the blue label exist
	 * Switch to User C 
	 * 		Check the blue label not exist?
	 * Switch back to User A
	 * 		Delete chat room.
	 */

	@LargeTest
	public void testTheBlueLabelForMention() {
		String roomName = createChatRoom();
		String sender = allUserName[0];
		String receiver = allUserName[1];
		
		// User A
		addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
	
		chatAction.mentionSomeone(receiver, chatSomeoneText); // it mean mention all
		device().waitForText(chatSomeoneText);
		chatAction.backToLobby();

		// User B
		switchAccountToTestAc3();
		goChatRoom(roomName);
		checkMentionTextExist(sender, receiver, true);
		chatAction.backToLobby();
		
		
		// User C
		switchAccountToTestAc4();
		goChatRoom(roomName);
		checkMentionTextExist(sender, receiver, false);
		chatAction.backToLobby();
		
		// User A
		switchAccountToTestAc2();
		goChatRoom(roomName);
		chatAction.deleteChatRoom();
	}

	//======================================================================

	public String createChatRoom() {
		String roomName = null;
		while(true) {
			roomName = getUUID();
			device().clickOnView(device().getView("id/create_chat_view"));
			device().waitForActivity("CreateChatRoomActivity");
			device().clearEditText((EditText)device().getView("id/chat_room_name_edit_text"));
	    	device().enterText((EditText) device().getView("id/chat_room_name_edit_text"), roomName);
	    	while (true){
				try{
					device().sleep(2000);
					device().clickOnView((TextView) device().getView("id/action_done"));
					device().waitForActivity("ChatRoomActivity");
					break;
				}catch(AssertionFailedError e) {
				}
			}
	    	break;
		}
		device().waitForText(String.format("#%s", roomName));
		device().sleep(4000);
		return roomName;
	}
	
	public void addOtherUser(String[] allName) {
		device().clickOnView((ImageView) device().getView("id/info_icon_image_view"));
		device().waitForActivity("ChatRoomDetailActivity");
		device().waitForText("Edit");
		device().sleep(2000);
		device().scrollDown();
		device().clickOnText("Add by Username");
		device().waitForActivity("AddByUsernameActivity");
		for(String s: allName) {
			device().clearEditText((EditText) device().getView("id/search_src_text"));
			device().enterText((EditText) device().getView("id/search_src_text"), s);
			device().pressSoftKeyboardSearchButton();
			device().waitForText("Search Users");
			device().clickOnButton(0);
			device().waitForText("Added");
			device().clickOnView((TextView) device().getView("id/user_search"));
		}
		device().goBackToActivity("ChatRoomActivity");
	}
	
	private void goChatRoom(String name) {
		device().clickOnText("#"+name);
		device().waitForActivity("ChatRoomActivity");
		device().sleep(4000);
	}

	//======================================================================

	private void checkWhisperTextExist(String sender, String receiver, boolean isExist) {
		String expectedString = String.format("%s whispered: @%s %s", sender, receiver, whisperText);
		if (isExist) {
			assertTrue("It haven't should the purple label for chat all message",
					device().getView("id/whisper_indicator").isEnabled());
			assertTrue("The whisper message cannot receive",
					device().waitForText(expectedString));
		} else {
			assertFalse("It should not receive this whisper message",
					device().waitForText(whisperText));
		}
	}

	private void checkNormalTextExist(String sender) {
		String expectedString = String.format("%s: %s", sender, normalText);
		assertTrue("The normal message cannot receive",
				device().waitForText(expectedString));
	}

	private void checkChatAllTextExist(String sender) {
		String expectedString = String.format("%s: @chat %s",sender, chatAllText);
		assertTrue("It haven't should the blue label for chat all message",
				device().getView("id/mention_indicator").isEnabled());
		assertTrue("The chat all message cannot receive",
				device().waitForText(expectedString));
	}
	
	private void checkMentionTextExist(String sender, String receiver, boolean isLabelExist) {
		String expectedString = String.format("%s: @%s %s",sender, receiver, chatSomeoneText);
		if(isLabelExist) {
			assertTrue("It haven't should the blue label for message that isn't mention him",
					device().getView("id/mention_indicator").isEnabled());
		}
		Log.i("ssss", expectedString);
		assertTrue("The mention message cannot receive",
				device().waitForText(expectedString));
	}
	
	public void checkEmoteTextExist(String sender) {
		String expectedString = String.format("%s %s", sender, emoteText);
		assertTrue("The normal message cannot receive",
				device().waitForText(expectedString));
	}
	
	 public static String getUUID(){ 
        String s = UUID.randomUUID().toString(); 
        return s.substring(0,8);
    } 

}
