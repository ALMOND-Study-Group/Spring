package com.example.advancedBoard.entity.status;

public enum UserStatus {
    ACTIVE("활성"),
    DELETED("삭제됨"),
    BLOCKED("차단됨");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
