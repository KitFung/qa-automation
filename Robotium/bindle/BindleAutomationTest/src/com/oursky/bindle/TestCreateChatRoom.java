package com.oursky.bindle;

import java.util.UUID;

import junit.framework.AssertionFailedError;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Smoke;
import android.widget.EditText;
import android.widget.TextView;

public class TestCreateChatRoom extends AndroidLoggedInTestBase {

	private OperationInChat chatAction;

	public TestCreateChatRoom() throws ClassNotFoundException {
		super();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
	}

	//Success Test
	@Smoke
	public void testSuccessCreateRoom() {
		String s = null;
		while(true) {
			s = getUUID();
			createChatRoom(s);
			while (true){
				try{
					device().sleep(2000);
					device().clickOnView((TextView) device().getView("id/action_done"));
					device().waitForActivity("ChatRoomActivity");
					break;
				}catch(AssertionFailedError e) {
					continue;
				}
			}
			break;
		}
		successCreateChatRoomChecking(String.format("#%s is ready for action.", s));
		chatAction.deleteChatRoom();
	}

	//Failed Test
	//=====================================================

	@SmallTest
    public void testCreateRepeatedChatRoom() {
		createChatRoom("TestAc2");
		failedCreateChatRoomChecking(String.format("#%s is already taken!", "TestAc2"));
		chatAction.backToLobby();
    }
	
	@SmallTest
	public void testKissYourMother() {
		createChatRoom("Fuck");
		failedCreateChatRoomChecking("You kiss your mother with that mouth?");
		chatAction.backToLobby();
	}
	
	@SmallTest
	public void testNoNameInput() {
		createChatRoom("");
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
			createChatRoom(w);
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
	
	public void createChatRoom(String roomName) {
		device().clickOnView(device().getView("id/create_chat_view"));
		device().waitForActivity("CreateChatRoomActivity");
		device().clearEditText((EditText)device().getView("id/chat_room_name_edit_text"));
    	device().enterText((EditText) device().getView("id/chat_room_name_edit_text"), roomName);
        device().clickOnView((TextView) device().getView("id/action_done"));
	}

	 public static String getUUID(){ 
        String s = UUID.randomUUID().toString(); 
        return s.substring(0,8);
    } 
}
