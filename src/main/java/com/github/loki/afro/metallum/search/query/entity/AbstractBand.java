package com.github.loki.afro.metallum.search.query.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@SuperBuilder
@NoArgsConstructor
public abstract class AbstractBand {

    @Setter
    private String genre;
    @Setter
    private String province;
    @Setter
    private String lyricalThemes;
    @Setter
    private Integer yearFormedIn;

    public Optional<String> getLyricalThemes() {
        return Optional.ofNullable(lyricalThemes);
    }

    public Optional<String> getProvince() {
        return Optional.ofNullable(province);
    }

    public Optional<String> getGenre() {
        return Optional.ofNullable(genre);
    }

    public Optional<Integer> getYearFormedIn() {
        return Optional.ofNullable(yearFormedIn);
    }

}
