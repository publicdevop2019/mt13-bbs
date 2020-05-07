package com.hw.aggregate.comment.model;

import com.hw.shared.BadRequestException;

public enum CommentSortOrderEnum {
    ASC("asc"),
    DESC("desc");
    private String sortOrder;

    CommentSortOrderEnum(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortOrder() {
        return this.sortOrder;
    }

    public static CommentSortOrderEnum fromString(String text) {
        for (CommentSortOrderEnum b : CommentSortOrderEnum.values()) {
            if (b.sortOrder.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new BadRequestException("No constant with text " + text + " found");
    }
}
