package com.example.filmthusiast.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;

public class Chat {
    private String username;
    private String user_id;
    private String text;

    private LocalDateTime date ;
    private String now;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        setNow();

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setNow();
    }

    public String getNow() {
        return now;
    }

    public void setNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm MMMM dd, yyyy");
        date = LocalDateTime.now();
        now = date.format(formatter);
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setNow();
    }
}
