package com.example.spring_basic.task02;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public String findUser(String name) {
        return "사용자 이름: " + name;
    }
}
