Reminder
=========

1. Change the value of the variable `newScreenName` in  `TestSignUpPage.java` everytime you run the test. Otherwise, it will take a long time to find the name that haven't register before.


List of Test Case
=========
1. **Tutotial Page**
	- Class
		- TestTutorialPage

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testTutorialPageExist |Test the existence of the tutorial page| Go to the tutorial page --> Check the page exist? 
	testNextPageOfTutorialPage |Test the redirect of the tutorial page| Go to tutorial page --> Click next --> Check the page after redirected
	
2. **Welcome Page**
	- Class
		- TestWelcomePage

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testSignUpBtn|Test the redirection of the sign up button| Go to Welcome Page --> Click the Sign up button --> Check the Activity
	testLoginBtn|Test the redirection of the login button| Go to Welcome Page --> Click the Log In button --> Check the Activity

3. **Sign Up Page**
	- Class
		- TestSignUpPage

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testSignUp|Test the sign up function work properly|Go to Sign Up Page --> Fill in the information --> Click OK --> Check the result(Success)
	testSignUpWithRegisteredEmail|Test the apps can reject the sign up if the email is registered|Go to Sign Up Page --> Fill in the information with a registered email --> Click OK --> Check the response(Warning message)
	testSignUpWithoutEnterAnyInfo|Test the apps can reject the sign up if the user haven't input any data|Go to Sign Up Page --> Click OK --> Check the response(Warning message)
	testSignUpWithWrongEmailFormat|Test the apps can detect the wrong email format and reject the sign up|Fill in the information with a wrong format email --> Click OK --> Check the response(Warning message)
	testSignUpMissingEmail|Test the apps can reject the sign up if the email is missing|Go to Sign Up Page --> Fill in the information except email --> Click OK --> Check the result(Warning message)
	testSignUpMissingPassword|Test the apps can reject the sign up if the password is missing|Go to Sign Up Page --> Fill in the information except password --> Click OK --> Check the result(Warning message)
	testSignUpWrongPasswordFormat|Test the apps can detect the wrong password format and reject the sign up|Fill in the information with a wrong format password --> Click OK --> Check the response(Warning message)
	testMissingBDate|Test the apps can reject the sign up if the birthday is missing|Go to Sign Up Page --> Fill in the information except birthday --> Click OK --> Check the result(Warning message)
	testWrongBDate|Test the apps can detect the wrong birthday range and reject the sign up|Fill in the information with a wrong birthday range --> Click OK --> Check the response(Warning message)
	testSignUpScreenNameWithSpace|Test the apps can detect the wrong screen name format and reject the sign up|Fill in the information with a name which contain space --> Click OK --> Check the response(Warning message)

	

4. **Log In Page**
	- Class
		- TestLoginPage

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testLoginWithEmailWithSms|Test the login function can work successfully and show the SMS validate dialog if the user haven't validated before|Go to Login page --> Log in --> Check the response (Login Success)
	testLoginWithEmail|Test the login function can work successfully with email|Go to Login page --> Log in with email --> Check the response (Login Success)
	testLoginWithUserName|Test the login function can work successfully with user name|Go to Login page --> Log in with user name --> Check the response (Login Success)
	testLoginWithUpperCaseUserName|Test the login function can work successfully with upper case user name since it is case-insensitive|Go to Login page --> Log in with upper case user name --> Check the response (Login Success)
	testLoginInLandscape|Test the login function can work succesfully when the phone is under landscape mode|Change to landscape mode --> Go to Login page --> Log in --> Check the response (Login Success)
	testLoginWithWrongPassword|Test the apps can reject login when the password is wrong|Go to Login Page --> Login with wrong password --> Check the response (Login failed)
	testLoginMissingAccountAndPassword|Test the apps can reject login when all the field in empty| Go to Login Page --> Login with empty information --> Check the response (Login failed)
	testLoginMissingAccount|Test the apps can reject login when there are no account name|Go to Login Page --> Login without account name/email --> Check the response (Login failed)
	testLoginMissingPassword|Test the apps can reject login when there are no password|Go to Login Page --> Login without password --> Check the response (Login failed)

5. **Forget Passowrd Page**	
	- Class
		- TestLoginPage

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testForgetPasswordWithAcName|Test the forget password function with a registered account name| Go to Forget Password Page --> Forget password by using the account name --> Check the response(Success and redirect to Login page)
	testForgetPasswordWithEmail|Test the forget password function with a registered email| Go to Forget Password Page --> Forget password by using the email --> Check the response(Success and redirect to Login page)
	testForgetPasswordWithDifferentCase|Test the Forget Password Function with the different casing user name|Go to Forget Password Page --> Forget password by using the account name(lower case) --> Check the response(Success and redirect to Login page) --> Go to Forget Password Page --> Forget password by using the account name(upper case) --> Check the response(Success and redirect to Login page)
	testForgetPasswordWithNotExistEmail|Test the apps can reject and show the warning toast/dialog when the user input the not registered email| Go to Forget Password Page --> Forget password by using the not registered email --> Check the response(Failed and show warning message)
	testForgetPasswordWithNotExistUserName|Test the apps can reject and show the warning toast/dialog when the user input the not registered user name| Go to Forget Password Page --> Forget password by using the not registered user name --> Check the response(Failed and show warning message)
	
6. **Create Chat Room Page**	
	- Class
		- TestCreateChatRoom

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testSuccessCreateRoom|Test the create chat room function can work properly|Login to a pre-registered account --> Create a chat room (keep trying different chat room name until success) --> Success created --> Check the outcome(Room was created)
	testCreateRepeatedChatRoom|Test to create a chat room which name is created before, the apps should be able to rejecting/warning it|Login to a pre-registered account --> Create a chat room (use a name that is pre-registered) --> Failed --> Check the response(Warning message)
	testKissYourMother|Test the apps can show the warning messaage when the user trying to use dirty to be room name|Login to a pre-registered account --> Create a chat room (use a name that is pre-registered) --> Failed --> Check the response(Warning message)
	testNoNameInput|Test that it should not be able to create a chat room with no name|Login to a pre-registered account --> Create a chat room (without name) --> Nothing happen
	testWrongNameFormat|Test the apps can reject the wrong chat room name format while create create chat room|Login to a pre-registered account --> Create a chat room (use a set of name that not fit the format) --> Failed --> Check the response(Warning message)

7. **Lobby Page**	
	- Class
		- TestLobbyRoomSorting

	Test Case | Objective | Test Flow
	----------|-----------|---------- 
	testRoomSorting|Test that the room sorting that the most recent update chatroom will place at top|Login with a pre-registered account --> Create chat room R1 --> Talk in room R1 --> Create chat room R2 --> Talk in room R2 --> Checking the sorting order(R2 located at the first row, R1 in second row)

7. **Open Chat Page**	
	- Class
		- TestOpenChat
	
	Test Case | Objective | Test Flow
	----------|-----------|---------- 
	testOpenChat| Test Join Open Chat, Send message in Open Chat, Send emote in Open Chat, Reject no target whisper, Reject whisper all, Send GIF, Leave Open Chat| Login to a pre-registered account --> Find a Open Chat --> Join the Open chat room --> Send normal message --> Send GIF --> Send emote --> Send whisper with no target --> Send whisper to all --> Leave Open Chat

8. **Search and Join Private Chat Room**
	- Class
		- TestSearchAndJoinPrivateChat
	
	Test Case | Objective | Test Flow
	----------|-----------|---------- 
	testJoinPrivateChatAccepted|Test searching a private chat room ad join it successfully|Login to a pre-registered account: User A --> Create a private room --> Switch account to another pre-registered account: User B --> Search the chat room --> Sent request to join the chat room --> Back to lobby and check the room exist --> Switch to User A --> Accept the User B join request --> Check whether he join successfully --> Switch to User B --> Check whether he join successfully
	testJoinPrivateChatRejected|Test reject the join room request. This test covered all three possible reaction, rejected once -> leave, rejected once -> knock again, rejected permanently|Login to a pre-registered account: User A --> Create a private chat room --> Switch to another pre-registered account: User B --> Search the room and sent join request --> Switch to User A --> Reject once --> Switch to User B --> Check the request status --> Leave that chat room --> Search the chat room and sent join request --> Switch to User A --> Reject Once --> Switch to User B --> Reknock the chat room --> Switch to User A --> Reject Permanently --> Switch to User B --> Check the response(Cannot join again)
	testJoinJoinedRoom|Test the redirection and reaction while the user search and join a joined room|Login to a pre-registered account --> Search and Join a joined room --> Check the response (Redirect the user)
	testJoinWaitingAcceptRoom|Test the response when chat room that is waiting for accept|Login to a pre-registered account:User A --> Create a new chat room --> Switch to another pre-registered account --> Search and sent the join request --> Search and sent the join request again --> Check the response ("Warning Message")
	testSearchNotExistRoom|Test the result when search the not exist room|Login to a pre-registered account --> Find a room that not exist --> Check the response
	testSearchInvalidHashTag|Test the response when input invalid room name format/hashtag|Login to a pre-registered account --> Search the room with invalid hashtag --> Check the response(Warning message)


9. **Private Chat Room Page**
	- Class
		- TestPrivateRoom

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testDeleteChatRoom|Test the chat room delete function and the redirection after delete|Login to a pre-registered account --> Create a private chat room --> Delete the chat room --> Check the response (Successfully deleted and redirect to lobby)
	testSendDifferentTypeOfMessage|Test send message and receive message in a private chat room. This also tested the add user by name function. This test covered whisper someone, normal message, @chat message, emote message.|Login to a pre-registered account: User A --> Create a private chat room --> Add user B, C by username --> User A send normal message --> Check the message have sent --> User A whisper User B --> Check the whisper message exist --> User A send emote message --> Check emote message exist --> User A send @chat message --> Check @chat message exist --> Switch to User B --> Check the whisper message exist --> Check the normal message exist --> Check the @chat message exist --> check the emote message exist --> Switch to User C --> Check the whisper message not exist --> check the normal message exist --> Check the @chat message exist --> Check the emote message exist
	testTheBlueLabelForMention|Test the blue label when someone mention you|Login to a pre-registered account --> Create a chat room --> Add user B,C into the chat room --> User A mention User B --> Switch to User B --> Go to Chat room and check the blue label and mention text exist --> Switch to User C --> Go to Chat room and check mention text exist. Also check the the blue label not exist
	testKickUserOutofChatRoomOnce|Test Kick Once function which the user can send join request again|Login to a pre-registered account --> Create the chat room --> Add User B,C by name --> Switch to User B, C to make sure they see the room and able to enter --> Switch back to User a --> Kick User B Once --> Switch to User B --> Make sure the room not display on list --> Search the room and knock --> Switch to User C --> Check the room appear in list
	testKictUserOutofChatRoomForever|Test Kick Forever function which the user cannot send join request again|Login to a pre-registered account: User A --> Create the chat room --> Add User B, C by name --> Switch to User B, C to make sure they see the room and able to enter the room --> Switch back to User A --> Kick User B permanently --> Switch Account to User B --> Check the room not exist --> Check whether he can knock the room again(expected not) --> Switch to User C --> Check the room exist

10. **Private Chat Permission Assign Page**
	- Class
		- TestPrivateChatPermissionAssign

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testRaiseUserToMod|Test raising a user to be moderator|Login to a pre-registered account: User A --> Create the chat room --> Add User B,C by name --> Raise User B to moderator --> Check the current permission of User B --> Switch Account to User B --> Check the current role/permission --> Switch Account to User C --> Check the current role/permission
	testRaiseUserToAdmin|Test raising a user to be admin|Login to a pre-ergistered account: User A --> Create the chat room --> And User B,C by name --> Raise User B to Admin --> Check the current role/permission of User A(should be moderator) --> Switch to User B --> Check the current role/permission --> Switch Account to User C --> Check the current role/permission

11. **Private Chat Setting Page**
	- Class
		- TestPrivateChatSetting

	Test Case | Objective | Test Flow
	----------|-----------|----------
	testSetRoomDescription|Test whether the user can change the page description|Login to a pre-registered account: User A --> Create a Chat Room --> Add User B by name --> Raise User B to moderator --> Change the room description --> Switch account to User B --> Checking the description --> Changing the description --> Switch account to User A --> Check the change in the description
	testOnlyModsCanUseChat|Test the setting that only the moderator can use @chat function|Login to a pre-registered account: User A --> Create a Chat Room --> Add User B, C to chat room --> Raise User B to moderator --> Setting only moderators can use @chat --> Switch Account to User B --> Send a @chat message --> Switch Account to User C --> Try to Send a @chat message(Check it should fail)
	testOnlyModsCanAddNewPeople|Test the setting that only the moderator can use add new people|Login to a pre-registered account: User A --> Create a Chat Room --> Add User B, C to chat room --> Raise User B to moderator --> Setting only moderators can add user --> Switch Account to User B --> Try to add User D --> Switch Account to User C --> Check the add user button disappeared
	testCustomNotifications|Test the personal custom notification setting. This test case dosen't test the function of the notification, it just check whether the setting have been saved and will the setting of one user affect another user|Login to a pre-registered account: User A --> Create a Chat Room --> Add User B to chat room --> Modify the notification setting --> Leave and reEnter the chat room --> Checking the previous change --> Switch Account to User B --> Checking the notification setting(it should not change due to the change in User A)

	