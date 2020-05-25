package com.hw.aggregate.reaction.representation;

import com.hw.aggregate.reaction.model.RankedUserReaction;
import lombok.Data;

import java.util.List;

@Data
public class RankedUserReactionRepresentation {
    private List<RankedUserReaction> results;

    public RankedUserReactionRepresentation(List<RankedUserReaction> results) {
        this.results = results;
    }
}
