package com.m41den.coffeeplex.repository.mysql;

import com.m41den.coffeeplex.model.Comment;
import com.m41den.coffeeplex.repository.CommentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlCommentRepository implements CommentRepository {
    private final Connection conn;

    public MysqlCommentRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Comment save(Comment comment) {
        try {
            if (comment.getId() != null) {
                this.update(comment);
                return comment;
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO comments (photoId, userId, comment) VALUES (?,?,?)"
            );
            stmt.setInt(1, comment.getPhoto());
            stmt.setInt(2, comment.getUserId());
            stmt.setString(3, comment.getContent());
            stmt.executeUpdate();
            return comment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Comment> findById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM comments WHERE id=?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Comment(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        rs.getString("comment")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Comment> findAllByPhotoId(Integer photoId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM comments WHERE photoId=?"
            );
            stmt.setInt(1, photoId);
            ResultSet rs = stmt.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                comments.add(new Comment(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        rs.getString("comment")
                ));
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Comment> findAll() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM comments"
            );
            ResultSet rs = stmt.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                comments.add(new Comment(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        rs.getString("comment")
                ));
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM comments WHERE id=?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Comment comment) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE comments SET photoId=?, userId=?, comment=? WHERE id=?"
            );
            stmt.setInt(1, comment.getPhoto());
            stmt.setInt(2, comment.getUserId());
            stmt.setString(3, comment.getContent());
            stmt.setInt(4, comment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
