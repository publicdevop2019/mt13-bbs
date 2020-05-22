package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.ReactionRepository;
import com.hw.aggregate.reaction.exception.*;
import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"referenceType", "referenceId", "createdBy", "reactionType"}))
@SequenceGenerator(name = "likeId_gen", sequenceName = "likeId_gen", initialValue = 100)
@Data
@NoArgsConstructor
@Slf4j
public class UserReaction extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "likeId_gen")
    private Long id;

    @Column(length = 100)
    private String referenceId;

    @Column(length = 10)
    @Convert(converter = ReferenceEnum.DBConverter.class)
    private ReferenceEnum referenceType;

    @Column(length = 10)
    @Convert(converter = ReactionEnum.DBConverter.class)
    private ReactionEnum reactionType;


    public static UserReaction createOrUpdate(String refId,
                                              ReferenceEnum refEnum,
                                              ReactionEnum reactionEnum,
                                              Map<ReferenceEnum, ReferenceService> refServices,
                                              ReactionRepository reactionRepository,
                                              String userId,
                                              EntityManager entityManager
    ) {
        if (refId == null || refEnum == null || reactionEnum == null || refServices == null || reactionRepository == null || userId == null)
            throw new FieldValidationException();
        ReferenceService refService = refServices.get(refEnum);
        if (refService == null)
            throw new ReferenceServiceNotFoundException();
        if (!refService.existById(refId))
            throw new ReferenceNotFoundException();
        if (reactionEnum.equals(ReactionEnum.LIKE)) {
            log.info("update dislike to like if exist");
            Optional<UserReaction> stored = reactionRepository.findReaction(userId, refId, refEnum, ReactionEnum.DISLIKE);
            if (stored.isPresent()) {
                stored.get().setReactionType(ReactionEnum.LIKE);
                return reactionRepository.save(stored.get());
            } else {
                return insertAIfBNotExistConcurrent(ReactionEnum.LIKE.name(), ReactionEnum.DISLIKE.name(), entityManager, new UserReaction(userId, new Random().nextLong(), refId, refEnum, reactionEnum));
            }
        } else if (reactionEnum.equals(ReactionEnum.DISLIKE)) {
            log.info("update like to dislike if exist");
            Optional<UserReaction> reaction = reactionRepository.findReaction(userId, refId, refEnum, ReactionEnum.LIKE);
            if (reaction.isPresent()) {
                reaction.get().setReactionType(ReactionEnum.DISLIKE);
                return reactionRepository.save(reaction.get());
            } else {
                return insertAIfBNotExistConcurrent(ReactionEnum.DISLIKE.name(), ReactionEnum.LIKE.name(), entityManager, new UserReaction(userId, new Random().nextLong(), refId, refEnum, reactionEnum));
            }
        } else {
            return null;
        }

    }

    private static UserReaction insertAIfBNotExistConcurrent(String a, String b, EntityManager entityManager, UserReaction reaction) {
        int i = entityManager.createNativeQuery(
                "INSERT INTO user_reaction (id, reference_id, reference_type,reaction_type,created_at,created_by,modified_at,modified_by) " +
                        "SELECT ?1, ?2, ?3 , ?4, ?5 , ?6, ?7, ?8 " +
                        "FROM user_reaction ur " +
                        "WHERE (ur.reference_id = ?2 AND ur.reference_type = ?3 AND ur.reaction_type = ?9 ) HAVING COUNT(*) = 0")
                .setParameter(1, reaction.getId())
                .setParameter(2, reaction.getReferenceId())
                .setParameter(3, reaction.getReferenceType().name())
                .setParameter(4, a)
                .setParameter(5, new Timestamp(reaction.getCreatedAt().getTime()))
                .setParameter(6, reaction.getCreatedBy())
                .setParameter(7, new Timestamp(reaction.getModifiedAt().getTime()))
                .setParameter(8, reaction.getModifiedBy())
                .setParameter(9, b)
                .executeUpdate();
//        int i = entityManager.createNativeQuery("INSERT INTO user_reaction (id, reference_id, reference_type,reaction_type,created_at,created_by,modified_at,modified_by) " +
//                "SELECT " + reaction.getId() + ", " + reaction.getReferenceId() + ", '" + reaction.getReferenceType().name() + "', '" + a + "'," +
//                timestamp.toString() + ",'" + reaction.getCreatedBy() + "'," + timestamp2.toString() + ",'" + reaction.getModifiedBy() + "' FROM user_reaction ur " +
//                "WHERE (ur.reference_id = " + reaction.getReferenceId() + " AND ur.reference_type = '" + reaction.getReferenceType().name() + "' AND ur.reaction_type = '" + b + "') HAVING COUNT(*) = 0")
//                .executeUpdate();
        if (i == 1)
            return reaction;
        else
            throw new InsertViolationException();

    }

    private UserReaction(String userId, Long id, String referenceId, ReferenceEnum referenceType, ReactionEnum reactionEnum) {
        this.id = id;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.reactionType = reactionEnum;
        this.setCreatedAt(new Date());
        this.setModifiedAt(new Date());
        this.setCreatedBy(userId);
        this.setModifiedBy(userId);

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
