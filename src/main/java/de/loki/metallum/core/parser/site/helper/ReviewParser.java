package de.loki.metallum.core.parser.site.helper;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Review;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class ReviewParser {
	private final Document doc;

	public ReviewParser(final long discId) throws ExecutionException {
		String html = Downloader.getHTML(MetallumURL.assembleReviewsURL(discId));
		this.doc = Jsoup.parse(html);
	}

	public List<Review> parse() {
		final List<Review> reviews = new ArrayList<Review>();
		Elements elements = this.doc.select("div[class=reviewBox]");
		for (Element element : elements) {
			final Review review = new Review();
			review.setId(parseReviewId(element));
			review.setName(parseTitle(element));
			review.setPercent(parseRecentage(element));
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
		String date = elem.getElementsByClass("reviewTitle").first().nextElementSibling().text();
		date = date.substring(date.indexOf(", ") + 2);
		return MetallumUtil.getMetallumDate(date).toString();
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
			String title = elements.get(0).text();
			return title;
		}
		return "";
	}

	private int parseRecentage(final Element elem) {
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
		return MetallumUtil.parseHtmlWithLineSeperators(bodyElem.html());
	}

	private String parseAuthor(final Element elem) {
		Elements elements = elem.getElementsByAttributeValue("class", "profileMenu");
		if (!elements.isEmpty()) {
			String author = elements.get(0).text();
			return author;
		}
		return "";

	}
}
