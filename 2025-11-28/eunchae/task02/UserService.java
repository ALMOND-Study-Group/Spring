package com.example.spring_basic.task02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public void setRepo(UserRepository repo) {
        this.repo = repo;
    }

    public String getUserName(String name) {
        return repo.findUser(name);
    }

}
