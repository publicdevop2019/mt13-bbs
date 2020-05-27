package com.hw.aggregate.comment;

import com.hw.aggregate.comment.command.CreateCommentCommand;
import com.hw.aggregate.comment.command.DeleteCommentAdminCommand;
import com.hw.aggregate.comment.command.DeleteCommentCommand;
import com.hw.aggregate.comment.exception.CommentUnsupportedSortOrderException;
import com.hw.aggregate.comment.model.Comment;
import com.hw.aggregate.comment.model.CommentSortCriteriaEnum;
import com.hw.aggregate.comment.model.CommentSortOrderEnum;
import com.hw.aggregate.comment.representation.CommentCountPublicRepresentation;
import com.hw.aggregate.comment.representation.CommentSummaryAdminRepresentation;
import com.hw.aggregate.comment.representation.CommentSummaryPrivateRepresentation;
import com.hw.aggregate.comment.representation.CommentSummaryPublicRepresentation;
import com.hw.aggregate.post.PostApplicationService;
import com.hw.aggregate.reaction.ReactionApplicationService;
import com.hw.aggregate.reaction.model.ReferenceService;
import com.hw.shared.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentApplicationService implements ReferenceService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IdGenerator idGenerator;
    private List<ReferenceService> refServices;

    @Autowired
    public void setRefServices(PostApplicationService postApplicationService) {
        List<ReferenceService> referenceServices = new ArrayList<>();
        referenceServices.add(postApplicationService);
        this.refServices = referenceServices;
    }

    @Autowired
    private ReactionApplicationService likeApplicationService;

    //private any user
    @Transactional(readOnly = true)
    public CommentSummaryPrivateRepresentation getAllCommentsForUser(String userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Comment> commentsForUser = commentRepository.findCommentsForUser(userId, pageRequest);
        return new CommentSummaryPrivateRepresentation(commentsForUser.getContent());
    }

    @Transactional(readOnly = true)
    public CommentSummaryAdminRepresentation getAllCommentsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Comment> commentsForUser = commentRepository.findAll(pageRequest);
        List<CommentSummaryAdminRepresentation.CommentAdminCard> collect = commentsForUser.get().map(CommentSummaryAdminRepresentation.CommentAdminCard::new).collect(Collectors.toList());
        return new CommentSummaryAdminRepresentation(collect, commentsForUser.getTotalElements());
    }

    //private any user
    @Transactional
    public void addCommentToPost(String postId, CreateCommentCommand command) {
        Comment.create(command.getContent(), command.getReplyTo(), postId, refServices, commentRepository, idGenerator);
    }

    //private owner only
    @Transactional
    public void deleteComment(DeleteCommentCommand command) {
        Comment.delete(command.getCommentId(), command.getUserId(), commentRepository);
    }

    @Transactional
    public void deleteCommentForAdmin(DeleteCommentAdminCommand command) {
        Comment.deleteForAdmin(command.getCommentId(), commentRepository);
    }

    // public
    @Transactional(readOnly = true)
    public CommentSummaryPublicRepresentation getAllCommentsForPost(String postId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        List<CommentSummaryPublicRepresentation.CommentPublicCard> collect =
                commentRepository.findCommentsByReferenceIdId(postId, pageRequest).getContent().stream().map(e ->
                        new CommentSummaryPublicRepresentation.CommentPublicCard(e,
                                likeApplicationService.countLikeForComment(String.valueOf(e.getId())).getCount(),
                                likeApplicationService.countDislikeForComment(String.valueOf(e.getId())).getCount()))
                        .collect(Collectors.toList());
        return new CommentSummaryPublicRepresentation(collect);
    }

    // internal
    @Transactional(readOnly = true)
    public CommentCountPublicRepresentation countCommentForPost(String postId) {
        Long aLong = commentRepository.countCommentByReferenceId(postId);
        return new CommentCountPublicRepresentation(aLong);
    }

    // internal
    @Transactional
    public void purgeComments(String refId) {
        commentRepository.purgeCommentsForReference(refId);
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
