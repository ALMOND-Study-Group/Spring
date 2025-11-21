package com.Schedule.Schedule.service;

import java.util.List;

public class ColorService {
    private final List<String> COLORS = List.of(
            "#FF6B3B3", "#FFB3FF", "#B3B3FF", "#B3FFFF",
            "#B3FFB3", "#FFFFB3", "#FFD9B3", "#E6B3FF",
            "#B3E6FF", "#D9FFB3"
    );

    public String getColor(String title) {
        if (title == null || title.isEmpty()) return COLORS.get(0);
        int index = Math.abs(title.hashCode()) % COLORS.size();
        return COLORS.get(index);
    }
}
