package com.hw.aggregate.comment.model;

import com.hw.shared.BadRequestException;

public enum CommentSortCriteriaEnum {
    id("id"),
    createdAt("date");
    private String sortCriteria;

    CommentSortCriteriaEnum(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public String getSortCriteria() {
        return this.sortCriteria;
    }

    public static CommentSortCriteriaEnum fromString(String text) {
        for (CommentSortCriteriaEnum b : CommentSortCriteriaEnum.values()) {
            if (b.sortCriteria.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new BadRequestException("No constant with text " + text + " found");
    }
}
