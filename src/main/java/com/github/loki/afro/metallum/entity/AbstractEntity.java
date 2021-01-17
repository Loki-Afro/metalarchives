package com.github.loki.afro.metallum.entity;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractEntity extends AbstractIdentifiable {

    @Getter
    @Setter
    protected String addedBy;
    @Getter
    @Setter
    protected String modifiedBy;
    @Getter
    @Setter
    protected String addedOn;
    @Getter
    @Setter
    protected String lastModifiedOn;

    public AbstractEntity(final long id, final String name) {
        super(id, name);
    }

}
