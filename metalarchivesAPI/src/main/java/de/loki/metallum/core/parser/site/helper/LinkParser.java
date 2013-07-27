package de.loki.metallum.core.parser.site.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Link;
import de.loki.metallum.enums.LinkCategory;

public class LinkParser {

	public final static int	LABEL_PARSER	= 0;
	public final static int	MEMBER_PARSER	= 1;
	private final Document	document;

	public LinkParser(final long id, final int parseMode) throws ExecutionException {
		String html;
		switch (parseMode) {
			case LABEL_PARSER:
				html = Downloader.getHTML(MetallumURL.assembleLabelLinkURL(id));
				break;
			case MEMBER_PARSER:
				html = Downloader.getHTML(MetallumURL.assembleMemberLinksURL(id));
				break;
			default:
				html = "";
				throw new IllegalArgumentException("Wrong parse mode! - use the constants");
		}
		this.document = Jsoup.parse(html);
	}

	public Link[] parse() {
		final List<Link> out = new ArrayList<Link>();
		Element tableElement = this.document.getElementById("linksTablemain");
		LinkCategory actualLinkCategory = null;
		Elements tdElements = tableElement.getElementsByTag("td");
		for (Element element : tdElements) {
			if (element.parent().id().contains("header_")) {
				actualLinkCategory = LinkCategory.getLinkCategoryForString(element.parent().id().replaceAll("header_", ""));
			} else {
				final Link link = new Link();
				link.setCategory(actualLinkCategory);
				link.setName(element.text());
				link.setURL(element.child(0).attr("href"));
				out.add(link);
			}
		}
		Link[] linkArr = new Link[out.size()];
		return out.toArray(linkArr);
	}

}
