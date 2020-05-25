package com.hw.aggregate.reaction;

import com.hw.aggregate.reaction.command.*;
import com.hw.aggregate.reaction.model.SortOrderEnum;
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
        reactionApplicationService.addReaction(new AddLikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/comments/{commentId}/likes")
    public ResponseEntity<?> addLikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.addReaction(new AddLikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/posts/{postId}/likes")
    public ResponseEntity<?> removeLikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.removeReaction(new RemoveLikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}/likes")
    public ResponseEntity<?> removeLikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.removeReaction(new RemoveLikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/posts/{postId}/dislikes")
    public ResponseEntity<?> addDislikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.addReaction(new AddDislikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/comments/{commentId}/dislikes")
    public ResponseEntity<?> addDislikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.addReaction(new AddDislikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/posts/{postId}/dislikes")
    public ResponseEntity<?> removeDislikePost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.removeReaction(new RemoveDislikePostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}/dislikes")
    public ResponseEntity<?> removeDislikeComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.removeReaction(new RemoveDislikeCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/posts/{postId}/reports")
    public ResponseEntity<?> addReportPost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.addReaction(new AddReportPostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/comments/{commentId}/reports")
    public ResponseEntity<?> addReportComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.addReaction(new AddReportCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/posts/{postId}/reports")
    public ResponseEntity<?> removeReportPost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.removeReaction(new RemoveReportPostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}/reports")
    public ResponseEntity<?> removeReportComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.removeReaction(new RemoveReportCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/posts/{postId}/notInterested")
    public ResponseEntity<?> addNotInterestedPost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.addReaction(new AddNotInterestedPostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("private/comments/{commentId}/notInterested")
    public ResponseEntity<?> addNotInterestedComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.addReaction(new AddNotInterestedCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/posts/{postId}/notInterested")
    public ResponseEntity<?> removeNotInterestedPost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId) {
        reactionApplicationService.removeReaction(new RemoveNotInterestedPostCommand(ServiceUtility.getUserId(authorization), postId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}/notInterested")
    public ResponseEntity<?> removeNotInterestedComment(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        reactionApplicationService.removeReaction(new RemoveNotInterestedCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("admin/likes")
    public ResponseEntity<?> getLikesForAdmin(@RequestParam("pageNum") Integer pageNumber,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam("sortOrder") SortOrderEnum sortOrder) {
        return ResponseEntity.ok(reactionApplicationService.getLikes(pageNumber, pageSize, sortOrder));
    }

    @GetMapping("admin/dislikes")
    public ResponseEntity<?> getDisLikesForAdmin(@RequestParam("pageNum") Integer pageNumber,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("sortOrder") SortOrderEnum sortOrder) {
        return ResponseEntity.ok(reactionApplicationService.getDislikes(pageNumber, pageSize, sortOrder));
    }

    @GetMapping("admin/reports")
    public ResponseEntity<?> getReportsForAdmin(@RequestParam("pageNum") Integer pageNumber,
                                                @RequestParam("pageSize") Integer pageSize,
                                                @RequestParam("sortOrder") SortOrderEnum sortOrder) {
        return ResponseEntity.ok(reactionApplicationService.getReports(pageNumber, pageSize, sortOrder));
    }

    @GetMapping("admin/notInterested")
    public ResponseEntity<?> getNotInterestedForAdmin(@RequestParam("pageNum") Integer pageNumber,
                                                      @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam("sortOrder") SortOrderEnum sortOrder) {
        return ResponseEntity.ok(reactionApplicationService.getNotInterested(pageNumber, pageSize, sortOrder));
    }
}
