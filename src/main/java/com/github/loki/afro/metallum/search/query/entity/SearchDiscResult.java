package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

public class SearchDiscResult extends AbstractDisc implements Identifiable {

    @Getter
    private final long id;

    @Setter
    @Getter
    private long bandId;

    @Setter
    @Getter
    private List<PartialBand> splitBands;

    // optionals
    @Setter
    private String releaseDate;
    @Setter
    private Country bandCountry;
    @Setter
    private DiscType discType;

    public SearchDiscResult(long id, String name) {
        this.id = id;
        setName(name);
    }

    public Optional<String> getReleaseDate() {
        return Optional.ofNullable(releaseDate);
    }

    public Optional<DiscType> getDiscType() {
        return Optional.ofNullable(discType);
    }

    public Optional<Country> getBandCountry() {
        return Optional.ofNullable(bandCountry);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getBandName() {
        return this.bandName;
    }

}
