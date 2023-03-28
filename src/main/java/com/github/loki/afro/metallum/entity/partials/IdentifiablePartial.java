package com.github.loki.afro.metallum.entity.partials;

import lombok.ToString;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentifiablePartial<?> that = (IdentifiablePartial<?>) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
