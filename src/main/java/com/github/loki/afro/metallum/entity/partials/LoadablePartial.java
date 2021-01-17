package com.github.loki.afro.metallum.entity.partials;

abstract class LoadablePartial<X> extends Partial {
    public LoadablePartial(long id, String name) {
        super(id, name);
    }

    public abstract X load();
}
