package com.oursky.bindle;

import java.util.Arrays;

import junit.framework.AssertionFailedError;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TestPrivateRoom extends AndroidLoggedInTestBase{

	private String whisperText = "whisperText";
	private String normalText = "normalText";
	private String chatAllText = "chatAllText";
	private String chatSomeoneText = "chatSomeoneText";
	private String emoteText = "emoteText";
	private OperationInChat chatAction;
	private OperationInLobby lobbyAction;
	
	public TestPrivateRoom() throws ClassNotFoundException {
		super();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
		lobbyAction = new OperationInLobby(device());
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
		String roomName = lobbyAction.createChatRoomUntilSuccess();
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
		lobbyAction.goChatRoom(roomName);
		checkWhisperTextExist(sender, receiver, true);
		checkNormalTextExist(sender);
		checkChatAllTextExist(sender);
		checkEmoteTextExist(sender);
		chatAction.backToLobby();
		
		
		// User C
		switchAccountToTestAc4();
		receiver = allUserName[2];
		lobbyAction.goChatRoom(roomName);
		checkWhisperTextExist(sender, receiver, false);
		checkNormalTextExist(sender);
		checkChatAllTextExist(sender);
		checkEmoteTextExist(sender);
		chatAction.backToLobby();
		
		// User A
		switchAccountToTestAc2();
		lobbyAction.goChatRoom(roomName);
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
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		String sender = allUserName[0];
		String receiver = allUserName[1];
		
		// User A
		addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
	
		chatAction.mentionSomeone(receiver, chatSomeoneText); // it mean mention all
		device().waitForText(chatSomeoneText);
		chatAction.backToLobby();

		// User B
		switchAccountToTestAc3();
		lobbyAction.goChatRoom(roomName);
		checkMentionTextExist(sender, receiver, true);
		chatAction.backToLobby();
		
		// User C
		switchAccountToTestAc4();
		lobbyAction.goChatRoom(roomName);
		checkMentionTextExist(sender, receiver, false);
		chatAction.backToLobby();
		
		// User A
		switchAccountToTestAc2();
		lobbyAction.goChatRoom(roomName);
		chatAction.deleteChatRoom();
	}
	
	@LargeTest
	public void testKickUserOutofChatRoomOnce() {
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		String poorguy = allUserName[1];
		
		// User A add user
		addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		chatAction.backToLobby();
		// User B check room
		device().sleep(5000);
		switchAccountToTestAc3();
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();
		// User C check room
		device().sleep(5000);
		switchAccountToTestAc4();
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();
		// User A kick User B
		device().sleep(5000);
		switchAccountToTestAc2();
		lobbyAction.goChatRoom(roomName);
		chatAction.kickUser(poorguy, false);
		chatAction.backToLobby();
		
		// User B check room exist?
		device().sleep(5000);
		switchAccountToTestAc3();
		checkRoomExist(roomName, false);
		lobbyAction.searchChatRoom(roomName);
		checkAbletoKnock(roomName);
		chatAction.backToLobby();
		checkRoomExist(roomName, false);
		
		// User C check room exist?
		device().sleep(5000);
		switchAccountToTestAc4();
		checkRoomExist(roomName, true);
		chatAction.backToLobby();
		
		// User A delete chat room
		device().sleep(5000);
		switchAccountToTestAc2();
		lobbyAction.goChatRoom(roomName);
		chatAction.deleteChatRoom();
	}
	
	
	@LargeTest
	public void testKictUserOutofChatRoomForever() {
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		String poorguy = allUserName[1];
		// User A add user
		addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		chatAction.backToLobby();
		// User B check room
		switchAccountToTestAc3();
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();
		// User C check room
		switchAccountToTestAc4();
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();
		// User A kick user B permanently
		switchAccountToTestAc2();
		lobbyAction.goChatRoom(roomName);
		chatAction.kickUser(poorguy, true);
		chatAction.backToLobby();
		
		// User B check room
		switchAccountToTestAc3();
		checkRoomExist(roomName, false);
		lobbyAction.searchChatRoom(roomName);
		checkGetBannedToChat();
		chatAction.backToLobby();
		checkRoomExist(roomName, false);
		// User C check room
		switchAccountToTestAc4();
		checkRoomExist(roomName, true);
		chatAction.backToLobby();
		// User A delete room
		switchAccountToTestAc2();
		lobbyAction.goChatRoom(roomName);
		chatAction.deleteChatRoom();
	}

//	@LargeTest
//	public void testRaiseUserToMod() {
//		
//	}
	
//	@LargeTest
//	public void testRaiseUserToAdmin() {
//		
//	}
	
	//======================================================================

	public void addOtherUser(String[] allName) {
		device().sleep(10000);
		device().clickOnView((ImageView) device().getView("id/info_icon_image_view"));
		device().waitForActivity("ChatRoomDetailActivity");
		device().waitForText("Edit");
		device().sleep(4000);
		device().scrollDown();
		device().clickOnText("Add by Username");
		device().waitForActivity("AddByUsernameActivity");
		for(String s: allName) {
			while(true) {
				try {
					device().clearEditText((EditText) device().getView("id/search_src_text"));
					device().enterText((EditText) device().getView("id/search_src_text"), s);
					device().pressSoftKeyboardSearchButton();
					device().waitForText("Search Users");
					device().clickOnButton(0);
					device().waitForText("Added");
					device().clickOnView((TextView) device().getView("id/user_search"));
					break;
				} catch (AssertionFailedError e) {
					if(device().searchText("Opps")) {
						device().clickOnButton(0);
					}
					device().goBackToActivity("ChatRoomDetailActivity");
					device().waitForText("Edit");
					device().sleep(4000);
					device().scrollDown();
					device().clickOnText("Add by Username");
					device().waitForActivity("AddByUsernameActivity");
					continue;
				}
			}
		}
		device().goBackToActivity("ChatRoomActivity");
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
	
	public void checkRoomExist(String roomName, boolean shouldExist) {
		device().sleep(4000);
		if (shouldExist) {
			assertTrue("The expected chat room is disappear",
					device().searchText(roomName));
		} else {
			assertFalse("The chat romm should not exist in the list since you have be kicked out",
					device().searchText(roomName));
		}
	}
	
	public void checkGetBannedToChat() {
		assertFalse("Still can be able to knock the chat room which you have been permentaly kicked",
				device().searchText("Sorry, a moderator has banned you from this chat."));
	}
	
	public void checkAbletoKnock(String roomName) {
		device().waitForActivity("id/channel_name_text_view");
		device().clickInList(0);
		device().waitForActivity("JoinChatRoomActivity");
		assertEquals("#"+roomName, ((TextView) device().getView("id/channel_name_text_view")).getText());
		assertTrue("Cannot knock the room after kicked once",
				device().waitForView((Button) device().getView("id/join_button")));
		device().clickOnView((ImageButton) device().getView("id/dismiss_button"));
	}

}
