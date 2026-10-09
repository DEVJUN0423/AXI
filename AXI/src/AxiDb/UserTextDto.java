package AxiDb;

import java.sql.Timestamp;

public class UserTextDto {
	
	// USER_TEXT TABLE 입력 값
    private String userText;
    private String appType;
	private Timestamp msgTime;
	private String userId;

	
    public UserTextDto(String userText, String appType, String userId, Timestamp msgTime) {
        this.userText = userText;
        this.appType = appType;
        this.userId = userId;
        this.msgTime = msgTime;
    }

    
    public String getUserText() { return userText; }
    public String getAppType() { return appType; }
    public String getUserId() { return userId; }
    public Timestamp getMsgTime() {return msgTime; }

    
    
}