package com.github.loki.afro.metallum.core.parser.site.helper.band;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.DiscType;
import com.google.common.base.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public final class DiscParser {

    private static final Logger logger = LoggerFactory.getLogger(DiscParser.class);
    private final Document doc;
    private final long bandId;

    public DiscParser(final long bandId) {
        this.bandId = bandId;
        this.doc = Jsoup.parse(Downloader.getHTML(MetallumURL.assembleDiscographyURL(this.bandId)));
    }

    public final List<Band.PartialDisc> parse() {
        List<Band.PartialDisc> discography = new LinkedList<>();
        Elements rows = this.doc.getElementsByTag("tbody").first().getElementsByTag("tr");
        try {
            for (Element row : rows) {
                Elements cols = row.getElementsByTag("td");

                long discId = parseDiscId(cols.first());
                String name = cols.first().text();
                DiscType discType = parseDiscType(cols.get(1));
                int releaseYear = Integer.parseInt(cols.get(2).text());

                String text = cols.get(3).text();
                int count = 0;
                Integer averagePercentage = null;
                if (!Strings.isNullOrEmpty(text)) {
                    String[] thirdColumn = text.split(" ");
                    count = Integer.parseInt(thirdColumn[0]);
                    averagePercentage = Integer.parseInt(thirdColumn[1].replaceAll("[^\\d.]", ""));
                }
                discography.add(new Band.PartialDisc(discId, name, discType, releaseYear, count, averagePercentage));
            }
        } catch (NoDiscAvailableException e) {
            logger.debug("Band {} has no Discography", bandId);
        }
        return discography;
    }

    private final long parseDiscId(final Element element) throws NoDiscAvailableException {
        Element linkElem = element.getElementsByTag("a").first();
        if (linkElem == null) {
            throw new NoDiscAvailableException();
        }
        String link = linkElem.attr("href");
        String id = link.substring(link.lastIndexOf("/") + 1);
        return Long.parseLong(id);
    }

    private final DiscType parseDiscType(final Element element) {
        final String discType = element.text();
        return DiscType.getTypeDiscTypeForString(discType);
    }

    private static class NoDiscAvailableException extends Exception {

    }
}
