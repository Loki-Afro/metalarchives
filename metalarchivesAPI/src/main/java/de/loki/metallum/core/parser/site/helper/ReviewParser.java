package de.loki.metallum.core.parser.site.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Review;

public class ReviewParser {
	private final String	html;

	public ReviewParser(final long discId) throws ExecutionException {
		this.html = Downloader.getHTML(MetallumURL.assembleReviewsURL(discId));
	}

	public List<Review> parse() {
		final String[] reviewHtmlArray = this.html.split("class=\"reviewBox\"");
		final List<Review> reviews = new ArrayList<Review>();
		for (int i = 1; i < reviewHtmlArray.length; i++) {
			final Review review = new Review();
			review.setName(parseTitle(reviewHtmlArray[i]));
			review.setPercent(parseRecentage(reviewHtmlArray[i]));
			review.setContent(parseBody(reviewHtmlArray[i]));
			review.setAuthor(parseAuthor(reviewHtmlArray[i]));
			review.setDate(parseDate(reviewHtmlArray[i]));
			reviews.add(review);
		}
		return reviews;
	}

	private String parseDate(final String html) {
		String date = html.substring(html.indexOf("class=\"profileMenu\""));
		date = date.substring(date.indexOf("</a>, ") + 6, date.indexOf("</div>") - 1);
		return MetallumUtil.getMetallumDate(date).toString();
	}

	private String parseTitle(final String html) {
		String title = html.substring(html.indexOf("<h3 class=\"reviewTitle\">") + 24);
		title = title.substring(0, title.indexOf("</h3>")).trim();
		title = title.substring(0, title.lastIndexOf("-") - 1);
		return title;
	}

	private int parseRecentage(final String html) {
		String percentage = html.substring(html.indexOf("<h3 class=\"reviewTitle\">") + 24);
		percentage = percentage.substring(0, percentage.indexOf("</h3>")).trim();
		percentage = percentage.substring(percentage.lastIndexOf("-") + 2);
		percentage = percentage.substring(0, percentage.length() - 1);
		return Integer.parseInt(percentage);
	}

	private String parseBody(final String html) {
		String body = html.substring(html.indexOf("<p id=\"reviewText"));
		body = body.substring(body.indexOf(">") + 1, body.indexOf("</p>")).trim();
		body = MetallumUtil.parseHtmlWithLineSeperators(body);
		return body;
	}

	private String parseAuthor(final String html) {
		String user = html.substring(html.indexOf("class=\"profileMenu\""));
		user = user.substring(user.indexOf(">") + 1, user.indexOf("</a>"));
		return user;

	}
}
