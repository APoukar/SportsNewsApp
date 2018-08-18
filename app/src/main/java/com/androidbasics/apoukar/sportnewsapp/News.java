package com.androidbasics.apoukar.sportnewsapp;

import java.util.Date;

public class News {

    private String title;
    private String section;
    private String published;
    private String author;
    private String url;

    public News(String title, String section, String published, String author, String url) {
        this.title = title;
        this.section = section;
        this.published = published;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getPublished() {
        return published;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}
