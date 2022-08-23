package com.example.todoList.repository;

import com.example.todoList.connection.ConnectionConst;
import com.example.todoList.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;



@Slf4j
@RequiredArgsConstructor
public class MemberRepository {

    private final DataSource dataSource;

    public Member save(Member member) throws SQLException {
        String sql = "insert into Member(member_id, member_pw, name, age, coin) values(?,?,?,?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getMemberPw());
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());
            pstmt.setInt(5, member.getCoin());
            pstmt.executeUpdate();
            return member;

        } catch (SQLException e) {
            log.error("db error");
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()){
                String findMemberId = rs.getString("member_id");
                String findMemberPw = rs.getString("member_pw");
                String findMemberName = rs.getString("name");
                int findMemberAge = rs.getInt("age");
                int findMemberCoin = rs.getInt("coin");

                Member member= new Member(findMemberId,findMemberPw,findMemberName,findMemberAge,findMemberCoin);

                return member;
            }
            else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public Member findById(Connection con,String memberId) throws SQLException {
        String sql = "select * from member where member_id=?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()){
                String findMemberId = rs.getString("member_id");
                String findMemberPw = rs.getString("member_pw");
                String findMemberName = rs.getString("name");
                int findMemberAge = rs.getInt("age");
                int findMemberCoin = rs.getInt("coin");

                Member member= new Member(findMemberId,findMemberPw,findMemberName,findMemberAge,findMemberCoin);

                return member;
            }
            else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //close(con, pstmt, rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void updatePw(String memberId, String memberPw) throws SQLException {
        String sql = "update Member set member_pw=? where member_id =?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberPw);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void updatePw(Connection con,String memberId, String memberPw) throws SQLException {
        String sql = "update Member set member_pw=? where member_id =?";

        PreparedStatement pstmt = null;

        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberPw);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //close(con, pstmt, null);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void updateCoin(String memberId, int coin) throws SQLException {
        String sql = "update Member set coin=? where member_id =?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, coin);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);

        }
    }

    public void updateCoin(Connection con, String memberId, int coin) throws SQLException {
        String sql = "update Member set coin=? where member_id =?";

        PreparedStatement pstmt = null;

        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, coin);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //close(con, pstmt, null);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from Member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);

        }

    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }


    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs != null){
            try{
                rs.close();
            } catch (SQLException e){
                log.info("error", e);
            }
        }

        if (stmt != null){
            try{
                stmt.close();
            } catch (SQLException e){
                log.info("error", e);
            }
        }

        if (con != null){
            try{
                con.close();
            } catch (SQLException e){
                log.info("error", e);
            }
        }
    }
}
