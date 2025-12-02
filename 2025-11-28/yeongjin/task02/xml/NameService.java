package com.example.bootdailymission.springBasic.task02.xml;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameService {
    private String name;

    public NameService() {
        this.name = "name";
    }

    public NameService(String name) {
        this.name = name;
    }
}
