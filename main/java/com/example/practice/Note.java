package com.example.practice;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Note {
    @Id
    long id;
    public boolean status;

    public String text;

    public Note(String text) {
    }

    public Note() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
