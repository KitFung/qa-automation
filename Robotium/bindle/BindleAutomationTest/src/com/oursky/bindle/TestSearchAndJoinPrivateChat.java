package com.oursky.bindle;

import junit.framework.AssertionFailedError;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TestSearchAndJoinPrivateChat extends AndroidLoggedInTestBase{

	private OperationInChat chatAction;
	private OperationInLobby lobbyAction;
	private String description = "Hi, I am Yellow. The sky is blue. Ha Ha Ha";
	
	public TestSearchAndJoinPrivateChat() throws ClassNotFoundException {
		super();
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
		lobbyAction = new OperationInLobby(device());
	}

	@LargeTest
	public void testJoinPrivateChatAccepted() {
		Log.d(TAG, ">>> Test: testJoinPrivateChatAccepted   -- Start -- <<<");

		Log.d(TAG, "Creating a new chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created the new chat room");
		
		String userName = allUserName[1];
		Log.d(TAG, "Switching to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched to user B");
		
		Log.d(TAG, "Searching the chat room");
		lobbyAction.searchChatRoom(roomName);
		Log.d(TAG, "Searched the chat room");
		
		Log.d(TAG, "Send join request to that chat room");
		lobbyAction.joinChatRoom(description);
		Log.d(TAG, "Request sent");
		
		Log.d(TAG, "Checking the join response");
		checkResponseAfterSendRequest(roomName);
		Log.d(TAG, "Checked the join response");
		
		Log.d(TAG, "Switching to user A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched to user A");
		
		lobbyAction.goChatRoom(roomName);
		
		Log.d(TAG, "User A checking the knocking message");
		checkTheKnockMessage(userName, roomName, description);
		Log.d(TAG, "User A checked the knocking message");

		Log.d(TAG, "User A accepting the request");
		chatAction.acceptJoinRequest(userName);
		Log.d(TAG, "User A accepted the request");
		
		Log.d(TAG, "Checking user have successfully joined");
		checkUserSuccessJoinInRoomInfoPage(userName);
		Log.d(TAG, "Checked user have successfully joined");
		
		chatAction.backToLobby();
		
		Log.d(TAG, "Switching to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched to user B");
		
		Log.d(TAG, "Trying to open the new joined chat room");
		lobbyAction.goChatRoom(roomName);
		Log.d(TAG, "Success open the new joined chat room");

		chatAction.backToLobby();
		
		Log.d(TAG, "Switching to user A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched to user A");
		
		Log.d(TAG, "Deleting the chat room");
		lobbyAction.goChatRoom(roomName);
		chatAction.deleteChatRoom();
		Log.d(TAG, "Deleted the chat room");

		Log.d(TAG, ">>> Test: testJoinPrivateChatAccepted   -- End -- <<<");
	}

	
	private void actionRejectJoinRequest(String roomName, String userName, boolean permanently) {
		Log.d(TAG, "Switching to user A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched to user A");
		
		lobbyAction.goChatRoom(roomName);
		
		Log.d(TAG, "User A checking the knocking message");
		checkTheKnockMessage(userName, roomName, description);
		Log.d(TAG, "User A checked the knocking message");

		Log.d(TAG, "User A rejecting the request");
		chatAction.rejectJoinRequest(userName, permanently);
		Log.d(TAG, "User A rejected the request");
		
		Log.d(TAG, "Checking user is not in the chat room");
		checkUserSuccessRejectedInRoomInfoPage(userName, permanently);
		chatAction.backToLobby();
		Log.d(TAG, "Checked user is not in the chat room");
		
		Log.d(TAG, "Switching to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched to user B");
	}

	private void actionSearchJoinAndRejectJoinRequest(String roomName, String userName, boolean permanently) {
		Log.d(TAG, "Searching the chat room");
		lobbyAction.searchChatRoom(roomName);
		Log.d(TAG, "Searched the chat room");
		
		Log.d(TAG, "Send join request to that chat room");
		lobbyAction.joinChatRoom(description);
		Log.d(TAG, "Request sent");
		
		Log.d(TAG, "Checking the join response");
		checkResponseAfterSendRequest(roomName);
		Log.d(TAG, "Checked the join response");
		
		actionRejectJoinRequest(roomName, userName, permanently);
	}
	// This test all three possible reaction, rejected once -> leave, rejected once -> knock again, rejected permanently
	@LargeTest
	public void testJoinPrivateChatRejected() {
		Log.d(TAG, ">>> Test: testJoinPrivateChatRejected   -- Start -- <<<");

		Log.d(TAG, "Creating a new chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created the new chat room");
		
		String userName = allUserName[1];
		Log.d(TAG, "Switching to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched to user B");
		
		// Part A --------------------------------
		Log.d(TAG, "# Part A - Reject Once #");

		actionSearchJoinAndRejectJoinRequest(roomName, userName, false);		
		
		Log.d(TAG, "Checking the rejected response");
		checkRoomExist(roomName, true);
		Log.d(TAG, "Checked the rejected response");

		Log.d(TAG, "Leave the rejected room");
		lobbyAction.leaveARejectedRoom(roomName);
		Log.d(TAG, "Left the rejected room");
		
		Log.d(TAG, "Checking the rejected response");
		device().sleep(2000);
		checkRoomExist(roomName, false);
		Log.d(TAG, "Checked the rejected response");
		// ---------------------------------------
		
		// Part B Knock Again --------------------
		Log.d(TAG, "# Part B - Knock Again #");
		actionSearchJoinAndRejectJoinRequest(roomName, userName, false);

		Log.d(TAG, "Checking the rejected response");
		checkRoomExist(roomName, true);
		Log.d(TAG, "Checked the rejected response");

		Log.d(TAG, "Knock again");
		lobbyAction.reknockARejectedRoom(roomName, description);
		Log.d(TAG, "Left the rejected room");
		
		Log.d(TAG, "Checking the reknock response");
		checkWaitingJoinMessage(roomName);
		Log.d(TAG, "Checked the reknock response");
		
		actionRejectJoinRequest(roomName, userName, false);

		Log.d(TAG, "Leave the rejected room");
		lobbyAction.leaveARejectedRoom(roomName);
		Log.d(TAG, "Left the rejected room");
		// ----------------------------------------

		// Part C Reject Permanently --------------
		Log.d(TAG, "# Part C - Reject Permanently #");
		actionSearchJoinAndRejectJoinRequest(roomName, userName, true);

		Log.d(TAG, "Checking the rejected response");
		checkRoomExist(roomName, false);
		Log.d(TAG, "Checked the rejected response");
		
		Log.d(TAG, "Trying to knock the room again and confirm the response");
		checkGetPermanentlyRejectToChat(roomName);
		Log.d(TAG, "Confirmed");
		
		device().goBack();
		
		// ----------------------------------------
		
		// TearDown
		Log.d(TAG, "Switching to user A");
		switchAccountToTestAc2();
		Log.d(TAG, "Switched to user A");
		
		Log.d(TAG, "Deleting the chat room");
		lobbyAction.goChatRoom(roomName);
		chatAction.deleteChatRoom();
		Log.d(TAG, "Deleted the chat room");

		Log.d(TAG, ">>> Test: testJoinPrivateChatRejected   -- End -- <<<");
	}

	// ============================================================

	@MediumTest
	public void testJoinJoinedRoom() {
		Log.d(TAG, ">>> Test: testJoinJoinedRoom   -- Start -- <<<");

		String joinedRoom = "TestAc2";

		Log.d(TAG, "Searching the chat room");
		lobbyAction.searchChatRoom(joinedRoom);
		Log.d(TAG, "Searched the chat room");
		
		Log.d(TAG, "Clicking the first item from the result");
		device().waitForActivity("id/channel_name_text_view");
		device().clickInList(0);
		Log.d(TAG, "Clicked");
		
		Log.d(TAG, "Checking the redirect result");
		checkTheUserHaveRedirectToTheRoom(joinedRoom);
		Log.d(TAG, "Checked");

		device().goBack();
		Log.d(TAG, ">>> Test: testJoinJoinedRoom   -- End -- <<<");
	}
	
	@MediumTest
	public void testJoinWaitingAcceptRoom() {
		Log.d(TAG, ">>> Test: testJoinWaitingAcceptRoom   -- Start -- <<<");

		Log.d(TAG, "Creating a new chat room");
		String roomName = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created the new chat room");
		
		Log.d(TAG, "Switching to user B");
		switchAccountToTestAc3();
		Log.d(TAG, "Switched to user B");
		
		Log.d(TAG, "Searching the chat room");
		lobbyAction.searchChatRoom(roomName);
		Log.d(TAG, "Searched the chat room");
		
		Log.d(TAG, "Send join request to that chat room");
		lobbyAction.joinChatRoom(description);
		Log.d(TAG, "Request sent");
		checkResponseAfterSendRequest(roomName);
		
		Log.d(TAG, "Click on the room item AGAIN");
		device().clickInList(0);
		Log.d(TAG, "Click");
		
		Log.d(TAG, "Check the response(request sent)");
		checkTheRepeatRequestMessage();
		Log.d(TAG, "Checked");
		
		device().goBack();
		Log.d(TAG, ">>> Test: testJoinWaitingAcceptRoom   -- End -- <<<");
	}
	

	@SmallTest
	public void testSearchNotExistRoom() {
		Log.d(TAG, ">>> Test: testSearchNotExistRoom   -- Start -- <<<");
		
		String notExistRoom = "1OPsd23";
		Log.d(TAG, "Searching the chat room");
		lobbyAction.searchChatRoom(notExistRoom);
		Log.d(TAG, "Searched the chat room");
		Log.d(TAG, "Checking the remind toast exist");
		checkMessageWhenRoomNotFound();
		Log.d(TAG, "Checked the remind toast exist");
		device().goBack();
		
		Log.d(TAG, ">>> Test: testSearchNotExistRoom   -- End -- <<<");
	}
	
	@SmallTest
	public void testSearchInvalidHashTag() {
		
		Log.d(TAG, ">>> Test: testSearchInvalidHashTag   -- Start -- <<<");
		String[] invalidHashTag = {" ", "p p", "' OR 1=1;/-- ", "@#%$^@#$%", "[]", "><{\""};
		for(String s: invalidHashTag) {
			Log.d(TAG, "Searching the chat room: " + s);
			lobbyAction.searchChatRoom(s);
			Log.d(TAG, "Searched the chat room");
			Log.d(TAG, "Checking the remind toast exist");
			checkInvalidSearchToast();
			Log.d(TAG, "Checked the remind toast exist");
			
			//wait it to dismiss
			device().sleep(2000);

			device().goBack();
		}
		
		Log.d(TAG, ">>> Test: testSearchInvalidHashTag   -- End -- <<<");
	}
	
	//========================================
	
	public void checkResponseAfterSendRequest(String roomName) {
		
		// *******
		//Some bad magic before this bug in apps have been fix
		if (device().waitForText("An error has occurred")) {
			Log.d(TAG, "It popup a error message");
		} else {
			String expectedText = String.format("#%s got your request to join! They have 24 hours to let you in. Godspeed.", roomName);
			assertTrue("The response title is incorrect", device().waitForText("*Knock Knock!*"));
			assertTrue("The response text is incorrect", device().waitForText(expectedText));
		}
		device().clickOnButton("OK");
	}
	
	public void checkRoomExist(String roomName, boolean shouldExist) {
		device().sleep(4000);
		if (shouldExist) {
			assertTrue("The expected chat room is disappear",
					device().waitForText(roomName));
		} else {
			assertFalse("The chat romm should not exist in the list since you have left after rejected",
					device().waitForText(roomName));
		}
	}

	public void checkTheKnockMessage(String user, String roomName, String message) {
		String expectedString1 = String.format("*Knock* @%s wants to join #%s:", user, roomName);
		String expectedString2 = String.format("\"%s\"", message);
		assertTrue("The knocking message having shown", device().waitForText(expectedString1));
		assertTrue("The knocking description is incorrectly", device().waitForText(expectedString2));
		device().getView("id/info_icon_indicator_view");
	}
	
	public void checkUserSuccessJoinInRoomInfoPage(String name) {
		chatAction.openInfoPage();
		device().scrollDown();
		assertTrue("Cannot find the user name in the info page", device().searchText(name));
	}
	
	public void checkUserSuccessRejectedInRoomInfoPage(String name, boolean permanently) {
		do {
			 // wait until it have finish operation
		} while(device().waitForView(ProgressBar.class));
		device().sleep(4000);
		chatAction.openInfoPage();
		device().scrollToBottom();
		if (permanently) {
			device().waitForText("@" + name);
			TextView nameView = device().getText("@" + name);
			TextView banView = device().getText("Banned by moderators");
			
			assertEquals("The permanently rejected user should be banned", nameView.getParent(), banView.getParent());
			
		} else {
			assertFalse("The rejected user have shown in the info page", device().waitForText("@" + name));
		}
	}
	
	public void checkInvalidSearchToast() {
		assertTrue("Cannot find the toast", device().waitForText("Hashtag can only contain digits and letters"));
	}
	
	public void checkMessageWhenRoomNotFound() {
		assertTrue("Haven't show the nt found message", device().waitForText("No chats found with that name"));
	}
	
	public void checkTheUserHaveRedirectToTheRoom(String roomName) {
		device().assertCurrentActivity("The user haven't redirect to the chat room", "ChatRoomActivity");
		assertEquals("It have redirect to the wrong room", 
				String.format("#%s", roomName), ((TextView) device().getView("id/action_bar_title_text_view")).getText());
	}
	
	public void checkTheRepeatRequestMessage() {
		assertTrue("It haven't show the correct message for repeated join room request",
				device().waitForText("Request had been sent. Please wait patiently."));
		device().clickOnButton("OK");
	}
	
	public void checkWaitingJoinMessage(String roomName) {
		device().sleep(2000);
		checkRoomExist(roomName, true);
		device().clickOnText("#" + roomName);
		assertTrue("It haven't show the correct message for waiting join room",
				device().waitForText("Still waiting for someone to answer your Knock! We'll let you know when someone lets you in."));
		device().clickOnButton("OK");
	}
	
	public void checkGetPermanentlyRejectToChat(String roomName) {
		lobbyAction.searchChatRoom(roomName);
		device().clickInList(0);
		assertTrue("Still can be able to knock the chat room which you have been permentaly rejected",
				device().waitForText("Sorry, a moderator has banned you from this chat."));
		device().clickOnButton("OK");
	}

}
