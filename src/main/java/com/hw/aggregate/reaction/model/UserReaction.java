package com.hw.aggregate.reaction.model;

import com.hw.aggregate.reaction.ReactionRepository;
import com.hw.aggregate.reaction.exception.*;
import com.hw.aggregate.reaction.representation.RankedUserReactionRepresentation;
import com.hw.shared.Auditable;
import com.hw.shared.IdGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"referenceType", "referenceId", "createdBy", "reactionType"}))
@Data
@NoArgsConstructor
@Slf4j
public class UserReaction extends Auditable {
    @Id
    private Long id;

    @Column(length = 100)
    private String referenceId;

    @Column(length = 10)
    @Convert(converter = ReferenceEnum.DBConverter.class)
    private ReferenceEnum referenceType;

    @Column(length = 20)
    @Convert(converter = ReactionEnum.DBConverter.class)
    private ReactionEnum reactionType;

    public static UserReaction createOrUpdate(String refId,
                                              ReferenceEnum refEnum,
                                              ReactionEnum reactionEnum,
                                              Map<ReferenceEnum, ReferenceService> refServices,
                                              ReactionRepository reactionRepository,
                                              String userId,
                                              EntityManager entityManager,
                                              IdGenerator idGenerator
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
                return insertAIfBNotExistConcurrent(ReactionEnum.LIKE.name(), ReactionEnum.DISLIKE.name(), entityManager, new UserReaction(userId, idGenerator.getId(), refId, refEnum, reactionEnum));
            }
        } else if (reactionEnum.equals(ReactionEnum.DISLIKE)) {
            log.info("update like to dislike if exist");
            Optional<UserReaction> reaction = reactionRepository.findReaction(userId, refId, refEnum, ReactionEnum.LIKE);
            if (reaction.isPresent()) {
                reaction.get().setReactionType(ReactionEnum.DISLIKE);
                return reactionRepository.save(reaction.get());
            } else {
                return insertAIfBNotExistConcurrent(ReactionEnum.DISLIKE.name(), ReactionEnum.LIKE.name(), entityManager, new UserReaction(userId, idGenerator.getId(), refId, refEnum, reactionEnum));
            }
        } else if (reactionEnum.equals(ReactionEnum.NOT_INTERESTED)) {
            return reactionRepository.save(new UserReaction(idGenerator.getId(), refId, refEnum, reactionEnum));
        } else if (reactionEnum.equals(ReactionEnum.REPORT)) {
            return reactionRepository.save(new UserReaction(idGenerator.getId(), refId, refEnum, reactionEnum));
        } else {
            throw new ReactionTypeNotFoundException();
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

    private UserReaction(Long id, String referenceId, ReferenceEnum referenceType, ReactionEnum reactionEnum) {
        this.id = id;
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

    public static RankedUserReactionRepresentation findType(ReactionEnum reaction, Integer pageNumber, Integer pageSize, SortOrderEnum sortOrder, EntityManager entityManager) {
        List<Object[]> resultList = entityManager.createNativeQuery("SELECT COUNT( reference_id ), reference_id,reference_type" +
                " FROM user_reaction ur WHERE ur.reaction_type = ?1  GROUP BY ur.reference_id , ur.reference_type ORDER BY COUNT( reference_id ) " + sortOrder.name() + " LIMIT ?2, ?3 ")
                .setParameter(1, reaction.name())
                .setParameter(2, pageNumber * pageSize)
                .setParameter(3, pageSize)
                .getResultList();
        List<RankedUserReaction> rankedUserReactions = new ArrayList<>(resultList.size());
        for (Object[] row : resultList) {
            rankedUserReactions.add(new RankedUserReaction(((BigInteger) row[0]).longValue(), (String) row[1], (ReferenceEnum.valueOf((String) row[2]))));
        }
        List resultList1 = entityManager.createNativeQuery("SELECT COUNT(DISTINCT reference_id , reference_type)  FROM user_reaction ur WHERE ur.reaction_type = ?1")
                .setParameter(1, reaction.name())
                .getResultList();
        BigInteger o = (BigInteger) resultList1.get(0);
        return new RankedUserReactionRepresentation(rankedUserReactions, o.longValue());
    }
}
