package com.example.di.autowired;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public String getMessage() {
        return "[Autowired] Hello from MessageService!";
    }
}
