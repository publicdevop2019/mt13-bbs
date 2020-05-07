package com.hw.aggregate.post.model;

import com.hw.aggregate.post.command.CreatePostCommand;
import com.hw.shared.Auditable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

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
    @Column
    private Date publishAt;
    @Column
    private String content;

//    @Column(length = 100000)
//    private ArrayList<Comment> commentList;
    @Version
    private Integer version;

    public static Post create(CreatePostCommand command) {
        return new Post(command.getTitle(), command.getTopic(), command.getContent());
    }

    private Post(String title, String topic, String content) {
        this.title = title;
        this.topic = topic;
        this.publishAt = new Date();
        this.content = content;
//        this.commentList = new ArrayList<>();
    }
}
