package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.Country;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SearchMemberResult implements Identifiable {

    private final long id;
    private final String name;
    @Getter
    @Setter
    private String alternativeName;
    @Getter
    @Setter
    private String realName;
    @Getter
    @Setter
    private Country country;
    @Getter
    @Setter
    private List<PartialBand> bands;

    public SearchMemberResult(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

}
