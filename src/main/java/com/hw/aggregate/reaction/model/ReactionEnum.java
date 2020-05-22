package com.hw.aggregate.reaction.model;

import javax.persistence.AttributeConverter;

public enum ReactionEnum {
    LIKE,
    DISLIKE,
    NOT_INTERESTED,
    REPORT;

    public static class DBConverter implements AttributeConverter<ReactionEnum, String> {
        @Override
        public String convertToDatabaseColumn(ReactionEnum reactionEnum) {
            return reactionEnum.name();
        }

        @Override
        public ReactionEnum convertToEntityAttribute(String s) {
            return ReactionEnum.valueOf(s);
        }
    }
}
