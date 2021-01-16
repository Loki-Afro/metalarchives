package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.enums.Country;
import lombok.Getter;
import lombok.Setter;

public class SearchLabelResult implements Identifiable {
    @Getter
    private final long id;
    @Getter
    private final String name;
    @Setter
    @Getter
    private Country country;
    @Setter
    @Getter
    private String specialisation;

    public SearchLabelResult(long id, String name) {
        this.id = id;
        this.name = name;
    }


}
