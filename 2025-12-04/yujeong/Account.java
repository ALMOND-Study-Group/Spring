package com.example.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int money;

    public Account(int money) {
        this.money = money;
    }

    // Service 단으로 옮기기
    public void withdraw(int amount) {
        if(money < amount) {
            throw new IllegalArgumentException("잔액 부족!");
        }
        this.money -= amount;
    }

    public void deposit(int amount) {
        this.money += amount;
    }
}
