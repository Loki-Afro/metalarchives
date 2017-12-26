package com.github.loki.afro.metallum.core.parser.site.helper.band;

import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.DiscType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class DiscParser {

    private static final Logger logger = LoggerFactory.getLogger(DiscParser.class);
    private final Document doc;
    private final long bandId;

    public DiscParser(final long bandId) throws ExecutionException {
        this.bandId = bandId;
        this.doc = Jsoup.parse(Downloader.getHTML(MetallumURL.assembleDiscographyURL(this.bandId)));
    }

    public final Disc[] parse() {
        List<Disc> discography = new LinkedList<>();
        Elements rows = this.doc.getElementsByTag("tr");
        try {
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.getElementsByTag("td");
                Disc disc = new Disc();
                disc.setId(parseDiscId(cols.first()));
                disc.setName(cols.first().text());
                disc.setDiscType(parseDiscType(cols.get(1)));
                disc.setReleaseDate(cols.get(2).text());
                disc.setHasReview(parseReview(cols.get(3)));
                discography.add(disc);
            }
        } catch (NoDiscAvailableException e) {
            logger.debug("Band {} has no Discography", bandId);
        }
        return discography.toArray(new Disc[discography.size()]);
    }

    private final boolean parseReview(final Element element) {
        String content = element.text();
        content = MetallumUtil.trimNoBreakSpaces(content);
        return !content.isEmpty();
    }

    private final long parseDiscId(final Element element) throws NoDiscAvailableException {
        Element linkElem = element.getElementsByTag("a").first();
        if (linkElem == null) {
            throw new NoDiscAvailableException();
        }
        String link = linkElem.attr("href");
        String id = link.substring(link.lastIndexOf("/") + 1, link.length());
        return Long.parseLong(id);
    }

    private final DiscType parseDiscType(final Element element) {
        final String discType = element.text();
        return DiscType.getTypeDiscTypeForString(discType);
    }

    private static class NoDiscAvailableException extends Exception {

    }
}
