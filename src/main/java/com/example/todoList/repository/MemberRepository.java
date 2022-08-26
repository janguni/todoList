package com.example.todoList.repository;

import com.example.todoList.model.Member;

public interface MemberRepository {
    Member save(Member member);

    Member findById(String memberId);
    void updatePw(String memberId, String memberPw);

    void updateCoin(String memberId, int coin);
    void delete(String memberId);

}
