package com.example.todoList.repository;


import com.example.todoList.exception.MyDbException;
import com.example.todoList.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;


import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;



@Slf4j
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate template;

    public MemberRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Member save(Member member) {
        String sql = "insert into Member(member_id, member_pw, name, age, coin) values(?,?,?,?,?)";
        template.update(sql, member.getMemberId(), member.getMemberPw(), member.getName(), member.getAge(), member.getCoin());
        return member;
    }

    public Member findById(String memberId){
        String sql = "select * from member where member_id=?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return ((rs, rowNum) -> {
            String memberId = rs.getString("member_id");
            String memberPw = rs.getString("member_pw");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            int coin = rs.getInt("coin");
            Member member = new Member(memberId, memberPw, name, age, coin);
            return member;
        });
    }

    public void updatePw(String memberId, String memberPw) {
        String sql = "update Member set member_pw=? where member_id =?";
        template.update(sql, memberPw, memberId);
    }


    public void updateCoin(String memberId, int coin){
        String sql = "update Member set coin=? where member_id =?";
        template.update(sql, coin, memberId);
    }


    public void delete(String memberId) {
        String sql = "delete from Member where member_id=?";
        template.update(sql, memberId);
    }

}
