package com.hw.aggregate.post;

import com.hw.aggregate.post.command.CreatePostCommand;
import com.hw.aggregate.post.command.DeletePostAdminCommand;
import com.hw.aggregate.post.command.DeletePostCommand;
import com.hw.aggregate.post.command.UpdatePostCommand;
import com.hw.shared.ServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(produces = "application/json")
public class PostController {
    @Autowired
    private PostApplicationService postApplicationService;

    @GetMapping("public/posts")
    public ResponseEntity<?> getPostByTopic(@RequestParam(name = "topic") String topic, @RequestParam("pageNum") Integer pageNumber,
                                            @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") String sortBy,
                                            @RequestParam("sortOrder") String sortOrder) {
        return ResponseEntity.ok(postApplicationService.getByTopic(topic, pageNumber, pageSize, sortBy, sortOrder).getPostCardList());
    }

    @GetMapping("admin/posts")
    public ResponseEntity<?> getAllPostForAdmin(@RequestParam("pageNum") Integer pageNumber,
                                                @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") String sortBy,
                                                @RequestParam("sortOrder") String sortOrder) {
        return ResponseEntity.ok(postApplicationService.getAll(pageNumber, pageSize, sortBy, sortOrder).getPostCardList());
    }

    @GetMapping("private/posts")
    public ResponseEntity<?> getPostForUser(@RequestHeader("authorization") String authorization,
                                            @RequestParam("pageNum") Integer pageNumber,
                                            @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") String sortBy,
                                            @RequestParam("sortOrder") String sortOrder) {
        return ResponseEntity.ok(postApplicationService.getForUser(ServiceUtility.getUserId(authorization), pageNumber, pageSize, sortBy, sortOrder).getPostCardList());
    }

    @PostMapping("private/posts")
    public ResponseEntity<?> createPost(@RequestHeader("authorization") String authorization, @RequestBody CreatePostCommand command) {
        return ResponseEntity.ok().header("Location", postApplicationService.createPost(command).getId()).build();
    }

    @GetMapping("public/posts/{postId}")
    public ResponseEntity<?> readPostById(@PathVariable(name = "postId") String postId) {
        return ResponseEntity.ok(postApplicationService.readPostById(postId));
    }

    @PutMapping("private/posts/{postId}")
    public ResponseEntity<?> updatePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId, @RequestBody UpdatePostCommand command) {
        postApplicationService.updatePost(ServiceUtility.getUserId(authorization), postId, command);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("private/posts/{postId}")
    public ResponseEntity<?> deletePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        postApplicationService.deletePost(new DeletePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("admin/posts/{postId}")
    public ResponseEntity<?> deletePostForAdmin(@PathVariable(name = "postId") String postId) {
        postApplicationService.deletePostForAdmin(new DeletePostAdminCommand(postId));
        return ResponseEntity.ok().build();
    }

}
