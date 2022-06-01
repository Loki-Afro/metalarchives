package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.search.API;

public class PartialLabel extends IdentifiablePartial<Label> {
    public PartialLabel(long id, String name) {
        super(id, name);
    }

    @Override
    public Label load() {
        return API.getLabelById(getId());
    }
}
