package de.loki.metallum.core.parser.search;

import org.json.JSONArray;
import org.json.JSONException;

import de.loki.metallum.entity.Band;

/**
 * Parses the data which was gained by the search
 * 
 * @author Zarathustra
 * 
 */
public class BandSearchParser extends AbstractSearchParser<Band> {

	private boolean	isAbleToParseProvince		= false;
	private boolean	isAbleToParseLabel			= false;
	private boolean	isAbleToParseYear			= false;
	private boolean	isAbleToParseLyricalThemes	= false;
	private boolean	isAbleToParseCountry		= false;

	public void setIsAbleToParseCountry(final boolean isAbleToParse) {
		this.isAbleToParseCountry = isAbleToParse;
	}

	public void setIsAbleToParseProvince(final boolean isAbleToParse) {
		this.isAbleToParseProvince = isAbleToParse;
	}

	public void setIsAbleToParseYear(final boolean isAbleToParse) {
		this.isAbleToParseYear = isAbleToParse;
	}

	public void setIsAbleToParseLyricalThemes(final boolean isAbleToParse) {
		this.isAbleToParseLyricalThemes = isAbleToParse;
	}

	public void setIsAbleToParseLabel(final boolean isAbleToParse) {
		this.isAbleToParseLabel = isAbleToParse;
	}

	@Override
	protected final Band useSpecificSearchParser(final JSONArray hits) throws JSONException {
		Band band = new Band(parseId(hits.getString(0)));
		band.setName(parseName(hits.getString(0)));
		band.setGenre(parseGenres(hits.getString(1)));
		band = parseOptionalFields(hits, band);
		return band;
	}

	private final Band parseOptionalFields(final JSONArray hits, final Band band) throws JSONException {
		int index = 2;
		band.setCountry(this.isAbleToParseCountry ? parseCountry(hits.getString(index++)) : "");
		band.setProvince((this.isAbleToParseProvince ? parseProvince(hits.getString(index++)) : ""));
		band.setLyricalThemes((this.isAbleToParseLyricalThemes ? parseLyricalThemes(hits.getString(index++)) : ""));
		band.setYearFormedIn((this.isAbleToParseYear ? parseYear((hits.getString(index++))) : 0));
		band.getLabel().setName(this.isAbleToParseLabel ? parseLabel(hits.getString(index++)) : "");
		return band;
	}

	private String parseProvince(final String hit) {
		// currently we do not really have to parse
		return hit.trim();
	}

	private String parseLyricalThemes(final String hit) {
		// currently we do not really have to parse
		return hit;
	}

	private int parseYear(final String hit) {
		// currently we do not really have to parse
		return Integer.parseInt(hit);
	}

	private String parseLabel(final String hit) {
		// currently we do not really have to parse
		return hit;
	}

	private String parseGenres(final String hitPart) {
		// currently we do not really have to parse
		return hitPart;
	}

	private String parseCountry(final String hitPart) {
		// currently we do not really have to parse
		return hitPart;
	}

	private String parseName(String hitPart) {
		hitPart = hitPart.substring(hitPart.indexOf(">") + 1);
		return hitPart.substring(0, hitPart.indexOf("</a>"));
	}

	private long parseId(String hitPart) {
		hitPart = hitPart.substring(hitPart.indexOf("/bands/") + 7, hitPart.indexOf(">") - 1);
		return Long.parseLong((hitPart.replaceAll(".*?/", "")));
	}

}
