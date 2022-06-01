package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.entity.partials.PartialLyrics;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.DiscType;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

public class Track extends AbstractEntity {

    private final PartialDisc partialDisc;
    private final PartialBand bandPartial;
    private boolean instrumental = false;

    private PartialLyrics partialLyrics;
    private String playTime;
    private int trackNumber;
    private int discNumber = 1;
    private String collaborationBandName;


    public Track(Disc disc, String bandName, long id, String name) {
        this(disc, disc.getBand().getId(), bandName, id, name);
    }

    private Track(Disc disc, long bandId, String bandName, long id, String name) {
        this(PartialDisc.of(disc),
                new PartialBand(bandId, bandName),
                id, name);
    }

    public Track(PartialDisc disc, PartialBand bandPartial, long id, String name) {
        super(id, name);
        this.partialDisc = disc;
        this.bandPartial = bandPartial;
    }

    public static Track createCollaborationTrack(Disc disc, long trackId, String name) {
        if (disc.getType() != DiscType.COLLABORATION) {
            throw new IllegalStateException();
        }
        PartialDisc partialDisc = new PartialDisc(disc.getId(), disc.getName(), disc.getType());
        Track track = new Track(partialDisc, null, trackId, name);
        track.collaborationBandName = disc.getBandName();
        return track;
    }

    public static Track createSplitTrack(Disc disc, String bandName, long trackId, String trackTitle) {
        List<PartialBand> splitBands = disc.getSplitBands();
        Optional<PartialBand> first = splitBands.stream()
                .filter(pb -> pb.getName().equals(bandName))
                .findFirst();
        if (first.isPresent()) {
            return new Track(disc, first.get().getId(), bandName, trackId, trackTitle);
        } else {
            throw new IllegalStateException("could not find split band from previously parsed disc with id " + disc.getId());
        }
    }


    public String getBandName() {
        if (partialDisc.getDiscType() == DiscType.COLLABORATION) {
            return this.collaborationBandName;
        } else if (this.partialDisc.getDiscType().isSplit()) {
            return this.bandPartial.getName();
        } else {
            return this.bandPartial.getName();
        }
    }

    public String getPlayTime() {
        return this.playTime;
    }

    public int getTrackNumber() {
        return this.trackNumber;
    }

    public boolean isInstrumental() {
        return this.instrumental;
    }

    public void setLyrics(final PartialLyrics partialLyrics) {
        this.partialLyrics = partialLyrics;
    }

    public Optional<PartialLyrics> getLyricsPartial() {
        return Optional.ofNullable(partialLyrics);
    }

    public Optional<String> getLyrics() {
        return Optional.ofNullable(partialLyrics).map(PartialLyrics::load);
    }

    public void setPlayTime(final String playTime) {
        this.playTime = playTime;
    }

    public void setTrackNumber(final int trackNo) {
        this.trackNumber = trackNo;
    }

    public void setInstrumental(final boolean b) {
        this.instrumental = b;
    }

    public void setDiscNumber(final int discNumber) {
        this.discNumber = discNumber;
    }

    public int getDiscNumber() {
        return this.discNumber;
    }

    public String getDiscName() {
        return partialDisc.getName();
    }

    public PartialDisc getDisc() {
        return partialDisc;
    }

    public PartialBand getBand() {
        return bandPartial;
    }

    public static final class PartialDisc extends com.github.loki.afro.metallum.entity.partials.PartialDisc {

        @Getter
        private final DiscType discType;

        public PartialDisc(long id, String name, DiscType discType) {
            super(id, name);
            this.discType = discType;
        }

        static PartialDisc of(Disc disc) {
            return new PartialDisc(disc.getId(), disc.getName(), disc.getType());
        }
    }

}
