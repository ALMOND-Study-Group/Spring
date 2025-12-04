package H2tran.hello;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TestServiceTest {
    @Autowired AccountRepository rp;
    @Autowired TestService sv;


    private void printBalance(String acc1, String acc2) {
        Account a = rp.findByAccountNumber(acc1);
        Account b = rp.findByAccountNumber(acc2);
        System.out.println(acc1 + " : " + a.getMoney() + "원");
        System.out.println(acc2 + " : " + b.getMoney() + "원");
        System.out.println("실제 클래스   : " + sv.getClass().getName());
        System.out.println("내가 Proxy인가? " + AopUtils.isAopProxy(sv));
        System.out.println("CGLIB Proxy인가? " + AopUtils.isCglibProxy(sv));
    }

    @BeforeEach
    void setUp() {
        Account a = new Account("159-595", 90_000L); //9만원
        Account b = new Account("452-555", 10_000L); //만원
        rp.save(a);
        rp.save(b);

        System.out.println("=== 초기 잔액 ===");
        printBalance(a.getAccountNumber(), b.getAccountNumber());
    }

    @Test
    @DisplayName("시나리오1. 정상 송금")
    void transferSuccess() {


        //When(실행)
        sv.transferSuccess("159-595", "452-555", 10_000L);

        //Then(검증)
        Account findA1 = rp.findByAccountNumber("159-595");
        Account findA2 = rp.findByAccountNumber("452-555");
        assertThat(findA1.getMoney()).isEqualTo(80_000L);
        assertThat(findA2.getMoney()).isEqualTo(20_000L);

    }

    @Test
    @DisplayName("시나리오2. 예외 발생")
    void transferFailure() {
        //Given(준비)

        //When(실행)
        assertThatThrownBy(() -> sv.transferWithException("159-595", "452-555", 10_000L))
                .isInstanceOf(RuntimeException.class);

        //Then(검증)
        Account findA = rp.findByAccountNumber("159-595");
        Account findB = rp.findByAccountNumber("452-555");
        
        //롤백되었는지 잔액확인
        System.out.println("====롤백 후 잔액 확인=======");
        assertThat(findA.getMoney()).isEqualTo(90_000L);
        assertThat(findB.getMoney()).isEqualTo(10_000L);
        printBalance("159-595", "452-555");


    }

    @AfterEach
    void tearDown() {
        rp.deleteAll(); //삭제
    }

}