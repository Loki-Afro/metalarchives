package de.loki.metallum.core.parser.site.helper;

import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Link;
import de.loki.metallum.enums.LinkCategory;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LinkParser {

	private static      Logger logger        = LoggerFactory.getLogger(LinkParser.class);
	public final static int    LABEL_PARSER  = 0;
	public final static int    MEMBER_PARSER = 1;
	private final String html;

	public LinkParser(final long id, final int parseMode) throws ExecutionException {
		switch (parseMode) {
			case LABEL_PARSER:
				this.html = Downloader.getHTML(MetallumURL.assembleLabelLinkURL(id));
				break;
			case MEMBER_PARSER:
				this.html = Downloader.getHTML(MetallumURL.assembleMemberLinksURL(id));
				break;
			default:
				logger.error("wrong parse mode!");
				this.html = "";
		}
	}

	public Link[] parse() {
		final String[] linkHtml = this.html.split("id=\"header_");
		final List<Link> linkList = new ArrayList<Link>();
		for (int i = 1; i < linkHtml.length; i++) {
			final LinkCategory category = parseLinkCategory(linkHtml[i]);
			final String[] catLinks = linkHtml[i].split("id=\"link[\\d]");
			for (int j = 1; j < catLinks.length; j++) {
				final Link link = new Link();
				link.setCategory(category);
				link.setName(parseLinkName(catLinks[j]));
				link.setURL(parseLinkURL(catLinks[j]));
				linkList.add(link);
			}
		}
		return linkListAsArray(linkList);
	}

	private Link[] linkListAsArray(final List<Link> linkList) {
		Link[] linkArr = new Link[linkList.size()];
		return linkList.toArray(linkArr);
	}

	private LinkCategory parseLinkCategory(final String html) {
		String cat = html.substring(0, html.indexOf("\">"));
		cat = cat.replaceAll("_", " ");
		return LinkCategory.getLinkCategoryForString(cat);
	}

	private String parseLinkURL(final String html) {
		String linkURL = prepareHtml(html);
		linkURL = linkURL.substring(linkURL.indexOf("href=\"") + 6, linkURL.length());
		linkURL = linkURL.substring(0, linkURL.indexOf("\" "));
		return linkURL;
	}

	private String parseLinkName(final String html) {
		String linkName = prepareHtml(html);
		linkName = Jsoup.parse(linkName).text();
		return linkName;
	}

	private String prepareHtml(final String html) {
		return "<a " + html.substring(html.indexOf("href"), html.length());
	}
}
