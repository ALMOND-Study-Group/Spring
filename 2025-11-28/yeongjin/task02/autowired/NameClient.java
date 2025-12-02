package com.example.bootdailymission.springBasic.task02.autowired;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NameClient {

    private NameService nameService;

    @Autowired
    public void setNameService(NameService nameService) {
        this.nameService = nameService;
    }

    public void printName() {
        System.out.println("[@Autowired]" + nameService.getName());
    }
}
