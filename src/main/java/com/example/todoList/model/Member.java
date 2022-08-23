package com.example.todoList.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;

//@Data
@Getter
public class Member {

    private String memberId;
    private String memberPw;
    private String name;
    private int age;

    private int coin;
    //private Date dateJoined;


    public Member() {
    }

    public Member(String memberId, String memberPw, String name, int age, int coin) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.name = name;
        this.age = age;
        this.coin = coin;
    }

    public void changePw(String newPw){
        this.memberPw = newPw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return age == member.age && Objects.equals(memberId, member.memberId) && Objects.equals(memberPw, member.memberPw) && Objects.equals(name, member.name);
    }
}
