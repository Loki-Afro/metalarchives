package com.github.loki.afro.metallum.entity.partials;

import lombok.ToString;

@ToString
abstract class IdentifiablePartial<X> extends Partial<X> {
    private final long id;
    private final String name;

    public IdentifiablePartial(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
