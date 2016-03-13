package de.loki.metallum.entity;

import de.loki.metallum.enums.DiscType;

public class Track extends AbstractEntity {

	private Disc    discFromTrack = new Disc();
	private boolean instrumental  = false;

	private String lyrics = "";
	private String playTime;
	private int    trackNumber;
	private String splitBandName = "";
	/**
	 * 1 by default.
	 */
	private int    discNumber    = 1;

	/**
	 * the Track will always have the id from the Disc!
	 *
	 * @param id
	 */
	public Track(final long id) {
		super(id);
	}

	public Track() {
		super(0);
	}

	public void setDiscName(final String discName) {
		this.discFromTrack.setName(discName);
	}

	public String getBandName() {
		if (this.discFromTrack.isSplit()) {
			return this.splitBandName;
		} else {
			return this.discFromTrack.getBand().getName();
		}
	}

	// exists because there are also split discs, and we have no chance to determine which track is
	// from which band
	@Deprecated
	public void setBandName(final String bandName) {
		this.splitBandName = bandName;
	}

	public void setSplitBandName(final String bandName) {
		this.splitBandName = bandName;
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

	public void setDiscType(final DiscType discType) {
		this.discFromTrack.setDiscType(discType);
	}

	public Disc getDiscOfThisTrack() {
		return this.discFromTrack;
	}

	public void setGenre(final String genre) {
		this.discFromTrack.getBand().setGenre(genre);
	}

	public void setLyrics(final String lyrics) {
		this.lyrics = lyrics;
	}

	public Band getBand() {
		return this.discFromTrack.getBand();
	}

	public String getGenre() {
		return this.discFromTrack.getGenre();
	}

	public String getLyrics() {
		return this.lyrics;
	}

	public DiscType getDiscTyp() {
		return this.discFromTrack.getType();
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
		if (this.discFromTrack.getDiscCount() < discNumber) {
			this.discFromTrack.setDiscCount(discNumber);
		}
	}

	public int getDiscNumber() {
		return this.discNumber;
	}

	public String getDiscName() {
		return this.discFromTrack.getName();
	}

	public void setDisc(final Disc disc) {
		this.discFromTrack = disc;
	}

	public Disc getDisc() {
		return this.discFromTrack;
	}

}
