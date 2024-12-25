package com.m41den.coffeeplex.model;

import java.io.Serializable;

public enum ZodiacSign implements Serializable {
    ARIES("aries"),
    TAURUS("taurus"),
    GEMINI("gemini"),
    CANCER("cancer"),
    LEO("leo"),
    VIRGO("virgo"),
    LIBRA("libra"),
    SCORPIO("scorpio"),
    SAGITTARIUS("sagittarius"),
    CAPRICORN("capricorn"),
    AQUARIUS("aquarius"),
    PISCES("pisces");

    private String named;
    ZodiacSign(String named) {
        this.named = named;
    }

    public String getNamed() {
        return named;
    }

    public static ZodiacSign findByName(String name) {
        for (ZodiacSign sign : values()) {
            if (sign.getNamed().equalsIgnoreCase(name)) {
                return sign;
            }
        }
        return null;
    }
}
