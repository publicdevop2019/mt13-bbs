package com.hw.aggregate.post.representation;

import com.hw.aggregate.post.model.Post;
import lombok.Data;

@Data
public class PostCreateRepresentation {
    private String id;

    public PostCreateRepresentation(Post save) {
        id = save.getId().toString();
    }
}
