package com.example.todoList.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class Member {

    private String memberId;
    private String memberPw;
    private String name;
    private Date dateJoined;

    public void changePw(String newPw){
        this.memberPw = newPw;
    }
}
