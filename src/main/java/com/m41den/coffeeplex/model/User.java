package com.m41den.coffeeplex.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String passwordHash;
    private Gender gender;
    private Integer age;
    private ZodiacSign zodiacSign;
    private String bio;

    public User(Integer id, String username, String passwordHash, Gender gender, Integer age, ZodiacSign zodiacSign, String bio) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.age = age;
        this.zodiacSign = zodiacSign;
        this.bio = bio;
    }

    public User(String username, String passwordHash, Gender gender, Integer age, ZodiacSign zodiacSign, String bio) {
        this(null, username, passwordHash, gender, age, zodiacSign, bio);
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public ZodiacSign getZodiacSign() {
        return zodiacSign;
    }

    public String getBio() {
        return bio;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
