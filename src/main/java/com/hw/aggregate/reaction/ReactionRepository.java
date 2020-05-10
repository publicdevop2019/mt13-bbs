package com.hw.aggregate.reaction;

import com.hw.aggregate.reaction.model.ReactionEnum;
import com.hw.aggregate.reaction.model.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReactionRepository extends JpaRepository<UserReaction, Long> {
    @Query("SELECT COUNT(p) FROM #{#entityName} as p WHERE p.referenceId = ?1 AND p.referenceType = ?2 AND p.reactionType = ?3")
    Long countReaction(String referenceId, String type, ReactionEnum reactionEnum);

    @Modifying
    @Query("DELETE FROM #{#entityName} as p WHERE p.createdBy = ?1 AND p.referenceId= ?2 AND p.referenceType= ?3 AND p.reactionType = ?4")
    void deleteReaction(String userId, String referenceId, String type, ReactionEnum reactionEnum);

}
