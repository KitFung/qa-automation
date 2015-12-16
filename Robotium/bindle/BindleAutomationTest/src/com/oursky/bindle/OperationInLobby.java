package com.oursky.bindle;

import java.util.UUID;

import junit.framework.AssertionFailedError;

import com.robotium.solo.Solo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OperationInLobby {
	
	Solo device;
	
	OperationInLobby(Solo device) {
		this.device = device;
	}

	public void goChatRoom(String name) {
		device.clickOnText("#"+name);
		device.waitForActivity("ChatRoomActivity");
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
}
