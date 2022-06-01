package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.SearchTrackResult;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.entity.TrackQuery;
import com.github.loki.afro.metallum.search.query.entity.TrackSearchQuery;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TrackSearchServiceTest {

    @Test
    public void searchTestIdWithoutLyrics() {
        long trackId = 4337410L;
        String trackName = "Hunger";

        Iterable<SearchTrackResult> searchTrackResults = new TrackSearchService().get(TrackQuery.builder().bandName("Eaten")
                .discName("Eaten")
                .bandName("Eaten")
                .name(trackName)
                .build());

        SearchTrackResult result = Iterables.getOnlyElement(searchTrackResults);
        assertThat(result.getId()).isEqualTo(trackId);

        Disc disc = API.getDiscById(result.getDiscId());
        assertThat(disc.getTrackList())
                .extracting(Track::getId, Track::getName)
                .contains(tuple(trackId, trackName));
    }

    @Test
    public void songTitleTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Fucking Wizard", false);
        final SearchTrackResult resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
        assertDefault(resultTrack);
    }

    @Test
    public void songTitleExactMatchTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Fucking Wizard", true);
        final SearchTrackResult resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
        assertDefault(resultTrack);
    }

    @Test
    public void bandNameTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setBandName("Drudkh", false);
        final List<SearchTrackResult> resultTrackList = service.performSearch(query);
        for (final SearchTrackResult resultTrack : resultTrackList) {
            assertThat(resultTrack.getResolvedBandName()).contains("Drudkh");
            assertDefault(resultTrack);
        }
    }

    @Test
    public void bandNameExactMatchTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Cromwell", false);
        query.setBandName("Reverend Bizarre", true);
        final SearchTrackResult resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Cromwell");
        assertDefault(resultTrack);
    }

    @Test
    public void releaseTitleTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setReleaseTitle("Vîrstele Pămîntului", false);
        final List<SearchTrackResult> resultTrackList = service.performSearch(query);
        for (final SearchTrackResult resultTrack : resultTrackList) {
            assertThat(resultTrack.getDiscName().contains("Vîrstele pămîntului")).isTrue();
            assertDefault(resultTrack);
        }
    }

    @Test
    public void releaseTitleExactTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setReleaseTitle("Vîrstele Pămîntului", true);
        final List<SearchTrackResult> resultTrackList = service.performSearch(query);
        for (final SearchTrackResult resultTrack : resultTrackList) {
            assertThat(resultTrack.getDiscName().contains("Vîrstele pămîntului")).isTrue();
            assertDefault(resultTrack);
        }
    }


    @Test
    public void singleReleaseTypeTest() throws MetallumException {
        TrackQuery query = TrackQuery.builder()
                .name("Ars Poetica")
                .discType(DiscType.FULL_LENGTH)
                .build();

        final SearchTrackResult resultTrack = Iterables.getOnlyElement(new TrackSearchService().get(query));
        assertThat(resultTrack.getBandName()).isEqualTo("Drudkh");
        assertThat(resultTrack.getDiscName()).isEqualTo("Microcosmos");
        assertThat(resultTrack.getName()).isEqualTo("Ars Poetica");
        assertThat(resultTrack.getBandId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscType()).contains(DiscType.FULL_LENGTH);
        assertThat(resultTrack.getLyrics()).isEmpty();
    }

    @Test
    public void multiReleaseTypeTest() throws MetallumException {
        TrackQuery query = TrackQuery.builder()
                .name("Solitary Endless Path")
                .discTypes(Sets.newHashSet(DiscType.FULL_LENGTH, DiscType.DEMO, DiscType.EP))
                .build();

        final SearchTrackResult resultTrack = Iterables.getOnlyElement(new TrackSearchService().get(query));
        assertThat(resultTrack.getBandName()).isEqualTo("Drudkh");
        assertThat(resultTrack.getDiscName()).isEqualTo("Відчуженість (Estrangement)");
        assertThat(resultTrack.getName()).isEqualTo("Самітня нескінченна тропа (Solitary Endless Path)");
        assertDefault(resultTrack);
    }

    private void assertDefault(SearchTrackResult resultTrack) {
        assertThat(resultTrack.getBandId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscType()).isNotNull();
        assertThat(resultTrack.getLyrics()).isEmpty();
    }

    @Test
    public void lyricsSearchTest() throws MetallumException {

        TrackQuery query = TrackQuery.builder()
                .name("Fucking Wizard")
                .discType(DiscType.FULL_LENGTH)
                .lyrics("I'm your saviour, your")
                .build();

        final SearchTrackResult resultTrack = Iterables.getOnlyElement(new TrackSearchService().get(query));

        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
        assertThat(resultTrack.getBandId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscType()).contains(DiscType.FULL_LENGTH);
        assertThat(resultTrack.getLyrics()).isEmpty();
    }

    @Test
    public void lyricsTest() throws MetallumException {
        TrackQuery query = TrackQuery.builder()
                .name("War")
                .bandName("Burzum")
                .discName("Burzum")
                .discType(DiscType.FULL_LENGTH)
                .build();

        final Track resultTrack = new TrackSearchService().getSingleUniqueByQuery(query);
        assertThat(resultTrack.getBandName()).isEqualTo("Burzum");
        assertThat(resultTrack.getDiscName()).isEqualTo("Burzum");
        assertThat(resultTrack.getName()).isEqualTo("War");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getDiscType()).isSameAs(DiscType.FULL_LENGTH);
        assertThat(resultTrack.getLyrics().get()).startsWith("This is war!");
        assertThat(resultTrack.getLyrics().get()).endsWith("War!");
    }

    @Test
    public void genreTest() throws MetallumException {
        TrackQuery query = TrackQuery.builder()
                .name("Fucking Wizard")
                .discType(DiscType.FULL_LENGTH)
                .genre("Doom Metal")
                .build();


        final Iterable<Track> resultTrackList = new TrackSearchService().getFully(query);
        for (final Track resultTrack : resultTrackList) {
            assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
            assertThat(resultTrack.getDiscName().isEmpty()).isFalse();
            assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
            assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDisc().getDiscType()).isNotNull();
            assertThat(resultTrack.getLyricsPartial()).isNotNull();
        }
    }

    @Test
    public void metallumExceptionTest() {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        assertThatThrownBy(() -> service.performSearchAndLoadFully(query)).isInstanceOf(MetallumException.class);
    }

    /*
     * This was once an issue, it happened randomly:
     * https://code.google.com/p/metalarchives/issues/detail?id=1&can=1
     */
    @Test
    public void wasOnceAnIssue() throws MetallumException {
        TrackQuery query = TrackQuery.builder()
                .name("Fatal Self-Inflicted Disfigurement")
                .exactNameMatch(true)
                .discType(DiscType.FULL_LENGTH)
                .build();
        Iterable<SearchTrackResult> trackList = new TrackSearchService().get(query);
        SearchTrackResult track = Iterables.getOnlyElement(trackList);

        assertThat(track.getBandName()).isEqualTo("Defeated Sanity");
        assertThat(track.getDiscName()).isEqualTo("Psalms of the Moribund");
        assertThat(track.getName()).isEqualTo("Fatal Self-Inflicted Disfigurement");
        assertThat(track.getGenre()).isNull();
    }

    @Test
    public void bla() {
        API.getTracks(TrackQuery.builder()
                .name("")
                .build()
        );
    }

}
