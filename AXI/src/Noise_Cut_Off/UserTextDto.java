package Noise_Cut_Off;

public class UserTextDto {
	
    private String userText;
    private String appType;
    private int userId;

    public UserTextDto(String userText, String appType, int userId) {
        this.userText = userText;
        this.appType = appType;
        this.userId = userId;
    }

    public String getUserText() { return userText; }
    public String getAppType() { return appType; }
    public int getUserId() { return userId; }
}