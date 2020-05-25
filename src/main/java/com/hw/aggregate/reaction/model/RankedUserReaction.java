package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.model.ReferenceEnum;
import lombok.Data;

@Data
public class RankedUserReaction {
    private Long count;
    private String referenceId;
    private ReferenceEnum referenceType;

    public RankedUserReaction(Long count, String referenceId, ReferenceEnum referenceType) {
        this.count = count;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
    }
}
