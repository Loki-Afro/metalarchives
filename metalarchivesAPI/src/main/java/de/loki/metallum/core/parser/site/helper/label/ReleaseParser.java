package de.loki.metallum.core.parser.site.helper.label;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import de.loki.metallum.core.parser.site.LabelSiteParser.PARSE_STYLE;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.enums.DiscType;

public class ReleaseParser extends AbstractRosterParser<Band, List<Disc>> {

	public ReleaseParser(long labelId, byte numberPerPage, boolean alphabetical, PARSE_STYLE style) {
		super(labelId, numberPerPage, alphabetical, style);
	}

	private DiscType parseDiscType(final String hit) {
		return DiscType.getTypeDiscTypeForString(hit);
	}

	private String parseYear(final String hit) {
		return hit;
	}

	@Override
	protected void parseSpecific(final JSONArray hits) throws JSONException {
		final Band band = getABand(hits.getString(0));
		final List<Disc> discList = getDiscList(band);

		final Disc disc = getADisc(hits.getString(1));
		disc.setDiscType(parseDiscType(hits.getString(2)));
		disc.setReleaseDate(parseYear(hits.getString(3)));
		if (disc.isSplit()) {
			disc.addSplitBand(parseSplitBands(hits.getString(0)));
		} else {
			disc.setBand(band);
			band.addToDiscography(disc);
		}
		discList.add(disc);
		this.mainMap.put(band, discList);
	}

	private Band[] parseSplitBands(final String bandData) {
		final String[] strBandArray = bandData.split("</a>");
		Band[] bandArray = new Band[strBandArray.length];
		for (int i = 0; i < strBandArray.length; i++) {
			String bandName = strBandArray[i].substring(strBandArray[i].indexOf("\">") + 2, strBandArray[i].length());
			String bandId = strBandArray[i].substring(0, strBandArray[i].length() - (bandName.length() + 2));
			bandId = bandId.substring(bandId.lastIndexOf("/") + 1, bandId.length());
			bandArray[i] = new Band(Long.parseLong(bandId), bandName);
		}
		return bandArray;
	}

	private List<Disc> getDiscList(Band band) {
		List<Disc> discList = null;
		if (this.mainMap.containsKey(band)) {
			discList = this.mainMap.get(band);
		} else {
			discList = new ArrayList<Disc>();
		}

		return discList;
	}

	@Override
	protected String getSearchURL(long labelId, byte numberPerPage, boolean alphabetical, int sortType) {
		return MetallumURL.assembleLabelReleasesURL(labelId, numberPerPage, alphabetical, sortType);
	}

}
