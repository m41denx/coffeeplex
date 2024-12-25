package com.m41den.coffeeplex.controller.servlets.profiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m41den.coffeeplex.model.Comment;
import com.m41den.coffeeplex.model.Photo;
import com.m41den.coffeeplex.model.RelationshipType;
import com.m41den.coffeeplex.model.User;
import com.m41den.coffeeplex.repository.Persist;
import com.m41den.coffeeplex.service.CommentService;
import com.m41den.coffeeplex.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/profiles/get")
public class get extends HttpServlet {
    UserService userService;
    CommentService commentService;


    @Override
    public void init() {
        var p = (Persist) getServletContext().getAttribute("ctx");
        userService = new UserService(p.relationshipRepository, p.userRepository);
        commentService = new CommentService(p.commentRepository, p.photoRepository, p.likeRepository);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var om = new ObjectMapper();
        Map<String, Object> body = om.readValue(req.getInputStream(), HashMap.class);

        try {
            var uid = Integer.parseInt(body.get("uid").toString());
            var currentUser = userService.getUserById(uid);
            var username = body.get("username").toString();

            User userProfile = userService.getUserProfile(username);
            RelationshipType relationship = null;
            if (userProfile.getId().equals(currentUser.getId())) {
                relationship = RelationshipType.FRIENDSHIP;
            } else {
                try {
                    relationship = userService.getRelationshipType(currentUser, userProfile.getId());
                } catch (RuntimeException ignored) {}
            }

            Map<String, Object> map = new HashMap<>();

            map.put("username", userProfile.getUsername());

            if (relationship == RelationshipType.BLACKLIST) {
                map.put("blacklist", true);
                resp.getWriter().print(om.writeValueAsString(map));
                return;
            }
            map.put("gender", userProfile.getGender().name());
            map.put("age", String.valueOf(userProfile.getAge()));
            map.put("zodiac", userProfile.getZodiacSign().name());
            map.put("bio", userProfile.getBio());

            RelationshipType srel = null;
            try {
                srel = userService.getRelationshipTypeStrict(currentUser, userProfile.getId());
            } catch (RuntimeException ignored) {}
            if (
                    relationship != RelationshipType.FRIENDSHIP &&
                            srel != RelationshipType.FRIEND_REQUEST
            ) {
                map.put("relationship", relationship);
                resp.getWriter().print(om.writeValueAsString(map));
                return;
            }

            List<Photo> photos = commentService.getPhotosByUser(userProfile);
            List<Object> photosList = new ArrayList<>();
            if (!photos.isEmpty()) {
                for (Photo photo : photos) {
                    Map <String, Object> photosMap = new HashMap<>();
                    System.out.println("==== ID: " + photo.getId());
                    photosMap.put("id", photo.getId());
                    if (!photo.getCaption().isEmpty()) {
                        photosMap.put("caption", photo.getCaption());
                    }
                    System.out.printf(
                            "⬆%d ⬇%d | %s\n",
                            commentService.getLikesCount(photo),
                            commentService.getDislikesCount(photo),
                            photo.getCreatedAt().toString()
                    );
                    photosMap.put("likes", commentService.getLikesCount(photo));
                    photosMap.put("dislikes", commentService.getDislikesCount(photo));
                    photosMap.put("created_at", photo.getCreatedAt().toString());
                    if (relationship != RelationshipType.FRIENDSHIP) {
                        photosList.add(photosMap);
                        continue;
                    }
                    List<Object> comments = new ArrayList<>();
                    for (Comment comment : commentService.getCommentsForPhoto(photo)) {
                        try {
                            User cuser = userService.getUserById(comment.getUserId());
                            Map<String, Object> commentMap = new HashMap<>();
                            commentMap.put("id", comment.getId());
                            commentMap.put("user", cuser.getUsername());
                            commentMap.put("content", comment.getContent());
                            comments.add(commentMap);
                        } catch (Exception ignored) {}
                    }
                    photosMap.put("comments", comments);
                    photosList.add(photosMap);
                }
            }
            map.put("photos", photosList);
            resp.getWriter().print(om.writeValueAsString(map));
        } catch (RuntimeException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", "error");
            map.put("message", e.getMessage());
            resp.getWriter().print(om.writeValueAsString(map));
            e.printStackTrace();
        }
    }
}
