package com.hw.aggregate.post.model;

import com.hw.shared.BadRequestException;

public enum PostSortOrderEnum {
    ASC("asc"),
    DESC("desc");
    private String sortOrder;

    PostSortOrderEnum(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortOrder() {
        return this.sortOrder;
    }

    public static PostSortOrderEnum fromString(String text) {
        for (PostSortOrderEnum b : PostSortOrderEnum.values()) {
            if (b.sortOrder.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new BadRequestException("No constant with text " + text + " found");
    }
}
