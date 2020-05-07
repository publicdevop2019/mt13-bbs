package com.hw.aggregate.post;

import com.hw.aggregate.comment.command.CreateCommentCommand;
import com.hw.aggregate.comment.command.DeleteCommentCommand;
import com.hw.aggregate.comment.exception.CommentAccessException;
import com.hw.aggregate.post.command.CreatePostCommand;
import com.hw.aggregate.post.command.DeletePostCommand;
import com.hw.aggregate.post.command.UpdatePostCommand;
import com.hw.aggregate.post.exception.*;
import com.hw.aggregate.post.model.Comment;
import com.hw.aggregate.post.model.Post;
import com.hw.aggregate.post.model.SortCriteriaEnum;
import com.hw.aggregate.post.model.SortOrderEnum;
import com.hw.aggregate.post.representation.PostCardSummaryRepresentation;
import com.hw.aggregate.post.representation.PostCreateRepresentation;
import com.hw.aggregate.post.representation.PostDetailRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostApplicationService {
    @Autowired
    private PostRepository postRepository;

    //public
    public PostCardSummaryRepresentation getByTopic(String topic, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Post> postsByTopic = postRepository.findPostsByTopic(topic, pageRequest);
        return new PostCardSummaryRepresentation(postsByTopic.getContent());
    }

    //private any user
    public PostCardSummaryRepresentation getForUser(String userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Post> postsByTopic = postRepository.findPostsForUser(userId, pageRequest);
        return new PostCardSummaryRepresentation(postsByTopic.getContent());
    }

    //private any user
    public PostCreateRepresentation createPost(CreatePostCommand command) {
        return new PostCreateRepresentation(postRepository.save(Post.create(command)));
    }

    //public
    public PostDetailRepresentation readPostById(String postId) {
        return new PostDetailRepresentation(getPostById(postId));
    }

    //private owner only
    public void updatePost(String userId, String postId, UpdatePostCommand command) {
        Post postById = getPostById(postId);
        if (!postById.getCreatedBy().equals(userId))
            throw new PostAccessException();
        postById.setContent(command.getContent());
        postRepository.save(postById);
    }

    //private owner only
    public void deletePost(DeletePostCommand command) {
        Post postById = getPostById(command.getPostId());
        if (!postById.getCreatedBy().equals(command.getUserId()))
            throw new PostAccessException();
        postRepository.deleteById(Long.parseLong(command.getPostId()));
    }

    //private any user
    public void addCommentToPost(String userId, String postId, CreateCommentCommand command) {
        Comment comment = new Comment(command, userId);
        Post postById = getPostById(postId);
        postById.getCommentList().add(comment);
        postRepository.save(postById);
    }

    //private owner only
    public void deleteCommentFromPost(DeleteCommentCommand command) {
        Post postById = getPostById(command.getPostId());
        List<Comment> collect = postById.getCommentList().stream().filter(e -> e.getId().equals(command.getCommentId())).collect(Collectors.toList());
        if (collect.size() == 0)
            throw new CommentNotFoundException();
        if (collect.size() != 1)
            throw new MultiCommentFoundException();
        Comment comment = collect.get(0);
        if (!comment.getPublisherId().equals(command.getUserId()))
            throw new CommentAccessException();
        postById.getCommentList().remove(comment);
        postRepository.save(postById);
    }

    private Post getPostById(String postId) {
        Optional<Post> byId = postRepository.findById(Long.parseLong(postId));
        if (byId.isEmpty())
            throw new PostNotFoundException();
        return byId.get();
    }

    private PageRequest getPageRequest(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort initialSort = new Sort(Sort.Direction.ASC, SortCriteriaEnum.fromString(sortBy).getSortCriteria());
        Sort finalSort;
        if (sortOrder.equalsIgnoreCase(SortOrderEnum.ASC.getSortOrder())) {
            finalSort = initialSort.ascending();
        } else if (sortOrder.equalsIgnoreCase(SortOrderEnum.DESC.getSortOrder())) {
            finalSort = initialSort.descending();
        } else {
            throw new UnsupportedSortOrderException();
        }
        return PageRequest.of(pageNumber, pageSize, finalSort);
    }
}
