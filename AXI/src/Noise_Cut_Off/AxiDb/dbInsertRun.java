package Noise_Cut_Off.AxiDb;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	// DB 조회해서 user_name 과 같은 이름 = 같은 user_id 부여
	String sameName(String type, int num) throws Exception {

	    String name1 = reader.rawText(type, num);

	    // WHERE 절로 DB가 직접 필터링하게 함 (전체 조회 X)
	    String sql = "SELECT USER_ID FROM USER_DATA WHERE USER_NAME = ?";

	    try (Connection conn = oracleDB.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, name1);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String userId = rs.getString("USER_ID");
	                System.out.println("일치하는 이름 확인! userId: " + userId);
	                return userId;
	            }
	        }
	    }

	    return reader.rawText("text_id", num); // 못 찾음
	}

	// json 인스타, 카카오, 메시지 감지 각각 DB is_mapped  1, 2, 3 저장 0은 감지하지 못함
	int apptype (String type, int num) throws IOException, ParseException {
		String app = reader.rawText(type, num);
		if (app.equals("com.instagram.android")) {
			return 1;
		}else if ( app.equals("com.kakao.talk")) {
			return 2;
		}else if ( app.equals("SMS")){
			return 3;
		}
		return 0;
	}
}

//통합 실행 함수
    public void run() throws Exception {
    	getData getdata = new getData();
    	
        for (int i = 1; i <= reader.fileCount(); i++) {
			String userId = getdata.sameName("raw_sender", i);
			System.out.println(i + " userId: " + userId);
			
			String userName = reader.rawText("raw_sender", i);
			System.out.println(i + " userName: " + userName);
			
			String preText = reader.rawText("raw_text", i);
			System.out.println(i + " text: " + preText);
	        
	        String appType = reader.rawText("app_package", i);
	        System.out.println(i + " app: " + appType);
	        
	        int isMapped = getdata.apptype("app_package", i);
	        System.out.println(i + " isMapped: " + isMapped);
        	
            Timestamp msgTime = new Timestamp(Long.parseLong(reader.rawText("msg_time", i)));
            System.out.println(i + " msgTime: " + msgTime);
            System.out.println("================================");
            
//	        2. DTO로 포장
	        UserDataDto uddto = new UserDataDto(userId, userName, isMapped);
	        
//	      	String userText, String appType, int msgTime, Boolean isSummarized
	        UserTextDto utdto = new UserTextDto(preText, appType, userId, msgTime);

	        // 3. DB 저장
	        int newDataId = uddao.insert(uddto);
	        System.out.println(i + " newDataId: " + newDataId);
	        
	        int newTextId = utdao.insert(utdto);
	        System.out.println(i + " newTextId: " + newTextId);
        }
    }
}