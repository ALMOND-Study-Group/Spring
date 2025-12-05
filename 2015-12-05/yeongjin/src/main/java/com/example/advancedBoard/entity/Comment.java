package com.example.advancedBoard.entity;

import com.example.advancedBoard.entity.status.CommentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CommentStatus status = CommentStatus.PUBLIC;

    // 비즈니스 메서드
    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(CommentStatus status) {
        this.status = status;
    }

    public void delete() {
        this.status = CommentStatus.DELETED;
    }

    public void block() {
        this.status = CommentStatus.BLOCKED;
    }

    public void makePublic() {
        this.status = CommentStatus.PUBLIC;
    }

    public void makePrivate() {
        this.status = CommentStatus.PRIVATE;
    }

    public boolean isPublic() {
        return this.status == CommentStatus.PUBLIC;
    }

    public boolean isDeleted() {
        return this.status == CommentStatus.DELETED;
    }

    public boolean isOwnedBy(User user) {
        return this.user.getId().equals(user.getId());
    }

    // 연관관계 편의 메서드
    public void setUser(User user) {
        this.user = user;
        user.getComments().add(this);
    }

    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
}
