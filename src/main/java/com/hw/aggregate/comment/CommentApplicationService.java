package com.hw.aggregate.comment;

import com.hw.aggregate.comment.command.CreateCommentCommand;
import com.hw.aggregate.comment.command.DeleteCommentCommand;
import com.hw.aggregate.comment.exception.CommentAccessException;
import com.hw.aggregate.comment.exception.CommentNotFoundException;
import com.hw.aggregate.comment.exception.CommentUnsupportedSortOrderException;
import com.hw.aggregate.comment.model.Comment;
import com.hw.aggregate.comment.model.CommentSortCriteriaEnum;
import com.hw.aggregate.comment.model.CommentSortOrderEnum;
import com.hw.aggregate.comment.representation.CommentCountPublicRepresentation;
import com.hw.aggregate.comment.representation.CommentSummaryPrivateRepresentation;
import com.hw.aggregate.comment.representation.CommentSummaryPublicRepresentation;
import com.hw.aggregate.post.PostRepository;
import com.hw.aggregate.post.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentApplicationService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    //private any user
    public CommentSummaryPrivateRepresentation getAllCommentsForUser(String userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Comment> commentsForUser = commentRepository.findCommentsForUser(userId, pageRequest);
        return new CommentSummaryPrivateRepresentation(commentsForUser.getContent());
    }

    //private any user
    public void addCommentToPost(String postId, CreateCommentCommand command) {
        boolean b = postRepository.existsById(Long.parseLong(postId));
        if (!b)
            throw new PostNotFoundException();
        Comment comment = Comment.create(command.getContent(), command.getReplyTo(), postId);
        commentRepository.save(comment);
    }

    //private owner only
    public void deleteComment(DeleteCommentCommand command) {
        Optional<Comment> byId = commentRepository.findById(Long.parseLong(command.getCommentId()));
        if (byId.isEmpty())
            throw new CommentNotFoundException();
        if (!byId.get().getCreatedBy().equals(command.getUserId()))
            throw new CommentAccessException();
        commentRepository.delete(byId.get());
    }

    // public
    public CommentSummaryPublicRepresentation getAllCommentsForPost(String postId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Comment> commentsByPostId = commentRepository.findCommentsByPostId(Long.parseLong(postId), pageRequest);
        return new CommentSummaryPublicRepresentation(commentsByPostId.getContent());
    }

    // internal
    public CommentCountPublicRepresentation countCommentForPost(Long postId) {
        Long aLong = commentRepository.countCommentByPostId(postId);
        return new CommentCountPublicRepresentation(aLong);
    }

    private PageRequest getPageRequest(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort initialSort = new Sort(Sort.Direction.ASC, CommentSortCriteriaEnum.fromString(sortBy).name());
        Sort finalSort;
        if (sortOrder.equalsIgnoreCase(CommentSortOrderEnum.ASC.getSortOrder())) {
            finalSort = initialSort.ascending();
        } else if (sortOrder.equalsIgnoreCase(CommentSortOrderEnum.DESC.getSortOrder())) {
            finalSort = initialSort.descending();
        } else {
            throw new CommentUnsupportedSortOrderException();
        }
        return PageRequest.of(pageNumber, pageSize, finalSort);
    }

    public boolean existById(String commentId) {
        return commentRepository.existsById(Long.parseLong(commentId));
    }
}
