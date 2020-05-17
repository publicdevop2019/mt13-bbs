package com.hw.aggregate.reaction;

import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.ReferenceEnum;
import com.hw.aggregate.reaction.model.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<UserReaction, Long> {
    @Query("SELECT COUNT(p) FROM #{#entityName} as p WHERE p.referenceId = ?1 AND p.referenceType = ?2 AND p.reactionType = ?3")
    Long countReaction(String referenceId, ReferenceEnum referenceType, ReactionEnum reactionType);

    @Query("SELECT p FROM #{#entityName} as p WHERE p.createdBy = ?1 AND p.referenceId= ?2 AND p.referenceType= ?3 AND p.reactionType = ?4")
    Optional<UserReaction> findReaction(String userId, String referenceId, ReferenceEnum referenceType, ReactionEnum reactionType);

    @Modifying
    @Query("DELETE FROM #{#entityName} as p WHERE p.referenceId = ?1")
    void purgeReactionForReference(String referenceId);
}
