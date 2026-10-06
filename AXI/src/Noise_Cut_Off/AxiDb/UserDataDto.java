package Noise_Cut_Off.AxiDb;


public class UserDataDto {
	
	// USER_DATA 입력 값
	private String userId;
	private String userName;
	private int isMapped;
	
	public UserDataDto(String userId, String userName, int isMapped) {
		this.userId = userId;
		this.userName = userName;
		this.isMapped = isMapped;
	}
	
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getIsMapped() { return isMapped; }
}