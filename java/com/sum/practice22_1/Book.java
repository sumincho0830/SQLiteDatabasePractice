package com.sum.practice22_1;

import androidx.annotation.NonNull;

public class Book {
    int _id;
    String title;
    String author;
    String content;
    boolean isPaper = false;

    public Book() {

    }

    public Book(int _id, String title, String author, String content) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPaper() {
        return isPaper;
    }

    public void setPaper(boolean paper) {
        isPaper = paper;
    }

    @NonNull
    @Override
    public String toString() {
        return "Book{"+
                "title='"+title+"',"+
                "author='"+author+"',"+
                "contents='"+content+"'}";
    }
}
