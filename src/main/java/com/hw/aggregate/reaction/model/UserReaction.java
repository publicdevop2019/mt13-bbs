package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.ReactionRepository;
import com.hw.aggregate.reaction.exception.FieldValidationException;
import com.hw.aggregate.reaction.exception.ReferenceNotFoundException;
import com.hw.aggregate.reaction.exception.ReferenceServiceNotFoundException;
import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

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

    public static UserReaction create(String refId,
                                      ReferenceEnum refEnum,
                                      ReactionEnum reactionEnum,
                                      Map<ReferenceEnum, ReferenceService> refServices,
                                      ReactionRepository reactionRepository,
                                      String userId) {
        if (refId == null || refEnum == null || reactionEnum == null || refServices == null || reactionRepository == null || userId == null)
            throw new FieldValidationException();
        ReferenceService refService = refServices.get(refEnum);
        if (refService == null)
            throw new ReferenceServiceNotFoundException();
        if (!refService.existById(refId))
            throw new ReferenceNotFoundException();
        if (reactionEnum.equals(ReactionEnum.LIKE))
            reactionRepository.deleteReaction(userId, refId, refEnum, ReactionEnum.DISLIKE);
        if (reactionEnum.equals(ReactionEnum.DISLIKE))
            reactionRepository.deleteReaction(userId, refId, refEnum, ReactionEnum.LIKE);
        return new UserReaction(refId, refEnum, reactionEnum);
    }

    private UserReaction(String referenceId, ReferenceEnum referenceType, ReactionEnum reactionEnum) {
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.reactionType = reactionEnum;
    }
}
