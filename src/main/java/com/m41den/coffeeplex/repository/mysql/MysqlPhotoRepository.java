package com.m41den.coffeeplex.repository.mysql;

import com.m41den.coffeeplex.model.Photo;
import com.m41den.coffeeplex.repository.PhotoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlPhotoRepository implements PhotoRepository {
    private final Connection conn;

    public MysqlPhotoRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Photo save(Photo photo) {
        try {
            if (photo.getId() != null) {
                this.update(photo);
                return photo;
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO photos (image, ownerId, caption) VALUES (?,?,?)"
            );
            stmt.setBytes(1, photo.getData());
            stmt.setInt(2, photo.getOwnerId());
            stmt.setString(3, photo.getCaption());
            stmt.executeUpdate();
            return photo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Photo> findById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM photos WHERE id=?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Photo(
                        rs.getInt("id"),
                        rs.getBytes("image"),
                        rs.getDate("createdAt"),
                        rs.getInt("ownerId"),
                        rs.getString("caption")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Photo> findAll() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM photos"
            );
            ResultSet rs = stmt.executeQuery();
            List<Photo> photos = new ArrayList<>();
            while (rs.next()) {
                photos.add(new Photo(
                        rs.getInt("id"),
                        rs.getBytes("image"),
                        rs.getDate("createdAt"),
                        rs.getInt("ownerId"),
                        rs.getString("caption")
                ));
            }
            return photos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Photo> findAllByOwnerId(Integer ownerId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM photos WHERE ownerId=?"
            );
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            List<Photo> photos = new ArrayList<>();
            while (rs.next()) {
                photos.add(new Photo(
                        rs.getInt("id"),
                        rs.getBytes("image"),
                        rs.getDate("createdAt"),
                        rs.getInt("ownerId"),
                        rs.getString("caption")
                ));
            }
            return photos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM photos WHERE id=?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Photo photo) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE photos SET image=?, caption=? WHERE id=?"
            );
            stmt.setBytes(1, photo.getData());
            stmt.setString(2, photo.getCaption());
            stmt.setInt(3, photo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
