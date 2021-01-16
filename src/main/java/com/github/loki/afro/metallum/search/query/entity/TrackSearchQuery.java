package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.search.AbstractSearchQuery;

@Deprecated
public class TrackSearchQuery extends AbstractSearchQuery<TrackQuery> {

    public TrackSearchQuery() {
        super(TrackQuery.builder().build());
    }

    public TrackSearchQuery(final TrackQuery inputTrack) {
        super(inputTrack);
    }

    public void setSongTitle(final String titleName, final boolean exactMatch) {
        this.searchObject.setName(titleName);
        this.searchObject.setExactNameMatch(exactMatch);
    }

    public void setBandName(final String string, final boolean exactMatch) {
        this.searchObject.setBandName(string);
        this.searchObject.setExactBandNameMatch(exactMatch);
    }

    /**
     * To set the album/demo/EP, what ever name
     *
     * @param releaseName the name where the track could be found
     * @param exactMatch  if the track's album/demo/EP what ever we are searching for equals the
     *                    releaseName
     */
    public void setReleaseTitle(final String releaseName, final boolean exactMatch) {
        this.searchObject.setDiscName(releaseName);
        this.searchObject.setExactDiscNameMatch(exactMatch);
    }

    /**
     * Sets the lyrics of the track we are searching for
     *
     * @param lyrics you do not need to know the full lyrics, I think its enough to know some words
     */
    public void setLyrics(final String lyrics) {
        this.searchObject.setLyrics(lyrics);
    }

    public void setGenre(final String genre) {
        this.searchObject.setGenre(genre);
    }

}
