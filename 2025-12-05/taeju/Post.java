package notice_board.taeju.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


/**
 * 게시글엔티티, @id, title, content, viewCount
 * */

@Entity
@Data ///나중에 수정해야됨, setter말고 빌더 사용하고 수정할땐 update사용할꺼임
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; //길이제한 40
    
    @Lob// 긴 텍스트 저장 (Text, CLOB 타입), jpa에서 사용되는 에노테이션
    //Large Object 데이터는 긴 문자열, 이진 데이터 또는 바이너리 대량 데이터를 의미
    private String content;
    
    //조회수
    private int viewCount =0;

    //익명인 작성자일때
    private String anonymousName;
    private String anonymousPassword;

    //로그인한 작성자일떄, 연관관계 설정하기
    // N은 유저, 1은 poset, 익명도 허용해줘야됨
    //user_id
    private User user;

    //게시글에서 댓글을 알수있어야됨
    //게시글 삭제할떄 댓글도 같이 삭제되어야됨
    private List<Comment> comments = new ArrayList<>();
    
}
