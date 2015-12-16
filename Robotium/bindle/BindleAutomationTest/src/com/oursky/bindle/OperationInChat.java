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
	
	private void openInfoPage() {
		while(!device.getView("id/info_icon_image_view").isEnabled()) {
			device.sleep(2000);
		}
		device.clickOnView((ImageView) device.getView("id/info_icon_image_view"));
		device.waitForActivity("ChatRoomDetailActivity");
		// wait the info page fully loaded
		while(!device.searchText("Edit")) {
			device.sleep(2000);
		}
	}
	
	private void openEditPage() {
		device.clickOnView((View) device.getView("id/action_edit"));
		device.waitForActivity("ChatRoomEditActivity");
	}
	
	public void leaveChat() {
		device.clickOnView((ImageView) device.getView("id/info_icon_image_view"));
		device.waitForActivity("ChatRoomDetailActivity");
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
		device.goBackToActivity("LobbyActivity");
	}
	
	public void addOtherUser(String[] allName) {
		openInfoPage();
		device.scrollDown();
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
		
		device.clickOnView((Button) device.getView("id/delete_chat_button"));
		device.clickOnButton("Delete");
		device.clickOnButton("OK");
		device.waitForActivity("LobbyActivity");
	}

	public void kickUser(String poorguy, boolean permanentKick) {
		openInfoPage();
		device.scrollDown();
		device.sleep(2000);
		device.clickOnText(poorguy);
		device.clickOnText("Kick User");
		if (permanentKick) {
			device.clickOnText("Permanently");
		} else {
			device.clickOnText("Once");
		}
	}
	
	public void raiseUserToMod(String name) {
		openInfoPage();
		device.clickOnText(name);
		device.clickOnText("Promote to Moderator");
	}
	
	public void raiseUserToAdmin(String name) {
		openInfoPage();
		device.clickOnText(name);
		device.clickOnText("Promote to Admin");
	}

}
