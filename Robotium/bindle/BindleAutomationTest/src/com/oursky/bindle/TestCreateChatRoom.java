package com.oursky.bindle;

import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;

public class TestCreateChatRoom extends AndroidLoggedInTestBase {

	private OperationInChat chatAction;
	private OperationInLobby lobbyAction;

	public TestCreateChatRoom() throws ClassNotFoundException {
		super();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		lobbyAction = new OperationInLobby(device());
		chatAction = new OperationInChat(device());
	}

	//Success Test
	@Smoke
	public void testSuccessCreateRoom() {
		Log.d(TAG, ">>> Test: testSuccessCreateRoom   -- Start -- <<<");
		
		Log.d(TAG, "Creating chat room");
		String s = lobbyAction.createChatRoomUntilSuccess();
		successCreateChatRoomChecking(String.format("#%s is ready for action.", s));
		Log.d(TAG, "Created chat room");

		Log.d(TAG, "deleting chat room");
		chatAction.deleteChatRoom();
		Log.d(TAG, "deleted chat room");

		Log.d(TAG, ">>> Test: testSuccessCreateRoom   -- End -- <<<");
	}

	//Failed Test
	//=====================================================

	@SmallTest
    public void testCreateRepeatedChatRoom() {
		Log.d(TAG, ">>> Test: testCreateRepeatedChatRoom   -- Start -- <<<");

		Log.d(TAG, "Checking the response when create chat room with repeated name");
		lobbyAction.createChatRoom("TestAc2");
		failedCreateChatRoomChecking(String.format("#%s is already taken!", "TestAc2"));
		chatAction.backToLobby();
		Log.d(TAG, "Checked the response");

		Log.d(TAG, ">>> Test: testCreateRepeatedChatRoom   -- End -- <<<");
	}
	
	@SmallTest
	public void testKissYourMother() {
		Log.d(TAG, ">>> Test: testKissYourMother   -- Start -- <<<");

		Log.d(TAG, "Checking the response when create chat room with dirty word");
		lobbyAction.createChatRoom("Fuck");
		failedCreateChatRoomChecking("You kiss your mother with that mouth?");
		Log.d(TAG, "Checked the response");
		chatAction.backToLobby();

		Log.d(TAG, ">>> Test: testKissYourMother   -- End -- <<<");
	}
	
	@SmallTest
	public void testNoNameInput() {
		Log.d(TAG, ">>> Test: testNoNameInput   -- Start -- <<<");

		Log.d(TAG, "Checking the response when create chat room no name");
		lobbyAction.createChatRoom("");
		failedCreateChatRoomChecking("Give your chat a unique hashtag so others can find it.?");
		Log.d(TAG, "Checked the response");
		chatAction.backToLobby();
		
		Log.d(TAG, ">>> Test: testNoNameInput   -- End -- <<<");
	}
	
	@MediumTest
	public void testWrongNameFormat() {
		Log.d(TAG, ">>> Test: testWrongNameFormat   -- Start -- <<<");
	
		String[] wrongCase = 
			{
				"a b c", "a_b",
				"a1*op", "as[]p",
				"\\\n", "Drop table \",\"1;DROP TABLE users", 
				"1'; DROP TABLE users-- 1", "' OR 1=1 -- 1", 
				"' OR '1'='1", " "
			};
		for(String w:wrongCase) {
			Log.d(TAG, String.format("Checking the response when using %s as name", w));
			lobbyAction.createChatRoom(w);
			failedCreateChatRoomChecking("Name can contain letters and numbers only. No spaces.");
			chatAction.backToLobby();
			Log.d(TAG, "Checked the response");
		}
		
		Log.d(TAG, ">>> Test: testWrongNameFormat   -- End -- <<<");
	}
	
	//======================================================
	
	public void successCreateChatRoomChecking(String expectedMessage) {
		device().assertCurrentActivity("Failed Create Chat Room", "ChatRoomActivity");
		assertTrue("Don't have welcome message.",
    			device().waitForText(expectedMessage));
	}
	
	public void failedCreateChatRoomChecking(String expectedMessage) {
		device().assertCurrentActivity("Failed validation", "CreateChatRoomActivity");
		assertTrue("Failed Create Chat Room(Repeated name)",
    			device().waitForText(expectedMessage));
	}
}
