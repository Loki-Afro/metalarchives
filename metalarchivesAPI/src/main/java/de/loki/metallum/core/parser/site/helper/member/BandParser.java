package de.loki.metallum.core.parser.site.helper.member;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Disc;

public class BandParser {

	private static Logger	logger	= Logger.getLogger(BandParser.class);

	public enum Mode {
		ACTIVE, GUEST, PAST, MISC, LIVE
	}

	public Map<Band, Map<Disc, String>> parse(final String html, final Mode mode) {
		String htmlPart = null;
		switch (mode) {
			case ACTIVE:
				htmlPart = html.substring(html.indexOf("<div id=\"artist_tab_active") + 25);
				break;
			case GUEST:
				htmlPart = html.substring(html.indexOf("<div id=\"artist_tab_guest") + 26);
				break;
			case PAST:
				htmlPart = html.substring(html.indexOf("<div id=\"artist_tab_past") + 24);
				break;
			case MISC:
				htmlPart = html.substring(html.indexOf("<div id=\"artist_tab_misc") + 24);
				break;
			case LIVE:
				htmlPart = html.substring(html.indexOf("<div id=\"artist_tab_live") + 24);
				break;
			default:
				throw new IllegalStateException("Not a valid Mode!");
		}
		int indexOfDeath = htmlPart.indexOf("<div id=\"artist_tab_");
		if (indexOfDeath == -1) {
			indexOfDeath = htmlPart.length();
		}
		htmlPart = htmlPart.substring(0, indexOfDeath);
		return parseIntern(htmlPart.split("<div class=\"member_in_band"));
	}

	private Map<Band, Map<Disc, String>> parseIntern(final String[] html) {
		final Map<Band, Map<Disc, String>> returnMap = new LinkedHashMap<Band, Map<Disc, String>>();
		for (int i = 1; i < html.length; i++) {
			final Band band = parseBand(html[i]);
			final Map<Disc, String> albumMap = new LinkedHashMap<Disc, String>();

			final String[] roleinBand = html[i].split("id=\"memberInAlbum");
			for (int j = 1; j < roleinBand.length; j++) {
				albumMap.put(parseDisc(roleinBand[j], band), parseDiscComment(roleinBand[j]));
			}
			returnMap.put(band, albumMap);
		}
		return returnMap;
	}

	private Band parseBand(final String html) {
		final Band band = new Band(parseBandId(html));
		band.setName(parseBandName(html));
		return band;
	}

	private String parseBandName(final String html) {
		final String titleString = " title=\"";
		String bandName = null;
		if (html.contains(titleString)) {
			bandName = html.substring(html.indexOf(titleString) + titleString.length(), html.length());
			bandName = bandName.substring(0, bandName.indexOf("\">"));
		} else {
			bandName = html.substring(html.indexOf("class=\"member_in_band_name\">") + 28, html.length());
			bandName = bandName.substring(0, bandName.indexOf("</h3>"));
		}
		return bandName;
	}

	private long parseBandId(final String html) {
//		It is possible that this band isn't in the metal archives
		try {
			String bandId = "";
			if (html.contains("memberInBand_l_")) {
				bandId = html.substring(html.indexOf("memberInBand_l_") + 15, html.indexOf("\">"));
			} else {
				bandId = html.substring(html.indexOf("memberInBand_") + 13, html.indexOf("\">"));
			}
			return Long.parseLong(bandId);
		} catch (NumberFormatException numE) {
			logger.warn("Band is not in Ecyclopedia Metallum, html: " + html, numE);
			return 0;
		}
	}

	private Disc parseDisc(final String html, final Band bandFromDisc) {
		final Disc disc = new Disc(parseDiscId(html));
		disc.setName(parseDiscName(html));
		disc.setReleaseDate(parseDiscYear(html));
		disc.setBand(bandFromDisc);
		return disc;
	}

	private String parseDiscYear(final String html) {
		String year = html.substring(0, html.indexOf("</td>"));
		year = year.substring(year.lastIndexOf(">") + 1, year.length());
		return year;
	}

	private String parseDiscName(final String html) {
		String discName = html.substring(html.indexOf(" title=\"") + 8, html.length());
		discName = discName.substring(0, discName.indexOf("\">"));
		return discName;
	}

	private Long parseDiscId(final String html) {
		final String discId = html.substring(1, html.indexOf("\" class"));
		return Long.parseLong(discId);
	}

	private String parseDiscComment(final String html) {
		String comment = html.substring(html.indexOf("<td>") + 4, html.lastIndexOf("</td>"));
		comment = MetallumUtil.trimNoBreakSpaces(comment);
		return comment;
	}
}
