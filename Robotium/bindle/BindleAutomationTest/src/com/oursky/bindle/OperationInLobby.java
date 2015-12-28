package com.oursky.bindle;

import java.util.UUID;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import com.robotium.solo.Solo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class OperationInLobby {
	
	Solo device;
	
	OperationInLobby(Solo device) {
		this.device = device;
	}

	public void goChatRoom(String name) {
		device.waitForText(name);
		device.clickOnText("#"+name);
		device.waitForActivity("ChatRoomActivity");
		device.sleep(5000);
	}
	
	public void refreshRoomList() {
		device.scrollListToTop(0);
		ListView listview = (ListView) device.getView("id/channelListView");
		int location[] = new int[2];
		listview.getLocationOnScreen(location);
		device.drag(location[0]+10,location[0]+10,location[1],location[1]+listview.getHeight(),3);
		device.sleep(5000);
	}

	public void createChatRoom(String roomName) {
		device.clickOnView(device.getView("id/create_chat_view"));
		device.waitForActivity("CreateChatRoomActivity");
		device.clearEditText((EditText) device.getView("id/chat_room_name_edit_text"));
    	device.enterText((EditText) device.getView("id/chat_room_name_edit_text"), roomName);
        device.clickOnView((TextView) device.getView("id/action_done"));
	}
	
	public String createChatRoomUntilSuccess() {
		String roomName = null;
		while(true) {
			roomName = UUID.randomUUID().toString().substring(0,8);;
			device.clickOnView(device.getView("id/create_chat_view"));
			device.waitForActivity("CreateChatRoomActivity");
			device.clearEditText((EditText)device.getView("id/chat_room_name_edit_text"));
	    	device.enterText((EditText) device.getView("id/chat_room_name_edit_text"), roomName);
	    	while (true){
				try{
					device.sleep(2000);
					device.clickOnView((TextView) device.getView("id/action_done"));
					device.waitForActivity("ChatRoomActivity");
					device.assertCurrentActivity("", "ChatRoomActivity");
					Assert.assertFalse(device.waitForText("Create a chat"));
					break;
				}catch(AssertionFailedError e) {
					if(device.searchText("This chatroom already exists.")) {
						device.clickOnView((Button) device.getView("id/button2"));
					}
					break;
				}
			}
	    	break;
		}
		device.waitForText(String.format("#%s", roomName));
		device.sleep(4000);
		return roomName;
	}
	
	public void searchChatRoom(String roomName) {
		device.clickOnView(device.getView("id/join_chat_view"));
		device.waitForActivity("CreateChatRoomActivity");
		device.clearEditText((EditText) device.getView("id/search_edit_text"));
    	device.enterText((EditText) device.getView("id/search_edit_text"), roomName);
        device.clickOnView((TextView) device.getView("id/action_search"));
	}
	
	public void joinChatRoom(String descript) {
		device.waitForActivity("id/channel_name_text_view");
		device.clickInList(0);
		device.waitForActivity("JoinChatRoomActivity");
		device.enterText((EditText) device.getView("id/message_edit_text_view"), descript);
		device.clickOnButton("Join");
	}
	
	public void leaveARejectedRoom(String roomName) {
		device.clickOnText("#"+ roomName);
		device.clickOnText("Leave");
	}
	
	public void reknockARejectedRoom(String roomName, String description) {
		device.clickOnText("#"+ roomName);
		device.clearEditText((EditText) device.getView("id/message_edit_text_view"));
		device.enterText((EditText) device.getView("id/message_edit_text_view"), description);
		device.clickOnText("Knock again");
	}
}
