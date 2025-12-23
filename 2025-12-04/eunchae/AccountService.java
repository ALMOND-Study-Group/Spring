package com.example.spring_advanced.task01;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(String fromAccountNum, String toAccountNum, int amount, boolean induceException) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNum)
                .orElseThrow(() -> new NoSuchElementException("계좌 없음"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNum)
                .orElseThrow(() -> new NoSuchElementException("계좌 없음"));

        fromAccount.withdraw(amount);

        if (induceException) {
            System.out.println("RuntimeException 발생");
            throw new RuntimeException("송금 중 오류 발생!");
        }

        toAccount.deposit(amount);

    }
}
