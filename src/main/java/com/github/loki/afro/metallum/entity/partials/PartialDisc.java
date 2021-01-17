package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.search.API;

public class PartialDisc extends LoadablePartial<Disc> {
    public PartialDisc(long id, String name) {
        super(id, name);
    }

    @Override
    public Disc load() {
        return API.getDiscById(getId());
    }
}
