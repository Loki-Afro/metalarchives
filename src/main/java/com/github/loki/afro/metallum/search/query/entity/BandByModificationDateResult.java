package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.enums.Country;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BandByModificationDateResult implements Identifiable {

    @Getter
    private final long id;
    @Getter
    private final String name;
    @Getter
    private final Country country;
    @Getter
    private final String genre;
    @Getter
    private final String modifiedOn;

}
