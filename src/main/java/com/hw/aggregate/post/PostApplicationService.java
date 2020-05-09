package com.hw.aggregate.post;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.post.command.CreatePostCommand;
import com.hw.aggregate.post.command.DeletePostCommand;
import com.hw.aggregate.post.command.UpdatePostCommand;
import com.hw.aggregate.post.exception.PostAccessException;
import com.hw.aggregate.post.exception.PostNotFoundException;
import com.hw.aggregate.post.exception.PostUnsupportedSortOrderException;
import com.hw.aggregate.post.model.Post;
import com.hw.aggregate.post.model.PostSortCriteriaEnum;
import com.hw.aggregate.post.model.PostSortOrderEnum;
import com.hw.aggregate.post.representation.PostCardSummaryRepresentation;
import com.hw.aggregate.post.representation.PostCreateRepresentation;
import com.hw.aggregate.post.representation.PostDetailRepresentation;
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
public class PostApplicationService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentApplicationService commentApplicationService;

    //public
    @Transactional(readOnly = true)
    public PostCardSummaryRepresentation getByTopic(String topic, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Post> postsByTopic = postRepository.findPostsByTopic(topic, pageRequest);
        List<PostCardSummaryRepresentation.PostCard> collect = postsByTopic.get().map(e -> new PostCardSummaryRepresentation.PostCard(e, commentApplicationService.countCommentForPost(e.getId()).getCount())).collect(Collectors.toList());
        PostCardSummaryRepresentation postCardSummaryRepresentation = new PostCardSummaryRepresentation();
        postCardSummaryRepresentation.setPostCardList(collect);
        return postCardSummaryRepresentation;
    }

    //private any user
    @Transactional(readOnly = true)
    public PostCardSummaryRepresentation getForUser(String userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Post> postsByTopic = postRepository.findPostsForUser(userId, pageRequest);
        List<PostCardSummaryRepresentation.PostCard> collect = postsByTopic.get().map(e -> new PostCardSummaryRepresentation.PostCard(e, commentApplicationService.countCommentForPost(e.getId()).getCount())).collect(Collectors.toList());
        PostCardSummaryRepresentation postCardSummaryRepresentation = new PostCardSummaryRepresentation();
        postCardSummaryRepresentation.setPostCardList(collect);
        return postCardSummaryRepresentation;
    }

    //private any user
    @Transactional
    public PostCreateRepresentation createPost(CreatePostCommand command) {
        return new PostCreateRepresentation(postRepository.save(Post.create(command)));
    }

    //public
    @Transactional
    public PostDetailRepresentation readPostById(String postId) {
        Post postById = getPostByIdForUpdate(postId);
        postById.setViewNum(postById.getViewNum() + 1);
        postRepository.save(postById);
        return new PostDetailRepresentation(getPostById(postId));
    }

    //internal
    @Transactional
    public Boolean existById(String postId) {
        return postRepository.existsById(Long.parseLong(postId));
    }

    //private owner only
    @Transactional
    public void updatePost(String userId, String postId, UpdatePostCommand command) {
        Post postById = getPostById(postId);
        if (!postById.getCreatedBy().equals(userId))
            throw new PostAccessException();
        postById.setContent(command.getContent());
        postRepository.save(postById);
    }

    //private owner only
    @Transactional
    public void deletePost(DeletePostCommand command) {
        Post postById = getPostById(command.getPostId());
        if (!postById.getCreatedBy().equals(command.getUserId()))
            throw new PostAccessException();
        postRepository.deleteById(Long.parseLong(command.getPostId()));
    }


    private Post getPostById(String postId) {
        Optional<Post> byId = postRepository.findById(Long.parseLong(postId));
        if (byId.isEmpty())
            throw new PostNotFoundException();
        return byId.get();
    }

    private Post getPostByIdForUpdate(String postId) {
        Optional<Post> byId = postRepository.findByIdForUpdate(Long.parseLong(postId));
        if (byId.isEmpty())
            throw new PostNotFoundException();
        return byId.get();
    }

    private PageRequest getPageRequest(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort initialSort = new Sort(Sort.Direction.ASC, PostSortCriteriaEnum.fromString(sortBy).getSortCriteria());
        Sort finalSort;
        if (sortOrder.equalsIgnoreCase(PostSortOrderEnum.ASC.getSortOrder())) {
            finalSort = initialSort.ascending();
        } else if (sortOrder.equalsIgnoreCase(PostSortOrderEnum.DESC.getSortOrder())) {
            finalSort = initialSort.descending();
        } else {
            throw new PostUnsupportedSortOrderException();
        }
        return PageRequest.of(pageNumber, pageSize, finalSort);
    }
}
