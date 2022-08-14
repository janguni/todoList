package com.example.todoList.repository;

import com.example.todoList.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


@Slf4j
class MemberRepositoryTest {

    MemberRepository repository = new MemberRepository();

    @Test
    void crud() throws SQLException {
        Member member = new Member("low9", "pwpw", "unijang", 24);
        repository.save(member);

        Member findMember = repository.findById(member.getMemberId());
        log.info(findMember.getMemberId());
        Assertions.assertThat(findMember).isEqualTo(member);
        // isEqualTo는 내용을 비교함
        // Member의 내용을 어떻게 비교? -> member.equals()로 비교
        // @Data가 equals()를 오버라이딩 해줌

        // 그럼 여기서 의문점: @Data를 쓰지 않을 때는 어떻게함? 보통 @Setter를 포함시키지 않고 싶을 때가 더 많은데..
    }



}