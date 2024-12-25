package com.m41den.coffeeplex.repository.mysql;

import com.m41den.coffeeplex.model.Gender;
import com.m41den.coffeeplex.model.User;
import com.m41den.coffeeplex.model.ZodiacSign;
import com.m41den.coffeeplex.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlUserRepository implements UserRepository {
    private final Connection conn;

    public MysqlUserRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User save(User user) {
        try {
            if (user.getId() != null) {
                this.update(user);
                return user;
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (username, passwordHash, gender_i, age, zodiacSign_i, bio) VALUES (?,?,?,?,?,?)"
            );
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setInt(3, user.getGender().ordinal());
            stmt.setInt(4, user.getAge());
            stmt.setInt(5, user.getZodiacSign().ordinal());
            stmt.setString(6, user.getBio());
            stmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM users WHERE id=?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        Gender.values()[rs.getInt("gender_i")],
                        rs.getInt("age"),
                        ZodiacSign.values()[rs.getInt("zodiacSign_i")],
                        rs.getString("bio")
                ));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM users"
            );
            ResultSet rs = stmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        Gender.values()[rs.getInt("gender_i")],
                        rs.getInt("age"),
                        ZodiacSign.values()[rs.getInt("zodiacSign_i")],
                        rs.getString("bio")
                ));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM users WHERE username=?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        Gender.values()[rs.getInt("gender_i")],
                        rs.getInt("age"),
                        ZodiacSign.values()[rs.getInt("zodiacSign_i")],
                        rs.getString("bio")
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
                    "DELETE FROM users WHERE id=?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE users SET username=?, passwordHash=?, gender_i=?, age=?, zodiacSign_i=?, bio=? WHERE id=?"
            );
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setInt(3, user.getGender().ordinal());
            stmt.setInt(4, user.getAge());
            stmt.setInt(5, user.getZodiacSign().ordinal());
            stmt.setString(6, user.getBio());
            stmt.setInt(7, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
