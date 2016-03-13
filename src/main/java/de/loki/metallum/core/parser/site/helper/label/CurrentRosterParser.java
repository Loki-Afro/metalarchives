package de.loki.metallum.core.parser.site.helper.label;

import de.loki.metallum.core.parser.site.LabelSiteParser;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.entity.Band;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CurrentRosterParser extends AbstractRosterParser<Integer, Band> {

	private final List<Band> mainList = new ArrayList<Band>();

	public CurrentRosterParser(long labelId, byte numberPerPage, boolean alphabetical, LabelSiteParser.PARSE_STYLE sortType) {
		super(labelId, numberPerPage, alphabetical, sortType);
	}

	@Override
	protected void parseSpecific(final JSONArray hits) throws JSONException {
		final Band band = getABand(hits.getString(0));
		band.setGenre(parseGenre(hits.getString(1)));
		band.setCountry(parseBandCountry(hits.getString(2)));
		this.mainList.add(band);
	}

	@Override
	protected String getSearchURL(long labelId, byte numberPerPage, boolean alphabetical, int sortType) {
		return MetallumURL.assembleLabelCurrentRoster(labelId, numberPerPage, alphabetical, sortType);
	}

}
