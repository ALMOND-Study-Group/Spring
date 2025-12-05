package com.example.advancedBoard.entity;

import com.example.advancedBoard.entity.status.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PostStatus status = PostStatus.PUBLIC;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // 비즈니스 메서드
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void changeBoard(Board board) {
        this.board = board;
    }

    public void updateStatus(PostStatus status) {
        this.status = status;
    }

    public void delete() {
        this.status = PostStatus.DELETED;
    }

    public void block() {
        this.status = PostStatus.BLOCKED;
    }

    public void makePublic() {
        this.status = PostStatus.PUBLIC;
    }

    public void makePrivate() {
        this.status = PostStatus.PRIVATE;
    }

    public boolean isPublic() {
        return this.status == PostStatus.PUBLIC;
    }

    public boolean isDeleted() {
        return this.status == PostStatus.DELETED;
    }

    public boolean isOwnedBy(User user) {
        return this.user.getId().equals(user.getId());
    }

    // 연관관계 편의 메서드
    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }

    public void setBoard(Board board) {
        this.board = board;
        board.getPosts().add(this);
    }
}
