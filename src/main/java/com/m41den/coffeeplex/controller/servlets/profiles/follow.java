package com.m41den.coffeeplex.controller.servlets.profiles;

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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/profiles/follow")
public class follow extends HttpServlet {
    UserService userService;
    CommentService commentService;


    @Override
    public void init() {
        var p = (Persist) getServletContext().getAttribute("ctx");
        userService = new UserService(p.relationshipRepository, p.userRepository);
        commentService = new CommentService(p.commentRepository, p.photoRepository, p.likeRepository);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var uid = Integer.parseInt(req.getParameter("uid"));
            var username = req.getParameter("username");
            var currentUser = userService.getUserById(uid);
            userService.sendFriendRequest(currentUser, userService.getUserProfile(username).getId());
        } catch (RuntimeException e) {
            resp.sendError(404);
        }
    }
}