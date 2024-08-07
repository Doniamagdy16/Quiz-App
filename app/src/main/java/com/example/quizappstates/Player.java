package com.example.quizappstates;

import java.io.Serializable;

public class Player implements Serializable {
    public Player(String name, byte score) {
        this.name = name;
        this.score = score;
    }

    public Player() {
    }

    private String name;
    private byte score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getScore() {
        return score;
    }

    public void setScore(byte score) {
        this.score = score;
    }
}
