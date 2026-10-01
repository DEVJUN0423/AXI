package Noise_Cut_Off;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTextDao {

    private final OracleDB oracleDB;

    public UserTextDao(OracleDB oracleDB) {
        this.oracleDB = oracleDB;
    }

    /**
     * 전처리된 메시지를 user_text 테이블에 저장하고,
     * 자동 생성된 text_id를 반환한다.
     */
    public int insert(UserTextDto dto) {
        String sql = "INSERT INTO user_text (user_text, app_type, user_id) "
                    + "VALUES (?, ?, ?)";

        try (Connection conn = oracleDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, new String[] { "text_id" })) {

            ps.setString(1, dto.getUserText());
            ps.setString(2, dto.getAppType());
            ps.setInt(3, dto.getUserId());

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