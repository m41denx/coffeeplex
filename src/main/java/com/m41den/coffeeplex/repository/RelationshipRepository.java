package com.m41den.coffeeplex.repository;

import com.m41den.coffeeplex.model.Relationship;

import java.util.List;
import java.util.Optional;

public interface RelationshipRepository {
    Relationship save(Relationship relationship);
    Optional<Relationship> findById(Integer id);
    List<Relationship> findAll();
    List<Relationship> findByRequestId(Integer requestId);
    List<Relationship> findByTargetId(Integer targetId);
    Optional<Relationship> findByPairId(Integer userId, Integer targetId);
    void deleteById(Integer id);
    void update(Relationship relationship);
}
