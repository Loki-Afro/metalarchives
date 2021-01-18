package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.entity.partials.PartialLabel;
import com.github.loki.afro.metallum.entity.partials.PartialReview;
import com.github.loki.afro.metallum.enums.DiscType;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Disc extends AbstractEntity {

    private DiscType type;
    private List<Track> trackList = new ArrayList<>();
    private List<PartialReview> reviewList = new ArrayList<>();
    private String releaseDate;
    private PartialBand band;
    private PartialLabel label;
    private BufferedImage artwork = null;
    private String details;
    private String artworkURL = null;
    /**
     * Only filled if DiscType.isSplit(type) is true
     * Does only contain BandName and Id.
     */
    private final List<PartialBand> splitBands = new ArrayList<>();

    private Map<Member, String> lineup = new HashMap<>();
    private Map<Member, String> miscMember = new HashMap<>();
    private Map<Member, String> guestMember = new HashMap<>();

    private int discCount = 1;

    public Disc(final long id, String name) {
        super(id, name);
    }

    public final float getReviewPercentAverage() {
        float k = 0;
        for (final PartialReview review : this.reviewList) {
            k += review.getPercentage();
        }
        return k / this.reviewList.size();
    }

    public final boolean hasReviews() {
        return !this.reviewList.isEmpty();
    }

    public int getReviewCount() {
        return this.reviewList.size();
    }

    public DiscType getType() {
        return this.type;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(final String date) {
        this.releaseDate = date;
    }

    public int getTrackCount() {
        return this.trackList.size();
    }

    public List<Review> getReviews() {
        return this.reviewList.stream()
                .map(PartialReview::load)
                .collect(Collectors.toList());
    }

    public List<PartialReview> getPartialReviews() {
        return this.reviewList;
    }

    public void setDiscType(final DiscType discType) {
        this.type = discType;
    }

    public PartialBand getBand() {
        return this.band;
    }

    public Label getLabel() {
        return this.label.load();
    }

    public PartialLabel getLabelPartial() {
        return this.label;
    }

    public void setLabel(final PartialLabel label) {
        this.label = label;
    }

    public void setBand(final PartialBand band) {
        this.band = band;
    }

    public void addTracks(final List<Track> tracks) {
        trackList.addAll(tracks);
    }

    public void setArtwork(final BufferedImage artwork) {
        this.artwork = artwork;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public boolean isSplit() {
        return DiscType.isSplit(this.type);
    }

    public BufferedImage getArtwork() {
        return this.artwork;
    }

    public String getDetails() {
        return this.details;
    }


    public void setLineup(final Map<Member, String> lineUp) {
        this.lineup = lineUp;
    }

    public void setMiscLineup(final Map<Member, String> miscMember) {
        this.miscMember = miscMember;
    }

    public void setGuestLineup(final Map<Member, String> guestMember) {
        this.guestMember = guestMember;
    }

    public List<Track> getTrackList() {
        return this.trackList;
    }

    public List<Track> getTrackListOnDisc(final int discNumber) {
        final List<Track> trackListByDiscNumber = new ArrayList<>();
        for (final Track track : this.trackList) {
            if (track.getDiscNumber() == discNumber) {
                trackListByDiscNumber.add(track);
            }

        }
        return trackListByDiscNumber;
    }

    public Map<Member, String> getLineup() {
        return this.lineup;
    }

    public Map<Member, String> getMiscMember() {
        return this.miscMember;
    }

    public Map<Member, String> getGuestMember() {
        return this.guestMember;
    }

    public List<PartialBand> getSplitBands() {
        return this.splitBands;
    }

    public int getDiscCount() {
        return this.discCount;
    }

    public void setDiscCount(final int discCount) {
        this.discCount = discCount;
    }

    public String getBandName() {
        if (this.type == DiscType.COLLABORATION) {
            return getSplitBandsAsString();
        } else if (this.type.isSplit()) {
            return getSplitBandsAsString();
        } else {
            return this.band.getName();
        }
    }

    private String getSplitBandsAsString() {
        return getSplitBands().stream()
                .map(PartialBand::getName)
                .collect(Collectors.joining(" / "));
    }

    public final boolean hasArtwork() {
        return this.artwork != null;
    }

    public void setReviews(final List<PartialReview> reviewList) {
        this.reviewList = reviewList;
    }

    public void setSplitBands(final List<PartialBand> splitBands) {
        this.splitBands.addAll(splitBands);
    }

    public final void setTrackList(final List<Track> newTrackList) {
        this.trackList = newTrackList;
    }

    public String getArtworkURL() {
        return this.artworkURL;
    }

    public void setArtworkURL(final String artworkURL) {
        this.artworkURL = artworkURL;
    }

    /**
     * This Method is for convenience only.
     * It calls {@link Disc#getLineup()}, {@link Disc#getMiscMember()} and {@link Disc#getGuestMember()} and puts them all together in one map.
     *
     * @return a map with all Member, where the value the role at the specific album;
     */
    public Map<Member, String> getMember() {
        Map<Member, String> completeMemberMap = getLineup();
        completeMemberMap.putAll(getMiscMember());
        completeMemberMap.putAll(getGuestMember());
        return completeMemberMap;
    }

}