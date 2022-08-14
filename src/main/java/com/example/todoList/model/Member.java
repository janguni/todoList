package com.example.todoList.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
public class Member {

    private String memberId;
    private String memberPw;
    private String name;
    private int age;
    //private Date dateJoined;


    public Member() {
    }

    public Member(String memberId, String memberPw, String name, int age) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.name = name;
        this.age = age;
    }

    public void changePw(String newPw){
        this.memberPw = newPw;
    }
}
