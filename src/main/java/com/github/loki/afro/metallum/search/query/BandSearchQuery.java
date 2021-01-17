package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;

@Deprecated
public class BandSearchQuery extends AbstractSearchQuery<BandQuery> {

    public BandSearchQuery() {
        super(BandQuery.builder().build());
    }

    public BandSearchQuery(final BandQuery bandQuery) {
        super(bandQuery);
    }

    /**
     * @param bandName   name of the band
     * @param exactMatch if the bandName is equal to the band we are searching for
     */
    public void setBandName(final String bandName, final boolean exactMatch) {
        this.searchObject.setName(bandName);
        this.searchObject.setExactBandNameMatch(exactMatch);
    }

    /**
     * @param genre the genre from the band we are searching for
     */
    public void setGenre(final String genre) {
        this.searchObject.setGenre(genre);
    }

    public void setLyricalThemes(final String themes) {
        this.searchObject.setLyricalThemes(themes);
    }


    public final void setProvince(final String province) {
        this.searchObject.setProvince(province);
    }

    public final void setLabel(final String labelName, final boolean indieLabel) {
        this.searchObject.setLabelName(labelName);
        this.searchObject.setIndieLabel(indieLabel);
    }

}
