package com.oursky.bindle;

import java.util.Arrays;

import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import android.widget.TextView;

public class TestPrivateChatSetting extends AndroidLoggedInTestBase{

	private String roomDescriptionA = "This is a room, that is a pig.";
	private String roomDescriptionB = "Once upon a time, there is a pen. End";
	private OperationInChat chatAction;
	private OperationInLobby lobbyAction;
	
	public TestPrivateChatSetting() throws ClassNotFoundException {
		super();
	}
	
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
		lobbyAction = new OperationInLobby(device());
	}
	
	@LargeTest
	public void testSetRoomDescription() {
		Log.d(TAG, ">>> Test: testSetRoomDescription   -- Start -- <<<");

		String otherUser = allUserName[1];
		Log.d(TAG, "# Admin should be able to set description #");
		
		Log.d(TAG, "Start creating chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room");
		
		Log.d(TAG, "Adding User B to chat room");
		String[] userList = {otherUser};
		chatAction.addOtherUser(userList);
		Log.d(TAG, "Added User B to chat room");
		
		Log.d(TAG, "Raising User B to mod");
		chatAction.raiseUserToMod(otherUser);
		device().clickOnButton("Cool");
		Log.d(TAG, "Raised User B to mod");
		
		Log.d(TAG, "Changing the room description and save it");
		chatAction.openEditPage();
		chatAction.editRoomDescription(roomDescriptionA);
		chatAction.backToLobby();
		Log.d(TAG, "Saved the room description");
		
		Log.d(TAG, "Switching Account to User B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched Account to User B");
		
		Log.d(TAG, "Checking the new description");
		lobbyAction.goChatRoom(roomName);
		device().clickOnButton(0);
		chatAction.openInfoPage();
		checkTheDescriptionContent(roomDescriptionA);
		Log.d(TAG, "Checked the new description");
		
		Log.d(TAG, "# Mods should be able to set description #");
		
		Log.d(TAG, "Changing the room description and save it");
		chatAction.openEditPage();
		chatAction.editRoomDescription(roomDescriptionB);
		Log.d(TAG, "Saved the room description");
		
		Log.d(TAG, "Switching Account to User A");
		chatAction.backToLobby();
		switchAccountToTestAc2();
		Log.d(TAG, "Switched Account to User A");
		
		Log.d(TAG, "Checking the new description");
		lobbyAction.goChatRoom(roomName);
		chatAction.openInfoPage();
		checkTheDescriptionContent(roomDescriptionB);
		Log.d(TAG, "Checked the new description");
		
		Log.d(TAG, "Deleting the chat room");
		chatAction.openEditPage();
		chatAction.deleteChatRoomInEditPage();
		Log.d(TAG, "Deleted the chat room");
		
		Log.d(TAG, ">>> Test: testSetRoomDescription   -- End -- <<<");
	}
	
	@LargeTest
	public void testOnlyModsCanUseChat() {
		Log.d(TAG, ">>> Test: testOnlyModsCanUseChat   -- Start -- <<<");
		
		String mod = allUserName[1];
		String chatAllTextA = "Hi everybody A";
		String chatAllTextB = "Hi everybody B";
		
		Log.d(TAG, "Start creating chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room");
		
		Log.d(TAG, "Adding User B, C to chat room");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "Added User B, C to chat room");
		
		Log.d(TAG, "Raising User B to mod");
		chatAction.raiseUserToMod(mod);
		device().clickOnButton("Cool");
		Log.d(TAG, "Raised User B to mod");
		
		Log.d(TAG, "Setting only mods can use @chat");
		chatAction.modifyOnlyModCanUseChat(true);
		Log.d(TAG, "Set only mods can use @chat");
		
		Log.d(TAG, "Switching Account to User B");
		chatAction.backToLobby();
		switchAccountToTestAc3();
		Log.d(TAG, "Switched Account to User B");
		
		Log.d(TAG, "Trying to send a chat message (should success)");
		lobbyAction.goChatRoom(roomName);
		device().clickOnButton("Cool");
		chatAction.mentionSomeone("chat", chatAllTextA);
		checkChatAllResponse(true, chatAllTextA);
		Log.d(TAG, "Tried");
		
		Log.d(TAG, "Switching Account to User C");
		chatAction.backToLobby();
		switchAccountToTestAc4();
		Log.d(TAG, "Switched Account to User C");
		
		Log.d(TAG, "Trying to send a chat message (should failed)");
		lobbyAction.goChatRoom(roomName);
		chatAction.mentionSomeone("chat", chatAllTextB);
		checkChatAllResponse(false, chatAllTextB);
		Log.d(TAG, "Tried");

		Log.d(TAG, "Switching Account to User A");
		chatAction.backToLobby();
		switchAccountToTestAc2();
		Log.d(TAG, "Switched Account to User A");
		
		Log.d(TAG, "Checking the chat room message");
		lobbyAction.goChatRoom(roomName);
		checkMessageExist(chatAllTextA, true);
		checkMessageExist(chatAllTextB, false);
		Log.d(TAG, "Checked the chat room message");
		
		Log.d(TAG, "Deleting the chat room");
		chatAction.deleteChatRoom();
		Log.d(TAG, "Deleted the chat room");
		
		Log.d(TAG, ">>> Test: testOnlyModsCanUseChat   -- End -- <<<");
	}
	
	@LargeTest
	public void testOnlyModsCanAddNewPeople() {
		Log.d(TAG, ">>> Test: testOnlyModsCanAddNewPeople   -- Start -- <<<");
		
		String mod = allUserName[1];
		
		Log.d(TAG, "Start creating chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room");
		
		Log.d(TAG, "Adding User B, C to chat room");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "Added User B, C to chat room");
		
		Log.d(TAG, "Raising User B to mod");
		chatAction.raiseUserToMod(mod);
		device().clickOnButton("Cool");
		Log.d(TAG, "Raised User B to mod");

		Log.d(TAG, "Setting only mods can add new people");
		chatAction.modifyOnlyModAddUser(true);
		Log.d(TAG, "Set only mods can add new people");
		
		Log.d(TAG, "Switching Account to User B");
		chatAction.backToLobby();
		switchAccountToTestAc3();
		Log.d(TAG, "Switched Account to User B");
		
		Log.d(TAG, "Trying to add new user (should success)");
		lobbyAction.goChatRoom(roomName);
		device().clickOnButton("Cool");
		String [] joyz = {"joyz"};
		chatAction.addOtherUser(joyz);
		Log.d(TAG, "Tried");
		
		Log.d(TAG, "Switching Account to User C");
		chatAction.backToLobby();
		switchAccountToTestAc4();
		Log.d(TAG, "Switched Account to User C");
		
		Log.d(TAG, "Trying to add new user (should failed)");
		lobbyAction.goChatRoom(roomName);
		chatAction.openInfoPage();
		checkAddUserRowExist(false);
		Log.d(TAG, "Tried");

		Log.d(TAG, "Switching Account to User A");
		chatAction.backToLobby();
		switchAccountToTestAc2();
		Log.d(TAG, "Switched Account to User A");
		
		Log.d(TAG, "Deleting the chat room");
		lobbyAction.goChatRoom(roomName);
		chatAction.deleteChatRoom();
		Log.d(TAG, "Deleted the chat room");
		
		Log.d(TAG, ">>> Test: testOnlyModsCanAddNewPeople   -- End -- <<<");
	}
	
	/**
	 * This test case dosen't test the function of the notification, 
	 * it just check whether the setting have been saved
	 */
	@MediumTest
	public void testCustomNotifications() {
		Log.d(TAG, ">>> Test: testCustomNotifications   -- Start -- <<<");
		
		Log.d(TAG, "Start creating chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room");
		
		Log.d(TAG, "Setting the Custom Notifications");
		Log.d(TAG, "Set the Custom Notifications");
		
		Log.d(TAG, "Leaving and reEnter chat room");
		Log.d(TAG, "reEntered");
		
		Log.d(TAG, "Checking the previous change");
		Log.d(TAG, "Checked");
		
		Log.d(TAG, "Deleting the chat room");
		Log.d(TAG, "Deleted the chat room");
		
		Log.d(TAG, ">>> Test: testCustomNotifications   -- End -- <<<");
	}

	public void checkTheDescriptionContent(String expectedDescription) {
		TextView tvFromText = device().getText(expectedDescription);
		TextView tvFromId = (TextView) device().getView("id/channel_description");
		assertEquals("Some problem occur while checking the changed description", tvFromText, tvFromId);
	}
	
	public void checkMessageExist(String msg, boolean shouldExist) {
		if(shouldExist) {
			assertTrue("It haven't show the chat all message", device().waitForText(msg));
		} else{
			assertFalse("It have show the chat all message which should be disabled", device().waitForText(msg));
		}
		
	}
	
	public void checkAddUserRowExist(boolean shouldExist) {
		String addUserRowText = "Add by Username";
		if(shouldExist) {
			assertTrue("It haven't show the add user row", device().waitForText(addUserRowText));
		} else{
			assertFalse("It shouldn't show the add user row", device().waitForText(addUserRowText));
		}
	}
	
	public void checkChatAllResponse(boolean shouldSuccess, String msg) {
		if(shouldSuccess) {
			checkMessageExist(msg, true);
		} else {
			assertTrue("It haven't show the warning test while ", 
					device().waitForText("This chat's settings only allow moderators to use @chat. Ask one of them to blast your message."));
			device().clickOnText("OK");
		}
	}

}
