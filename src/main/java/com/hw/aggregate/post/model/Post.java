package com.hw.aggregate.post.model;

import com.hw.aggregate.post.PostRepository;
import com.hw.aggregate.post.command.CreatePostCommand;
import com.hw.aggregate.post.exception.PostAccessException;
import com.hw.aggregate.post.exception.PostNotFoundException;
import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table
@SequenceGenerator(name = "postId_gen", sequenceName = "postId_gen", initialValue = 100)
@Data
@NoArgsConstructor
public class Post extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "postId_gen")
    private Long id;
    @Column
    private String title;
    @Column
    private String topic;
    @Column(length = 10000)
    private String content;
    @Column
    private Long viewNum;
    @Column
    private Boolean userModified;
    @Version
    private Integer version;

    public static Post create(CreatePostCommand command, PostRepository postRepository) {
        return postRepository.save(new Post(command.getTitle(), command.getTopic(), command.getContent()));
    }

    private Post(String title, String topic, String content) {
        this.title = title;
        this.topic = topic;
        this.content = content;
        this.viewNum = 0L;
        this.userModified = Boolean.FALSE;
    }

    public static void delete(String postId, String userId, PostRepository postRepository) {
        Optional<Post> byId = postRepository.findById(Long.parseLong(postId));
        if (byId.isEmpty())
            throw new PostNotFoundException();
        if (!byId.get().getCreatedBy().equals(userId))
            throw new PostAccessException();
        postRepository.delete(byId.get());
    }

    public static void deleteForAdmin(String postId, PostRepository postRepository) {
        Optional<Post> byId = postRepository.findById(Long.parseLong(postId));
        if (byId.isEmpty())
            throw new PostNotFoundException();
        postRepository.delete(byId.get());
    }

    public static Post read(String postId, PostRepository postRepository) {
        Optional<Post> byId = postRepository.findById(Long.parseLong(postId));
        if (byId.isEmpty())
            throw new PostNotFoundException();
        return byId.get();
    }

    public static Post readThenUpdateCount(String postId, PostRepository postRepository) {
        Optional<Post> byId = postRepository.findByIdForUpdate(Long.parseLong(postId));
        if (byId.isEmpty())
            throw new PostNotFoundException();
        byId.get().setViewNum(byId.get().getViewNum() + 1);
        return byId.get();
    }

    public void updateContent(String userId, String updated) {
        if (!getCreatedBy().equals(userId))
            throw new PostAccessException();
        this.content = updated;
        this.userModified = Boolean.TRUE;
    }
}
