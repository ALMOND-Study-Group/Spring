package com.example.bootdailymission.springBasic.task02.xml;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameClient {
    private NameService nameService;

    public void printName() {
        System.out.println("[XML]" + nameService.getName());
    }
}
