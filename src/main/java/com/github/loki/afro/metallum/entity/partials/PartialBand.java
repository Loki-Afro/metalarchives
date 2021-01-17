package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.API;

public class PartialBand extends LoadablePartial<Band> {

    public PartialBand(long id, String name) {
        super(id, name);
    }

    @Override
    public Band load() {
        return API.getBandById(getId());
    }
}
