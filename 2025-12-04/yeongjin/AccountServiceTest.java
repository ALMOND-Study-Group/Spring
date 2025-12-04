package com.example.bootdailymission.springAdvanced.task01;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Slf4j
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository repository;

    @Test
    void transfer_success() {
        // given
        accountService.save(new Account(1L, 10_000L));
        accountService.save(new Account(2L, 5_000L));

        log.info("=== [송금 성공 테스트] 시작 ===");
        log.info("초기 잔액 -> 계좌1: {}, 계좌2: {}",
                repository.findById(1L).map(Account::getBalance).orElse(null),
                repository.findById(2L).map(Account::getBalance).orElse(null));

        // when
        accountService.transfer(1L, 2L, 3_000L);

        // then
        Account a = repository.findById(1L).orElseThrow();
        Account b = repository.findById(2L).orElseThrow();

        log.info("처리 후 잔액 -> 계좌1: {}, 계좌2: {}", a.getBalance(), b.getBalance());
        log.info("=== [송금 성공 테스트] 종료 ===\n");

        assertThat(a.getBalance()).isEqualTo(7_000L);
        assertThat(b.getBalance()).isEqualTo(8_000L);
    }

    @Test
    @Transactional
    void transfer_rollback_on_exception() {
        // given
        accountService.save(new Account(1L, 10_000L));
        accountService.save(new Account(2L, 5_000L));

        log.info("=== [송금 롤백 테스트] 시작 ===");
        log.info("초기 잔액 -> 계좌1: {}, 계좌2: {}",
                repository.findById(1L).map(Account::getBalance).orElse(null),
                repository.findById(2L).map(Account::getBalance).orElse(null));

        // when
        assertThatThrownBy(() ->
                accountService.transferWithError(1L, 2L, 3_000L)
        ).isInstanceOf(RuntimeException.class);

        log.info("서비스에서 예외 발생 확인 (롤백 대상)");

        // then: 서비스 메서드 내부 트랜잭션은 예외로 롤백,
        // 현재 테스트 트랜잭션에서는 처음 저장한 상태 그대로 조회된다.
        Account a = repository.findById(1L).orElseThrow();
        Account b = repository.findById(2L).orElseThrow();

        log.info("예상 대로 롤백 후 잔액 -> 계좌1: {}, 계좌2: {}", a.getBalance(), b.getBalance());
        log.info("=== [송금 롤백 테스트] 종료 ===\n");

        assertThat(a.getBalance()).isEqualTo(10_000L);
        assertThat(b.getBalance()).isEqualTo(5_000L);
    }
}