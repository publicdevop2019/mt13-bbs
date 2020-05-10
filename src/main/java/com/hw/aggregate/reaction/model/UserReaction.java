package com.hw.aggregate.reaction.model;

import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"referenceId", "createdBy", "referenceType", "reactionType"}))
@SequenceGenerator(name = "likeId_gen", sequenceName = "likeId_gen", initialValue = 100)
@Data
@NoArgsConstructor
public class UserReaction extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "likeId_gen")
    private Long id;
    @Column
    private String referenceId;
    @Column
    private ReferenceEnum referenceType;
    @Column
    private ReactionEnum reactionType;

    public static UserReaction create(String referenceId, ReferenceEnum referenceType, ReactionEnum reactionEnum) {
        return new UserReaction(referenceId, referenceType, reactionEnum);
    }

    private UserReaction(String referenceId, ReferenceEnum referenceType, ReactionEnum reactionEnum) {
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.reactionType = reactionEnum;
    }
}
