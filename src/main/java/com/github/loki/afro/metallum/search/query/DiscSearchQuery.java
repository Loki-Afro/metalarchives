package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.parser.search.DiscSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * This class represents the advanced album search
 *
 * @author Zarathustra
 * All methods should have very similar names to the fields at this site
 * "Release title, exact Match?" -&gt; setReleaseTitle(String titleName, boolean exactMatch)
 */
/*
 * 
 * @see http://www.metal-archives.com/content/help?index=3#tab_db
 */
public class DiscSearchQuery extends AbstractSearchQuery<Disc> {

    private boolean exactDiscNameMatch = false;
    private boolean indieLabel = false;
    private boolean exactBandName = false;
    private int fromYear = 0;
    private int toMonth = 0;
    private int toYear = 0;
    private int fromMonth = 0;
    private final List<Country> countries = new ArrayList<>();
    private final List<DiscType> discTypes = new ArrayList<>();

    public DiscSearchQuery() {
        super(new Disc());
    }

    public DiscSearchQuery(final Disc inputDisc) {
        super(inputDisc);
    }

    /**
     * Sets the name of the band from the release we are searching for.
     *
     * @param bandName   the band that made that release we are searching for!
     * @param exactMatch if it equals the band we are searching for
     */
    public void setBandName(final String bandName, final boolean exactMatch) {
        this.searchObject.setBandName(bandName);
        this.exactBandName = exactMatch;
    }

    /**
     * Sets the name releaseTitle we are searching for
     *
     * @param releaseTitle the name of the release we are searching for
     * @param exactMatch   if the name of the release we are searching for equals the releaseTitle
     */
    public void setReleaseName(final String releaseTitle, final boolean exactMatch) {
        this.searchObject.setName(releaseTitle);
        this.exactDiscNameMatch = exactMatch;
    }

    public void setReleaseYearFrom(final int fromYear) {
        this.fromYear = fromYear;
    }

    public void setReleaseMonthFrom(final int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public void setReleaseYearTo(final int toYear) {
        this.toYear = toYear;
    }

    public void setReleaseMonthTo(final int toMonth) {
        this.toMonth = toMonth;
    }

    public void setProvince(final String province) {
        this.searchObject.getBand().setProvince(province);
    }

    /**
     * metal-archives allows us now to search for more as one country simultaneously
     *
     * @param country to add to the query
     * @return true if the country was added, false otherwise
     */
    public boolean setCountry(final String country) {
        final Country c = Country.ofMetallumDisplayName(country);
        if (c != Country.ANY) {
            this.countries.add(c);
            return true;
        } else {
            return false;
        }
    }

    /**
     * metal-archives allows us now to search for more as one country simultaneously
     *
     * @param countries to add to the query
     */
    public void setCountries(final Country... countries) {
        for (final Country country : countries) {
            if (country != Country.ANY) {
                this.countries.add(country);
            }
        }
    }

    /**
     * @param labelName the labelName of the disc we are searching for
     * @param indie     actually I don't know what makes a label to a indie label
     */
    public void setLabel(final String labelName, final boolean indie) {
        this.searchObject.getLabel().setName(labelName);
        this.indieLabel = indie;
    }

    public void setLabel(final Label label) {
        this.searchObject.setLabel(label);
    }

    public void setGenre(final String genre) {
        this.searchObject.setGenre(genre);
    }

    /**
     * Adds the DiscTypes we are searching for
     *
     * @param discTypes the types of release we are searching for
     */
    public void setReleaseTypes(final DiscType... discTypes) {
        for (final DiscType discType : discTypes) {
            if (discType != null) {
                this.discTypes.add(discType);
            }
        }
    }

    public boolean setReleaseType(final String discType) {
        final DiscType posType = DiscType.getTypeDiscTypeForString(discType);
        if (posType != null) {
            setReleaseTypes(posType);
            return true;
        }
        return false;
    }

    private String getStringForTime(final int time) {
        if (time == 0) {
            return "";
        } else {
            return String.valueOf(time);
        }
    }

    private final String getBandName() {
        final String bandName = this.searchObject.getBandName();
        this.isAValidQuery = (this.isAValidQuery || !bandName.isEmpty());
        return "bandName=" + MetallumURL.asURLString(bandName);
    }

    private final String getReleaseTitle() {
        final String title = this.searchObject.getName();
        this.isAValidQuery = (this.isAValidQuery || !title.isEmpty());
        return "releaseTitle=" + MetallumURL.asURLString(title);
    }

    private final String getYearMonth() {
        StringBuilder buf = new StringBuilder();
        if (this.fromYear != 0 || this.toMonth != 0) {
            this.isAValidQuery = true;
            buf.append("releaseYearFrom=" + getStringForTime(this.fromYear) + "&");
            buf.append("releaseMonthFrom=" + getStringForTime(this.fromMonth) + "&");
            buf.append("releaseYearTo=" + getStringForTime(this.toYear) + "&");
            buf.append("releaseMonthTo=" + getStringForTime(this.toMonth) + "&");
        }
        return buf.toString();
    }

    private final String getCountries() {
        final StringBuilder buf = new StringBuilder();
        if (!this.countries.isEmpty()) {
            this.isAValidQuery = true;
            for (final Country country : this.countries) {
                buf.append("country[]=" + country.getShortForm() + "&");
            }
        }
        return buf.toString();
    }

    private final String getReleaseLabelName() {
        final String releaseLabelName = this.searchObject.getLabel().getName();
        this.isAValidQuery = (this.isAValidQuery || !releaseLabelName.isEmpty());
        return "releaseLabelName=" + MetallumURL.asURLString(releaseLabelName);
    }

    private final String getGenre() {
        final String genre = this.searchObject.getGenre();
        this.isAValidQuery = (this.isAValidQuery || !genre.isEmpty());
        return "genre=" + MetallumURL.asURLString(genre);
    }

    private final String getLocation() {
        final String location = this.searchObject.getBand().getProvince();
        this.isAValidQuery = (this.isAValidQuery || !location.isEmpty());
        return "location=" + MetallumURL.asURLString(location);
    }

    private final String getDiscTypes() {
        final StringBuilder buf = new StringBuilder();
        for (final DiscType type : this.discTypes) {
            this.isAValidQuery = true;
            buf.append("releaseType[]=" + type.asSearchNumber() + "&");
        }
        return buf.toString();
    }

    @Override
    protected final String assembleSearchQuery(final int startPage) {
        final StringBuilder searchQueryBuf = new StringBuilder();
        searchQueryBuf.append(getBandName());
        searchQueryBuf.append(getReleaseTitle());
        searchQueryBuf.append("exactReleaseMatch=" + (this.exactDiscNameMatch ? 1 : 0) + "&");
        searchQueryBuf.append("exactBandMatch=" + (this.exactBandName ? 1 : 0) + "&");
        searchQueryBuf.append("indieLabel=" + (this.indieLabel ? 1 : 0) + "&");
        searchQueryBuf.append(getYearMonth());
        searchQueryBuf.append(getCountries());
        searchQueryBuf.append(getReleaseLabelName());
        searchQueryBuf.append(getGenre());
        searchQueryBuf.append(getLocation());
        searchQueryBuf.append(getDiscTypes());
        return MetallumURL.assembleDiscSearchURL(searchQueryBuf.toString(), startPage);
    }

    private boolean isAbleToParseDiscType() {
        if (this.discTypes.isEmpty()) {
            return true;
        }
        // is able when there are more than 2 entries in the list
        int foundCountries = 0;
        for (final DiscType type : this.discTypes) {
            if (type != null && ++foundCountries > 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isAbleToParseCountry() {
        int foundCountries = 0;
        for (final Country country : this.countries) {
            if (country != Country.ANY && ++foundCountries > 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void setSpecialFieldsInParser(final AbstractSearchParser<Disc> parser) {
        final DiscSearchParser discParser = (DiscSearchParser) parser;
        if (this.toMonth != 0 || this.toYear != 0 || this.fromMonth != 0 || this.fromYear != 0) {
            discParser.setIsAbleToParseDate(true);
        }
        discParser.setIsAbleToParseDiscType(isAbleToParseDiscType());
        discParser.setIsAbleToParseGenre(!this.searchObject.getGenre().isEmpty());
        discParser.setIsAbleToParseLabel(!this.searchObject.getLabel().getName().isEmpty());
        discParser.setIsAbleToParseCountry(isAbleToParseCountry());
        discParser.setIsAbleToParseProvince(!this.searchObject.getBand().getProvince().isEmpty());
    }

    @Override
    public void reset() {
        this.searchObject = new Disc();
        this.exactDiscNameMatch = false;
        this.indieLabel = false;
        this.exactBandName = false;
        this.fromYear = 0;
        this.toMonth = 0;
        this.toYear = 0;
        this.fromMonth = 0;
        this.countries.clear();
        this.discTypes.clear();
    }

    @Override
    protected SortedMap<SearchRelevance, List<Disc>> enrichParsedEntity(final SortedMap<SearchRelevance, List<Disc>> resultMap) {
        if (this.discTypes.size() == 1) {
            final DiscType discType = this.discTypes.get(0);
            for (final List<Disc> discList : resultMap.values()) {
                for (final Disc disc : discList) {
                    // if there is a discType we are overwriting it!
                    disc.setDiscType(discType);
                }
            }
        }
        if (this.countries.size() == 1) {
            final Country country = this.countries.get(0);
            for (final List<Disc> discList : resultMap.values()) {
                for (final Disc disc : discList) {
                    disc.getBand().setCountry(country);
                }
            }
        }
        return resultMap;
    }
}