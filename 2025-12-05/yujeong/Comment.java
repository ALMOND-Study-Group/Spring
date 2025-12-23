package com.yujeong.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(columnDefinition = "TEXT") // columnDefinition : DB에 생성될 컬럼의 SQL 타입을 직접 지정
    private String content; // 내용 : "TEXT" 긴 본문 저장

    private String postId;
}
