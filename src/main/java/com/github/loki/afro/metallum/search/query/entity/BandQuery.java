package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.google.common.collect.Iterables;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.Set;

import static com.github.loki.afro.metallum.search.query.entity.IQuery.asPair;
import static com.github.loki.afro.metallum.search.query.entity.IQuery.getForQuery;

@SuperBuilder
public class BandQuery extends AbstractBand implements IQuery{

    @Setter
    private String labelName;
    @Getter
    @Setter
    private boolean indieLabel;
    @Singular
    @Getter
    private final Set<Country> countries;
    @Singular
    @Getter
    private final Set<BandStatus> statuses;
    @Setter
    private String name;

    @Setter
    @Getter
    private boolean exactBandNameMatch;

    private Integer yearOfFormationFromYear;
    private Integer yearOfFormationToYear;

    public BandQuery setYearOfFormationFromYear(Integer yearOfFormationFromYear) {
        this.yearOfFormationFromYear = yearOfFormationFromYear;
        return this;
    }

    public BandQuery setYearOfFormationToYear(Integer yearOfFormationToYear) {
        this.yearOfFormationToYear = yearOfFormationToYear;
        return this;
    }

    public Optional<Integer> getYearOfFormationFromYear() {
        return Optional.ofNullable(yearOfFormationFromYear);
    }

    public Optional<Integer> getYearOfFormationToYear() {
        return Optional.ofNullable(yearOfFormationToYear);
    }


    public Optional<String> getLabelName() {
        return Optional.ofNullable(labelName);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void addStatus(BandStatus bandStatus) {
        this.statuses.add(bandStatus);
    }

    public void addCountry(Country country) {
        this.countries.add(country);
    }

    public static BandQuery bandWithExactName(String name) {
        return byName(name, true);
    }

    public static BandQuery byName(String name, boolean exactMatch) {
        return BandQuery.builder()
                .name(name)
                .exactBandNameMatch(exactMatch)
                .build();
    }

    @Override
    public boolean isValid() {
        final String bandName = getName().orElse("");
        boolean isAValidQuery = !bandName.isEmpty();

        final String genre = getGenre().orElse("");
        isAValidQuery = (isAValidQuery || !genre.isEmpty());

        Set<Country> countries = getCountries();
        isAValidQuery = (isAValidQuery || !countries.isEmpty());

        isAValidQuery = (isAValidQuery || getYearOfFormationFromYear().isPresent());

        isAValidQuery = (isAValidQuery || getYearOfFormationToYear().isPresent());

        Set<BandStatus> status = getStatuses();
        isAValidQuery = (isAValidQuery || !status.isEmpty());

        final String lyricalThemes = getLyricalThemes().orElse("");
        isAValidQuery = (isAValidQuery || !lyricalThemes.isEmpty());

        final String location = getProvince().orElse("");
        isAValidQuery = (isAValidQuery || !location.isEmpty());

        final String labelName = getLabelName().orElse("");
        isAValidQuery = (isAValidQuery || !labelName.isEmpty());

        return isAValidQuery;
    }

    @Override
    public String assembleQueryUrl(int page) {
        final StringBuilder searchQueryBuf = new StringBuilder();
        searchQueryBuf.append(asPair("bandName", getName()).replaceAll("%2B", "+"));
        searchQueryBuf.append("exactBandMatch=" + (isExactBandNameMatch() ? 1 : 0) + "&");
        searchQueryBuf.append(asPair("genre", getGenre()));
        searchQueryBuf.append(getCountryForQuery());
        searchQueryBuf.append(getFromYearForQuery());
        searchQueryBuf.append(getToYearForQuery());
        searchQueryBuf.append(getForQuery("status", getStatuses(), BandStatus::asSearchNumber));
        searchQueryBuf.append(asPair("themes", getLyricalThemes()).replaceAll("%2B", "+"));
        searchQueryBuf.append(asPair("location", getProvince()));
        searchQueryBuf.append(asPair("bandLabelName", getLabelName()));
        searchQueryBuf.append("indieLabel=" + (isIndieLabel() ? 1 : 0) + "&");
        return MetallumURL.assembleBandSearchURL(searchQueryBuf.toString(), page);
    }

    private String getCountryForQuery() {
        Set<Country> countries = getCountries();
        if (!countries.isEmpty()) {
            final StringBuilder buf = new StringBuilder();
            // &country[]=DM&country[]=EG&country[]=DE
            // is there a maximum?
            if (countries.size() == 1) {
                buf.append("country=" + MetallumURL.asURLString(Iterables.getOnlyElement(countries).getShortForm()));
            } else {
                return getForQuery("country", countries, Country::getShortForm);
            }
            return buf.toString();
        } else {
            return "";
        }
    }

    private final String getFromYearForQuery() {
        if (getYearOfFormationFromYear().isPresent()) {
            return "yearCreationFrom=" + getYearOfFormationFromYear().get() + "&";
        } else {
            return "";
        }
    }

    private final String getToYearForQuery() {
        if (getYearOfFormationToYear().isPresent()) {
            return "yearCreationTo=" + getYearOfFormationToYear().get() + "&";
        } else {
            return "";
        }
    }
}
