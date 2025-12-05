package notice_board.taeju.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원엔티티
 * id, loginid, password,nickname,User 생성자
 * */

@Entity
@Data ///나중에 수정해야됨, setter말고 빌더 사용하고 수정할땐 update사용할꺼임
@Table(name = "users")// 이름 충돌 방지
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    *  로그인 아이디-식별코드(유니크한 값으로 설정)
    *  비밀번호
    *  닉네임
    * */
    private String loginId; //길이제한 두기 15
    private String password; //암호화된 비번이 필요, 길이제한 20
    private String nickname; //닉네임, 무조건 설정, 나중에 중복확인도 20

    // 나중에 리포지에서 user가 작성한 게시글과 댓글을 볼수있게 하자.

    //편의상 생성자 사용
    public User(String nickname, String password, String loginId) {
        this.nickname = nickname;
        this.password = password;
        this.loginId = loginId;
    }
}
