package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.entity.Band;

public final class NullBand extends PartialBand {
    public NullBand(String name) {
        super(-666L, name);
    }

    @Override
    public Band load() {
        throw new IllegalStateException("Band can not be loaded due to it is not recognized by encyclopedia metallum," +
                "this can happen if it appears on a split but not actually on encyclopedia metallum with an id");
    }
}
