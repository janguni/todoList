package com.example.todoList.service;

import com.example.todoList.model.Member;
import com.example.todoList.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DataSource dataSource;


    // Coin 기부
    public void accountTransfer(String fromId, String toId, int coin) throws SQLException {
        Connection con = dataSource.getConnection();
        try{
            con.setAutoCommit(false); // 트랜잭션 시작
            bizLogic(fromId, toId, coin, con);
            con.commit(); //성공시 커밋
        } catch (Exception e){
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            con.setAutoCommit(true); // 다시 자동커밋으로 돌려놓기
            con.close();
        }

    }

    private void bizLogic(String fromId, String toId, int coin, Connection con) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.updateCoin(con, fromId, fromMember.getCoin() - coin);
        validation(toMember);
        memberRepository.updateCoin(con, toId, toMember.getCoin() + coin);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("Swindler")) { //사기꾼
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}