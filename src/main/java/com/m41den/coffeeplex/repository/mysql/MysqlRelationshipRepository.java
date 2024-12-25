package com.m41den.coffeeplex.repository.mysql;

import com.m41den.coffeeplex.model.Relationship;
import com.m41den.coffeeplex.model.RelationshipType;
import com.m41den.coffeeplex.repository.RelationshipRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlRelationshipRepository implements RelationshipRepository {

    private final Connection conn;

    public MysqlRelationshipRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Relationship save(Relationship relationship) {
        try {
            if (relationship.getId() != null) {
                this.update(relationship);
                return relationship;
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO relationships (requestId, targetId, relType) VALUES (?,?,?)"
            );
            stmt.setInt(1, relationship.getRequestId());
            stmt.setInt(2, relationship.getTargetId());
            stmt.setInt(3, relationship.getType().ordinal());
            stmt.executeUpdate();
            return relationship;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Relationship> findById(Integer id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM relationships WHERE id=?"
            );
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Relationship(
                        rs.getInt("id"),
                        rs.getInt("requestId"),
                        rs.getInt("targetId"),
                        RelationshipType.values()[rs.getInt("relType")]
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Relationship> findAll() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM relationships"
            );
            ResultSet rs = stmt.executeQuery();
            List<Relationship> relationships = new ArrayList<>();
            while (rs.next()) {
                relationships.add(new Relationship(
                        rs.getInt("id"),
                        rs.getInt("requestId"),
                        rs.getInt("targetId"),
                        RelationshipType.values()[rs.getInt("relType")]
                ));
            }
            return relationships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Relationship> findByRequestId(Integer requestId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM relationships WHERE requestId=?"
            );
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            List<Relationship> relationships = new ArrayList<>();
            while (rs.next()) {
                relationships.add(new Relationship(
                        rs.getInt("id"),
                        rs.getInt("requestId"),
                        rs.getInt("targetId"),
                        RelationshipType.values()[rs.getInt("relType")]
                ));
            }
            return relationships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Relationship> findByTargetId(Integer targetId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM relationships WHERE targetId=?"
            );
            stmt.setInt(1, targetId);
            ResultSet rs = stmt.executeQuery();
            List<Relationship> relationships = new ArrayList<>();
            while (rs.next()) {
                relationships.add(new Relationship(
                        rs.getInt("id"),
                        rs.getInt("requestId"),
                        rs.getInt("targetId"),
                        RelationshipType.values()[rs.getInt("relType")]
                ));
            }
            return relationships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Relationship> findByPairId(Integer userId, Integer targetId) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM relationships WHERE (requestId=? AND targetId=?) OR (requestId=? AND targetId=?)"
            );
            stmt.setInt(1, userId);
            stmt.setInt(2, targetId);
            stmt.setInt(3, targetId);
            stmt.setInt(4, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Relationship(
                        rs.getInt("id"),
                        rs.getInt("requestId"),
                        rs.getInt("targetId"),
                        RelationshipType.values()[rs.getInt("relType")]
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
                    "DELETE FROM relationships WHERE id=?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Relationship relationship) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE relationships SET requestId=?, targetId=?, relType=? WHERE id=?"
            );
            stmt.setInt(1, relationship.getRequestId());
            stmt.setInt(2, relationship.getTargetId());
            stmt.setInt(3, relationship.getType().ordinal());
            stmt.setInt(4, relationship.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
