package com.github.loki.afro.metallum.search.query.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@SuperBuilder
@NoArgsConstructor
public abstract class AbstractTrack {

    @Setter
    String name;

    @Setter
    String bandName;

    @Setter
    String discName;

    @Setter
    String genre;

    @Setter
    private String lyrics;


    public Optional<String> getLyrics() {
        return Optional.ofNullable(lyrics);
    }
}
