package com.hw.aggregate.like;

import com.hw.shared.ServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(produces = "application/json")
public class LikeController {

    @Autowired
    private LikeApplicationService likeApplicationService;

    @PostMapping("private/posts/{postId}/likes")
    public ResponseEntity<?> addLikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        likeApplicationService.addLikePost(ServiceUtility.getUserId(authorization), postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/comments/{commentId}/likes")
    public ResponseEntity<?> addLikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        likeApplicationService.addLikeComment(ServiceUtility.getUserId(authorization), commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/posts/{postId}/likes")
    public ResponseEntity<?> removeLikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        likeApplicationService.removeLike(ServiceUtility.getUserId(authorization), postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}/likes")
    public ResponseEntity<?> removeLikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        likeApplicationService.removeLike(ServiceUtility.getUserId(authorization), commentId);
        return ResponseEntity.ok().build();
    }
}
