package com.github.loki.afro.metallum.core.parser.site;

import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.AbstractEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class AbstractSiteParser<T extends AbstractEntity> {

    @Deprecated
    protected final String html;
    final Document doc;
    final boolean loadLinks;
    protected final long entityId;

    AbstractSiteParser(final long entityId, final boolean loadLinks) {
        this.entityId = entityId;
        this.loadLinks = loadLinks;
        this.html = Downloader.getHTML(getSiteURL());
        this.doc = Jsoup.parse(this.html);
    }

    public abstract T parse();

    /**
     * To download the Specific Site as HTML content, only used in the Abstract Constructor.
     *
     * @return the URL for the specific site
     */
    protected abstract String getSiteURL();

    T parseModifications(final T entity) {
        final Element footer = this.doc.getElementById("auditTrail");
        final Elements elements = footer.select("td");
        entity.setAddedBy(modificationElementToString(elements.get(0)));
        entity.setModifiedBy(modificationElementToString(elements.get(1)));
        entity.setAddedOn(modificationElementToString(elements.get(2)));
        entity.setLastModifiedOn(modificationElementToString(elements.get(3)));
        return entity;
    }

    /**
     * @return the parsed element behind the ":", N/A and (Unknown User) is also possible
     */
    private final String modificationElementToString(final Element element) {
        String text = element.text();
        text = text.replaceAll(".*?:\\s", "");
        return text;
    }

    /**
     * Convenience Method to extract an image URL from html.
     *
     * @param cssClass where the image link is in.
     */
    final String parseImageURL(final String cssClass) {
        String imageURL = this.doc.getElementsByClass(cssClass)
                .select("img")
                .attr("src");
        if ("".equals(imageURL)) {
            return null;
        } else {
            return imageURL;
        }
    }

}
