package com.example.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Long fromId, Long toId, int amount) {
        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("보내는 계좌 없음"));

        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("받는 계좌 없음"));

        // 출금
        from.withdraw(amount);

        // 예외 발생 테스트용
        if (amount == 999) {
            throw new RuntimeException("송금 중 오류 발생(테스트)!");
        }

        // 입금
        to.deposit(amount);

        // 변경 내용 저장
        accountRepository.save(from);
        accountRepository.save(to);
    }
}
