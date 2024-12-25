package com.m41den.coffeeplex.repository;

import com.m41den.coffeeplex.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Integer id);
    List<Comment> findAllByPhotoId(Integer photoId);
    List<Comment> findAll();
    void deleteById(Integer id);
    void update(Comment comment);
}
