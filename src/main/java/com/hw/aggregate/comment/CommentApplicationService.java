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
import com.hw.aggregate.post.PostApplicationService;
import com.hw.aggregate.post.exception.PostNotFoundException;
import com.hw.aggregate.reaction.ReactionApplicationService;
import com.hw.aggregate.reaction.model.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentApplicationService implements ReferenceService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostApplicationService postApplicationService;

    @Autowired
    private ReactionApplicationService likeApplicationService;

    //private any user
    @Transactional(readOnly = true)
    public CommentSummaryPrivateRepresentation getAllCommentsForUser(String userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Comment> commentsForUser = commentRepository.findCommentsForUser(userId, pageRequest);
        return new CommentSummaryPrivateRepresentation(commentsForUser.getContent());
    }

    //private any user
    @Transactional
    public void addCommentToPost(String postId, CreateCommentCommand command) {
        boolean b = postApplicationService.existById(postId);
        if (!b)
            throw new PostNotFoundException();
        Comment comment = Comment.create(command.getContent(), command.getReplyTo(), postId);
        commentRepository.save(comment);
    }

    //private owner only
    @Transactional
    public void deleteComment(DeleteCommentCommand command) {
        Optional<Comment> byId = commentRepository.findById(Long.parseLong(command.getCommentId()));
        if (byId.isEmpty())
            throw new CommentNotFoundException();
        if (!byId.get().getCreatedBy().equals(command.getUserId()))
            throw new CommentAccessException();
        commentRepository.delete(byId.get());
    }

    // public
    @Transactional(readOnly = true)
    public CommentSummaryPublicRepresentation getAllCommentsForPost(String postId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Comment> commentsByPostId = commentRepository.findCommentsByPostId(Long.parseLong(postId), pageRequest);
        List<Comment> content = commentsByPostId.getContent();
        List<CommentSummaryPublicRepresentation.CommentPublicCard> collect =
                content.stream().map(e ->
                        new CommentSummaryPublicRepresentation.CommentPublicCard(e,
                                likeApplicationService.countLikeForComment(String.valueOf(e.getId())).getCount(),
                                likeApplicationService.countDislikeForComment(String.valueOf(e.getId())).getCount()))
                        .collect(Collectors.toList());
        CommentSummaryPublicRepresentation commentSummaryPublicRepresentation = new CommentSummaryPublicRepresentation();
        commentSummaryPublicRepresentation.setCommentList(collect);
        return commentSummaryPublicRepresentation;
    }

    // internal
    @Transactional(readOnly = true)
    public CommentCountPublicRepresentation countCommentForPost(Long postId) {
        Long aLong = commentRepository.countCommentByPostId(postId);
        return new CommentCountPublicRepresentation(aLong);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existById(String refId) {
        return commentRepository.existsById(Long.parseLong(refId));
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
}
