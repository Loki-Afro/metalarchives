package com.github.loki.afro.metallum.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class AbstractIdentifiable implements Identifiable {

    private final long id;
    private final String name;

    public AbstractIdentifiable(long id, String name) {
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
