package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.SearchRelevance;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import com.google.common.collect.Iterables;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import static com.github.loki.afro.metallum.core.util.MetallumUtil.isNotBlank;

public class DiscSearchParser extends AbstractSearchParser<SearchDiscResult> {

    private final DiscQuery discQuery;
    private boolean isAbleToParseDate = false;
    private boolean isAbleToParseLabel = false;
    private boolean isAbleToParseGenre = false;

    private boolean possibleSplitDisc = false;
    private boolean isAbleToParseDiscType;
    private boolean isAbleToParseCountry = false;
    private boolean isAbleToParseProvince;

    public DiscSearchParser(DiscQuery discQuery) {
        this.discQuery = discQuery;
        setIsAbleToParseDate(discQuery.isAbleToParseDate());

        setIsAbleToParseDiscType(discQuery.isAbleToParseDiscType());
        setIsAbleToParseGenre(isNotBlank(discQuery.getGenre()));
        setIsAbleToParseLabel(isNotBlank(discQuery.getLabelName()));
        setIsAbleToParseCountry(discQuery.isAbleToParseCountry());
        setIsAbleToParseProvince(isNotBlank(discQuery.getBandProvince()));
    }

    @Override
    protected SearchDiscResult useSpecificSearchParser(final JSONArray hit) throws JSONException {
        SearchDiscResult disc = new SearchDiscResult(parseDiscId(hit.getString(1)), parseDiscName(hit.getString(1)));
        disc.setBandName(parseBandName(hit.getString(0)));
        disc.setBandId((this.possibleSplitDisc ? 0 : parseBandId(hit.getString(0))));
        if (this.possibleSplitDisc) {
            disc.setSplitBands(parseSplitBands(hit.getString(0)));
        }

        return parseOptionalFields(disc, hit);
    }

    private List<PartialBand> parseSplitBands(final String string) {
        List<PartialBand> list = new ArrayList<>();
        final String[] bandLinks = string.split("\\s\\|\\s");
        for (String bandLink : bandLinks) {
            long id = parseBandId(bandLink);
            String name = parseBandName(bandLink);
            list.add(new PartialBand(id, name));
        }
        return list;
    }

    /**
     * Here we do have the method for the optional fields. Optional Fields are fields which do only
     * appear under given circumstances.
     * <br>
     * So it gives the disc optional values, Genre, ReleaseDate and Label
     *
     * @param hit the JSONArray with contains the potential data
     * @return the disc with optional values
     * @throws JSONException will be thrown if a Field is not at the right place
     */
    private SearchDiscResult parseOptionalFields(final SearchDiscResult disc, final JSONArray hit) throws JSONException {
        int index = 2;
        disc.setDiscType((this.isAbleToParseDiscType ? parseDiscType(hit.getString(index++)) : null));
        disc.setGenre((this.isAbleToParseGenre ? parseGenre(hit.getString(index++)) : null));
        disc.setBandCountry(this.isAbleToParseCountry ? parseCountry(hit.getString(index++)) : null);
        disc.setBandProvince(this.isAbleToParseProvince ? parseBandProvince(hit.getString(index++)) : null);
        disc.setReleaseDate((this.isAbleToParseDate ? parseDate(hit.getString(index++)) : null));
        disc.setLabelName(this.isAbleToParseLabel ? parseLabel(hit.getString(index)) : null);

        return enrichParsedEntity(discQuery, disc);
    }


    private SearchDiscResult enrichParsedEntity(DiscQuery query, final SearchDiscResult result) {
        if (query.getDiscTypes().size() == 1) {
            final DiscType discType = Iterables.getOnlyElement(query.getDiscTypes());
            result.setDiscType(discType);
        }
        if (query.getCountries().size() == 1) {
            final Country country = Iterables.getOnlyElement(query.getCountries());
            result.setBandCountry(country);
        }
        return result;
    }

    /**
     * Parse the Band province from the JSon hit.
     *
     * @param hit the JSon hit.
     * @return if this disc is a split, this method will return an empty String.
     */
    private String parseBandProvince(final String hit) {
        if (this.possibleSplitDisc) {
            return "";
        }
        return hit;
    }

    /**
     * Parse the Country from the JSon hit.
     *
     * @param hit the JSon hit.
     * @return the parsed Country.
     */
    private Country parseCountry(final String hit) {
        return Country.ofMetallumDisplayName(hit);
    }

    private DiscType parseDiscType(final String string) {
        return DiscType.getTypeDiscTypeForString(string);
    }

    /**
     * Parse the Disc name from the JSon hit.
     *
     * @param hit the JSon hit.
     * @return the parsed Disc name.
     */
    private String parseDiscName(final String hit) {
        return Jsoup.parse(hit).text();
    }

    /**
     * Parse the Band name from the JSon hit.
     *
     * @param hit the JSon hit.
     * @return the parsed Band name.
     */
    private String parseBandName(final String hit) {
        if (hit.contains("> | <")) {
            this.possibleSplitDisc = true;
            return "";
        }
        return Jsoup.parse(hit).text();
    }

    /**
     * Parse the Country from the JSon hit.<br>
     * If this Disc is a Split Disc.
     *
     * @param hit the JSon hit.
     * @return The parsed genre, if this Disc is a Split Disc this method returns an empty String.
     */
    private String parseGenre(final String hit) {
        // because the genre depends on the band, there is no genre field on the Disc site......
        if (this.possibleSplitDisc) {
            return "";
        }
        return hit;
    }

    /**
     * Parse the date from the JSon hit.<br>
     *
     * @param hit the JSon hit.
     * @return The parsed date.
     */
    private String parseDate(final String hit) {
        // sampleString: "November 4th, 2006 <!-- 2006-11-04 -->"
        return hit.replaceAll(".*?<!--\\s", "").replaceAll("\\s-->", "");
    }

    /**
     * Parse the Label from the JSon hit.<br>
     *
     * @param hit the JSon hit.
     * @return The parsed label with id 0.
     */
    private String parseLabel(final String hit) {
        return hit;
    }

    /**
     * Parse the Disc id from the JSon hit.<br>
     *
     * @param hit the JSon hit.
     * @return The parsed Disc id.
     */
    private long parseDiscId(final String hit) {
        String id = hit.substring(hit.indexOf("/albums/") + 8);
        id = id.substring(0, id.indexOf("\""));
        return Long.parseLong(id.replaceAll(".*?/", ""));
    }

    public void setIsAbleToParseDate(final boolean isAbleToParseDate) {
        this.isAbleToParseDate = isAbleToParseDate;
    }

    public void setIsAbleToParseLabel(final boolean isAbleToParseLabel) {
        this.isAbleToParseLabel = isAbleToParseLabel;
    }

    public void setIsAbleToParseGenre(final boolean isAbleToParseGenre) {
        this.isAbleToParseGenre = isAbleToParseGenre;
    }

    public void setIsAbleToParseDiscType(final boolean isAbleToParseDiscType) {
        this.isAbleToParseDiscType = isAbleToParseDiscType;
    }

    public void setIsAbleToParseCountry(final boolean isAbleToParse) {
        this.isAbleToParseCountry = isAbleToParse;

    }

    public void setIsAbleToParseProvince(final boolean isAbleToParse) {
        this.isAbleToParseProvince = isAbleToParse;
    }

    @Override
    protected final SearchRelevance getSearchRelevance(final JSONArray hits) throws JSONException {
        String relevanceString = hits.getString(1);
        relevanceString = relevanceString.substring(relevanceString.indexOf("<!-- ") + 5, relevanceString.indexOf(" -->"));
        return new SearchRelevance(relevanceString);
    }

}
