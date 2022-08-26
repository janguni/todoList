package com.example.todoList.repository;

import com.example.todoList.model.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.example.todoList.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;


@Slf4j
class MemberRepositoryTest {

    MemberRepositoryImpl repository;
    Member member = new Member("low9", "pwpw", "unijang", 24, 500);

    @BeforeEach
    void beforeEach(){
        // 커넥션 풀링: HikariProxyConnection -> JdbcConnection
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository = new MemberRepositoryImpl(dataSource);
    }

    @Test
    void crud() throws SQLException {
        log.info("start!!");

        // save
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        assertThat(findMember).isEqualTo(member);

        // update
        repository.updatePw(member.getMemberId(), "wpwp");
        Member findMember2 = repository.findById(member.getMemberId());
        assertThat(findMember2.getMemberPw()).isEqualTo("wpwp");

        // update coin
        repository.updateCoin(member.getMemberId(), 200);
        Member findMember3 = repository.findById(member.getMemberId());
        assertThat(findMember3.getCoin()).isEqualTo(200);

        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }




}