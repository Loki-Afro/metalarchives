package de.loki.metallum.core.parser.site.helper.label;

import org.json.JSONArray;
import org.json.JSONException;

import de.loki.metallum.core.parser.site.LabelSiteParser.PARSE_STYLE;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.entity.Band;

public class PastRosterParser extends AbstractRosterParser<Band, Integer> {

	public PastRosterParser(long labelId, byte numberPerPage, boolean alphabetical, PARSE_STYLE style) {
		super(labelId, numberPerPage, alphabetical, style);
	}

	@Override
	protected void parseSpecific(JSONArray hits) throws JSONException {
		Band band = getABand(hits.getString(0));
		band.setGenre(parseGenre(hits.getString(1)));
		band.setCountry(parseBandCountry(hits.getString(2)));
		this.mainMap.put(band, hits.getInt(3));
	}

	@Override
	protected String getSearchURL(long labelId, byte numberPerPage, boolean alphabetical, int sortType) {
		return MetallumURL.assembleLabelPastRoster(labelId, numberPerPage, alphabetical, sortType);
	}

}
