package com.hw.aggregate.post;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.post.command.CreatePostCommand;
import com.hw.aggregate.post.command.DeletePostAdminCommand;
import com.hw.aggregate.post.command.DeletePostCommand;
import com.hw.aggregate.post.command.UpdatePostCommand;
import com.hw.aggregate.post.exception.PostUnsupportedSortOrderException;
import com.hw.aggregate.post.model.Post;
import com.hw.aggregate.post.model.PostSortCriteriaEnum;
import com.hw.aggregate.post.model.PostSortOrderEnum;
import com.hw.aggregate.post.representation.PostCardSummaryRepresentation;
import com.hw.aggregate.post.representation.PostCreateRepresentation;
import com.hw.aggregate.post.representation.PostDetailRepresentation;
import com.hw.aggregate.reaction.ReactionApplicationService;
import com.hw.aggregate.reaction.model.ReferenceService;
import com.hw.aggregate.reaction.representation.ReactionCountRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostApplicationService implements ReferenceService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentApplicationService commentApplicationService;
    @Autowired
    private ReactionApplicationService reactionApplicationService;

    //public
    @Transactional(readOnly = true)
    public PostCardSummaryRepresentation getByTopic(String topic, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Post> postsByTopic = postRepository.findPostsByTopic(topic, pageRequest);
        List<PostCardSummaryRepresentation.PostCard> collect = postsByTopic.get().map(e -> new PostCardSummaryRepresentation.PostCard(e, commentApplicationService.countCommentForPost(e.getId().toString()).getCount())).collect(Collectors.toList());
        return new PostCardSummaryRepresentation(collect);
    }

    //public
    @Transactional(readOnly = true)
    public PostCardSummaryRepresentation getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Post> postsByTopic = postRepository.findAll(pageRequest);
        List<PostCardSummaryRepresentation.PostCard> collect = postsByTopic.get().map(e -> new PostCardSummaryRepresentation.PostCard(e, commentApplicationService.countCommentForPost(e.getId().toString()).getCount())).collect(Collectors.toList());
        return new PostCardSummaryRepresentation(collect);
    }

    //private any user
    @Transactional(readOnly = true)
    public PostCardSummaryRepresentation getForUser(String userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortBy, sortOrder);
        Page<Post> postsByTopic = postRepository.findPostsForUser(userId, pageRequest);
        List<PostCardSummaryRepresentation.PostCard> collect = postsByTopic.get().map(e -> new PostCardSummaryRepresentation.PostCard(e, commentApplicationService.countCommentForPost(e.getId().toString()).getCount())).collect(Collectors.toList());
        return new PostCardSummaryRepresentation(collect);
    }

    //private any user
    @Transactional
    public PostCreateRepresentation createPost(CreatePostCommand command) {
        return new PostCreateRepresentation(Post.create(command, postRepository));
    }

    //public
    @Transactional
    public PostDetailRepresentation readPostById(String postId) {
        Post postById = Post.readThenUpdateCount(postId, postRepository);
        ReactionCountRepresentation likes = reactionApplicationService.countLikeForPost(postId);
        ReactionCountRepresentation dislikes = reactionApplicationService.countDislikeForPost(postId);
        return new PostDetailRepresentation(postById, likes.getCount(), dislikes.getCount());
    }

    //internal
    @Transactional(readOnly = true)
    public Boolean existById(String postId) {
        return postRepository.existsById(Long.parseLong(postId));
    }

    //private owner only
    @Transactional
    public void updatePost(String userId, String postId, UpdatePostCommand command) {
        Post postById = Post.read(postId, postRepository);
        postById.updateContent(userId, command.getContent());
        postRepository.save(postById);
    }

    //private owner only
    @Transactional
    public void deletePost(DeletePostCommand command) {
        Post.delete(command.getPostId(), command.getUserId(), postRepository);
        commentApplicationService.purgeComments(command.getPostId());
        reactionApplicationService.purgeReactions(command.getPostId());
    }

    //private owner only
    @Transactional
    public void deletePostForAdmin(DeletePostAdminCommand command) {
        Post.deleteForAdmin(command.getPostId(), postRepository);
        commentApplicationService.purgeComments(command.getPostId());
        reactionApplicationService.purgeReactions(command.getPostId());
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
