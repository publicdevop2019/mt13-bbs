package com.hw.aggregate.reaction.model;

import javax.persistence.AttributeConverter;

public enum ReferenceEnum {
    POST,
    COMMENT;

    public static class DBConverter implements AttributeConverter<ReferenceEnum, String> {
        @Override
        public String convertToDatabaseColumn(ReferenceEnum reactionEnum) {
            return reactionEnum.name();
        }

        @Override
        public ReferenceEnum convertToEntityAttribute(String s) {
            return ReferenceEnum.valueOf(s);
        }
    }
}
