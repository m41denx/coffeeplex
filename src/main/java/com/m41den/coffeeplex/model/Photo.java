package com.m41den.coffeeplex.model;

import java.io.Serializable;
import java.util.Date;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private byte[] data;
    private Date createdAt;
    private Integer ownerId;
    private String caption;

    public Photo(Integer id, byte[] data, Date createdAt, Integer ownerId, String caption) {
        this.id = id;
        this.data = data;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
        this.caption = caption;
    }

    public Photo(byte[] data, Integer ownerId, String caption) {
        this(null, data, new Date(), ownerId, caption);
    }

    public Integer getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public String getCaption() {
        return caption;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
