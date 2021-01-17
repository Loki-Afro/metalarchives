package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.entity.AbstractEntity;
import com.github.loki.afro.metallum.entity.Identifiable;

public class Partial implements Identifiable {

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
