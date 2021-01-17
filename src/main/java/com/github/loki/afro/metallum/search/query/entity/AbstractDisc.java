package com.github.loki.afro.metallum.search.query.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@SuperBuilder
@NoArgsConstructor
public abstract class AbstractDisc {

    @Setter
    String name;

    @Setter
    String bandName;

    @Setter
    private String genre;

    @Setter
    private String bandProvince;

    @Setter
    private String labelName;

    public Optional<String> getGenre() {
        return Optional.ofNullable(genre);
    }

    public Optional<String> getBandProvince() {
        return Optional.ofNullable(bandProvince);
    }

    public Optional<String> getLabelName() {
        return Optional.ofNullable(labelName);
    }
}
