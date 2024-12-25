package com.m41den.coffeeplex.controller.servlets.photos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m41den.coffeeplex.repository.Persist;
import com.m41den.coffeeplex.service.CommentService;
import com.m41den.coffeeplex.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/photos/post")
public class post extends HttpServlet {
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
            var caption = body.get("caption").toString();
            //base64 to byte[]
            var data = body.get("data").toString();
            var photo = Base64.getDecoder().decode(data);
            commentService.postPhoto(currentUser, photo, caption);
            Map<String, Object> map = new HashMap<>();
            map.put("status", "success");
            resp.getWriter().print(om.writeValueAsString(map));
        } catch (RuntimeException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", "error");
            map.put("message", e.getMessage());
            resp.getWriter().print(om.writeValueAsString(map));
        }
    }
}
