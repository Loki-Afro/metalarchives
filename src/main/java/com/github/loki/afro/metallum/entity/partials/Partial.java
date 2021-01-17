package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.entity.Identifiable;
import lombok.ToString;

@ToString
abstract class Partial implements Identifiable {

    private final long id;
    private final String name;

    public Partial(long id, String name) {
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
