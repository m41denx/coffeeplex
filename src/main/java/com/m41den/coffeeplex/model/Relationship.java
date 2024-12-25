package com.m41den.coffeeplex.model;


import java.io.Serializable;

public class Relationship implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer requestId;
    private Integer targetId;
    private RelationshipType type;

    public Relationship(Integer id, Integer requestId, Integer targetId, RelationshipType type) {
        this.id = id;
        this.requestId = requestId;
        this.targetId = targetId;
        this.type = type;
    }

    public Relationship(Integer requestId, Integer targetId, RelationshipType type) {
        this(null, requestId, targetId, type);
    }

    public Integer getId() {
        return id;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
}
