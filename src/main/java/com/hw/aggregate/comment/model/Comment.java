package com.hw.aggregate.comment.model;

import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@SequenceGenerator(name = "commentId_gen", sequenceName = "commentId_gen", initialValue = 100)
@Data
@NoArgsConstructor
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "commentId_gen")
    private Long id;
    @Column
    private String content;
    @Column
    private String replyTo;
    @Column
    private Long postId;

    public static Comment create(String content, String replyTo, String postId) {
        return new Comment(content, replyTo, Long.parseLong(postId));
    }

    private Comment(String content, String replyTo, Long postId) {
        this.content = content;
        this.replyTo = replyTo;
        this.postId = postId;
    }
}
