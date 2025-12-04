package H2tran.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final AccountRepository ar;

    /**
     * 시나리오1. 정상 송금
     */

    @Transactional
    public void transferSuccess(String from, String to, Long money) {

        Account A = ar.findByAccountNumber(from); //누구가
        Account B = ar.findByAccountNumber(to); //누구에게

        //A 3천원 -> B 3천원
        //그럼 A 3천원 다운, B 3천원 up
        A.down(money);
        B.up(money);

        System.out.println("정상 송금 완료: " + from + " → " + to + " (" + money + "원)");

    }

    /**
     * 시나리오2. 예외 발생 → 롤백 확인용
     */
    @Transactional
    public void transferWithException(String from, String to, Long money) {

        Account A = ar.findByAccountNumber(from);
        Account B   = ar.findByAccountNumber(to);

        A.down(money);
        B.up(money);

        // 의도적으로 RuntimeException 발생 → 트랜잭션 롤백 유도
        throw new RuntimeException("송금 중 예외 발생! (의도적)");

    }
}