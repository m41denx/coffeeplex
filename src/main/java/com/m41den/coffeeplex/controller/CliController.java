package com.m41den.coffeeplex.controller;

import com.m41den.coffeeplex.model.*;
import com.m41den.coffeeplex.repository.*;
import com.m41den.coffeeplex.repository.inmemory.*;
import com.m41den.coffeeplex.repository.mysql.*;
import com.m41den.coffeeplex.service.CommentService;
import com.m41den.coffeeplex.service.UserService;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CliController {
    private final Scanner scanner = new Scanner(System.in);

    private CommentRepository commentRepository;
    private LikeRepository likeRepository;
    private PhotoRepository photoRepository;
    private RelationshipRepository relationshipRepository;
    private UserRepository userRepository;

    private UserService userService;
    private CommentService commentService;

    private User currentUser;

    public enum Backend {
        IN_MEMORY,
        SQL_DATABASE
    }

    public CliController(Backend backend, String jdbcUrl) throws SQLException {
        if (backend == Backend.IN_MEMORY) {
            commentRepository = new InMemoryCommentRepository();
            likeRepository = new InMemoryLikeRepository();
            photoRepository = new InMemoryPhotoRepository();
            relationshipRepository = new InMemoryRelationshipRepository();
            userRepository = new InMemoryUserRepository();
        } else if (backend == Backend.SQL_DATABASE) {
            Connection conn = DriverManager.getConnection(jdbcUrl);
            commentRepository = new MysqlCommentRepository(conn);
            likeRepository = new MysqlLikeRepository(conn);
            photoRepository = new MysqlPhotoRepository(conn);
            relationshipRepository = new MysqlRelationshipRepository(conn);
            userRepository = new MysqlUserRepository(conn);
        }

        userService = new UserService(relationshipRepository, userRepository);
        commentService = new CommentService(commentRepository, photoRepository, likeRepository);
    }

    public void inject(Persist persist) {
        this.commentRepository = persist.commentRepository;
        this.likeRepository = persist.likeRepository;
        this.photoRepository = persist.photoRepository;
        this.relationshipRepository = persist.relationshipRepository;
        this.userRepository = persist.userRepository;
    }

    public void dump(String path) throws IOException {
        Persist persist = new Persist(
                commentRepository,
                likeRepository,
                photoRepository,
                relationshipRepository,
                userRepository
        );
        FileOutputStream os = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(persist);
        oos.close();
    }

    public void load(String path) throws IOException, ClassNotFoundException {
        FileInputStream is = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(is);
        Persist loaded = (Persist) ois.readObject();
        ois.close();
        inject(loaded);

        userService = new UserService(relationshipRepository, userRepository);
        commentService = new CommentService(commentRepository, photoRepository, likeRepository);
    }

    public void loop() {
        help();
        while (readCommand()) {}
    }

    private boolean readCommand() {
        System.out.print("> ");
        String command = scanner.nextLine();
        switch (command.toLowerCase().trim()) {
            case "login":
                login();
                break;
            case "logout":
                logout();
                break;
            case "register":
                register();
                break;
            case "profile":
                viewUserProfile();
                break;
            case "follow":
                followUser();
                break;
            case "unfollow":
                unfollowUser();
                break;
            case "block":
                blockUser();
                break;
            case "unblock":
                unblockUser();
                break;
            case "post_photo":
                postPhoto();
                break;
            case "delete_photo":
                deletePhoto();
                break;
            case "like_photo":
                likePhoto();
                break;
            case "dislike_photo":
                dislikePhoto();
                break;
            case "comment":
                commentPhoto();
                break;
            case "delete_comment":
                deleteComment();
                break;
            case "help":
                help();
                break;
            case "exit":
                System.out.println("Kthxbye!");
                return false;
            default:
                System.out.println("Invalid command");
        }
        return true;
    }

    private boolean needAuth() {
        return currentUser == null;
    }

    // region Authentication

    private void login() {
        System.out.println("==== Login ====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            currentUser = userService.loginUser(username, password);
            System.out.println("Logged in as " + currentUser.getUsername());
        } catch (RuntimeException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out");
    }

    private void register() {
        System.out.println("==== Register ====");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Gender (M/F): ");
        String genderInput = scanner.nextLine();
        Gender gender = genderInput.equalsIgnoreCase("M")? Gender.MALE : Gender.FEMALE;
        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Fix \n
        System.out.print(
                "Zodiac sign (" +
                String.join(", ",
                        Arrays.stream(ZodiacSign.values()).map(ZodiacSign::getNamed).toArray(String[]::new)
                ) + "): "
        );
        String zodiacSign = scanner.nextLine();
        System.out.print("Bio: ");
        String bio = scanner.nextLine();

        try {
            userService.registerUser(username, password, gender.ordinal(), age, zodiacSign, bio);
            System.out.println("Registration successful");
        } catch (RuntimeException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
    // endregion

    // region Photos

    private void postPhoto() {
        if (needAuth()) {return;}
        System.out.println("==== Post a Photo ====");
        System.out.println("Image (aka ASCII art), end with \\nEOF: ");
        StringBuilder imageBuilder = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("EOF")) {
                break;
            }
            imageBuilder.append(line).append("\n");
        }
        System.out.print("Caption (optional): ");
        String caption = scanner.nextLine();

        try {
            commentService.postPhoto(currentUser, imageBuilder.toString().getBytes(), caption);
            System.out.println("Photo posted successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to post photo: " + e.getMessage());
        }
    }

    private void deletePhoto() {
        if (needAuth()) {return;}
        System.out.println("==== Delete a Photo ====");
        System.out.print("Enter the photo ID: ");
        int photoId = scanner.nextInt();
        scanner.nextLine();

        try {
            commentService.deletePhoto(currentUser, photoId);
            System.out.println("Photo deleted successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to delete photo: " + e.getMessage());
        }
    }

    private void likePhoto() {
        if (needAuth()) {return;}
        System.out.println("==== Like a Photo ====");
        System.out.print("Enter the photo ID: ");
        int photoId = scanner.nextInt();
        scanner.nextLine();

        try {
            commentService.likePhoto(currentUser, photoId);
            System.out.println("Liked photo successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to like photo: " + e.getMessage());
        }
    }

    private void dislikePhoto() {
        if (needAuth()) {return;}
        System.out.println("==== Dislike a Photo ====");
        System.out.print("Enter the photo ID: ");
        int photoId = scanner.nextInt();
        scanner.nextLine();

        try {
            commentService.dislikePhoto(currentUser, photoId);
            System.out.println("Disliked photo successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to dislike photo: " + e.getMessage());
        }
    }

    // endregion

    // region Comments
    private void commentPhoto() {
        if (needAuth()) {return;}
        System.out.println("==== Comment on a Photo ====");
        System.out.print("Enter the photo ID: ");
        int photoId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        try {
            Integer ownerId = commentService.getPhotoById(photoId).getOwnerId();
            if (
                    !currentUser.getId().equals(ownerId) &&
                    userService.getRelationshipType(currentUser, ownerId) != RelationshipType.FRIENDSHIP
            ) {
                throw new RuntimeException("You are not friends");
            }
            commentService.postComment(currentUser, photoId, comment);
            System.out.println("Comment posted successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to comment on photo: " + e.getMessage());
        }
    }

    private void deleteComment() {
        if (needAuth()) {return;}
        System.out.println("==== Delete a comment ====");
        System.out.print("Enter the comment ID: ");
        int commentId = scanner.nextInt();
        scanner.nextLine();

        try {
            commentService.deleteComment(currentUser, commentId);
            System.out.println("Comment deleted successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to delete comment: " + e.getMessage());
        }
    }
    // endregion

    // region Profiles

    private void viewUserProfile() {
        if (needAuth()) {return;}
        System.out.println("==== View User Profile ====");
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        try {
            User userProfile = userService.getUserProfile(username);
            RelationshipType relationship = null;
            if (userProfile.getId().equals(currentUser.getId())) {
                relationship = RelationshipType.FRIENDSHIP;
            } else {
                try {
                    relationship = userService.getRelationshipType(currentUser, userProfile.getId());
                } catch (RuntimeException ignored) {}
            }
            System.out.println("==== " + userProfile.getUsername() + " ====");
            if (relationship == RelationshipType.BLACKLIST) {
                return;
            }
            System.out.println("Gender: " + userProfile.getGender());
            System.out.println("Age: " + userProfile.getAge());
            System.out.println("Zodiac Sign: " + userProfile.getZodiacSign().getNamed());
            System.out.println("Bio: " + userProfile.getBio());
            RelationshipType srel = null;
            try {
                srel = userService.getRelationshipTypeStrict(currentUser, userProfile.getId());
            } catch (RuntimeException ignored) {}
            if (
                    relationship != RelationshipType.FRIENDSHIP &&
                    srel != RelationshipType.FRIEND_REQUEST
            ) {
               return;
            }
            List<Photo> photos = commentService.getPhotosByUser(userProfile);
            if (!photos.isEmpty()) {
                System.out.println("==== Photos ====");
                for (Photo photo : photos) {
                    System.out.println("==== ID: " + photo.getId());
                    System.out.println(new String(photo.getData()));
                    if (!photo.getCaption().isEmpty()) {
                        System.out.println("Caption: " + photo.getCaption());
                    }
                    System.out.printf(
                            "⬆%d ⬇%d | %s\n",
                            commentService.getLikesCount(photo),
                            commentService.getDislikesCount(photo),
                            photo.getCreatedAt().toString()
                    );
                    if (relationship != RelationshipType.FRIENDSHIP) {
                        continue;
                    }
                    for (Comment comment : commentService.getCommentsForPhoto(photo)) {
                        try {
                            User cuser = userService.getUserById(comment.getUserId());
                            System.out.printf(
                                    "[%d | %s]==> %s\n",
                                    comment.getId(),
                                    cuser.getUsername(),
                                    comment.getContent()
                            );
                        } catch (Exception ignored) {}
                    }
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Failed to view user profile: " + e.getMessage());
        }
    }

    private void followUser() {
        if (needAuth()) {return;}
        System.out.println("==== Follow a User ====");
        System.out.print("Enter the username to follow: ");
        String username = scanner.nextLine();

        try {
            userService.sendFriendRequest(currentUser, userService.getUserProfile(username).getId());
            System.out.println("Friend request sent successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to follow user: " + e.getMessage());
        }
    }

    private void unfollowUser() {
        if (needAuth()) {return;}
        System.out.println("==== Unfollow a User ====");
        System.out.print("Enter the username to unfollow: ");
        String username = scanner.nextLine();

        try {
            userService.revokeFriendRequest(currentUser, userService.getUserProfile(username).getId());
            System.out.println("Friend request revoked successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to unfollow user: " + e.getMessage());
        }
    }

    private void blockUser() {
        if (needAuth()) {return;}
        System.out.println("==== Block a User ====");
        System.out.print("Enter the username to block: ");
        String username = scanner.nextLine();

        try {
            userService.blockUser(currentUser, userService.getUserProfile(username).getId());
            System.out.println("User blocked successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to block user: " + e.getMessage());
        }
    }

    private void unblockUser() {
        if (needAuth()) {return;}
        System.out.println("==== Unblock a User ====");
        System.out.print("Enter the username to unblock: ");
        String username = scanner.nextLine();

        try {
            userService.unblockUser(currentUser, userService.getUserProfile(username).getId());
            System.out.println("User unblocked successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to unblock user: " + e.getMessage());
        }
    }

    // endregion

    private void help() {
        System.out.print("""
                Available commands:
                exit - Exit the application
                help - Display this help message
                login - Log in with a username and password
                logout - Log out
                register - Register a new user
                """);
        if (!needAuth()) {
            System.out.print("""
                profile - View a user's profile
                follow - Follow another user
                unfollow - Unfollow another user
                block - Block another user
                unblock - Unblock another user
                post_photo - Post a photo with a caption
                delete_photo - Delete a photo
                like_photo - Like a photo
                dislike_photo - Dislike a photo
                comment - Comment on a photo
                delete_comment - Delete a comment
                """);
        }
    }
}
