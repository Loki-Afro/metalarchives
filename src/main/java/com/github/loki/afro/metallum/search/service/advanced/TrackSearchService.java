package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.core.parser.search.TrackSearchParser;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.entity.partials.PartialLyrics;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.SearchTrackResult;
import com.github.loki.afro.metallum.search.query.entity.TrackQuery;

import java.util.function.Function;

public class TrackSearchService extends AbstractSearchService<Track, TrackQuery, SearchTrackResult> {

    @Override
    protected Function<SearchTrackResult, Track> parseFully() {
        return searchResult -> {
            Track.PartialDisc partialDisc = new Track.PartialDisc(searchResult.getDiscId(), searchResult.getDiscName(), searchResult.getDiscType().orElse(null));
            PartialBand bandPartial = new PartialBand(searchResult.getBandId(), searchResult.getBandName());
            Track track = new Track(partialDisc, bandPartial, searchResult.getId(), searchResult.getName());
            track.setLyrics(new PartialLyrics(track.getId(), track.getName()));
            return track;
        };
    }

    @Override
    protected Function<Long, Track> getById() {
        throw new UnsupportedOperationException("currently tracks cannot be searched by id");
    }

    @Override
    protected final TrackSearchParser getSearchParser(TrackQuery trackQuery) {
        return new TrackSearchParser(trackQuery);
    }

}