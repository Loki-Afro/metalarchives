package com.github.loki.afro.metallum.core.parser.site.helper.band;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.enums.LinkCategory;

import java.util.concurrent.ExecutionException;

public class BandLinkParser {

    private final String html;

    public BandLinkParser(final long bandId) throws ExecutionException {
        this.html = Downloader.getHTML(MetallumURL.assembleBandLinkURL(bandId));
    }

    public Link[] parse() {
        String[] cats = this.html.split("<!--");
        Link[] links = new Link[cats.length - 1];

        for (int i = 1; i < cats.length; i++) {
            String cat = cats[i].substring(0, cats[i].indexOf("-->")).trim();
            String[] linksHtml = cats[i].split("<a");

            for (int x = 1; x < linksHtml.length; x++) {
                Link link = new Link();
                link.setCategory(LinkCategory.getLinkCategoryForString(cat));

                String url = linksHtml[x].substring(linksHtml[x].indexOf("href=\"") + 6);
                url = url.substring(0, url.indexOf("\""));
                link.setURL(url);

                link.setName(linksHtml[x].substring(linksHtml[x].indexOf(">") + 1, linksHtml[x].indexOf("</a>")).trim());

                links[i - 1] = link;
            }
        }
        return links;
    }
}
