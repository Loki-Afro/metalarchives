package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.enums.DiscType;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.Set;

import static com.github.loki.afro.metallum.search.query.entity.IQuery.asPair;
import static com.github.loki.afro.metallum.search.query.entity.IQuery.getForQuery;


@SuperBuilder
public class TrackQuery extends AbstractTrack implements IQuery {
    @Setter
    @Getter
    private boolean exactNameMatch;
    @Setter
    @Getter
    private boolean exactBandNameMatch;
    @Setter
    @Getter
    private boolean exactDiscNameMatch;
    @Singular
    @Setter
    @Getter
    private Set<DiscType> discTypes;

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<String> getBandName() {
        return Optional.ofNullable(bandName);
    }

    public Optional<String> getDiscName() {
        return Optional.ofNullable(discName);
    }

    public Optional<String> getGenre() {
        return Optional.ofNullable(genre);
    }

    @Override
    public boolean isValid() {
        boolean isAValidQuery = MetallumUtil.isNotBlank(getBandName());

        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getName()));
        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getDiscName()));
        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getLyrics()));
        isAValidQuery = (isAValidQuery || MetallumUtil.isNotBlank(getGenre()));

        isAValidQuery = (isAValidQuery || !getDiscTypes().isEmpty());

        return isAValidQuery;
    }

    @Override
    public String assembleQueryUrl(int offset) {
        final StringBuilder searchQueryBuf = new StringBuilder();
        searchQueryBuf.append(asPair("bandName", getBandName()));
        searchQueryBuf.append(asPair("songTitle", getName()));
        searchQueryBuf.append(asPair("releaseTitle", getDiscName()));
        searchQueryBuf.append(asPair("lyrics", getLyrics()));
        searchQueryBuf.append(asPair("genre", getGenre()));

        searchQueryBuf.append("exactSongMatch=" + (this.exactNameMatch ? 1 : 0) + "&");
        searchQueryBuf.append("exactBandMatch=" + (this.isExactBandNameMatch() ? 1 : 0) + "&");
        searchQueryBuf.append("exactReleaseMatch=" + (this.isExactDiscNameMatch() ? 1 : 0) + "&");

        searchQueryBuf.append(getForQuery("releaseType", this.discTypes, DiscType::asSearchNumber));
        return MetallumURL.assembleTrackSearchURL(searchQueryBuf.toString(), offset);
    }
}
