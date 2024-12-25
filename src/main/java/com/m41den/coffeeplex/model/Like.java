package com.m41den.coffeeplex.model;

import java.io.Serializable;

public class Like implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;
    private Integer photoId;
    private LikeType type;

    public Like(Integer userId, Integer photoId, LikeType type) {
        this(null, userId, photoId, type);
    }

    public Like(Integer id, Integer userId, Integer photoId, LikeType type) {
        this.id = id;
        this.userId = userId;
        this.photoId = photoId;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public LikeType getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(LikeType type) {
        this.type = type;
    }
}
