package com.example.todoList.service;

import com.example.todoList.connection.ConnectionConst;
import com.example.todoList.model.Member;
import com.example.todoList.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.example.todoList.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";

    public static final String SWINDLER = "Swindler";


    @BeforeEach
    void before(){
        //여기부터
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepository(dataSource);
        memberService = new MemberService(memberRepository,dataSource);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(SWINDLER);
    }

    @Test
    void 정상이체() throws SQLException {
        // given
        Member member1 = new Member(MEMBER_A, "pwpw", "unijang", 24, 500);
        Member member2 = new Member(MEMBER_B, "pwpw", "unijang", 24, 1000);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        memberService.accountTransfer(member1.getMemberId(), member2.getMemberId(), 200);

        // then
        Member fromMember = memberRepository.findById(MEMBER_A);
        Member toMember = memberRepository.findById(MEMBER_B);
        assertThat(fromMember.getCoin()).isEqualTo(300);
        assertThat(toMember.getCoin()).isEqualTo(1200);
    }

    @Test
    void 계좌이체() throws SQLException {
        // given
        Member member1 = new Member(MEMBER_A, "pwpw", "unijang", 24, 500);
        Member member2 = new Member(SWINDLER, "pwpw", "unijang", 24, 1000);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        assertThatThrownBy(() ->
                memberService.accountTransfer(member1.getMemberId(), member2.getMemberId(), 200))
                .isInstanceOf(IllegalStateException.class);

        // then
        Member fromMember = memberRepository.findById(MEMBER_A);
        Member toMember = memberRepository.findById(SWINDLER);
        assertThat(fromMember.getCoin()).isEqualTo(500);
        assertThat(toMember.getCoin()).isEqualTo(1000);
    }
}