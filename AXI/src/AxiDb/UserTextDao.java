package AxiDb;

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
    // user_test table insert
    public int insert(Connection conn, UserTextDto utdto) {
        String sql = "INSERT INTO user_text (user_text, app_type, user_id, msg_time) "
                    + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(
                     sql, new String[] { "text_id" })) {

            ps.setString(1, utdto.getUserText());
            ps.setString(2, utdto.getAppType());
            ps.setString(3, utdto.getUserId());
            ps.setTimestamp(4, utdto.getMsgTime());

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