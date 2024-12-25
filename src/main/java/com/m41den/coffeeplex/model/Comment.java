package com.m41den.coffeeplex.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;
    private Integer photoId;
    private String content;

    public Comment(Integer id, Integer userId, Integer photoId, String content) {
        this.id = id;
        this.userId = userId;
        this.photoId = photoId;
        this.content = content;
    }

    public Comment(Integer userId, Integer photoId, String content) {
        this(null, userId, photoId, content);
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Integer getPhoto() {
        return photoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


