package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.parser.search.BandSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/*
 * 
 * @see http://www.metal-archives.com/content/help?index=3#tab_db
 */
public class BandSearchQuery extends AbstractSearchQuery<Band> {

    /**
     * the Band we are searching for
     */
    private boolean exactBandNameMatch = false;
    private int fromYear;
    private int toYear;
    private boolean indieLabel = false;
    private final List<Country> countries = new ArrayList<>();
    private final List<BandStatus> bandStatus = new ArrayList<>();

    public BandSearchQuery() {
        super(new Band());
    }

    public BandSearchQuery(final Band inputBand) {
        super(inputBand);
    }

    /**
     * @param bandName   name of the band
     * @param exactMatch if the bandName is equal to the band we are searching for
     */
    public void setBandName(final String bandName, final boolean exactMatch) {
        this.searchObject.setName(bandName);
        this.exactBandNameMatch = exactMatch;
    }

    /**
     * @param genre the genre from the band we are searching for
     */
    public void setGenre(final String genre) {
        this.searchObject.setGenre(genre);
    }

    public void setYearOfFormationFrom(final int from) {
        this.fromYear = from;
    }

    public void setYearOfFormationTo(final int to) {
        this.toYear = to;
    }

    public void setLyricalThemes(final String themes) {
        this.searchObject.setLyricalThemes(themes);
    }

    /**
     * A Band can have just one status but we can search for more than one at the same time.
     *
     * @param stat status from the band we are searching for
     */
    public void addStatus(final BandStatus... stat) {
        for (final BandStatus bandStatus : stat) {
            if (bandStatus != null) {
                if (!this.bandStatus.contains(bandStatus)) {
                    this.bandStatus.add(bandStatus);
                }
            }
        }
    }

    /**
     * A Band can have just one status but we can search for more than one at the same time.
     *
     * @param stat a possible status to search for.
     * @return true if the status has been added or is already part of the search query, false otherwise
     */
    public boolean addStatus(final String stat) {
        final BandStatus bandStatus = BandStatus.getTypeBandStatusForString(stat);
        if (bandStatus != null) {
            addStatus(bandStatus);
            return true;
        }
        return false;
    }

    /**
     * metal-archives allows us now to search for more as one country simultaneously.
     *
     * @param country the country to add to the query
     * @return true if the Country was added to the list
     */
    public final boolean addCountry(final String country) {
        Country c = Country.ofMetallumDisplayName(country);
        if (c != Country.ANY) {
            this.countries.add(c);
            return true;
        } else {
            return false;
        }
    }

    /**
     * metal-archives allows us now to search for more as one country simultaneously.
     *
     * @param countries the country's to add to the query
     */
    public final void addCountry(final Country... countries) {
        for (Country country : countries) {
            if (country != Country.ANY) {
                this.countries.add(country);
            }
        }
    }

    /**
     * @param country  the possible country of the band
     * @param province the possible province, city or state
     * @return if addCountry was successfully
     */
    public final boolean setProvince(final String country, final String province) {
        this.searchObject.setProvince(province);
        return addCountry(country);
    }

    public final void setProvince(final String province) {
        this.searchObject.setProvince(province);
    }

    public final void setLabel(final String labelName, final boolean indieLabel) {
        if (this.searchObject.getLabel() == null) {
            this.searchObject.setLabel(new Label(0, labelName));
        } else {
            this.searchObject.getLabel().setName(labelName);
        }
        this.indieLabel = indieLabel;
    }

    private final String getBandName() {
        final String bandName = this.searchObject.getName();
        this.isAValidQuery = (this.isAValidQuery || !bandName.isEmpty());
        return "bandName=" + MetallumURL.asURLString(bandName).replaceAll("%2B", "+");
    }

    private final String getGenre() {
        final String genre = this.searchObject.getGenre();
        this.isAValidQuery = (this.isAValidQuery || !genre.isEmpty());
        return "genre=" + MetallumURL.asURLString(genre);
    }

    private final String getCountries() {
        this.isAValidQuery = (this.isAValidQuery || !this.countries.isEmpty());
        if (!this.countries.isEmpty()) {
            final StringBuilder buf = new StringBuilder();
            // &country[]=DM&country[]=EG&country[]=DE
            // is there a maximum?
            if (this.countries.size() == 1) {
                buf.append("country=" + MetallumURL.asURLString(this.countries.get(0).getShortForm()));
            } else {
                for (Country country : this.countries) {
                    buf.append("country[]=" + MetallumURL.asURLString(country.getShortForm()));
                }
            }
            return buf.toString();
        } else {
            return "";
        }
    }

    private final String getFromYear() {
        this.isAValidQuery = (this.isAValidQuery || this.fromYear != 0);
        if (this.fromYear != 0) {
            return "yearCreationFrom=" + this.fromYear + "&";
        } else {
            return "";
        }
    }

    private final String getToYear() {
        this.isAValidQuery = (this.isAValidQuery || this.toYear != 0);
        if (this.toYear != 0) {
            return "yearCreationTo=" + this.toYear + "&";
        } else {
            return "";
        }
    }

    private final String getBandStatus() {
        this.isAValidQuery = (this.isAValidQuery || !this.bandStatus.isEmpty());
        if (!this.bandStatus.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            for (BandStatus stat : this.bandStatus) {
                buf.append("status=" + stat.asSearchNumber() + "&");
            }
            return buf.toString();
        } else {
            return "";
        }
    }

    private final String getLyricalThemes() {
        final String lyricalThemes = this.searchObject.getLyricalThemes();
        this.isAValidQuery = (this.isAValidQuery || !lyricalThemes.isEmpty());
        return "themes=" + MetallumURL.asURLString(lyricalThemes).replaceAll("%2B", "+");
    }

    private final String getLocation() {
        final String location = this.searchObject.getProvince();
        this.isAValidQuery = (this.isAValidQuery || !location.isEmpty());
        return "location=" + MetallumURL.asURLString(location);
    }

    private String getLabelName() {
        final String labelName = this.searchObject.getLabel().getName();
        this.isAValidQuery = (this.isAValidQuery || !labelName.isEmpty());
        return "bandLabelName=" + MetallumURL.asURLString(labelName);
    }

    @Override
    protected final String assembleSearchQuery(final int startPage) {
        final StringBuilder searchQueryBuf = new StringBuilder();
        searchQueryBuf.append(getBandName());
        searchQueryBuf.append("exactBandMatch=" + (this.exactBandNameMatch ? 1 : 0) + "&");
        searchQueryBuf.append(getGenre());
        searchQueryBuf.append(getCountries());
        searchQueryBuf.append(getFromYear());
        searchQueryBuf.append(getToYear());
        searchQueryBuf.append(getBandStatus());
        searchQueryBuf.append(getLyricalThemes());
        searchQueryBuf.append(getLocation());
        searchQueryBuf.append(getLabelName());
        searchQueryBuf.append("indieLabel=" + (this.indieLabel ? 1 : 0) + "&");
        return MetallumURL.assembleBandSearchURL(searchQueryBuf.toString(), startPage);
    }

    @Override
    protected final void setSpecialFieldsInParser(final AbstractSearchParser<Band> parser) {
        final BandSearchParser bandParser = (BandSearchParser) parser;
        if (this.fromYear != 0 || this.toYear != 0) {
            bandParser.setIsAbleToParseYear(true);
        }
        bandParser.setIsAbleToParseLabel(!this.searchObject.getLabel().getName().isEmpty());
        bandParser.setIsAbleToParseLyricalThemes(!this.searchObject.getLyricalThemes().isEmpty());
        bandParser.setIsAbleToParseProvince(!this.searchObject.getProvince().isEmpty() || !this.countries.isEmpty());
        if (this.countries.isEmpty()) {
            bandParser.setIsAbleToParseCountry(true);
        } else {
            int foundCountries = 0;
            for (Country country : this.countries) {
                if (country != Country.ANY && ++foundCountries > 1) {
                    bandParser.setIsAbleToParseCountry(true);
                    bandParser.setIsAbleToParseProvince(true);
                }
            }
        }

    }

    @Override
    public void reset() {
        this.fromYear = 0;
        this.toYear = 0;
        this.indieLabel = false;
        this.countries.clear();
        this.bandStatus.clear();
        this.exactBandNameMatch = false;
        this.searchObject = new Band();
    }

    @Override
    protected SortedMap<SearchRelevance, List<Band>> enrichParsedEntity(final SortedMap<SearchRelevance, List<Band>> resultMap) {
        if (this.countries.size() == 1) {
            final Country country = this.countries.get(0);
            for (final List<Band> bandList : resultMap.values()) {
                for (final Band band : bandList) {
                    band.setCountry(country);
                }
            }
        }

        if (this.bandStatus.size() == 1) {
            final BandStatus status = this.bandStatus.get(0);
            for (final List<Band> bandList : resultMap.values()) {
                for (Band band : bandList) {
                    band.setStatus(status);
                }
            }
        }
        return resultMap;
    }

}
