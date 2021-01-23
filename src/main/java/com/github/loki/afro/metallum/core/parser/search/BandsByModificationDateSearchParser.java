package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.SearchRelevance;
import com.github.loki.afro.metallum.search.query.entity.BandByModificationDateResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class BandsByModificationDateSearchParser  extends AbstractSearchParser<BandByModificationDateResult>{


    @Override
    protected BandByModificationDateResult useSpecificSearchParser(JSONArray hits) throws JSONException {
        String band = hits.getString(1);

        Elements bandElement = Jsoup.parse(band).getElementsByTag("a");
        long bandId = Long.parseLong(bandElement.attr("href").replaceAll("^.+/", ""));
        String bandName = bandElement.text();

        String countryStr = hits.getString(2);
        Country country = Country.valueOf(Jsoup.parse(countryStr).getElementsByTag("a").attr("href").replaceAll("^.+/", ""));
        String genre = hits.getString(3);
        String modifiedOn = hits.getString(4);
        return new BandByModificationDateResult(bandId, bandName, country, genre, modifiedOn);
    }

    @Override
    protected SearchRelevance getSearchRelevance(JSONArray hits) {
        return new SearchRelevance(0D);
    }
}
