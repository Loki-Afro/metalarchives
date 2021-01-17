package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.SearchRelevance;
import com.github.loki.afro.metallum.search.query.entity.SearchMemberResult;
import com.google.api.client.util.Strings;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MemberSearchParser extends AbstractSearchParser<SearchMemberResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberSearchParser.class);

    @Override
    protected SearchMemberResult useSpecificSearchParser(final JSONArray hits) throws JSONException {
        long id = parseId(hits.getString(0));
        String name = parseName(hits.getString(0));
        SearchMemberResult member = new SearchMemberResult(id, name);
        member.setAlternativeName(parseAlternativeName(hits.getString(0)));
        member.setRealName(parseRealName(hits.getString(1)));
        member.setCountry(parseCountry(hits.getString(2)));
        member.setBands(parseBands(hits.getString(3)));
        return member;
    }

    private String parseAlternativeName(final String hit) {
        String akaName = "";
        if (hit.contains("(a.k.a")) {
            akaName = hit.substring(hit.indexOf("(a.k.a. ") + 8, hit.length() - 1);
            akaName = Jsoup.parse(akaName).text();
        }
        return akaName;
    }

    private long parseId(String hit) {
        hit = hit.substring(0, hit.indexOf("\">"));
        hit = hit.substring(hit.lastIndexOf("/") + 1);
        return Long.parseLong(hit);
    }

    private List<PartialBand> parseBands(final String hit) {
        LOGGER.debug("new hit: " + hit);
        List<PartialBand> bandList = new ArrayList<>();
        Document doc = Jsoup.parse(hit);
        Elements links = doc.getElementsByAttribute("href");
        for (Element link : links) {
            String hrefAttr = link.attr("href");
            String bandId = hrefAttr.substring(hrefAttr.lastIndexOf("/") + 1);
//			because there are Members which do not have any band O.o 
//			see FENRIZ - http://www.metal-archives.com/artists/FENRIZ/407865
            if (!bandId.isEmpty()) {
                PartialBand band = new PartialBand(Long.parseLong(bandId), MetallumUtil.trimNoBreakSpaces(link.text()));
                LOGGER.debug("adding new Band to Member: " + band);
                bandList.add(band);
            }
        }
        return bandList;
    }

    private String parseRealName(final String hit) {
        return hit;
    }

    /**
     * Parses the name without the alternative name.
     * Example:
     * put in <a href="http://www.metal-archives.com/artists/%2232%22_Kondo/182832">"32" Kondo</a>
     * (a.k.a. <em>32</em>)
     * and you'll get: "32" Kondo
     *
     * @param hit the link
     * @return the name name of the link without the alternative name
     */
    private String parseName(final String hit) {
        String name = Jsoup.parse(hit).text();
        if (!name.contains("a.k.a")) {
            return name;
        } else {
            return nameWithoutAKA(name);
        }
    }

    private String nameWithoutAKA(final String name) {
        return name.replaceAll("\\s\\(a\\.k\\.a\\..*", "");
    }

    private Country parseCountry(final String hit) {
        if (Strings.isNullOrEmpty(hit) || hit.contains("&nbsp;")) {
            return null;
        } else {
            return Country.ofMetallumDisplayName(hit);
        }
    }

    @Override
    protected SearchRelevance getSearchRelevance(final JSONArray hits) throws JSONException {
        // they do all have the same searchrelevance!
        return new SearchRelevance(0d);
    }

}
