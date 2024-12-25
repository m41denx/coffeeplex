package com.m41den.coffeeplex.repository.inmemory;

import com.m41den.coffeeplex.model.Comment;
import com.m41den.coffeeplex.repository.CommentRepository;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryCommentRepository implements CommentRepository, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Map<Integer, Comment> commentStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(); // AUTO_INCREMENT

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(idGenerator.incrementAndGet());
        }
        commentStore.put(comment.getId(), comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(Integer id) {
        return Optional.ofNullable(commentStore.get(id));
    }

    @Override
    public List<Comment> findAllByPhotoId(Integer photoId) {
        return commentStore.values().stream()
               .filter(comment -> comment.getPhoto().equals(photoId))
               .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(commentStore.values());
    }

    @Override
    public void deleteById(Integer id) {
        commentStore.remove(id);
    }

    @Override
    public void update(Comment comment) {
        if (comment.getId() != null && commentStore.containsKey(comment.getId())) {
            commentStore.put(comment.getId(), comment);
        }
    }
}
