package com.m41den.coffeeplex.repository.inmemory;

import com.m41den.coffeeplex.model.Photo;
import com.m41den.coffeeplex.repository.PhotoRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryPhotoRepository implements PhotoRepository, Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, Photo> photoStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @Override
    public Photo save(Photo photo) {
        if (photo.getId() == null) {
            photo.setId(idGenerator.incrementAndGet());
        }
        photoStore.put(photo.getId(), photo);
        return photo;
    }

    @Override
    public Optional<Photo> findById(Integer id) {
        return Optional.ofNullable(photoStore.get(id));
    }

    @Override
    public List<Photo> findAll() {
        return new ArrayList<>(photoStore.values());
    }

    @Override
    public List<Photo> findAllByOwnerId(Integer ownerId) {
        return photoStore.values().stream()
               .filter(photo -> photo.getOwnerId().equals(ownerId))
               .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        photoStore.remove(id);
    }

    @Override
    public void update(Photo photo) {
        if (photo.getId() != null && photoStore.containsKey(photo.getId())) {
            photoStore.put(photo.getId(), photo);
        }
    }
}
