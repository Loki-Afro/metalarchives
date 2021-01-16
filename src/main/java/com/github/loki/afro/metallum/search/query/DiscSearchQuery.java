package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;

@Deprecated
public class DiscSearchQuery extends AbstractSearchQuery<DiscQuery> {

    public DiscSearchQuery() {
        super(DiscQuery.builder().build());
    }

    public DiscSearchQuery(final DiscQuery discQuery) {
        super(discQuery);
    }

    /**
     * Sets the name of the band from the release we are searching for.
     *
     * @param bandName   the band that made that release we are searching for!
     * @param exactMatch if it equals the band we are searching for
     */
    public void setBandName(final String bandName, final boolean exactMatch) {
        this.searchObject.setBandName(bandName);
        this.searchObject.setExactBandNameMatch(exactMatch);
    }

    /**
     * Sets the name releaseTitle we are searching for
     *
     * @param releaseTitle the name of the release we are searching for
     * @param exactMatch   if the name of the release we are searching for equals the releaseTitle
     */
    public void setReleaseName(final String releaseTitle, final boolean exactMatch) {
        this.searchObject.setName(releaseTitle);
        this.searchObject.setExactNameMatch(exactMatch);
    }

    public void setReleaseYearFrom(final int fromYear) {
        this.searchObject.setFromYear(fromYear);
    }

    public void setReleaseMonthFrom(final int fromMonth) {
        this.searchObject.setFromMonth(fromMonth);
    }

    public void setReleaseYearTo(final int toYear) {
        this.searchObject.setToYear(toYear);
    }

    public void setReleaseMonthTo(final int toMonth) {
        this.searchObject.setToMonth(toMonth);
    }

    public void setProvince(final String province) {
        this.searchObject.setBandProvince(province);
    }


    /**
     * @param labelName the labelName of the disc we are searching for
     * @param indie     actually I don't know what makes a label to a indie label
     */
    public void setLabel(final String labelName, final boolean indie) {
        this.searchObject.setLabelName(labelName);
        this.searchObject.setIndieLabel(indie);
    }

    public void setGenre(final String genre) {
        this.searchObject.setGenre(genre);
    }

}