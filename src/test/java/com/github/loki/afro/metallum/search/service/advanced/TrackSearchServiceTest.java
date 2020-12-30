package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.TrackSearchQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TrackSearchServiceTest {

    @Test
    public void songTitleTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Fucking Wizard", false);
        final Track resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscTyp()).isNotNull();
        assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
    }

    @Test
    public void songTitleExactMatchTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Fucking Wizard", true);
        final Track resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscTyp()).isNotNull();
        assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
    }

    @Test
    public void bandNameTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setBandName("Drudkh", false);
        final List<Track> resultTrackList = service.performSearch(query);
        for (final Track resultTrack : resultTrackList) {
            assertThat(resultTrack.getBandName().contains("Drudkh")).isTrue();
            assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDiscTyp()).isNotNull();
            assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
        }
    }

    @Test
    public void bandNameExactMatchTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Cromwell", false);
        query.setBandName("Reverend Bizarre", true);
        final Track resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Cromwell");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscTyp()).isNotNull();
        assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
    }

    @Test
    public void releaseTitleTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setReleaseTitle("Vîrstele Pămîntului", false);
        final List<Track> resultTrackList = service.performSearch(query);
        assertVirstele(resultTrackList);
    }

    private void assertVirstele(List<Track> resultTrackList) {
        for (final Track resultTrack : resultTrackList) {
            assertThat(resultTrack.getDiscName().contains("Vîrstele pămîntului")).isTrue();
            assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDiscTyp()).isNotNull();
            assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
        }
    }

    @Test
    public void releaseTitleExactTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setReleaseTitle("Vîrstele Pămîntului", true);
        final List<Track> resultTrackList = service.performSearch(query);
        assertVirstele(resultTrackList);
    }

    @Test
    public void singleReleaseTypeTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Ars Poetica", false);
        query.setDiscType(DiscType.FULL_LENGTH);
        final Track resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Drudkh");
        assertThat(resultTrack.getDiscName()).isEqualTo("Microcosmos");
        assertThat(resultTrack.getName()).isEqualTo("Ars Poetica");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(DiscType.FULL_LENGTH).isSameAs(resultTrack.getDiscTyp());
        assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
    }

    @Test
    public void multiReleaseTypeTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Solitary Endless Path", false);
        query.setDiscTypes(DiscType.FULL_LENGTH, DiscType.DEMO, DiscType.EP);
        final Track resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Drudkh");
        assertThat(resultTrack.getDiscName()).isEqualTo("Відчуженість (Estrangement)");
        assertThat(resultTrack.getName()).isEqualTo("Самітня нескінченна тропа (Solitary Endless Path)");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDiscTyp()).isNotNull();
        assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
    }

    @Test
    public void lyricsSearchTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Fucking Wizard", false);
        query.setDiscType(DiscType.FULL_LENGTH);
        query.setLyrics("I'm your saviour, your");
        final Track resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(resultTrack.getDiscName()).isEqualTo("II: Crush the Insects");
        assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(DiscType.FULL_LENGTH).isSameAs(resultTrack.getDiscTyp());
        assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
    }

    @Test
    public void lyricsTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService(true);
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("War", false);
        query.setBandName("Burzum", false);
        query.setReleaseTitle("Burzum", false);
        query.setDiscType(DiscType.FULL_LENGTH);
        final Track resultTrack = service.performSearch(query).get(0);
        assertThat(resultTrack.getBandName()).isEqualTo("Burzum");
        assertThat(resultTrack.getDiscName()).isEqualTo("Burzum");
        assertThat(resultTrack.getName()).isEqualTo("War");
        assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
        assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
        assertThat(DiscType.FULL_LENGTH).isSameAs(resultTrack.getDiscTyp());
        assertThat(resultTrack.getLyrics().isEmpty()).isFalse();
        assertThat(resultTrack.getLyrics()).startsWith("This is war!");
        assertThat(resultTrack.getLyrics()).endsWith("War!");
    }

    @Test
    public void genreTest() throws MetallumException {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Fucking Wizard", false);
        query.setDiscType(DiscType.FULL_LENGTH);
        query.setGenre("Doom Metal");
        final List<Track> resultTrackList = service.performSearch(query);
        for (final Track resultTrack : resultTrackList) {
            assertThat(resultTrack.getBandName()).isEqualTo("Reverend Bizarre");
            assertThat(resultTrack.getDiscName().isEmpty()).isFalse();
            assertThat(resultTrack.getName()).isEqualTo("Fucking Wizard");
            assertThat(resultTrack.getBand().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDisc().getId()).isNotSameAs(0);
            assertThat(resultTrack.getDiscTyp()).isNotNull();
            assertThat(resultTrack.getLyrics().isEmpty()).isTrue();
        }
    }

    @Test
    public void metallumExceptionTest() {
        final TrackSearchService service = new TrackSearchService();
        final TrackSearchQuery query = new TrackSearchQuery();
        assertThatThrownBy(() -> service.performSearch(query)).isInstanceOf(MetallumException.class);
    }

    /*
     * This was once an issue, it happened randomly:
     * https://code.google.com/p/metalarchives/issues/detail?id=1&can=1
     */
    @Test
    public void wasOnceAnIssue() throws MetallumException {
        TrackSearchService service = new TrackSearchService();
        TrackSearchQuery query = new TrackSearchQuery();
        query.setSongTitle("Fatal Self-Inflicted Disfigurement", true);
        query.setDiscType(DiscType.FULL_LENGTH);
        service.performSearch(query);
        List<Track> trackList = service.getResultAsList();
        assertThat(trackList).hasSize(1);
        Track track = trackList.get(0);

        assertThat(track.getBandName()).isEqualTo("Defeated Sanity");
        assertThat(track.getDiscName()).isEqualTo("Psalms of the Moribund");
        assertThat(track.getName()).isEqualTo("Fatal Self-Inflicted Disfigurement");
    }

}
