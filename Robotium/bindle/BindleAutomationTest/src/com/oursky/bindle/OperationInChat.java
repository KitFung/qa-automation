package com.oursky.bindle;

import junit.framework.AssertionFailedError;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.robotium.solo.Solo;

public class OperationInChat {
	
	private Solo device;
	
	OperationInChat(Solo device) {
		this.device = device;
	}
	
	public void openInfoPage() {
		device.sleep(10000); // Just let it fail if it load toooo slow
		device.clickOnView((ImageView) device.getView("id/info_icon_image_view"));
		device.waitForActivity("ChatRoomDetailActivity");
		// wait the info page fully loaded
		while(!device.searchText("Add by Username")) {
			device.sleep(2000);
		}
	}
	
	public void openEditPage() {
		device.clickOnView((View) device.getView("id/action_edit"));
		device.waitForActivity("ChatRoomEditActivity");
	}
	
	public void openOptionMenuForUser(String name) {
		openInfoPage();
		device.scrollDown();
		device.sleep(2000);
		device.clickOnText(name);
	}
	
	public void leaveChat() {
		openInfoPage();
		device.clickOnView((TextView) device.getView("id/leave_button"));
		device.clickOnButton("Leave");
		device.waitForActivity("LobbyActivity");
	}
	
	public void sendFirstGIF() {
		device.clickOnView((ImageButton) device.getView("id/gif_button"));
		device.waitForActivity("MediaSearchActivity");

		device.clickOnView((ImageView) device.getView("id/url_image_view"));
		device.waitForActivity("MediaSearchPreviewActivity");
		device.clickOnButton("Send");
		device.waitForActivity("ChatRoomActivity");	
	}
	
	public void talkNormal(String s) {
		device.clearEditText((EditText) device.getView("id/sendMessage_editText"));
    	device.enterText((EditText) device.getView("id/sendMessage_editText"), s);
    	device.clickOnView((ImageButton) device.getView("id/sendMessage_button"));

    	device.waitForText(s);
	}
	
	public void whisperUser(String userName, String text) {
		device.clearEditText((EditText)device.getView("id/sendMessage_editText"));
    	device.enterText((EditText) device.getView("id/sendMessage_editText"), String.format("/whisper @%s %s", userName, text));
    	device.clickOnView((ImageButton) device.getView("id/sendMessage_button"));
	}
	
	public void emoteText(String text) {
		device.clearEditText((EditText) device.getView("id/sendMessage_editText"));
    	device.enterText((EditText) device.getView("id/sendMessage_editText"), String.format("/emote %s", text));
    	device.clickOnView((ImageButton) device.getView("id/sendMessage_button"));
	}
	
	public void mentionSomeone(String username, String text) {
		String formattedText = String.format("@%s %s", username, text);
		device.clearEditText((EditText) device.getView("id/sendMessage_editText"));
    	device.enterText((EditText) device.getView("id/sendMessage_editText"), formattedText);
    	device.clickOnView((ImageButton) device.getView("id/sendMessage_button"));
	}
	
	public void backToLobby() {
		device.sleep(2000);
		device.goBackToActivity("LobbyActivity");
	}
	
	public void addOtherUser(String[] allName) {
		openInfoPage();
		device.scrollDown();
		device.sleep(1000);
		device.clickOnText("Add by Username");
		device.waitForActivity("AddByUsernameActivity");
		for(String s: allName) {
			while(true) {
				try {
					device.clearEditText((EditText) device.getView("id/search_src_text"));
					device.enterText((EditText) device.getView("id/search_src_text"), s);
					device.pressSoftKeyboardSearchButton();
					device.waitForText("Search Users");
					device.clickOnButton(0);
					device.waitForText("Added");
					device.clickOnView((TextView) device.getView("id/user_search"));
					break;
				} catch (AssertionFailedError e) {
					if(device.searchText("Opps")) {
						device.clickOnButton(0);
					}
					device.goBackToActivity("ChatRoomDetailActivity");
					device.waitForText("Edit");
					device.sleep(4000);
					device.scrollDown();
					device.clickOnText("Add by Username");
					device.waitForActivity("AddByUsernameActivity");
					continue;
				}
			}
		}
		device.goBackToActivity("ChatRoomActivity");
	}
	

	
	public void deleteChatRoom() {
		openInfoPage();
		openEditPage();
		
		deleteChatRoomInEditPage();
	}
	
	public void deleteChatRoomInEditPage() {
		device.clickOnView((Button) device.getView("id/delete_chat_button"));
		device.clickOnButton("Delete");
		device.clickOnButton("OK");
		device.waitForActivity("LobbyActivity");
	}

	public void kickUser(String poorguy, boolean permanentKick) {
		openOptionMenuForUser(poorguy);
		device.clickOnText("Kick User");
		if (permanentKick) {
			device.clickOnText("Permanently");
		} else {
			device.clickOnText("Once");
		}
	}
	
	public void raiseUserToMod(String name) {
		openOptionMenuForUser(name);
		device.clickOnText("Promote to Moderator");
	}
	
	public void raiseUserToAdmin(String name) {
		openOptionMenuForUser(name);
		device.clickOnText("Promote to Admin");
		device.clickOnButton("Yes");
	}
	
	public void acceptJoinRequest(String name) {
		device.clickOnView(device.getView("id/accept_button_view"));
	}
	
	public void rejectJoinRequest(String name, boolean permanently) {
		device.clickOnView(device.getView("id/reject_button_view"));
		if (permanently) {
			device.clickOnText("Permanently");
		} else {
			device.clickOnText("Once");
		}
	}
	
	public void editRoomDescription(String description) {
		EditText descriptionBox = (EditText)device.getView("id/chat_room_description_edit_text");
		device.clearEditText(descriptionBox);
		device.enterText(descriptionBox, description);
		device.clickOnView((View)device.getView("id/action_apply"));
	}

}
