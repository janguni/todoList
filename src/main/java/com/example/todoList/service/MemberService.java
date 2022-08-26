package com.example.todoList.service;

import com.example.todoList.model.Member;
import com.example.todoList.repository.MemberRepository;
import com.example.todoList.repository.MemberRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


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

    private void bizLogic(String fromId, String toId, int coin){
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