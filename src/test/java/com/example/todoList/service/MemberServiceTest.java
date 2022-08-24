package com.example.todoList.service;

import com.example.todoList.connection.ConnectionConst;
import com.example.todoList.model.Member;
import com.example.todoList.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.example.todoList.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String SWINDLER = "Swindler";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @TestConfiguration
    static class TestConfig{
        private final DataSource dataSource;

        public TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepository memberRepository(){
            return new MemberRepository(dataSource);
        }
        @Bean
        MemberService memberService(){
            return new MemberService(memberRepository());
        }
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(SWINDLER);
    }

    @Test
    void AopCheck(){
        log.info("memberService class={}", memberService.getClass());
        log.info("memberRepository class={}", memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
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