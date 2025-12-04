package H2tran.hello;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private Long money;   // 잔액 (Long으로 단순화)

    public Account(String id, Long a) {
        this.accountNumber = id;
        this.money = a;
    }

    // 잔액 증가
    public void up(Long a) {
        this.money += a;
    }

    // 잔액 감소
    public void down(Long a) {
        this.money -= a;
    }
}
