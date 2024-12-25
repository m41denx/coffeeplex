package com.m41den.coffeeplex.controller;

import com.m41den.coffeeplex.repository.Persist;
import com.m41den.coffeeplex.repository.mysql.*;
import com.m41den.coffeeplex.service.CommentService;
import com.m41den.coffeeplex.service.UserService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ServletController implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // I'm literally going to cry
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://coffeeplex:kotikzevnul@db:3306/coffeeplex");
            var commentRepository = new MysqlCommentRepository(conn);
            var likeRepository = new MysqlLikeRepository(conn);
            var photoRepository = new MysqlPhotoRepository(conn);
            var relationshipRepository = new MysqlRelationshipRepository(conn);
            var userRepository = new MysqlUserRepository(conn);
            sce.getServletContext().setAttribute("ctx", new Persist(
                    commentRepository,
                    likeRepository,
                    photoRepository,
                    relationshipRepository,
                    userRepository
            ));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
