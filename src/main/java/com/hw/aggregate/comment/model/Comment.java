package com.hw.aggregate.comment.model;

import com.hw.aggregate.comment.command.CreateCommentCommand;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@SequenceGenerator(name = "commentId_gen", sequenceName = "commentId_gen", initialValue = 100)
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "commentId_gen")
    private Long id;
    @Column
    private Date publishedAt;
    @Column
    private String publishedBy;
    @Column
    private String content;
    @Column
    private String replyTo;

    public static Comment create(CreateCommentCommand command) {
        return new Comment(command.getPublishedBy(), command.getContent(), command.getReplyTo());
    }

    private Comment(String publishedBy, String content, String replyTo) {
        this.publishedAt = new Date();
        this.publishedBy = publishedBy;
        this.content = content;
        this.replyTo = replyTo;
    }
}
