package com.m41den.coffeeplex.repository.mysql;

import com.m41den.coffeeplex.model.Like;
import com.m41den.coffeeplex.model.LikeType;
import com.m41den.coffeeplex.repository.LikeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlLikeRepository implements LikeRepository {
    private final Connection conn;

    public MysqlLikeRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Like save(Like like) {
        try {
            if (like.getId() != null) {
                this.update(like);
                return like;
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO likes (photoId, userId, likeType) VALUES (?,?,?)"
            );
            stmt.setInt(1, like.getPhotoId());
            stmt.setInt(2, like.getUserId());
            stmt.setInt(3, like.getType().ordinal());
            stmt.executeUpdate();
            return like;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Like> findById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM likes WHERE id=?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Like(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        LikeType.values()[rs.getInt("likeType")]
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Like> findAll() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM likes"
            );
            ResultSet rs = stmt.executeQuery();
            List<Like> likes = new ArrayList<>();
            while (rs.next()) {
                likes.add(new Like(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        LikeType.values()[rs.getInt("likeType")]
                ));
            }
            return likes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer countTypeForPhoto(Integer photoId, LikeType type) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM likes WHERE photoId=? AND likeType=?"
            );
            stmt.setInt(1, photoId);
            stmt.setInt(2, type.ordinal());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Like> findAllByUserId(Integer userId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM likes WHERE userId=?"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<Like> likes = new ArrayList<>();
            while (rs.next()) {
                likes.add(new Like(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        LikeType.values()[rs.getInt("likeType")]
                ));
            }
            return likes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Like> findAllByPhotoId(Integer photoId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM likes WHERE photoId=?"
            );
            stmt.setInt(1, photoId);
            ResultSet rs = stmt.executeQuery();
            List<Like> likes = new ArrayList<>();
            while (rs.next()) {
                likes.add(new Like(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        LikeType.values()[rs.getInt("likeType")]
                ));
            }
            return likes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Like> userLikedPhoto(Integer userId, Integer photoId, LikeType type) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM likes WHERE userId=? AND photoId=? AND likeType=?"
            );
            stmt.setInt(1, userId);
            stmt.setInt(2, photoId);
            stmt.setInt(3, type.ordinal());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Like(
                        rs.getInt("id"),
                        rs.getInt("photoId"),
                        rs.getInt("userId"),
                        LikeType.values()[rs.getInt("likeType")]
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM likes WHERE id=?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Like like) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE likes SET photoId=?, userId=?, likeType=? WHERE id=?"
            );
            stmt.setInt(1, like.getPhotoId());
            stmt.setInt(2, like.getUserId());
            stmt.setInt(3, like.getType().ordinal());
            stmt.setInt(4, like.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
