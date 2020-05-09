package com.hw.aggregate.like;

import com.hw.aggregate.comment.CommentApplicationService;
import com.hw.aggregate.like.exception.LikeReferenceNotFoundException;
import com.hw.aggregate.like.model.UserLike;
import com.hw.aggregate.like.representation.LikeRepresentation;
import com.hw.aggregate.post.PostApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeApplicationService {
    private static final String COMMENT = "COMMENT";
    private static final String POST = "POST";
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostApplicationService postApplicationService;
    @Autowired
    private CommentApplicationService commentApplicationService;

    //public
    @Transactional
    public LikeRepresentation getLike(String likeId) {
        Long aLong = likeRepository.countLikeForReference(likeId);
        return new LikeRepresentation(aLong);
    }

    //private
    @Transactional
    public void addLikePost(String userId, String postId) {
        if (!postApplicationService.existById(postId))
            throw new LikeReferenceNotFoundException();
        addLike(userId, postId, POST);
    }

    //private
    @Transactional
    public void addLikeComment(String userId, String commentId) {
        if (!commentApplicationService.existById(commentId))
            throw new LikeReferenceNotFoundException();
        addLike(userId, commentId, COMMENT);
    }

    //private
    @Transactional
    public void removeLike(String userId, String referencedId) {
        likeRepository.deleteByUserIdAndPostId(userId, referencedId);
    }

    private void addLike(String userId, String referencedId, String type) {
        UserLike like = UserLike.create(referencedId, type);
        likeRepository.save(like);
    }
}
