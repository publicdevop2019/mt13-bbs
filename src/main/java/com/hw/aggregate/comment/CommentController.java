package com.hw.aggregate.comment;

import com.hw.aggregate.comment.command.CreateCommentCommand;
import com.hw.aggregate.comment.command.DeleteCommentAdminCommand;
import com.hw.aggregate.comment.command.DeleteCommentCommand;
import com.hw.aggregate.comment.representation.CommentSummaryPrivateRepresentation;
import com.hw.aggregate.comment.representation.CommentSummaryPublicRepresentation;
import com.hw.shared.ServiceUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(produces = "application/json")
public class CommentController {
    @Autowired
    private CommentApplicationService commentApplicationService;

    @GetMapping("private/comments")
    public ResponseEntity<?> getAllCommentsForUser(@RequestHeader("authorization") String authorization, @RequestParam("pageNum") Integer pageNumber,
                                                   @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") String sortBy,
                                                   @RequestParam("sortOrder") String sortOrder) {
        CommentSummaryPrivateRepresentation allCommentsForUser =
                commentApplicationService.getAllCommentsForUser(
                        ServiceUtility.getUserId(authorization),
                        pageNumber, pageSize, sortBy, sortOrder
                );
        return ResponseEntity.ok(allCommentsForUser.getCommentList());
    }

    @GetMapping("admin/comments")
    public ResponseEntity<?> getAllCommentsForAdmin(@RequestParam("pageNum") Integer pageNumber,
                                                    @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") String sortBy,
                                                    @RequestParam("sortOrder") String sortOrder) {
        return ResponseEntity.ok(commentApplicationService.getAllCommentsForAdmin(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("public/posts/{postId}/comments")
    public ResponseEntity<?> getAllCommentsForPost(@PathVariable(name = "postId") String postId, @RequestParam("pageNum") Integer pageNumber,
                                                   @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") String sortBy,
                                                   @RequestParam("sortOrder") String sortOrder) {
        CommentSummaryPublicRepresentation allCommentsForUser =
                commentApplicationService.getAllCommentsForPost(postId, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(allCommentsForUser.getCommentList());
    }

    @PostMapping("private/posts/{postId}/comments")
    public ResponseEntity<?> addCommentToPost(@RequestHeader("authorization") String authorization, @PathVariable(name = "postId") String postId, @RequestBody CreateCommentCommand command) {
        commentApplicationService.addCommentToPost(postId, command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("private/comments/{commentId}")
    public ResponseEntity<?> deleteCommentFromPost(@RequestHeader("authorization") String authorization, @PathVariable(name = "commentId") String commentId) {
        commentApplicationService.deleteComment(new DeleteCommentCommand(ServiceUtility.getUserId(authorization), commentId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("admin/comments/{commentId}")
    public ResponseEntity<?> deleteCommentFromPostForAdmin(@PathVariable(name = "commentId") String commentId) {
        commentApplicationService.deleteCommentForAdmin(new DeleteCommentAdminCommand(commentId));
        return ResponseEntity.ok().build();
    }
}
