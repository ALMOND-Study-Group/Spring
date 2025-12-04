package com.example.bootdailymission.springAdvanced.task01;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void save(Account account){
        accountRepository.save(account);
    }
    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }
    public void printAll() {
        accountRepository.findAll().forEach(System.out::println);
    }

    // 정상작동하는 송금
    @Transactional
    public void transfer(Long fromId, Long toId, Long amount) {
        Account from = findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("출금 계좌 없음: " + fromId));
        Account to = findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("입금 계좌 없음: " + toId));

        // 입출금 로직은 서비스에서 처리
        if (from.getBalance() < amount) { // aop 로 하면 좋을듯
            throw new IllegalArgumentException("잔액 부족");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);


    }

    // 에러가 발생하는 송금 - 에러를 발생시켜 롤백되는지 확인
    @Transactional(propagation = Propagation.REQUIRES_NEW)
     public void transferWithError(Long fromId, Long toId, Long amount) {
         Account from = findById(fromId)
                 .orElseThrow(() -> new IllegalArgumentException("출금 계좌 없음: " + fromId));
         Account to = findById(toId)
                 .orElseThrow(() -> new IllegalArgumentException("입금 계좌 없음: " + toId));

         if (from.getBalance() < amount) { // aop 로 하면 좋을듯
             throw new IllegalArgumentException("잔액 부족");
         }

         from.setBalance(from.getBalance() - amount);
         to.setBalance(to.getBalance() + amount);


         // 여기서 런타임 예외 → 트랜잭션 롤백 대상
         throw new RuntimeException("송금 중 예외 발생, 롤백!");
     }


}
