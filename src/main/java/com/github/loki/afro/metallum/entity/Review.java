package com.github.loki.afro.metallum.entity;

public class Review extends AbstractEntity {

    private String author;
    private int percent;
    private String content;
    private String date;
    private Disc discFromReview;

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

    /**
     * @return the author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return this.date;
    }

    public void setDisc(final Disc disc) {
        this.discFromReview = disc;
    }

    public Disc getDisc() {
        return this.discFromReview;
    }

}
