package com.mycompany.project.database;

import java.sql.Date;

public class PlayerScore {
    private String mode;
    private String time;
    private int score;
    private Date dateCompleted;

    public PlayerScore(String mode, String time, int score, Date dateCompleted) {
        this.mode = mode;
        this.time = time;
        this.score = score;
        this.dateCompleted = dateCompleted;
    }

    // Getters và setters
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
