package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.SearchRelevance;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

/**
 * Parses the data which was gained by the search
 *
 * @author Zarathustra
 */
public class DiscSearchParser extends AbstractSearchParser<Disc> {

    private boolean isAbleToParseDate = false;
    private boolean isAbleToParseLabel = false;
    private boolean isAbleToParseGenre = false;

    private boolean possibleSplitDisc = false;
    private boolean isAbleToParseDiscType;
    private boolean isAbleToParseCountry = false;
    private boolean isAbleToParseProvince;

    @Override
    protected Disc useSpecificSearchParser(final JSONArray hit) throws JSONException {
        Disc disc = new Disc(parseDiscId(hit.getString(1)));
        disc.setName(parseDiscName(hit.getString(1)));
        disc.setBandName(parseBandName(hit.getString(0)));
        disc.getBand().setId((this.possibleSplitDisc ? 0 : parseBandId(hit.getString(0))));
        if (this.possibleSplitDisc) {
            disc.addSplitBand(parseSplitBands(hit.getString(0)));
        }

        disc = parseOptionalFields(disc, hit);
        return disc;
    }

    private Band[] parseSplitBands(final String string) {
        final String[] bandLinks = string.split("\\s\\|\\s");
        Band[] bandArray = new Band[bandLinks.length];
        for (int i = 0; i < bandLinks.length; i++) {
            Band band = new Band();
            band.setId(parseBandId(bandLinks[i]));
            band.setName(parseBandName(bandLinks[i]));
            bandArray[i] = band;
        }
        return bandArray;
    }

    /**
     * Here we do have the method for the optional fields. Optional Fields are fiels which do only
     * apper under given circumstances.
     * <br>
     * So it gives the disc optional values, Genre, ReleaseDate and Label
     *
     * @param hit the JSONArray with contains the potential data
     * @return the disc with optional values
     * @throws JSONException will be thrown if a Field is not at the right place
     */
    private Disc parseOptionalFields(final Disc disc, final JSONArray hit) throws JSONException {
        int index = 2;
        disc.setDiscType((this.isAbleToParseDiscType ? parseDiscType(hit.getString(index++)) : null));
        disc.setGenre((this.isAbleToParseGenre ? parseGenre(hit.getString(index++)) : ""));
        disc.getBand().setCountry(this.isAbleToParseCountry ? parseCountry(hit.getString(index++)) : "");
        disc.getBand().setProvince(this.isAbleToParseProvince ? parseBandProvince(hit.getString(index++)) : "");
        disc.setReleaseDate((this.isAbleToParseDate ? parseDate(hit.getString(index++)) : ""));
        disc.setLabel(this.isAbleToParseLabel ? parseLabel(hit.getString(index)) : new Label(0));
        return disc;
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
    private String parseCountry(final String hit) {
        return hit;
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
    private Label parseLabel(final String hit) {
        return new Label(0, hit);
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
    protected final SearchRelevance getSearchRelevance(final JSONArray htis) throws JSONException {
        String relevanceString = htis.getString(1);
        relevanceString = relevanceString.substring(relevanceString.indexOf("<!-- ") + 5, relevanceString.indexOf(" -->"));
        return new SearchRelevance(relevanceString);
    }

}
