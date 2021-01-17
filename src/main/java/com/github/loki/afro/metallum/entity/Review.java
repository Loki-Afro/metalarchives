package com.github.loki.afro.metallum.entity;

public class Review extends AbstractIdentifiable {

    private String author;
    private int percent;
    private String date;
    private String content;

    public Review(long id, String name) {
        super(id, name);
    }

    public void setPercent(final int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return this.percent;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDate() {
        return this.date;
    }

}
