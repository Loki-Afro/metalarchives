package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.enums.DiscType;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class SearchTrackResult extends AbstractTrack implements Identifiable {


    @Getter
    @Setter
    private long discId;

    @Getter
    @Setter
    private long bandId;

    @Setter
    private DiscType discType;

    @Getter
    @Setter
    private String splitBandName;

    @Getter
    private final long id;

    public SearchTrackResult(long id, String name) {
        this.id = id;
        setName(name);
    }

    public String getName() {
        return name;
    }

    public Optional<DiscType> getDiscType() {
        return Optional.ofNullable(discType);
    }


    public String getBandName() {
        return this.bandName;

    }

    public String getDiscName() {
        return this.discName;
    }

    public String getGenre() {
        return genre;
    }


    public String getResolvedBandName() {
        if (this.discType == DiscType.COLLABORATION) {
            return splitBandName;
        } else if (this.discType.isSplit()) {
            return this.splitBandName;
        } else {
            return getBandName();
        }
    }
}
