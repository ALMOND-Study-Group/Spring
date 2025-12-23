package com.example.advancedBoard.entity.status;

public enum CommentStatus {
    PUBLIC("공개"),
    PRIVATE("비공개"),
    DELETED("삭제됨"),
    BLOCKED("차단됨");

    private final String description;

    CommentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
