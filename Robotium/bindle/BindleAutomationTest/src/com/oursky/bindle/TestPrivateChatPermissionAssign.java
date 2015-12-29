package com.oursky.bindle;

import java.util.Arrays;

import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

public class TestPrivateChatPermissionAssign extends AndroidLoggedInTestBase{
	
	private final int USER_ADMIN = 0;
	private final int USER_MOD = 1;
	private final int USER_SMALL_POTATO = 2;
	private OperationInChat chatAction;
	private OperationInLobby lobbyAction;

	public TestPrivateChatPermissionAssign() throws ClassNotFoundException {
		super();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
		lobbyAction = new OperationInLobby(device());
	}
	
	@LargeTest
	public void testRaiseUserToMod() {
		String mod = allUserName[1];
		String victim = allUserName[2];

		Log.d(TAG, ">>> Test: testRaiseUserToMod   -- Start -- <<<");

		Log.d(TAG, "Start creating chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room");		

		Log.d(TAG, "Start adding other user to chat room");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "Added the user to chat room");

		Log.d(TAG, "Start raise userB to mod");
		lobbyAction.goChatRoom(roomName);
		chatAction.raiseUserToMod(mod);
		Log.d(TAG, "Raised userB to mod");
		checkAlertDialogAfterYouSetMod(mod);
		device().clickOnButton(0);
		chatAction.backToLobby();
		
		Log.d(TAG, "Switching Account to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched Account to user B");
		Log.d(TAG, "Checking user B have the permission of mod");
		lobbyAction.goChatRoom(roomName);
		checkAlertDialogAfterYouBecomeMod();
		device().clickOnButton(0);
		checkModPermission(victim, USER_SMALL_POTATO);
		Log.d(TAG, "Checked the permission of User B");
		chatAction.backToLobby();
		
		Log.d(TAG, "Switching Account to user C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switch Account to user C");
		Log.d(TAG, "Checking the permission of user C (should not have change)");
		lobbyAction.goChatRoom(roomName);
		checkCurrentUserPermission(mod);
		Log.d(TAG, "Checked the permission of User C");
		chatAction.backToLobby();

		Log.d(TAG, "Switching Account to user A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched Account to user A");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "Delete chat room");
		chatAction.deleteChatRoom();
		Log.d(TAG, "Room Deleted");
		
		Log.d(TAG, ">>> Test: testRaiseUserToMod   -- End -- <<<");
	}

	@LargeTest
	public void testRaiseUserToAdmin() {
		String oldAdmin = allUserName[0];
		String newAdmin = allUserName[1];
		String victim = allUserName[2];

		Log.d(TAG, ">>> Test: testRaiseUserToAdmin   -- Start -- <<<");

		Log.d(TAG, "Start creating chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room");		

		Log.d(TAG, "Start adding other user to chat room");
		chatAction.addOtherUser(Arrays.copyOfRange(allUserName, 1, 3));
		Log.d(TAG, "Added the user to chat room");

		Log.d(TAG, "Start raise userB to Admin");
		lobbyAction.goChatRoom(roomName);
		chatAction.raiseUserToAdmin(newAdmin);
		Log.d(TAG, "Raised userB to Admin");
		device().goBackToActivity("ChatRoomActivity");
		Log.d(TAG, "Checking User A permission(should be mod)");
		checkAlertDialogAfterYouBecomeMod();
		device().clickOnButton(0);
		checkModPermission(victim, USER_SMALL_POTATO);
		Log.d(TAG, "Checked");
		chatAction.backToLobby();
		
		Log.d(TAG, "Switching Account to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched Account to user B");
		Log.d(TAG, "Checking user B have the permission of Admin");
		lobbyAction.goChatRoom(roomName);
		checkAdminPermission(victim, USER_SMALL_POTATO);
		device().goBackToActivity("ChatRoomActivity");
		checkAdminPermission(oldAdmin, USER_MOD);
		Log.d(TAG, "Checked the permission of User B");
		chatAction.backToLobby();
		
		Log.d(TAG, "Switching Account to user C");
		switchAccountToTestAc4();
		Log.d(TAG, "Switched Account to user C");
		Log.d(TAG, "Checking the permission of user C (should not have change)");
		lobbyAction.goChatRoom(roomName);
		checkCurrentUserPermission(newAdmin);
		Log.d(TAG, "Checked the permission of User C");
		chatAction.backToLobby();

		Log.d(TAG, "Switching Account to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched Account to user B");
		Log.d(TAG, "Delete chat room");
		lobbyAction.goChatRoom(roomName);
		chatAction.deleteChatRoom();
		Log.d(TAG, "Room Deleted");
		
		Log.d(TAG, ">>> Test: testRaiseUserToAdmin   -- End -- <<<");
	}
	
	//=====================================
	public void checkAlertDialogAfterYouBecomeMod() {
		device().waitForText("You are now a moderator");
	}
	
	public void checkAlertDialogAfterYouSetMod(String name) {
		device().waitForText(String.format("@%s is now a moderator", name));
	}

	public void checkCurrentUserPermission(String victim) {
		chatAction.openOptionMenuForUser(victim);
		device().waitForText("Report User");
		assertFalse("There are invalid option (Kick User) in the option menu",
				device().waitForText("Kick User", 1, 1000));
		assertFalse("There are invalid option (Promote to Moderator) in the option menu",
				device().waitForText("Promote to Moderator", 1, 1000));
		assertFalse("There are invalid option (Promote to Admin) in the option menu",
				device().waitForText("Promote to Admin", 1, 1000));
		device().goBack();
	}
	
	public void checkModPermission(String victim, int targetPermission) {
		chatAction.openOptionMenuForUser(victim);
		device().waitForText("Report User");
		switch (targetPermission) {
		case USER_SMALL_POTATO:
			device().waitForText("Kick User");
			device().waitForText("Promote to Moderator");
			break;
		case USER_MOD:
			device().waitForText("Remove as Moderator");
			break;
		case USER_ADMIN:
			break;
		}
		device().goBack();
		
		assertTrue("Mod should be able to see the edit button",
				device().waitForText("Edit"));
	}
	
	public void checkAdminPermission(String victim, int targetPermission) {
		chatAction.openOptionMenuForUser(victim);
		device().waitForText("Report User");
		switch (targetPermission) {
		case USER_SMALL_POTATO:
			device().waitForText("Kick User");
			device().waitForText("Promote to Moderator");
			device().waitForText("Promote to Admin");
			break;
		case USER_MOD:
			device().waitForText("Remove as Moderator");
			device().waitForText("Promote to Admin");
			break;
		}
		device().goBack();
		assertTrue("Admin should be able to see the edit button",
				device().waitForText("Edit"));
	}

}
