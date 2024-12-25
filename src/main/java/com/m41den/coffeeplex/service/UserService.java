package com.m41den.coffeeplex.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.m41den.coffeeplex.model.*;
import com.m41den.coffeeplex.repository.RelationshipRepository;
import com.m41den.coffeeplex.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private UserRepository userRepository;
    private RelationshipRepository relationshipRepository;

    public UserService(RelationshipRepository relationshipRepository, UserRepository userRepository) {
        this.relationshipRepository = relationshipRepository;
        this.userRepository = userRepository;
    }

    public void registerUser(String username, String password, Integer genderId, Integer age, String zodiacSign, String bio) throws RuntimeException {
        String passHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (bio.length() > 1000) {
            throw new RuntimeException("Bio is too long");
        }
        Gender gender = Gender.values()[genderId]; // :3
        User user = new User(username, passHash, gender, age, ZodiacSign.findByName(zodiacSign), bio);
        this.userRepository.save(user);
    }

    public User loginUser(String username, String password) throws RuntimeException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }
        User user = userOpt.get();
        if (!BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash()).verified) {
            throw new RuntimeException("Invalid username or password");
        }
        return user;
    }

    // region Relationships

    public void sendFriendRequest(User user, Integer targetId) throws RuntimeException {
        Optional<User> targetUserOpt = userRepository.findById(targetId);
        if (targetUserOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User targetUser = targetUserOpt.get();
        if (user.getId().equals(targetUser.getId())) {
            throw new RuntimeException("Cannot send friend request to yourself");
        }
        Optional<Relationship> existingRelationshipOpt = relationshipRepository.findByPairId(user.getId(), targetUser.getId());
        if (existingRelationshipOpt.isPresent()) {
            throw new RuntimeException("Friend request already sent");
        }
        Optional<Relationship> incomingRequest = relationshipRepository.findByPairId(targetUser.getId(), user.getId());
        if (incomingRequest.isPresent()) {
            // User sent us a friend request, accept it
            incomingRequest.get().setType(RelationshipType.FRIENDSHIP);
            relationshipRepository.update(incomingRequest.get());
            return;
        }
        Relationship relationship = new Relationship(user.getId(), targetUser.getId(), RelationshipType.FRIEND_REQUEST);
        relationshipRepository.save(relationship);
    }

    public void revokeFriendRequest(User user, Integer targetId) throws RuntimeException {
        Optional<User> targetUserOpt = userRepository.findById(targetId);
        if (targetUserOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User targetUser = targetUserOpt.get();
        if (user.getId().equals(targetUser.getId())) {
            throw new RuntimeException("Cannot revoke friend request to yourself");
        }
        Optional<Relationship> relationship = relationshipRepository.findByPairId(user.getId(), targetUser.getId());
        if (relationship.isEmpty()) {
            throw new RuntimeException("No friend request found");
        }
        if (relationship.get().getType()!= RelationshipType.FRIEND_REQUEST) {
            throw new RuntimeException("This is not a friend request");
        }
        relationshipRepository.deleteById(relationship.get().getId());
    }

    public void blockUser(User user, Integer targetId) throws RuntimeException {
        Optional<User> targetUserOpt = userRepository.findById(targetId);
        if (targetUserOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User targetUser = targetUserOpt.get();
        if (user.getId().equals(targetUser.getId())) {
            throw new RuntimeException("Cannot block yourself");
        }
        List<Relationship> relationships = relationshipRepository.findByRequestId(user.getId());
        relationships.addAll(relationshipRepository.findByRequestId(targetUser.getId()));
        Relationship blockRelationship = new Relationship(user.getId(), targetUser.getId(), RelationshipType.BLACKLIST);
        for (Relationship relationship : relationships) {
            if (relationship.getType() != RelationshipType.BLACKLIST) {
                relationshipRepository.deleteById(relationship.getId());
            } else {
                if (relationship.getTargetId().equals(targetUser.getId())) {
                    blockRelationship = relationship;
                }
            }
        }
        if (blockRelationship.getId()==null) {
            relationshipRepository.save(blockRelationship);
        } else {
            relationshipRepository.update(blockRelationship);
        }
    }

    public void unblockUser(User user, Integer targetId) throws RuntimeException {
        Optional<User> targetUserOpt = userRepository.findById(targetId);
        if (targetUserOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User targetUser = targetUserOpt.get();
        if (user.getId().equals(targetUser.getId())) {
            throw new RuntimeException("Cannot unblock yourself");
        }
        Optional<Relationship> relationship = relationshipRepository.findByPairId(user.getId(), targetUser.getId());
        if (relationship.isEmpty()) {
            relationship = relationshipRepository.findByPairId(targetUser.getId(), user.getId());
        }
        if (relationship.isEmpty()) {
            throw new RuntimeException("No relationship found");
        }
        if (relationship.get().getType() != RelationshipType.BLACKLIST) {
            throw new RuntimeException("This is not a blacklist relationship");
        }
        relationshipRepository.deleteById(relationship.get().getId());
    }

    public RelationshipType getRelationshipType(User user, Integer targetId) throws RuntimeException {
        Optional<Relationship> relationship = relationshipRepository.findByPairId(user.getId(), targetId);
        if (relationship.isEmpty()) {
            // Hm, no way
            relationship = relationshipRepository.findByPairId(targetId, user.getId());
        }
        if (relationship.isEmpty()) {
            throw new RuntimeException("No relationship found");
        }
        return relationship.get().getType();
    }

    public RelationshipType getRelationshipTypeStrict(User user, Integer targetId) throws RuntimeException {
        Optional<Relationship> relationship = relationshipRepository.findByPairId(user.getId(), targetId);
        if (relationship.isEmpty()) {
            // Hm, no way
            relationship = relationshipRepository.findByPairId(targetId, user.getId());
        }
        return relationship.get().getType();
    }

    // endregion

    // region Profiles

    public User getUserProfile(String username) throws RuntimeException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }

    public User getUserById(Integer userId) throws RuntimeException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }
    // endregion
}
