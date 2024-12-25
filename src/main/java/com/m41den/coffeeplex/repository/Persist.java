package com.m41den.coffeeplex.repository;

import java.io.Serializable;

public class Persist implements Serializable {
    private static final long serialVersionUID = 1L;

    public CommentRepository commentRepository;
    public LikeRepository likeRepository;
    public PhotoRepository photoRepository;
    public RelationshipRepository relationshipRepository;
    public UserRepository userRepository;

    public Persist(
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            PhotoRepository photoRepository,
            RelationshipRepository relationshipRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.photoRepository = photoRepository;
        this.relationshipRepository = relationshipRepository;
        this.userRepository = userRepository;
    }
}
