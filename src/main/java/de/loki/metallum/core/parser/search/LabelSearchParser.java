package de.loki.metallum.core.parser.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import de.loki.metallum.entity.Label;
import de.loki.metallum.enums.Country;
import de.loki.metallum.search.SearchRelevance;

/**
 * Parses the data which was gained by the search
 * 
 * @author Zarathustra
 * 
 */
public class LabelSearchParser extends AbstractSearchParser<Label> {

	@Override
	protected Label useSpecificSearchParser(JSONArray hits) throws JSONException {
		Label label = new Label(parseLabelId(hits.getString(0)));
		label.setName(parseLabelName(hits.getString(0)));
		label.setCountry(parseLabelCountry(hits.getString(1)));
		label.setSpecialisation(parseSpecialisation(hits.getString(2)));
		return label;
	}

	private long parseLabelId(final String hit) {
		String id = hit.substring(0, hit.indexOf("\">"));
		id = id.substring(id.lastIndexOf("/") + 1, id.length());
		return Long.parseLong(id);
	}

	private String parseLabelName(final String hit) {
		return Jsoup.parse(hit).text();
	}

	private Country parseLabelCountry(final String hit) {
		return Country.getRightCountryForString(hit);
	}

	private String parseSpecialisation(final String hit) {
		return hit;
	}

	@Override
	protected final SearchRelevance getSearchRelevance(JSONArray htis) throws JSONException {
		// actually there is no searchrelevance :'(
		return new SearchRelevance(0d);
	}

}
