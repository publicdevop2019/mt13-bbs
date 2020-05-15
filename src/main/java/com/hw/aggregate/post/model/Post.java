package com.hw.aggregate.post.model;

import com.hw.aggregate.post.command.CreatePostCommand;
import com.hw.aggregate.post.exception.PostAccessException;
import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public static Post create(CreatePostCommand command) {
        return new Post(command.getTitle(), command.getTopic(), command.getContent());
    }

    private Post(String title, String topic, String content) {
        this.title = title;
        this.topic = topic;
        this.content = content;
        this.viewNum = 0L;
        this.userModified = Boolean.FALSE;
    }

    public void updateContent(String userId, String updated) {
        if (!getCreatedBy().equals(userId))
            throw new PostAccessException();
        this.content = updated;
        this.userModified = Boolean.TRUE;
    }
}
