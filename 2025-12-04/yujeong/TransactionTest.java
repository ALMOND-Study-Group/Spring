package com.example.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "spring.sql.init.mode=never")
public class TransactionTest {

    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;

    Long aId, bId;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        Account a = accountRepository.save(new Account(1000));
        Account b = accountRepository.save(new Account(1000));
        aId = a.getId();
        bId = b.getId();
    }

    @Test
    void transferTest() {
        accountService.transfer(aId, bId, 200);

        Account a = accountRepository.findById(aId).get();
        Account b = accountRepository.findById(bId).get();

        assertThat(a.getMoney()).isEqualTo(800);
        assertThat(b.getMoney()).isEqualTo(1200);
    }

    // 롤백이 실제로 일어났는지 ‘데이터 상태’를 검증
    @Test
    void exceptionRollbackTest() {
        try {
            accountService.transfer(aId, bId, 999); // 예외 발생!
        } catch (Exception ignored) {}

        Account a = accountRepository.findById(aId).get();
        Account b = accountRepository.findById(bId).get();

        // 롤백되었으므로 둘 다 원래 값 유지
        assertThat(a.getMoney()).isEqualTo(1000);
        assertThat(b.getMoney()).isEqualTo(1000);
    }

    // 예외가 제대로 터지는지 ‘예외 발생 자체’를 검증
    @Test
    void transferErrorRollback() {
        assertThrows(RuntimeException.class, () -> {
            accountService.transfer(aId, bId, 999);
        });
    }
}
