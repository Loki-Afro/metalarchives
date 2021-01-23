package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;

import java.time.Month;
import java.time.Year;

public class DateQuery implements IQuery {

    private final Month month;
    private final Year year;

    public DateQuery(Month month, Year year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public boolean isValid() {
        return month != null && year != null;
    }

    @Override
    public String assembleQueryUrl(int offset) {
        return MetallumURL.assembleBandsByModificationDateUrl(offset, this.year, this.month);
    }
}
