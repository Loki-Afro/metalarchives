package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.core.parser.site.helper.ReviewParser;
import com.github.loki.afro.metallum.search.query.entity.Partial;
import lombok.Getter;

import java.util.List;

public class PartialReview extends Partial {

    @Getter
    private final long discId;
    @Getter
    private final int percentage;
    @Getter
    private final String author;
    @Getter
    private final String date;

    public PartialReview(long id, long discId, String name, int percentage, String author, String date) {
        super(id, name);
        this.discId = discId;
        this.percentage = percentage;
        this.author = author;
        this.date = date;
    }

    public Review load() {
        ReviewParser reviewParser = new ReviewParser(discId, getId());
        List<Review> parse = reviewParser.parse();
        return parse.get(0);
    }


}
