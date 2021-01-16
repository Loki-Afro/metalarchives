package com.github.loki.afro.metallum.search.query.entity;


import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.Set;

import static com.github.loki.afro.metallum.search.query.entity.IQuery.asPair;
import static com.github.loki.afro.metallum.search.query.entity.IQuery.getForQuery;

@SuperBuilder
public class DiscQuery extends AbstractDisc implements IQuery {

    @Singular
    @Setter
    @Getter
    private Set<Country> countries;
    @Singular
    @Setter
    @Getter
    private Set<DiscType> discTypes;

    @Getter
    @Setter
    private boolean indieLabel;

    @Getter
    @Setter
    private boolean exactNameMatch;

    @Getter
    @Setter
    private boolean exactBandNameMatch;

    @Setter
    private Integer fromYear;
    @Setter
    private Integer fromMonth;
    @Setter
    private Integer toYear;
    @Setter
    private Integer toMonth;

    public Optional<Integer> getFromYear() {
        return Optional.ofNullable(fromYear);
    }

    public Optional<Integer> getFromMonth() {
        return Optional.ofNullable(fromMonth);
    }

    public Optional<Integer> getToYear() {
        return Optional.ofNullable(toYear);
    }

    public Optional<Integer> getToMonth() {
        return Optional.ofNullable(toMonth);
    }

    public Optional<String> getBandName() {
        return Optional.ofNullable(this.bandName);
    }


    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }


    @Override
    public boolean isValid() {
        boolean isAValidQuery = MetallumUtil.isNotBlank(getBandName());

        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getName()));

        if (getFromYear().isPresent() || getToMonth().isPresent()) {
            isAValidQuery = true;
        }

        if (!getCountries().isEmpty()) {
            isAValidQuery = true;
        }

        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getLabelName()));

        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getGenre()));

        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getBandProvince()));

        isAValidQuery = (isAValidQuery || !getDiscTypes().isEmpty());

        return isAValidQuery;
    }

    @Override
    public String assembleQueryUrl(int page) {
        final StringBuilder searchQueryBuf = new StringBuilder();
        searchQueryBuf.append(asPair("bandName", getBandName()));
        searchQueryBuf.append(asPair("releaseTitle", getName()));
        searchQueryBuf.append("exactReleaseMatch=" + (isExactNameMatch() ? 1 : 0) + "&");
        searchQueryBuf.append("exactBandMatch=" + (isExactBandNameMatch() ? 1 : 0) + "&");
        searchQueryBuf.append("indieLabel=" + (isIndieLabel() ? 1 : 0) + "&");
        searchQueryBuf.append(getYearMonth());
        searchQueryBuf.append(getForQuery("country", getCountries(), Country::getShortForm));
        searchQueryBuf.append(asPair("releaseLabelName", getLabelName()));
        searchQueryBuf.append(asPair("genre", getGenre()));
        searchQueryBuf.append(asPair("location", getBandProvince()));
        searchQueryBuf.append(getForQuery("releaseType", getDiscTypes(), DiscType::asSearchNumber));
        return MetallumURL.assembleDiscSearchURL(searchQueryBuf.toString(), page);
    }

    private final String getYearMonth() {
        StringBuilder buf = new StringBuilder();
        if (getFromYear().isPresent() || getToMonth().isPresent()) {
            buf.append("releaseYearFrom=" + getStringForTime(getFromYear().orElse(0)) + "&");
            buf.append("releaseMonthFrom=" + getStringForTime(getFromMonth().orElse(0)) + "&");
            buf.append("releaseYearTo=" + getStringForTime(getToYear().orElse(0)) + "&");
            buf.append("releaseMonthTo=" + getStringForTime(getToMonth().orElse(0)) + "&");
        }
        return buf.toString();
    }

    private static String getStringForTime(final int time) {
        if (time == 0) {
            return "";
        } else {
            return String.valueOf(time);
        }
    }

    public boolean isAbleToParseDate() {
        return getFromYear().isPresent()
                || getFromMonth().isPresent()
                || getToYear().isPresent()
                || getToMonth().isPresent();
    }

    public boolean isAbleToParseDiscType() {
        if (getDiscTypes().isEmpty()) {
            return true;
        }
        // is able when there are more than 2 entries in the list
        int foundCountries = 0;
        for (final DiscType type : getDiscTypes()) {
            if (type != null && ++foundCountries > 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isAbleToParseCountry() {
        return getCountries().size() >1;
    }

}
