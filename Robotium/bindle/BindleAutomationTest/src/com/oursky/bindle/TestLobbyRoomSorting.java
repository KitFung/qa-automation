package com.oursky.bindle;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class TestLobbyRoomSorting extends AndroidLoggedInTestBase{
	
	private final String meaninglessWord = "I am zero";
	private OperationInChat chatAction;
	private OperationInLobby lobbyAction;

	public TestLobbyRoomSorting() throws ClassNotFoundException {
		super();
	}
	
	public void setUp() throws Exception {
		super.setUp();
		chatAction = new OperationInChat(device());
		lobbyAction = new OperationInLobby(device());
	}
	
	/**
	 * Since there are too many possible case
	 * This just test the basic case - There are two chat room
	 * User A talk in room R1, then the position of R! should be on top of the R2.
	 * then user A talk in room R2 then R1, then the position if R2 should be on the top of R1.
	 * Then change to Room R3, check the top 2 item of the list. 
	 */
	public void testRoomSorting() {
		Log.d(TAG, ">>> Test: testRoomSorting   -- Start -- <<<");
		
		Log.d(TAG, "# Create two room for testing #");
		
		Log.d(TAG, "Start creating chat room R1");
		String roomNameR1 = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room");
		
		Log.d(TAG, "Talking in Room R1");
		chatAction.talkNormal(meaninglessWord);
		Log.d(TAG, "Talked");
		chatAction.backToLobby();
		
		Log.d(TAG, "Start creating chat room R2");
		String roomNameR2 = lobbyAction.createChatRoomUntilSuccess();
		Log.d(TAG, "Created chat room R2");
		
		Log.d(TAG, "Talking in Room R2");
		chatAction.talkNormal(meaninglessWord);
		Log.d(TAG, "Talked");
		chatAction.backToLobby();
		
		Log.d(TAG, "# Test the sorting in User A view #");
		
		Log.d(TAG, "Checking the sorting in User A view");
		String[] expectedRoomName = {roomNameR2, roomNameR1};
		checkSorting(2, expectedRoomName);
		Log.d(TAG, "Checked the sorting in User A view");

		Log.d(TAG, "Switching Account to User A");
		chatAction.backToLobby();
		switchAccountToTestAc2();
		Log.d(TAG, "Switched Account to User A");
		
		Log.d(TAG, "Deleting the chat room");
		lobbyAction.goChatRoom(roomNameR2);
		chatAction.deleteChatRoom();
		device().goBackToActivity("LobbyActivity");
		lobbyAction.goChatRoom(roomNameR1);
		chatAction.deleteChatRoom();
		Log.d(TAG, "Deleted the chat room");
		
		Log.d(TAG, ">>> Test: testRoomSorting   -- End -- <<<");
	}
	
	public void checkSorting(int firstNItem, String[] expectedRoomName) {
		ListView lv = (ListView) device().getView("id/channelListView");
		lobbyAction.refreshRoomList();
		device().scrollListToLine(lv, 0);
		for(int i=0; i < firstNItem; i++) {
			View v = device().getText("#" + expectedRoomName[i]);
			View ppv = (View) v.getParent().getParent().getParent();
			assertTrue("The ", lv.getChildAt(i).equals(ppv));
		}
	}
}
