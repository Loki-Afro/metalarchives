package de.loki.metallum.core.parser.site.helper.label;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import de.loki.metallum.core.parser.IJSONParser;
import de.loki.metallum.core.parser.site.LabelSiteParser.PARSE_STYLE;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.enums.Country;

public abstract class AbstractRosterParser<K, V> implements IJSONParser {

	private final long			labelId;
	private final byte			numberPerPage;
	private final boolean		alphabetical;
	private final PARSE_STYLE	sortType;

	private final Band			dummyBand	= new Band(0, "Various Artists");
	protected final Map<K, V>	mainMap		= new HashMap<K, V>();

	public AbstractRosterParser(final long labelId, final byte numberPerPage, final boolean alphabetical, final PARSE_STYLE sortType) {
		this.labelId = labelId;
		this.numberPerPage = numberPerPage;
		this.alphabetical = alphabetical;
		this.sortType = sortType;
	}

	public final Map<K, V> parse() throws JSONException, ExecutionException {
		final String url = getSearchURL(this.labelId, this.numberPerPage, this.alphabetical, this.sortType.asSearchNumber());
		final JSONObject jsonObj = getJSONObject(url);
		if (jsonObj.getInt(TOTAL_SEARCH_RESULTS_FROM_JSON) > 0) {
			JSONArray jsonMainArray = jsonObj.getJSONArray(MAIN_JSON_ARRAY_STRING);
			for (int i = 0; i < jsonMainArray.length(); i++) {
				final JSONArray jsonArray = jsonMainArray.getJSONArray(i);
				parseSpecific(jsonArray);
			}
		}
		return this.mainMap;
	}

	/**
	 * parses a JSon Array and fills the {@link AbstractRosterParser#mainMap}
	 * 
	 * @param jsonArray
	 * @throws JSONException
	 */
	protected abstract void parseSpecific(JSONArray jsonArray) throws JSONException;

	protected abstract String getSearchURL(final long labelId, final byte numberPerPage, final boolean alphabetical, final int sortType);

	private JSONObject getJSONObject(final String url) throws JSONException, ExecutionException {
		return new JSONObject(Downloader.getHTML(url));
	}

	/**
	 * 
	 * @param hit the hit where the Disc information occurs
	 * @return A Disc with name and Id
	 */
	protected Disc getADisc(final String hit) {
		Disc disc = new Disc(parseId(hit));
		disc.setName(parseName(hit));
		return disc;
	}

	/**
	 * 
	 * @param hit the hit where the Band information occurs
	 * @return A Band with name and Id
	 */
	protected Band getABand(final String hit) {
		if (hit.contains("</a> / <a")) {
			return this.dummyBand;
		}
		final Band band = new Band(parseId(hit));
		band.setName(parseName(hit));
		return band;
	}

	private long parseId(final String hit) {
		final String name = Jsoup.parse(hit).text();
		int index, i = 1;
		final char df = '>';
		while (df != ((hit.charAt(hit.lastIndexOf(name) - i)))) {
			i++;
		}
		index = hit.lastIndexOf(name) - i;
		String id = hit.substring(0, index - 1);
		id = id.substring(id.lastIndexOf("/") + 1, id.length());
		return Long.parseLong(id);
	}

	private String parseName(final String hit) {
		return Jsoup.parse(hit).text();
	}

	protected String parseGenre(final String hit) {
		return hit;
	}

	protected Country parseBandCountry(final String hit) {
		return Country.getRightCountryForString(hit);
	}
}
