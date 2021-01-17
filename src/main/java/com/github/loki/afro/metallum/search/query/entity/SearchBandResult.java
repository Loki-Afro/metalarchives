package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class SearchBandResult extends AbstractBand implements Identifiable {

    @Getter
    private final long id;
    @Getter
    private final String name;
    @Setter
    private Country country;
    @Setter
    private BandStatus status;
    @Getter
    @Setter
    private String labelName;

    @Getter
    @Setter
    private long labelId;

    public SearchBandResult(long id, String name) {
        this.id = id;
        this.name = name;

    }

    public Optional<BandStatus> getStatus() {
        return Optional.ofNullable(status);
    }

    public Optional<Country> getCountry() {
        return Optional.ofNullable(country);
    }


}
