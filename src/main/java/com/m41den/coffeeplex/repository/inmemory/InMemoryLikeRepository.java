package com.m41den.coffeeplex.repository.inmemory;

import com.m41den.coffeeplex.model.Like;
import com.m41den.coffeeplex.model.LikeType;
import com.m41den.coffeeplex.repository.LikeRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryLikeRepository implements LikeRepository, Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, Like> likeStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @Override
    public Like save(Like like) {
        if (like.getId() == null) {
            like.setId(idGenerator.incrementAndGet());
        }
        likeStore.put(like.getId(), like);
        return like;
    }

    @Override
    public Optional<Like> findById(Integer id) {
        return Optional.ofNullable(likeStore.get(id));
    }

    @Override
    public List<Like> findAll() {
        return new ArrayList<>(likeStore.values());
    }

    @Override
    public Integer countTypeForPhoto(Integer photoId, LikeType type) {
        return (int) likeStore.values().stream() // casting wizard money gang
               .filter(l -> l.getPhotoId().equals(photoId) && l.getType().equals(type))
               .count();
    }

    @Override
    public List<Like> findAllByUserId(Integer userId) {
        return likeStore.values().stream()
               .filter(l -> l.getUserId().equals(userId))
               .collect(Collectors.toList());
    }

    @Override
    public List<Like> findAllByPhotoId(Integer photoId) {
        return likeStore.values().stream()
               .filter(l -> l.getPhotoId().equals(photoId))
               .collect(Collectors.toList());
    }

    @Override
    public Optional<Like> userLikedPhoto(Integer userId, Integer photoId, LikeType type) {
        return likeStore.values().stream()
               .filter(l -> l.getUserId().equals(userId) && l.getPhotoId().equals(photoId) && l.getType().equals(type))
               .findFirst();
    }

    @Override
    public void deleteById(Integer id) {
        likeStore.remove(id);
    }

    @Override
    public void update(Like like) {
        if (like.getId() != null && likeStore.containsKey(like.getId())) {
            likeStore.put(like.getId(), like);
        }
    }
}
