package com.example.todoList.service;

import com.example.todoList.model.Member;
import com.example.todoList.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // Coin 기부
    @Transactional
    public void accountTransfer(String fromId, String toId, int coin) throws SQLException {
        bizLogic(fromId, toId, coin);
    }

    private void bizLogic(String fromId, String toId, int coin) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.updateCoin(fromId, fromMember.getCoin() - coin);
        validation(toMember);
        memberRepository.updateCoin(toId, toMember.getCoin() + coin);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("Swindler")) { //사기꾼
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}