package notice_board.taeju.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * 댓글엔티티, id, content
 * 댓글을 작성할 수 도있고, 아닐 수 도있고
 * */

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content; //글자수 길이도 제한을 해야되나 싶음.

    //연관관계는 게시글에 n:1 - 필수로 있어야됨, 단방향으로
    private Post post;

    // 작성자에 N:1 - 익명허용, 단방향으로
    private User user;

    //익명 작성자
    private String anonymousName;
    private String anonymousPassword; //댓글지울때 비번이 필요함

}
