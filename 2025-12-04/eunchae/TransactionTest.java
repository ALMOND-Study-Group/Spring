package com.example.spring_advanced.task01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransactionTest {
    private static final Logger log = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private static final String ACCOUNT_A_NUM = "111-111-111";
    private static final String ACCOUNT_B_NUM = "222-222-222";
    private static final int INITIAL_BALANCE_A = 10000;
    private static final int INITIAL_BALANCE_B = 5000;
    private static final int TRANSFER_AMOUNT = 2000;

    private int getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NoSuchElementException("계좌 없음"))
                .getBalance();
    }

    @BeforeEach
    public void setup() {
        accountRepository.deleteAll();

        Account A = new Account(null, ACCOUNT_A_NUM, INITIAL_BALANCE_A);
        Account B = new Account(null, ACCOUNT_B_NUM, INITIAL_BALANCE_B);
        accountRepository.save(A);
        accountRepository.save(B);
    }

    // 정상 송금 테스트 (Commit 확인)
    @Test
    @DisplayName("Commit Test")
    public void transferCommit() {

        accountService.transfer(ACCOUNT_A_NUM, ACCOUNT_B_NUM, TRANSFER_AMOUNT, false);

        assertThat(getBalance(ACCOUNT_A_NUM)).as("계좌 A 잔액").isEqualTo(INITIAL_BALANCE_A - TRANSFER_AMOUNT);
        assertThat(getBalance(ACCOUNT_B_NUM)).as("계좌 B 잔액").isEqualTo(INITIAL_BALANCE_B + TRANSFER_AMOUNT);

        log.info("[COMMIT 성공] A: {}, B: {}", getBalance(ACCOUNT_A_NUM), getBalance(ACCOUNT_B_NUM));
    }

    // 예외 발생 테스트 (Rollback 확인)
    @Test
    @DisplayName("Rollback Test")
    public void transferRollback() {

        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            accountService.transfer(ACCOUNT_A_NUM, ACCOUNT_B_NUM, TRANSFER_AMOUNT, true);
        }, "RuntimeException이 발생해야합니다.");
        log.info("에러 메세지: {}", e.getMessage());

        assertThat(getBalance(ACCOUNT_A_NUM)).as("롤백 후 A 잔액").isEqualTo(INITIAL_BALANCE_A);
        assertThat(getBalance(ACCOUNT_B_NUM)).as("롤백 후 B 잔액").isEqualTo(INITIAL_BALANCE_B);

        log.info("[ROLLBACK 성공] A: {}, B: {}", getBalance(ACCOUNT_A_NUM), getBalance(ACCOUNT_B_NUM));
    }
}