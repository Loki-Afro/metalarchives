package com.github.loki.afro.metallum.core.parser.site.helper;

import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Review;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class ReviewParser {
    private final Document doc;

    public ReviewParser(final long discId) throws ExecutionException {
        String html = Downloader.getHTML(MetallumURL.assembleReviewsURL(discId));
        this.doc = Jsoup.parse(html);
    }

    public List<Review> parse() {
        final List<Review> reviews = new ArrayList<>();
        Elements elements = this.doc.select("div[class=reviewBox]");
        for (final Element element : elements) {
            final Review review = new Review(parseReviewId(element), parseTitle(element));
            review.setPercent(parsePercentage(element));
            review.setContent(parseBody(element));
            review.setAuthor(parseAuthor(element));
            review.setDate(parseDate(element));
            reviews.add(review);
        }
        return reviews;
    }

    private long parseReviewId(final Element elem) {
        String idStr = elem.id();
        idStr = idStr.substring(idStr.lastIndexOf("_") + 1);
        return Long.parseLong(idStr);
    }

    private String parseDate(final Element elem) {
        String dateStr = elem.getElementsByClass("reviewTitle").first().nextElementSibling().text();
        dateStr = dateStr.substring(dateStr.indexOf(", ") + 2);
        if (dateStr.contains("Written based on this version")) {
            dateStr = dateStr.replaceAll("\\sWritten based on this version.*$", "");
        }

        Date metallumDate = MetallumUtil.getMetallumDate(dateStr);
        if (metallumDate != null) {
            return metallumDate.toString();
        } else {
            return null;
        }
    }

    private String parseTitle(final Element elem) {
        String title = parseTitleIntern(elem);
        int lastIndexOfHyphen = title.lastIndexOf("-");
        if (lastIndexOfHyphen > 1) {
            title = title.substring(0, lastIndexOfHyphen - 1);
        }
        return title.trim();
    }

    private String parseTitleIntern(final Element elem) {
        Elements elements = elem.getElementsByAttributeValue("class", "reviewTitle");
        if (!elements.isEmpty()) {
            return elements.get(0).text();
        }
        return "";
    }

    private int parsePercentage(final Element elem) {
        String title = parseTitleIntern(elem);
        int lastIndexOfHyphen = title.lastIndexOf("-");
        if (lastIndexOfHyphen > 1) {
//			last sign is always a percent sign
            title = title.substring(lastIndexOfHyphen + 2, title.length() - 1).trim();
        }
        return Integer.parseInt(title);
    }

    private String parseBody(final Element elem) {
        Element bodyElem = elem.getElementById(elem.id().replaceAll("reviewBox", "reviewText"));
        return MetallumUtil.parseHtmlWithLineSeparators(bodyElem.html());
    }

    private String parseAuthor(final Element elem) {
        Elements elements = elem.getElementsByAttributeValue("class", "profileMenu");
        if (!elements.isEmpty()) {
            return elements.get(0).text();
        }
        return "";

    }
}
