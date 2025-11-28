package com.example.bootdailymission.springBasic.task02.autowired;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class NameService {
    private String name;

    public NameService() {
        this.name = "name";
    }

    public NameService(String name) {
        this.name = name;
    }
}
