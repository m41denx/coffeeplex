package com.m41den.coffeeplex.service;

import com.m41den.coffeeplex.model.*;
import com.m41den.coffeeplex.repository.CommentRepository;
import com.m41den.coffeeplex.repository.LikeRepository;
import com.m41den.coffeeplex.repository.PhotoRepository;

import java.util.List;
import java.util.Optional;

public class CommentService {
    private CommentRepository commentRepository;
    private PhotoRepository photoRepository;
    private LikeRepository likeRepository;

    public CommentService(CommentRepository commentRepository, PhotoRepository photoRepository, LikeRepository likeRepository) {
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
        this.likeRepository = likeRepository;
    }

    public void postPhoto(User user, byte[] content, String caption) {
        Photo photo = new Photo(content, user.getId(), caption);
        photoRepository.save(photo);
    }

    public void deletePhoto(User user, Integer photoId) throws RuntimeException {
        Optional<Photo> photo = photoRepository.findById(photoId);
        if (photo.isPresent() && photo.get().getOwnerId().equals(user.getId())) {
            photoRepository.deleteById(photoId);
            for (Comment comment : commentRepository.findAllByPhotoId(photoId)) {
                commentRepository.deleteById(comment.getId());
            }
            for (Like like : likeRepository.findAllByPhotoId(photoId)) {
                likeRepository.deleteById(like.getId());
            }
        } else {
            throw new RuntimeException("User does not own the photo");
        }
    }

    public List<Photo> getPhotosByUser(User user) {
        return photoRepository.findAllByOwnerId(user.getId());
    }

    public Photo getPhotoById(Integer photoId) throws RuntimeException {
        Optional<Photo> photo = photoRepository.findById(photoId);
        if (photo.isPresent()) {
            return photo.get();
        }
        throw new RuntimeException("Photo not found");
    }

    public Integer getLikesCount(Photo photo) {
        return likeRepository.countTypeForPhoto(photo.getId(), LikeType.LIKE);
    }

    public Integer getDislikesCount(Photo photo) {
        return likeRepository.countTypeForPhoto(photo.getId(), LikeType.DISLIKE);
    }

    public void likePhoto(User user, Integer photoId) {
        Like like = new Like(null, user.getId(), photoId, LikeType.LIKE);
        if (likeRepository.userLikedPhoto(user.getId(), photoId, LikeType.LIKE).isPresent()) {
            return;
        }
        Optional<Like> dislike = likeRepository.userLikedPhoto(user.getId(), photoId, LikeType.DISLIKE);
        if (dislike.isPresent()) {
            like = dislike.get();
            like.setType(LikeType.LIKE);
        }
        likeRepository.save(like);
    }

    public void dislikePhoto(User user, Integer photoId) {
        Like dislike = new Like(null, user.getId(), photoId, LikeType.DISLIKE);
        if (likeRepository.userLikedPhoto(user.getId(), photoId, LikeType.DISLIKE).isPresent()) {
            return;
        }
        Optional<Like> like = likeRepository.userLikedPhoto(user.getId(), photoId, LikeType.LIKE);
        if (like.isPresent()) {
            dislike = like.get();
            dislike.setType(LikeType.DISLIKE);
        }
        likeRepository.save(dislike);
    }

    public List<Comment> getCommentsForPhoto(Photo photo) {
        return commentRepository.findAllByPhotoId(photo.getId());
    }

    public void postComment(User user, Integer photoId, String content) throws RuntimeException {
        if (photoRepository.findById(photoId).isEmpty()) {
            throw new RuntimeException("Photo not found");
        }
        Comment comment = new Comment(user.getId(), photoId, content);
        commentRepository.save(comment);
    }

    public void deleteComment(User user, Integer commentId) throws RuntimeException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent() && comment.get().getUserId().equals(user.getId())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new RuntimeException("User does not own the comment");
        }
    }
}
