package com.hw.aggregate.post.model;

import com.hw.shared.BadRequestException;

public enum PostSortCriteriaEnum {
    ID("id");
    private String sortCriteria;

    PostSortCriteriaEnum(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public String getSortCriteria() {
        return this.sortCriteria;
    }

    public static PostSortCriteriaEnum fromString(String text) {
        for (PostSortCriteriaEnum b : PostSortCriteriaEnum.values()) {
            if (b.sortCriteria.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new BadRequestException("No constant with text " + text + " found");
    }
}
