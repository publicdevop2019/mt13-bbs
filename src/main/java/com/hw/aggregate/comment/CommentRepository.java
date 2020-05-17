package com.hw.aggregate.comment;

import com.hw.aggregate.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT p FROM #{#entityName} as p WHERE p.referenceId = ?1")
    Page<Comment> findCommentsByReferenceIdId(Long postId, Pageable pageable);

    @Query("SELECT COUNT(p) FROM #{#entityName} as p WHERE p.referenceId = ?1")
    Long countCommentByPostId(Long postId);

    @Query("SELECT p FROM #{#entityName} as p WHERE p.createdBy = ?1")
    Page<Comment> findCommentsForUser(String userId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM #{#entityName} as p WHERE p.referenceId = ?1")
    void purgeCommentsForReference(String referenceId);
}
