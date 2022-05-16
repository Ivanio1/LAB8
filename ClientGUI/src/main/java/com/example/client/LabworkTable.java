package com.example.client;

import app.collection.*;

public class LabworkTable {
    private Integer id;
    private String name;
    private long x;
    private long y;
    private String creationDate;
    private double minimalPoint;
    private Difficulty difficulty;
    private String PersonName;
    private Color eyeColor;
    private Country country;
    private String PersonBirth;
    private String Owner;

    LabworkTable(Integer id, String owner, String name, long x, long y, String creationDate, Double minimalPoint, Difficulty difficulty, String persname, String dateOfBirthday, Color eyeColor, Country nat) {
        this.id = id;
        this.Owner = owner;
        this.name = name;
        this.x = x;
        this.y = y;
        this.creationDate = creationDate;
        this.minimalPoint=minimalPoint;
        this.PersonName=persname;
        this.country=nat;
        this.eyeColor=eyeColor;
        this.PersonBirth = dateOfBirthday;
        this.difficulty=difficulty;
    }

    public long getY() {
        return y;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public void setY(long y) {
        this.y = y;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public double getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(double minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }



    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }



    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        this.Owner = owner;
    }

    public String getPersonBirth() {
        return PersonBirth;
    }

    public String getPersonName() {
        return PersonName;
    }
}
