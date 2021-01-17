package com.github.loki.afro.metallum.core.parser.site.helper.label;

import com.github.loki.afro.metallum.core.parser.site.LabelSiteParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.DiscType;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ReleaseParser extends AbstractRosterParser<Band, List<Disc>> {

    public ReleaseParser(final long labelId, final byte numberPerPage, final boolean alphabetical, final LabelSiteParser.PARSE_STYLE style) {
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
            disc.setSplitBands(parseSplitBands(hits.getString(0)));
        } else {
            disc.setBand(new PartialBand(band.getId(), band.getName()));
        }
        discList.add(disc);
        this.mainMap.put(band, discList);
    }

    private List<PartialBand> parseSplitBands(final String bandData) {
        List<PartialBand> list = new ArrayList<>();
        final String[] strBandArray = bandData.split("</a>");
        for (String s : strBandArray) {
            String bandName = s.substring(s.indexOf("\">") + 2);
            String bandId = s.substring(0, s.length() - (bandName.length() + 2));
            bandId = bandId.substring(bandId.lastIndexOf("/") + 1);
            list.add(new PartialBand(Long.parseLong(bandId), bandName));
        }
        return list;
    }

    private List<Disc> getDiscList(final Band band) {
        List<Disc> discList;
        if (this.mainMap.containsKey(band)) {
            discList = this.mainMap.get(band);
        } else {
            discList = new ArrayList<>();
        }

        return discList;
    }

    @Override
    protected String getSearchURL(final long labelId, final byte numberPerPage, final boolean alphabetical, final int sortType) {
        return MetallumURL.assembleLabelReleasesURL(labelId, numberPerPage, alphabetical, sortType);
    }

}
