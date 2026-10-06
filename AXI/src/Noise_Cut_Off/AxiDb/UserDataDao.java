package Noise_Cut_Off.AxiDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataDao {
    private final OracleDB oracleDB;

    public UserDataDao(OracleDB oracleDB) {
        this.oracleDB = oracleDB;
    }
    
	// user_data table insert
	public int insert(UserDataDto uddto) {
	    String sql = "INSERT INTO user_data (user_name, is_mapped) "
	                + "VALUES (?, ?)";

	    try (Connection conn = oracleDB.getConnection();
	         PreparedStatement ps = conn.prepareStatement(
	                 sql, new String[] { "user_id" })) {

	        ps.setString(1, uddto.getUserName());
	        ps.setInt(2, uddto.getIsMapped());

	        ps.executeUpdate();

	        // 방금 생성된 text_id(자동증가 PK) 확인
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // 실제 서비스에선 로거(Logger)로 교체 추천
	    }
	    return -1; // 실패 시
	}
}
