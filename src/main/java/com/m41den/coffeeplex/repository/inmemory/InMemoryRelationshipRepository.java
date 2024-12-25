package com.m41den.coffeeplex.repository.inmemory;

import com.m41den.coffeeplex.model.Relationship;
import com.m41den.coffeeplex.repository.RelationshipRepository;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryRelationshipRepository implements RelationshipRepository, Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, Relationship> relationshipStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @Override
    public Relationship save(Relationship relationship) {
        if (relationship.getId() == null) {
            relationship.setId(idGenerator.incrementAndGet());
        }
        relationshipStore.put(relationship.getId(), relationship);
        return relationship;
    }

    @Override
    public Optional<Relationship> findById(Integer id) {
        return Optional.ofNullable(relationshipStore.get(id));
    }

    @Override
    public List<Relationship> findAll() {
        return new ArrayList<>(relationshipStore.values());
    }

    @Override
    public List<Relationship> findByRequestId(Integer requestId) {
        return relationshipStore.values().stream()
                .filter(relationship -> Objects.equals(relationship.getRequestId(), requestId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Relationship> findByTargetId(Integer targetId) {
        return relationshipStore.values().stream()
                .filter(relationship -> Objects.equals(relationship.getTargetId(), targetId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Relationship> findByPairId(Integer userId, Integer targetId) {
        return relationshipStore.values().stream()
                .filter(relationship -> Objects.equals(relationship.getTargetId(), targetId))
                .filter(relationship -> Objects.equals(relationship.getRequestId(), userId))
                .findFirst();
    }

    @Override
    public void deleteById(Integer id) {
        relationshipStore.remove(id);
    }

    @Override
    public void update(Relationship relationship) {
        if (relationship.getId() != null && relationshipStore.containsKey(relationship.getId())) {
            relationshipStore.put(relationship.getId(), relationship);
        }
    }
}
