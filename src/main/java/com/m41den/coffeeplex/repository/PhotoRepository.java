package com.m41den.coffeeplex.repository;

import com.m41den.coffeeplex.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    Photo save(Photo photo);
    Optional<Photo> findById(Integer id);
    List<Photo> findAll();
    List<Photo> findAllByOwnerId(Integer ownerId);
    void deleteById(Integer id);
    void update(Photo photo);
}
