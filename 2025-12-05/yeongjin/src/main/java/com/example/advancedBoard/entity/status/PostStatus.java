package com.example.advancedBoard.entity.status;

public enum PostStatus {
    PUBLIC("공개"),
    PRIVATE("비공개"),
    DELETED("삭제됨"),
    BLOCKED("차단됨");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
