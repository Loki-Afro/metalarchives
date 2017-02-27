package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.parser.search.TrackSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

/**
 * Represents the advanced search for tracks. This class is to assemble the Query we are using to
 * get the interesting content
 *
 * @author Zarathustra
 *
 * All methods should have very similar names to the fields at this site
 * "Song title, exact Match?" -&gt; setSongTitle(String titleName, boolean exactMatch)
 */
public class TrackSearchQuery extends AbstractSearchQuery<Track> {

    private boolean exactTitleNameMatch = false;
    private boolean exactBandNameMatch = false;
    private boolean exactReleaseNameMatch = false;
    private final List<DiscType> discTypes = new ArrayList<>();

    public TrackSearchQuery() {
        super(new Track());
    }

    public TrackSearchQuery(final Track inputTrack) {
        super(inputTrack);
    }

    public void setSongTitle(final String titleName, final boolean exactMatch) {
        this.searchObject.setName(titleName);
        this.exactTitleNameMatch = exactMatch;
    }

    public void setBandName(final String string, final boolean exactMatch) {
        this.searchObject.getBand().setName(string);
        this.exactBandNameMatch = exactMatch;
    }

    /**
     * To set the album/demo/EP, what ever name
     *
     * @param releaseName the name where the track could be found
     * @param exactMatch  if the track's album/demo/EP what ever we are searching for equals the
     *                    releaseName
     */
    public void setReleaseTitle(final String releaseName, final boolean exactMatch) {
        this.searchObject.getDiscOfThisTrack().setName(releaseName);
        this.exactReleaseNameMatch = exactMatch;
    }

    /**
     * Setters the lyrics of the track we are searching for
     *
     * @param lyrics you do not need to know the full lyrics, I think its enough to know some words
     */
    public void setLyrics(final String lyrics) {
        this.searchObject.setLyrics(lyrics);
    }

    public void setGenre(final String genre) {
        this.searchObject.setGenre(genre);
    }

    public void setDiscType(final DiscType releaseType) {
        this.discTypes.add(releaseType);
    }

    public void setDiscTypes(final DiscType... releaseTypes) {
        Collections.addAll(this.discTypes, releaseTypes);
    }

    private final String asPair(String attribute, String toAdd) {
        this.isAValidQuery = (this.isAValidQuery || !toAdd.isEmpty());
        return attribute + "=" + MetallumURL.asURLString(toAdd);
    }

    private final String getDiscTypes() {
        final StringBuilder buf = new StringBuilder();
        for (final DiscType type : this.discTypes) {
            this.isAValidQuery = true;
            buf.append("releaseType[]=" + type.asSearchNumber() + "&");
        }
        return buf.toString();
    }

    @Override
    protected final String assembleSearchQuery(final int startPage) {
        final StringBuilder searchQueryBuf = new StringBuilder();
        searchQueryBuf.append(asPair("bandName", this.searchObject.getBandName()));
        searchQueryBuf.append(asPair("songTitle", this.searchObject.getName()));
        searchQueryBuf.append(asPair("releaseTitle", this.searchObject.getDiscName()));
        searchQueryBuf.append(asPair("lyrics", this.searchObject.getLyrics()));
        searchQueryBuf.append(asPair("genre", this.searchObject.getGenre()));

        searchQueryBuf.append("exactSongMatch=" + (this.exactTitleNameMatch ? 1 : 0) + "&");
        searchQueryBuf.append("exactBandMatch=" + (this.exactBandNameMatch ? 1 : 0) + "&");
        searchQueryBuf.append("exactReleaseMatch=" + (this.exactReleaseNameMatch ? 1 : 0) + "&");

        searchQueryBuf.append(getDiscTypes());
        return MetallumURL.assembleTrackSearchURL(searchQueryBuf.toString(), startPage);
    }

    @Override
    protected void setSpecialFieldsInParser(final AbstractSearchParser<Track> parser) {
        final TrackSearchParser trackParser = (TrackSearchParser) parser;
        trackParser.setIsAbleToParseGenre(!this.searchObject.getGenre().isEmpty());
        trackParser.setIsAbleToParseDiscType(isAbleToParseDiscType());
    }

    private boolean isAbleToParseDiscType() {
        if (this.discTypes.isEmpty()) {
            return true;
        }
        // is able when there are more than 2 entries in the list
        int foundTypes = 0;
        for (final DiscType type : this.discTypes) {
            if (type != null && ++foundTypes > 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        this.exactTitleNameMatch = false;
        this.exactBandNameMatch = false;
        this.exactReleaseNameMatch = false;
        this.searchObject = new Track();
    }

    @Override
    protected SortedMap<SearchRelevance, List<Track>> enrichParsedEntity(final SortedMap<SearchRelevance, List<Track>> resultMap) {
        if (this.discTypes.size() == 1) {
            final DiscType discType = this.discTypes.get(0);
            for (final List<Track> trackList : resultMap.values()) {
                for (final Track track : trackList) {
                    track.getDisc().setDiscType(discType);
                }
            }
        }
        return resultMap;
    }

}
