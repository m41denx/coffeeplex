package com.m41den.coffeeplex.repository;

import com.m41den.coffeeplex.model.Like;
import com.m41den.coffeeplex.model.LikeType;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {
    Like save(Like like);
    Optional<Like> findById(Integer id);
    List<Like> findAll();
    Integer countTypeForPhoto(Integer photoId, LikeType type);
    List<Like> findAllByUserId(Integer userId);
    List<Like> findAllByPhotoId(Integer photoId);
    Optional<Like> userLikedPhoto(Integer userId, Integer photoId, LikeType type);
    void deleteById(Integer id);
    void update(Like like);
}
