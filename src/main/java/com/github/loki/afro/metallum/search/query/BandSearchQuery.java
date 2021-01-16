package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.parser.search.BandSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

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
