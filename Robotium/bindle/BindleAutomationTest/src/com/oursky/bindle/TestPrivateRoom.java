package com.oursky.bindle;

import java.util.Arrays;

import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
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
	
	@Smoke
	public void testDeleteChatRoom() {
		Log.d(TAG, ">>> Test: testDeleteChatRoom   -- Start -- <<<");
		Log.d(TAG, "Creating Chat Room");
		lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created Chat Room");
		Log.d(TAG, "Deleting Chat Room");
		chatAction.deleteChatRoom();
		Log.d(TAG, "Deleted Chat Room");
		Log.d(TAG, "Checking response");
		checkSuccessDeleteChatRoom();
		Log.d(TAG, "Checked response");
		
		Log.d(TAG, ">>> Test: testDeleteChatRoom   -- End -- <<<");
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
		String sender = allUserName[0];
		String receiver = allUserName[1];

		Log.d(TAG, ">>> Test: testSendDifferentTypeOfMessage   -- Start -- <<<");
		Log.d(TAG, "Creating Chat Room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created Chat Room");
		
		// User A
		Log.d(TAG, "User A adding other user");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "User A added other user");

		Log.d(TAG, "User A Send the normal message");
		chatAction.talkNormal(normalText);
		checkNormalTextExist(sender);
		Log.d(TAG, "User A Sended the normal message");
		device().sleep(2000);

		Log.d(TAG, "User A whisper User B");
		chatAction.whisperUser(receiver, whisperText);
		checkWhisperTextExist("You", receiver, true);
		Log.d(TAG, "User A whispered User B");
		device().sleep(2000);

		Log.d(TAG, "User A emote message");
		chatAction.emoteText(emoteText);
		checkEmoteTextExist(sender);
		Log.d(TAG, "User A emoted message");
		device().sleep(2000);
		
		Log.d(TAG, "User A send chat message");
		chatAction.mentionSomeone("chat", chatAllText); // it mean mention all
		device().waitForText(chatAllText);
		Log.d(TAG, "User A sent chat message");
		device().sleep(2000);

		chatAction.backToLobby();

		// User B
		Log.d(TAG, "Switching user to User B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched user to User B");
		lobbyAction.goChatRoom(roomName);

		Log.d(TAG, "User B checking the whisper message");
		checkWhisperTextExist(sender, receiver, true);
		Log.d(TAG, "User B checked the whisper message");

		Log.d(TAG, "User B checking the Normal message");
		checkNormalTextExist(sender);
		Log.d(TAG, "User B checked the Normal message");

		Log.d(TAG, "User B checking the chat message");
		checkChatAllTextExist(sender);
		Log.d(TAG, "User B checked the chat message");

		Log.d(TAG, "User B checking the emote message");
		checkEmoteTextExist(sender);
		Log.d(TAG, "User B checked the emote message");
		chatAction.backToLobby();
		
		
		// User C
		Log.d(TAG, "Switching user to User C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switching user to User C");
		receiver = allUserName[2];
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User C checking the whisper message (should not exist)");
		checkWhisperTextExist(sender, receiver, false);
		Log.d(TAG, "User C checked the whisper message");
		
		Log.d(TAG, "User C checking the normal message");
		checkNormalTextExist(sender);
		Log.d(TAG, "User C checked the normal message");
		
		Log.d(TAG, "User C checking the chat message");
		checkChatAllTextExist(sender);
		Log.d(TAG, "User C checked the chat message");
		
		Log.d(TAG, "User C checking the emote message");
		checkEmoteTextExist(sender);
		Log.d(TAG, "User C checked the emote message");
		chatAction.backToLobby();
		
		// User A
		Log.d(TAG, "Switching user to User A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switching user to User A");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User A deleting chatroom");
		chatAction.deleteChatRoom();
		Log.d(TAG, "User A deleted chatroom");
		Log.d(TAG, ">>> Test: testSendDifferentTypeOfMessage   -- End -- <<<");
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
		String sender = allUserName[0];
		String receiver = allUserName[1];
		
		Log.d(TAG, ">>> Test: testTheBlueLabelForMention   -- Start -- <<<");
		Log.d(TAG, "Creating Chat Room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created Chat Room");
		
		
		// User A
		Log.d(TAG, "User A adding other user to room");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "User A added other user to room");

		Log.d(TAG, "User A mention User B");
		chatAction.mentionSomeone(receiver, chatSomeoneText); // it mean mention all
		device().waitForText(chatSomeoneText);
		Log.d(TAG, "User A mentioned User B");
		chatAction.backToLobby();

		// User B
		Log.d(TAG, "Switching user to User B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched user to User B");
		lobbyAction.goChatRoom(roomName);

		Log.d(TAG, "User B checking the mention message");
		checkMentionTextExist(sender, receiver, true);
		Log.d(TAG, "User B checked the mention message");
		chatAction.backToLobby();
		
		// User C
		Log.d(TAG, "Switching user to User C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switching user to User C");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User C checking the mention message");
		checkMentionTextExist(sender, receiver, false);
		Log.d(TAG, "User C checked the mention message");
		chatAction.backToLobby();
		
		// User A
		Log.d(TAG, "Switching user to User A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched user to User A");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User A deleting the chat room");
		chatAction.deleteChatRoom();
		Log.d(TAG, "User A deleted the chat room");
		Log.d(TAG, ">>> Test: testTheBlueLabelForMention   -- End -- <<<");
	}
	
	@LargeTest
	public void testKickUserOutofChatRoomOnce() {
		String poorguy = allUserName[1];
		Log.d(TAG, ">>> Test: testKickUserOutofChatRoomOnce   -- Start -- <<<");
		Log.d(TAG, "Creating Chat Room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created Chat Room");

		// User A add user
		Log.d(TAG, "User A adding other user to room");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "User A added other user to room");
		chatAction.backToLobby();

		// User B check room
		Log.d(TAG, "Switching account to User B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched account to User B");
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();

		// User C check room
		Log.d(TAG, "Switching account to User C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switched account to User C");
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();

		// User A kick User B
		Log.d(TAG, "Switching account to User A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched account to User A");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User A kick user User B once");
		chatAction.kickUser(poorguy, false);
		Log.d(TAG, "User A kicked user User B once");
		chatAction.backToLobby();
		
		// User B check room exist?
		Log.d(TAG, "Switching account to User B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched account to User B");
		checkRoomExist(roomName, false);
		Log.d(TAG, "User B find the chat room");
		lobbyAction.searchChatRoom(roomName);
		Log.d(TAG, "User B found the chat room");
		Log.d(TAG, "User B checking whether he can knock the chat room");
		checkAbletoKnock(roomName);
		Log.d(TAG, "User B checked whether he can knock the chat room");
		chatAction.backToLobby();
		checkRoomExist(roomName, false);
		
		// User C check room exist?
		Log.d(TAG, "Switching account to User C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switched account to User C");
		checkRoomExist(roomName, true);
		chatAction.backToLobby();
		
		// User A delete chat room
		device().sleep(5000);
		Log.d(TAG, "Switching account to User A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched account to User A");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User A deleting chat room");
		chatAction.deleteChatRoom();
		Log.d(TAG, "User A deleted chat room");
		Log.d(TAG, ">>> Test: testKickUserOutofChatRoomOnce   -- End -- <<<");
	}
	
	
	@LargeTest
	public void testKictUserOutofChatRoomForever() {
		String poorguy = allUserName[1];
		Log.d(TAG, ">>> Test: testKictUserOutofChatRoomForever   -- Start -- <<<");
		Log.d(TAG, "Creating Chat Room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created Chat Room");
		// User A add user
		Log.d(TAG, "User A adding other user to room");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "User A added other user to room");
		chatAction.backToLobby();
		// User B check room
		Log.d(TAG, "Switching account to User B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched account to User B");
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();
		// User C check room
		Log.d(TAG, "Switching account to User C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switched account to User C");
		lobbyAction.goChatRoom(roomName);
		chatAction.backToLobby();
		// User A kick user B permanently
		Log.d(TAG, "Switching account to User A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched account to User A");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User A kicking User B permanently");
		chatAction.kickUser(poorguy, true);
		Log.d(TAG, "User A kicked User B");
		chatAction.backToLobby();
		
		// User B check room
		Log.d(TAG, "Switching account to User B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched account to User B");
		Log.d(TAG, "User B checking room exist(expect not)");
		checkRoomExist(roomName, false);
		Log.d(TAG, "User B checked room not exist");
		Log.d(TAG, "User B checking whether he can knock the room again(expected not)");
		lobbyAction.searchChatRoom(roomName);
		checkGetBannedToChat();
		Log.d(TAG, "User B checked he cannot knock the room");
		chatAction.backToLobby();
		checkRoomExist(roomName, false);
		// User C check room
		Log.d(TAG, "Switching account to User C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switched account to User C");
		Log.d(TAG, "User B checking room exist(expect yes)");
		checkRoomExist(roomName, true);
		Log.d(TAG, "User B checked room exist");
		chatAction.backToLobby();
		// User A delete room
		Log.d(TAG, "Switching account to User A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched account to User A");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "User A deleting chat room");
		chatAction.deleteChatRoom();
		Log.d(TAG, "User A deleted chat room");
		Log.d(TAG, ">>> Test: testKictUserOutofChatRoomForever   -- End -- <<<");
	}

	//======================================================================

	private void checkSuccessDeleteChatRoom() {
		device().assertCurrentActivity("The current activities should be the lobby", "LobbyActivity");
	}
	
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
					device().waitForText(roomName));
		} else {
			assertFalse("The chat romm should not exist in the list since you have be kicked out",
					device().waitForText(roomName));
		}
	}
	
	public void checkGetBannedToChat() {
		assertFalse("Still can be able to knock the chat room which you have been permentaly kicked",
				device().waitForText("Sorry, a moderator has banned you from this chat."));
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
