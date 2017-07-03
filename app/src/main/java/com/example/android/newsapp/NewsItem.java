package com.example.android.newsapp;


import java.util.ArrayList;

public class NewsItem {

    /**
     * Private variables to hold the information of 1 NewsItem.
     */
    private String headLine;
    private String author;
    private String infoLink;
    private String section;

    /**
     * Constructor to create a new NewsItem Object.
     */

    public NewsItem(String headLine, String author, String infoLink, String section) {
        this.headLine = headLine;
        this.author = author;
        this.infoLink = infoLink;
        this.section = section;
    }

    /**
     * Getter methods.
     */
    public String getHeadLine() {
        return headLine;
    }

    public String getAuthor() {
        return author;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public String getSection() {
        return section;
    }
}