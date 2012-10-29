package de.loki.metallum.core.parser.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import de.loki.metallum.entity.Band;
import de.loki.metallum.enums.Country;

/**
 * Parses the data which was gained by the search
 * 
 * @author Zarathustra
 * 
 */
public class LyricalThemesSearchParser extends AbstractSearchParser<Band> {

	@Override
	protected Band useSpecificSearchParser(JSONArray hits) throws JSONException {
		Band band = new Band(parsebandId(hits.getString(0)));
		band.setName(parseBandName(hits.getString(0)));
		band.setGenre(parseGenre(hits.getString(1)));
		band.setCountry(parseCountry(hits.getString(2)));
		band.setLyricalThemes(parseLyricalThemes(hits.getString(3)));
		// band.setAlternativeName(parseAlternativeBandName(hits.getString(0)));
		return band;
	}

	private long parsebandId(String hit) {
		String id = hit.substring(0, hit.indexOf("\">"));
		id = id.substring(id.lastIndexOf("/") + 1, id.length());
		return Long.parseLong(id);
	}

	private String parseBandName(String hit) {
		return Jsoup.parse(hit).text();
	}

	private String parseGenre(String hit) {
		return hit;
	}

	private Country parseCountry(String hit) {
		return Country.getRightCountryForString(hit);
	}

	private String parseLyricalThemes(String hit) {
		return hit;
	}

	// private String parseAlternativeBandName(String hit) {
	// String akaName = "";
	// if (hit.contains("(a.k.a")) {
	// akaName = hit.substring(hit.indexOf("(a.k.a. ") + 8, hit.length() - 1);
	// akaName = Jsoup.parse(akaName).text();
	// }
	// return akaName;
	// }

}
