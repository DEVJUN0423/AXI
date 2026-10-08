package Noise_Cut_Off.AxiDb;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.parser.ParseException;

import Noise_Cut_Off.JsonReader;

public class dbInsertRun {

    private final JsonReader reader;
    private final OracleDB oracleDB;
    private final UserTextDao utdao;
    private final UserDataDao uddao;
    private final getData gdata;

    public dbInsertRun() {
        this.reader = new JsonReader();
        this.oracleDB = new OracleDB();
        this.utdao = new UserTextDao(oracleDB);
        this.uddao = new UserDataDao(oracleDB);
		this.gdata = null;
    }


class getData {
	
    private final Connection conn;

	getData (Connection conn){
		this.conn = conn; 
	}
	
	// DB 조회해서 user_name 과 같은 이름 = 같은 user_id 부여
	String sameName(String type, int num) throws Exception {

	    String name1 = reader.rawText(type, num);

	    // WHERE 절로 DB가 직접 필터링하게 함 (전체 조회 X)
	    String sql = "SELECT USER_ID FROM USER_DATA WHERE USER_NAME = ?";

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, name1);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String userId = rs.getString("USER_ID");
//	                System.out.println("일치하는 이름 확인! userId: " + userId);
	                return userId;
	            }else {
	            	return null;
	            }
	        }
	    }

	}
	// DB 조회해서 user_name과 같은 ismapped return
	Integer ismapped(String type, int num) throws Exception {

	    String name1 = reader.rawText(type, num);

	    // WHERE 절로 DB가 직접 필터링하게 함 (전체 조회 X)
	    String sql = "SELECT IS_MAPPED FROM USER_DATA WHERE USER_NAME = ?";

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, name1);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("IS_MAPPED"); // IS_MAPPED 컬럼값 가져오기
	            } else {
	                return null; // DB에 해당 이름이 없는 경우
	            }
	        }
	    }
	}
	// is_mapped 결정 함수
	int mapped (int num) throws Exception {
		int printnum = 0;
		
		if(sameName("raw_sender", num) != null) {
			System.out.println("USER_ID = " + sameName("raw_sender", num));
			if(ismapped("raw_sender", num) == 7) {
				System.out.println("IS_MAPPED 값이 7입니다. 함수종료");
				return 7; }
			
			int number = ismapped("raw_sender", num);
			System.out.println("DB에 있는 IS_MAPPED = " + number);
			
			switch (number) {
			case 1: printnum = number + apptype("app_package",num); 
				System.out.println("return " + printnum);
				System.out.println("=======================================================");
				return printnum; 			
			case 2:	printnum = number + apptype("app_package",num); 
				System.out.println("return " + printnum);
				System.out.println("=======================================================");
				return printnum;
			case 4: printnum = number + apptype("app_package",num); 
				System.out.println("return " + printnum);
				System.out.println("=======================================================");
				return printnum;
			case 3: printnum = number + apptype("app_package",num); 
				System.out.println("return " + printnum);
				System.out.println("=======================================================");
				return printnum;
			case 6: printnum = number + apptype("app_package",num); 
				System.out.println("return " + printnum);
				System.out.println("=======================================================");
				return printnum;
			case 5: printnum = number + apptype("app_package",num); 
				System.out.println("return " + printnum);
				System.out.println("=======================================================");
				return printnum;
			}
		}
			
//			경우의 수
//			IS_MAPPED = 1 일때
//			1 + 2
//			1 + 4
//			IS_MAPPED = 2 일때
//			2 + 1
//			2 + 4
//			IS_MAPPED = 4 일때
//			4 + 1
//			4 +2
//			IS_MAPPED = 3 일때
//			3 + 4
//			IS_MAPPED = 6 일때
//			6 + 1
//			IS_MAPPED = 5 일떄
//			5 + 2
//			IS_MAPPED = 7 일때
//			7
		System.out.println("중복된 이름이 없습니다");
//		System.out.println("return: " + ismapped("raw_sender", num));
//		System.out.println("=======================================================");
		return ismapped("raw_sender", num);
	}
	
	// json 인스타, 카카오, 메시지 감지 각각 DB is_mapped  1, 2, 3 저장 0은 감지하지 못함
	int apptype (String type, int num) throws IOException, ParseException {
		String app = reader.rawText(type, num);
		if (app.equals("com.instagram.android")) {
			return 1;
		}else if ( app.equals("com.kakao.talk")) {
			return 2;
		}else if ( app.equals("SMS")){
			return 4;
		}
		return 0;
	}
	
	// DB같은 이름 조회 USER_ID 반환 함수
	String getUserId (String userName) throws SQLException {
		String sql = "SELECT USER_ID FROM USER_DATA WHERE USER_NAME = ?";
		
//	    System.out.println("getUserId userName" + userName);
	    
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, userName);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	            	
	            	String UserId = rs.getString("USER_ID");
//	            	System.out.println("getUserId USER_ID" + UserId);
	                return UserId; // DB에 존재하는 USER_ID 반환
	            }
	        }
	    }
	    return null; // 해당 이름이 없는 경우
	}
}

//통합 실행 함수
    public void run() throws Exception {
    	Connection conn = oracleDB.getConnection();
    	getData getdata = new getData(conn);
    	
        for (int i = 1; i <= reader.fileCount(); i++) {
			String userId = getdata.sameName("raw_sender", i);
			String userName = reader.rawText("raw_sender", i);
			String preText = reader.rawText("raw_text", i);
	        String appType = reader.rawText("app_package", i);
//	        int isMapped = getdata.mapped(i);
	        int isMapped = getdata.apptype("app_package", i);
            Timestamp msgTime = new Timestamp(Long.parseLong(reader.rawText("msg_time", i)));
            
         /* System.out.println("=====[ USER_DATA insert ]=====");
			System.out.println(i + " userId: " + userId);
			System.out.println(i + " userName: " + userName);
	        System.out.println(i + " isMapped: " + isMapped); */
	        
            // user_data table insert
            if (userId == null) {
    	        UserDataDto uddto = new UserDataDto(userId, userName, isMapped);
    	        
    	        int newDataId = uddao.insert(conn, uddto);
//    	        System.out.println(i + " newDataId: " + newDataId);
            } else {
            	System.out.println("DB에 이미 이름 존재 USER_DATA 저장 건너뜀");
            }
            
	        // 처음 저장되는 인물 -> 초기 userId = null 다라서 user_text 테이블 저장 불가능 user_data 저장시 user_id 자동 생성 조회후 user_text 추가
            userId = getdata.getUserId(reader.rawText("raw_sender", i));
            
         /* System.out.println("=====[ USER_TEXT insert ]=====");
			System.out.println(i + " text: " + preText);
	        System.out.println(i + " app: " + appType);
			System.out.println(i + " userId: " + userId);
            System.out.println(i + " msgTime: " + msgTime); */
            
//	      	String userText, String appType, int msgTime, Boolean isSummarized
	        UserTextDto utdto = new UserTextDto(preText, appType, userId, msgTime);

	        int newTextId = utdao.insert(conn, utdto);
//	        System.out.println(i + " newTextId: " + newTextId);
	        
	        //DB 에 전송한 파일명 리스트
	    	ArrayList<Integer> removeList = new ArrayList<>();
	    	
	        
        }
    }
}