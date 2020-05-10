package com.hw.aggregate.reaction;

import com.hw.aggregate.reaction.command.*;
import com.hw.shared.ServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(produces = "application/json")
public class ReactionController {

    @Autowired
    private ReactionApplicationService reactionApplicationService;

    @PostMapping("private/posts/{postId}/likes")
    public ResponseEntity<?> addLikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.addLikePost(new AddLikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/comments/{commentId}/likes")
    public ResponseEntity<?> addLikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.addLikeComment(new AddLikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/posts/{postId}/likes")
    public ResponseEntity<?> removeLikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.removePostLike(new RemoveLikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}/likes")
    public ResponseEntity<?> removeLikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.removeCommentLike(new RemoveLikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/posts/{postId}/dislikes")
    public ResponseEntity<?> addDislikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.addDislikePost(new AddDislikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/comments/{commentId}/dislikes")
    public ResponseEntity<?> addDislikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.addDislikeComment(new AddDislikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/posts/{postId}/dislikes")
    public ResponseEntity<?> removeDislikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.removeDislikePost(new RemoveDislikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}/dislikes")
    public ResponseEntity<?> removeDislikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.removeDislikeComment(new RemoveDislikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }
}
