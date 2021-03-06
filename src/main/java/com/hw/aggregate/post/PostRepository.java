package com.hw.aggregate.post;

import com.hw.aggregate.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM #{#entityName} as p WHERE p.topic = ?1")
    Page<Post> findPostsByTopic(String topic, Pageable pageable);

    @Query("SELECT p FROM #{#entityName} as p WHERE p.createdBy = ?1")
    Page<Post> findPostsForUser(String userId, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT p FROM #{#entityName} as p WHERE p.id = ?1")
    Optional<Post> findByIdForUpdate(Long id);
}
