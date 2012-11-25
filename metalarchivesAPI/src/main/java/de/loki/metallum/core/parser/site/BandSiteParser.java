package de.loki.metallum.core.parser.site;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import de.loki.metallum.core.parser.site.helper.ReviewParser;
import de.loki.metallum.core.parser.site.helper.band.BandLinkParser;
import de.loki.metallum.core.parser.site.helper.band.DiscParser;
import de.loki.metallum.core.parser.site.helper.band.MemberParser;
import de.loki.metallum.core.parser.site.helper.band.SimilarArtistsParser;
import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.entity.Label;
import de.loki.metallum.entity.Link;
import de.loki.metallum.entity.Review;
import de.loki.metallum.enums.BandStatus;
import de.loki.metallum.enums.Country;

public class BandSiteParser extends AbstractSiteParser<Band> {
	private boolean			loadReviews			= false;
	private boolean			loadSimilarArtists	= false;
	private boolean			loadReadMore		= false;
	private static Logger	logger				= Logger.getLogger(BandSiteParser.class);

	public BandSiteParser(final Band band, final boolean loadImages, final boolean loadReviews, final boolean loadSimilarArtists, final boolean loadLinks, final boolean loadReadMore) throws ExecutionException {
		super(band, loadImages, loadLinks);
		this.loadReviews = loadReviews;
		this.loadSimilarArtists = loadSimilarArtists;
		this.loadReadMore = loadReadMore;
	}

	@Override
	public final Band parse() {
		Band band = new Band(this.entity.getId());
		band.setName(parseBandName());

		band = parseFirstHtmlPart(band);
		band = parseSecondHtmlPart(band);

		band = parseBandImages(band);

		band.setInfo(parseInfo());
		for (final Disc disc : parseDiscography()) {
			disc.setBand(band);
			band.addToDiscography(disc);
		}
		band = parseMember(band);
		band.addToReviews(parseReviews(band));
		band.setSimliarArtists(parseSimilarArtists());
		band.addLinks(parseLinks());
		band = parseModfications(band);
		return band;
	}

	private final Band parseBandImages(final Band band) {
		final String logoUrl = parseLogoUrl();
		band.setLogoUrl(logoUrl);
		band.setLogo(parseBandLogo(logoUrl));
		final String photoUrl = parsePhotoUrl();
		band.setPhotoUrl(photoUrl);
		band.setPhoto(parseBandPhoto(photoUrl));
		return band;
	}

	private String parseLogoUrl() {
		return parseImageURL(this.doc, "band_name_img");
	}

	private String parsePhotoUrl() {
		return parseImageURL(this.doc, "band_img");
	}

	private final Band parseSecondHtmlPart(final Band band) {
		final String newhtml = this.html.substring(this.html.indexOf("</dl>") + 5);
		final String[] secondPart = newhtml.substring(0, newhtml.indexOf("</dl>")).split("<dd>");

		band.setGenre(parseGenre(secondPart[1]));
		band.setLyricalThemes(parseLyricalThemes(secondPart[2]));
		band.setLabel(parseCurrentLabel(secondPart[3]));
		return band;
	}

	/**
	 * This Method set the Country Province, Status and the year.
	 * 
	 * @param band
	 * @return
	 */
	private final Band parseFirstHtmlPart(final Band band) {
		final String[] firstPart = this.html.substring(0, this.html.indexOf("</dl>")).split("<dd");
		band.setCountry(parseCountry(firstPart[1]));
		band.setProvince(parseBandProvince(firstPart[2]));
		band.setStatus(parseStatus(firstPart[3]));
		band.setYearFormedIn(parseYearOfCreation(firstPart[4]));
		return band;
	}

	private final String parseBandProvince(final String firstParthtml) {
		return firstParthtml.substring(1, firstParthtml.indexOf("</dd>"));
	}

	private final String parseBandName() {
		Element bandNameElement = this.doc.getElementsByClass("band_name").first();
		return bandNameElement.text();
	}

	private final Country parseCountry(final String firstParthtml) {
		String possibleCountry = firstParthtml.substring(firstParthtml.indexOf(">") + 1, firstParthtml.indexOf("</dd>"));
		possibleCountry = possibleCountry.substring(possibleCountry.indexOf("\">") + 2, possibleCountry.indexOf("</a>"));
		return Country.getRightCountryForString(possibleCountry);
	}

	private BandStatus parseStatus(final String firstParthtml) {
		final String possibleStatus = firstParthtml.substring(firstParthtml.indexOf(">") + 1, firstParthtml.indexOf("</dd>"));
		return BandStatus.getTypeBandStatusForString(possibleStatus);
	}

	private final int parseYearOfCreation(final String firstParthtml) {
		String possibleYear = firstParthtml.substring(firstParthtml.indexOf(">") + 1, firstParthtml.indexOf("</dd>"));
		if (possibleYear.contains("N/A")) {
			return 0;
		}
		return Integer.parseInt(possibleYear);
	}

	private final String parseGenre(final String secondHtmlPart) {
		String possibleGenre = secondHtmlPart.substring(0, secondHtmlPart.indexOf("</dd>"));
		return possibleGenre.trim();
	}

	private final String parseLyricalThemes(final String secondHtmlPart) {
		final String possibleThemes = secondHtmlPart.substring(0, secondHtmlPart.indexOf("</dd>"));
		return possibleThemes;
	}

	private final Label parseCurrentLabel(final String secondHtmlPart) {
		// id
		String labelId;
		if (!secondHtmlPart.contains("Unsigned/independent")) {
			labelId = secondHtmlPart.substring(secondHtmlPart.indexOf("/labels/") + 8);
			labelId = labelId.substring(labelId.indexOf("/") + 1, labelId.indexOf("\">"));
		} else {
			labelId = "0";
		}
		final Label label = new Label(Long.parseLong(labelId));

		// name
		final String labelName = secondHtmlPart.substring(0, secondHtmlPart.indexOf("</dd>"));
		label.setName(Jsoup.parse(labelName).text());
		return label;

	}

	private final String parseInfo() {
		final String newHtml = this.html.substring(this.html.indexOf("</dl>") + 5);
		String info = newHtml.substring(newHtml.indexOf("<div class=\"band_comment"));
		info = info.substring(info.indexOf(">") + 1);
		if (info.contains("class=\"tool_strip")) {
			info = info.substring(0, info.indexOf("class=\"tool_strip"));
			info = info.substring(0, info.lastIndexOf("<"));
		} else {
			info = info.substring(0, info.indexOf("</div>"));
		}
		info = MetallumUtil.parseHtmlWithLineSeperators(info);
		if (this.entity.getInfo().length() > info.length()) {
			return this.entity.getInfo();
		} else if (this.loadReadMore && this.html.contains("btn_read_more")) {
			try {
				final String downloadedReadMore = Downloader.getHTML(MetallumURL.assembleMoreInfoURL(this.entity.getId()));
				return MetallumUtil.parseHtmlWithLineSeperators(downloadedReadMore);
			} catch (ExecutionException e) {
				logger.error("error in parsing additional information for band: " + this.entity, e);
			}
		}
		return info;
	}

	// we do not specify complete /main/live/demo/misc -> BandGeneric
	private final Disc[] parseDiscography() {
		final List<Disc> discs = this.entity.getDiscs();
		if (discs.isEmpty()) {
			final DiscParser bsdp = new DiscParser();
			try {
				return bsdp.parse(this.entity.getId());
			} catch (final ExecutionException e) {
				logger.fatal("error in parsing the discography for band: " + this.entity, e);
			}
		}
		final Disc[] discArray = new Disc[discs.size()];
		return discs.toArray(discArray);
	}

	private final Band parseMember(final Band band) {
		final MemberParser memberParser = new MemberParser();
		memberParser.parse(this.html);
		// split by cat
		band.setCurrentLineup(memberParser.getCurrentLineup());
		band.setPastLineup(memberParser.getPastLineup());
		band.setLiveLineup(memberParser.getLiveLieup());
		band.setLastKnownLineup(memberParser.getLastKnownLineup());
		return band;
	}

	private final Review[] parseReviews(final Band band) {
		final List<Review> reviews = this.entity.getReviews();
		if (!reviews.isEmpty()) {
			final Review[] reviewArr = new Review[reviews.size()];
			return reviews.toArray(reviewArr);
		} else if (this.loadReviews) {
			final List<Review> parsedReviewList = new ArrayList<Review>();
			for (final Disc disc : band.getDiscs()) {
				try {
					final ReviewParser parser = new ReviewParser(disc.getId());
					for (final Review review : parser.parse()) {
						review.setDisc(disc);
						parsedReviewList.add(review);
					}
				} catch (final ExecutionException e) {
					logger.error("error in parsing " + Review.class + " for band: " + band, e);
				}
			}
			final Review[] reviewArr = new Review[parsedReviewList.size()];
			return parsedReviewList.toArray(reviewArr);
		} else {
			return new Review[0];
		}

	}

	private final Map<Integer, List<Band>> parseSimilarArtists() {
		final Map<Integer, List<Band>> similarArtists = this.entity.getSimilarArtists();
		if (!similarArtists.isEmpty()) {
			return similarArtists;
		} else if (this.loadSimilarArtists) {
			try {
				final SimilarArtistsParser sap = new SimilarArtistsParser(this.entity.getId());
				return sap.parse();
			} catch (final ExecutionException e) {
				logger.error("error in parsing similar Artists for band: " + this.entity, e);
			}
		}
		return new HashMap<Integer, List<Band>>();
	}

	private final Link[] parseLinks() {
		final List<Link> linkLinst = this.entity.getLinks();
		if (!linkLinst.isEmpty()) {
			return (Link[]) linkLinst.toArray();
		}
		if (this.loadLinks) {
			try {
				final BandLinkParser parser = new BandLinkParser(this.entity.getId());
				return parser.parse();
			} catch (final ExecutionException e) {
				logger.error("error in parsing " + Link.class + " for band: " + this.entity, e);
			}
		}
		return new Link[0];
	}

	/**
	 * If the previous entity, may from cache, has already the band logo,
	 * this method will return the BufferedImage of the entity, otherwise if loadImage is true
	 * this method with try to get the Image, if it is in the Metal-Archives, via the Downloader.
	 * 
	 * @return null if loadImage is false or if there is no logo.
	 */
	private final BufferedImage parseBandLogo(final String imageUrl) {
		BufferedImage imageLogo = this.entity.getLogo();
		if (imageLogo == null && this.loadImage && imageUrl != null) {
			try {
				imageLogo = Downloader.getImage((imageUrl));
			} catch (final ExecutionException e) {
				logger.error("Exception while downloading an image from \"" + imageUrl + "\" ," + this.entity, e);
			}
		}
		// with other words, null
		return imageLogo;
	}

	/**
	 * If the previous entity, may from cache, has already the band photo,
	 * this method will return the BufferedImage of the entity, otherwise if loadImage is true
	 * this method with try to get the Image, if it is in the Metal-Archives, via the Downloader.
	 * 
	 * @return null if loadImage is false or if there is no photo.
	 */
	private final BufferedImage parseBandPhoto(final String photoUrl) {
		BufferedImage imagePhoto = this.entity.getPhoto();
		if (imagePhoto == null && this.loadImage && this.html.contains("class=\"band_img\"")) {
			String possiblePhotoURL = this.html.substring(this.html.indexOf("class=\"band_img\"") + 16);
			possiblePhotoURL = possiblePhotoURL.substring(possiblePhotoURL.indexOf("src=\"") + 5);
			possiblePhotoURL = possiblePhotoURL.substring(0, possiblePhotoURL.indexOf("\""));
			try {
				imagePhoto = Downloader.getImage(possiblePhotoURL);
			} catch (ExecutionException e) {
				logger.error("Exception while downloading an image from \"" + photoUrl + "\" ," + this.entity, e);
			}
		}
		return imagePhoto;
	}

	@Override
	protected String getSiteURL() {
		return MetallumURL.assembleBandURL(this.entity.getId());
	}
}
