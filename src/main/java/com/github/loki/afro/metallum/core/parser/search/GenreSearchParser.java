package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.Country;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

/**
 * Parses the data which was gained by the search
 *
 * @author Zarathustra
 */
public class GenreSearchParser extends AbstractSearchParser<Band> {

    @Override
    protected Band useSpecificSearchParser(JSONArray hits) throws JSONException {
        Band band = new Band(parseBandIdIntern(hits.getString(0)));
        band.setName(parseBandName(hits.getString(0)));
        band.setGenre(parseBandGenre(hits.getString(1)));
        band.setCountry(parseCountry(hits.getString(2)));
        // band.setAlternativeName(parseAlternativeName(hits.getString(0)));
        return band;
    }

    private long parseBandIdIntern(final String hit) {
        String id = hit.substring(0, hit.indexOf("\">"));
        id = id.substring(id.lastIndexOf("/") + 1, id.length());
        return Long.parseLong(id);
    }

    private String parseBandName(final String hit) {
        return Jsoup.parse(hit).text();
    }

    private String parseBandGenre(final String hit) {
        return hit;
    }

    private Country parseCountry(final String hit) {
        return Country.getRightCountryForString(hit);
    }

}
