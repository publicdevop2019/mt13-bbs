package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.ReactionRepository;
import com.hw.aggregate.reaction.exception.FieldValidationException;
import com.hw.aggregate.reaction.exception.ReactionNotFoundException;
import com.hw.aggregate.reaction.exception.ReferenceNotFoundException;
import com.hw.aggregate.reaction.exception.ReferenceServiceNotFoundException;
import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Map;
import java.util.Optional;

@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"referenceId", "createdBy", "referenceType"}))
@SequenceGenerator(name = "likeId_gen", sequenceName = "likeId_gen", initialValue = 100)
@Data
@NoArgsConstructor
@Slf4j
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
                                      String userId
    ) {
        if (refId == null || refEnum == null || reactionEnum == null || refServices == null || reactionRepository == null || userId == null)
            throw new FieldValidationException();
        ReferenceService refService = refServices.get(refEnum);
        if (refService == null)
            throw new ReferenceServiceNotFoundException();
        if (!refService.existById(refId))
            throw new ReferenceNotFoundException();
        if (reactionEnum.equals(ReactionEnum.LIKE)) {
            Optional<UserReaction> reaction = reactionRepository.findReaction(userId, refId, refEnum, ReactionEnum.DISLIKE);
            if (reaction.isPresent()) {
                log.info("update like to dislike if exist");
                reaction.get().setReactionType(ReactionEnum.DISLIKE);
                return reactionRepository.save(reaction.get());
            } else {
                return reactionRepository.save(new UserReaction(refId, refEnum, reactionEnum));
            }
        } else if (reactionEnum.equals(ReactionEnum.DISLIKE)) {
            Optional<UserReaction> reaction = reactionRepository.findReaction(userId, refId, refEnum, ReactionEnum.LIKE);
            if (reaction.isPresent()) {
                log.info("update dislike to like if exist");
                reaction.get().setReactionType(ReactionEnum.LIKE);
                return reactionRepository.save(reaction.get());
            } else {
                return reactionRepository.save(new UserReaction(refId, refEnum, reactionEnum));
            }
        } else {
            return null;
        }

    }

    private UserReaction(String referenceId, ReferenceEnum referenceType, ReactionEnum reactionEnum) {
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.reactionType = reactionEnum;
    }

    public static void delete(String userId, String refId, ReferenceEnum referenceEnum, ReactionEnum reactionEnum, ReactionRepository reactionRepository) {
        Optional<UserReaction> reaction = reactionRepository.findReaction(userId, refId, referenceEnum, reactionEnum);
        if (reaction.isEmpty()) {
            log.info("target not found");
            throw new ReactionNotFoundException();
        }
        reactionRepository.delete(reaction.get());
    }
}
