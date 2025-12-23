package H2tran.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class HelloApplication implements CommandLineRunner {

    private final AccountRepository rp;
    private final TestService sv;

    static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    private void printBalance(String acc1, String acc2) {
        Account a = rp.findByAccountNumber(acc1);
        Account b = rp.findByAccountNumber(acc2);
        System.out.println(acc1 + " : " + a.getMoney() + "원");
        System.out.println(acc2 + " : " + b.getMoney() + "원");
        System.out.println("실제 클래스   : " + sv.getClass().getName());
        System.out.println("내가 Proxy인가? " + AopUtils.isAopProxy(sv));
        System.out.println("CGLIB Proxy인가? " + AopUtils.isCglibProxy(sv));
    }


    @Override
    public void run(String... args) throws Exception {

        // 초기 데이터 생성
        Account a = new Account("159-595", 90_000L); //9만원
        Account b = new Account("452-555", 10_000L); //만원
        rp.save(a);
        rp.save(b);

        System.out.println("=== 초기 잔액 ===");
        printBalance(a.getAccountNumber(), b.getAccountNumber());


        // 1. 정상 송금
        try {
            sv.transferSuccess(a.getAccountNumber(), b.getAccountNumber(), 5_000L);
        } catch (Exception e) {
            System.out.println("예상치 못한 오류: " + e.getMessage());
        }

        System.out.println("\n=== 정상 송금 후 잔액 ===");
        //printBalance(a.getAccountNumber(), b.getAccountNumber());
        printBalance("159-595", "452-555");

        // 2. 예외 발생 송금 (롤백 확인)
        try {
            sv.transferWithException(a.getAccountNumber(), b.getAccountNumber(), 9_000L);
        } catch (Exception e) {
            System.out.println("예외 발생 (예상대로): " + e.getMessage());
        }

        System.out.println("\n=== 예외 발생 후 잔액 (롤백 확인) ===");
        printBalance("159-595", "452-555");
    }


}