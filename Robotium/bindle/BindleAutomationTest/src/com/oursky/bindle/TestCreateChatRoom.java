package com.oursky.bindle;

import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;

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
		String s = lobbyAction.createChatRoomUntilSuccess();
		successCreateChatRoomChecking(String.format("#%s is ready for action.", s));
		chatAction.deleteChatRoom();
	}

	//Failed Test
	//=====================================================

	@SmallTest
    public void testCreateRepeatedChatRoom() {
		lobbyAction.createChatRoom("TestAc2");
		failedCreateChatRoomChecking(String.format("#%s is already taken!", "TestAc2"));
		chatAction.backToLobby();
    }
	
	@SmallTest
	public void testKissYourMother() {
		lobbyAction.createChatRoom("Fuck");
		failedCreateChatRoomChecking("You kiss your mother with that mouth?");
		chatAction.backToLobby();
	}
	
	@SmallTest
	public void testNoNameInput() {
		lobbyAction.createChatRoom("");
		failedCreateChatRoomChecking("Give your chat a unique hashtag so others can find it.?");
		chatAction.backToLobby();
	}
	
	@MediumTest
	public void testWrongNameFormat() {
		String[] wrongCase = 
			{
				"a b c", "a_b",
				"a1*op", "as[]p",
				"\\\n", "Drop table \",\"1;DROP TABLE users", 
				"1'; DROP TABLE users-- 1", "' OR 1=1 -- 1", 
				"' OR '1'='1", " "
			};
		for(String w:wrongCase) {
			lobbyAction.createChatRoom(w);
			failedCreateChatRoomChecking("Name can contain letters and numbers only. No spaces.");
			chatAction.backToLobby();
		}
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
