package com.github.loki.afro.metallum.core.parser.site.helper.label;

import com.github.loki.afro.metallum.core.parser.site.LabelSiteParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Band;
import org.json.JSONArray;
import org.json.JSONException;

public class CurrentRosterParser extends AbstractRosterParser<Integer, Band> {

    public CurrentRosterParser(final long labelId, final byte numberPerPage, final boolean alphabetical, final LabelSiteParser.PARSE_STYLE sortType) {
        super(labelId, numberPerPage, alphabetical, sortType);
    }

    @Override
    protected void parseSpecific(final JSONArray hits) throws JSONException {
        final Band band = getABand(hits.getString(0));
        band.setGenre(parseGenre(hits.getString(1)));
        band.setCountry(parseBandCountry(hits.getString(2)));
    }

    @Override
    protected String getSearchURL(final long labelId, final byte numberPerPage, final boolean alphabetical, final int sortType) {
        return MetallumURL.assembleLabelCurrentRoster(labelId, numberPerPage, alphabetical, sortType);
    }

}
