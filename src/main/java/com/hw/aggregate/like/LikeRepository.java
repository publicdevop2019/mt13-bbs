package com.hw.aggregate.like;

import com.hw.aggregate.like.model.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<UserLike, Long> {
    @Query("SELECT COUNT(p) FROM #{#entityName} as p WHERE p.referenceId = ?1")
    Long countLikeForReference(String userId);

    @Modifying
    @Query("DELETE FROM #{#entityName} as p WHERE p.createdBy = ?1 AND p.referenceId= ?2")
    void deleteByUserIdAndPostId(String userId, String referenceId);
}