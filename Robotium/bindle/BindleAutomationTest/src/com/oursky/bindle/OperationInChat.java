package com.oursky.bindle;

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
	
	public void deleteChatRoom() {
		while(!device.getView("id/info_icon_image_view").isEnabled()) {
			device.sleep(2000);
		}
		device.clickOnView((ImageView) device.getView("id/info_icon_image_view"));
		device.waitForActivity("ChatRoomDetailActivity");
		while(!device.searchText("Edit")) {
			device.sleep(2000);
		}
		device.clickOnView((View) device.getView("id/action_edit"));
		device.waitForActivity("ChatRoomEditActivity");
		device.clickOnView((Button) device.getView("id/delete_chat_button"));
		device.clickOnButton("Delete");
		device.clickOnButton("OK");
		device.waitForActivity("LobbyActivity");
	}

	public void kickUser(String poorguy, boolean permanentKick) {
		device.clickOnView((ImageView) device.getView("id/info_icon_image_view"));
		device.waitForActivity("ChatRoomDetailActivity");
		while(!device.searchText("Edit")) {
			device.sleep(2000);
		}
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

}
