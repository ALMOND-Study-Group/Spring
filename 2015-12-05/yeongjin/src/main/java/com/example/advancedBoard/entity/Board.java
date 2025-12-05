package com.example.advancedBoard.entity;

import com.example.advancedBoard.entity.status.BoardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BoardStatus status = BoardStatus.PUBLIC;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    // 비즈니스 메서드
    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateStatus(BoardStatus status) {
        this.status = status;
    }

    public void delete() {
        this.status = BoardStatus.DELETED;
    }

    public void block() {
        this.status = BoardStatus.BLOCKED;
    }

    public void makePublic() {
        this.status = BoardStatus.PUBLIC;
    }

    public void makePrivate() {
        this.status = BoardStatus.PRIVATE;
    }

    public boolean isPublic() {
        return this.status == BoardStatus.PUBLIC;
    }

    public boolean isDeleted() {
        return this.status == BoardStatus.DELETED;
    }
}
