package com.example.todoList.repository;

import com.example.todoList.connection.ConnectionConst;
import com.example.todoList.connection.DBConnectionUtil;
import com.example.todoList.model.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;


@Slf4j
public class MemberRepository {
    public Member save(Member member) throws SQLException {
        String sql = "insert into Member(member_id, member_pw, name, age) values(?,?,?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getMemberPw());
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());
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
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()){
//                Member member = new Member();
//                String findMemberId = rs.getString("member_id");
//                String findMemberPw = rs.getString("member_pw");
//                String findMemberName = rs.getString("name");
//                int findMemberAge = rs.getInt("age");

                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMemberPw(rs.getString("member_pw"));
                member.setName(rs.getString("name"));
                member.setAge(rs.getInt("age"));
                return member;
                //Member findMember = new Member(findMemberId, findMemberPw, findMemberName, findMemberAge);
                //return findMember;
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

    public void update(String memberId, String memberPw) throws SQLException {
        String sql = "update Member set member_pw=? where member_id =?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberPw);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);


        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);

        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from Member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = DBConnectionUtil.getConnection();
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