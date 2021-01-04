package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.*;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.DiscSearchQuery;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class DiscSearchServiceTest {

    @Test
    public void objectToLoadTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        service.setObjectsToLoad(0);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Lantlôs", false);
        final List<Disc> discListResult = service.performSearch(query);
        assertThat(discListResult.isEmpty()).isFalse();
        for (final Disc discResult : discListResult) {
            assertThat(discResult.getBandName()).isEqualTo("Lantlôs");
            assertThat(discResult.getName().isEmpty()).isFalse();
            assertThat(discResult.getTrackList().isEmpty()).isTrue();
            assertThat(discResult.getType()).isNotNull();
            assertThat(discResult.getLabel()).isNotNull();
            assertThat(discResult.getArtwork()).isNull();
        }
    }

    @Test
    public void fullLengthTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.FULL_LENGTH);
        query.setReleaseName("Transilvanian Hunger", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Darkthrone");
        assertThat(discResult.getName()).isEqualTo("Transilvanian Hunger");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Peaceville Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void vhsTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.VIDEO);
        query.setReleaseName("Live Εσχατον: The Art of Rebellion", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Behemoth");
        assertThat(discResult.getName()).isEqualTo("Live Εσχατον: The Art of Rebellion");
        assertThat(discResult.getTrackList()).hasSize(12);
        assertThat(discResult.getType()).isEqualTo(DiscType.VIDEO);
        assertThat(discResult.getLabel().getName()).isEqualTo("Metal Mind Productions");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void epTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.EP);
        query.setReleaseName("Hordanes Land", false);
        service.setLoadLyrics(true);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Enslaved");
        assertThat(discResult.getName()).isEqualTo("Hordanes Land");
        assertThat(discResult.getTrackList()).hasSize(3);
        assertThat(discResult.getType()).isEqualTo(DiscType.EP);
        assertThat(discResult.getLabel().getName()).isEqualTo("Candlelight Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
        assertThat(discResult.getTrackList().get(1).getLyrics().isEmpty()).isFalse();
    }

    @Test
    public void dvdTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.VIDEO);
        query.setReleaseName("Return to Yggdrasill", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Enslaved");
        assertThat(discResult.getName()).isEqualTo("Return to Yggdrasill - Live in Bergen");
        assertThat(discResult.getTrackList()).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.VIDEO);
        assertThat(discResult.getLabel().getName()).isEqualTo("Tabu Recordings");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void liveTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.LIVE_ALBUM);
        query.setReleaseName("Live Kreation", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Kreator");
        assertThat(discResult.getName()).isEqualTo("Live Kreation");
        assertThat(discResult.getTrackListOnDisc(1)).hasSize(13);
        assertThat(discResult.getTrackListOnDisc(2)).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.LIVE_ALBUM);
        assertThat(discResult.getLabel().getName()).isEqualTo("Steamhammer");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void boxedSetTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.BOXED_SET);
        query.setReleaseName("In Memory of Quorthon", true);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Bathory");
        assertThat(discResult.getName()).isEqualTo("In Memory of Quorthon");
        assertThat(discResult.getTrackListOnDisc(1)).hasSize(4);
        assertThat(discResult.getType()).isEqualTo(DiscType.BOXED_SET);
        assertThat(discResult.getLabel().getName()).isEqualTo("Black Mark Production");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void splitTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.SPLIT);
        query.setReleaseName("Emperor / Hordanes Land", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getSplitBands().get(0).getName()).isEqualTo("Emperor");
        assertThat(discResult.getSplitBands().get(1).getName()).isEqualTo("Enslaved");
        assertThat(discResult.getBandName()).isEmpty();
        assertThat(discResult.getBand().getId()).isEqualTo(0L);
        assertThat(discResult.getName()).isEqualTo("Emperor / Hordanes Land");
        assertThat(discResult.getTrackList()).hasSize(7);
        assertThat(discResult.getType()).isEqualTo(DiscType.SPLIT);
        assertThat(discResult.getLabel().getName()).isEqualTo("Candlelight Records");
        assertThat(discResult.isSplit()).isTrue();
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void compilationTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.COMPILATION);
        query.setReleaseName("Abyssus Abyssum Invocat", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Behemoth");
        assertThat(discResult.getName()).isEqualTo("Abyssus Abyssum Invocat");
        assertThat(discResult.getTrackListOnDisc(1)).hasSize(11);
        assertThat(discResult.getTrackListOnDisc(2)).hasSize(7);
        assertThat(discResult.getType()).isEqualTo(DiscType.COMPILATION);
        assertThat(discResult.getLabel().getName()).isEqualTo("Peaceville Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void splitVideoTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.SPLIT_VIDEO);
        query.setReleaseName("Live & Plugged vol.2", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getSplitBands().get(0).getName()).isEqualTo("Dimmu Borgir");
        assertThat(discResult.getSplitBands().get(1).getName()).isEqualTo("Dissection");
        assertThat(discResult.getName()).isEqualTo("Live & Plugged Vol. 2");
        assertThat(discResult.getTrackList()).hasSize(17);
        assertThat(discResult.getType()).isEqualTo(DiscType.SPLIT_VIDEO);
        assertThat(discResult.getLabel().getName()).isEqualTo("Nuclear Blast");
        assertThat(discResult.isSplit()).isTrue();
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void demoTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.DEMO);
        query.setReleaseName("Burzum", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Burzum");
        assertThat(discResult.getName()).isEqualTo("Burzum");
        assertThat(discResult.getTrackList()).hasSize(2);
        assertThat(discResult.getType()).isEqualTo(DiscType.DEMO);
        assertThat(discResult.getLabel().getName()).isEqualTo("Independent");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void cityTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("A Touch of Medieval Darkness", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Desaster");
        assertThat(discResult.getName()).isEqualTo("A Touch of Medieval Darkness");
        assertThat(discResult.getTrackList()).hasSize(10);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Merciless Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void countryTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Autumn Aurora", false);
        query.setCountries(Country.UA);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Drudkh");
        assertThat(discResult.getName()).isEqualTo("Autumn Aurora");
        assertThat(discResult.getTrackList()).hasSize(6);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getBand().getCountry()).isEqualTo(Country.UA);
        assertThat(discResult.getLabel().getName()).isEqualTo("Supernal Music");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void multiCountryTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("The Somberlain", false);
        query.setReleaseTypes(DiscType.FULL_LENGTH);
        query.setCountries(Country.UA, Country.DE, Country.SE, Country.TW);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Dissection");
        assertThat(discResult.getName()).isEqualTo("The Somberlain");
        assertThat(discResult.getTrackList()).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("No Fashion Records");
        assertThat(discResult.getBand().getCountry()).isNotNull();
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void multiDiscTypeTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Alongside Death", false);
        query.setReleaseTypes(DiscType.FULL_LENGTH, DiscType.SPLIT);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("The Black");
        assertThat(discResult.getName()).isEqualTo("Alongside Death");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Hells Cargo");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void genreTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Stormblåst", false);
        query.setGenre("Symphonic Black Metal");
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Dimmu Borgir");
        assertThat(discResult.getName()).isEqualTo("Stormblåst");
        assertThat(discResult.getTrackList()).hasSize(10);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Cacophonous Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
        assertThat("January 25th, 1996").isEqualTo(discResult.getReleaseDate());
    }

    @Test
    public void releaseDateFromTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Det Som Engang Var", false);
        query.setReleaseMonthFrom(1);
        query.setReleaseYearFrom(1993);
        query.setReleaseYearTo(2007);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Burzum");
        assertThat(discResult.getName()).isEqualTo("Det som engang var");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Cymophane Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void releaseDateToTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Burzum", false);
        query.setReleaseMonthTo(4);
        query.setReleaseYearTo(1993);
        query.setReleaseTypes(DiscType.FULL_LENGTH);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Burzum");
        assertThat(discResult.getName()).isEqualTo("Burzum");
        assertThat(discResult.getTrackList()).hasSize(9);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Deathlike Silence Productions");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void labelNameTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Reverend Bizarre", false);
        query.setReleaseTypes(DiscType.FULL_LENGTH);
        query.setLabel("Spikefarm Records", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("II: Crush the Insects");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Spikefarm Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void indieLabelTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Practice Sessions", false);
        query.setLabel("", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("Practice Sessions");
        assertThat(discResult.getTrackList()).hasSize(4);
        assertThat(discResult.getType()).isEqualTo(DiscType.DEMO);
        assertThat(discResult.getLabel().getName()).isEqualTo("Independent");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    // NOT possible because there is a bug at metal-archives, exactMatch dosn't work

    @Test
    public void exactReleaseTitleTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("In the Rectory of the Bizarre Reverend", true);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("In the Rectory of the Bizarre Reverend");
        assertThat(discResult.getTrackList()).hasSize(6);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Sinister Figure");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    // NOT possible because there is a bug at metal-archives, exactMatch dosn't work
    // Also a bug to search for smth. with ":"

    @Test
    public void exactBandNameTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Reverend Bizarre", true);
        query.setReleaseName("II: Crush the Insects", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("II: Crush the Insects");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Spikefarm Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void artworkTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService(true);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Belphegor", false);
        query.setReleaseName("Pestapokalypse VI", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Belphegor");
        assertThat(discResult.getName()).isEqualTo("Pestapokalypse VI");
        assertThat(discResult.getTrackList()).hasSize(9);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Nuclear Blast");
        assertThat(discResult.getArtwork()).isNotNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void lyricsTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        service.setLoadLyrics(true);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Cannibal Corpse", false);
        query.setReleaseName("Eaten Back to Life", false);
        final Disc discResult = service.performSearch(query).get(0);
        for (final Track resultTrack : discResult.getTrackList()) {
            assertThat(resultTrack.getLyrics().isEmpty()).isFalse();
        }
        assertThat(discResult.getBandName()).isEqualTo("Cannibal Corpse");
        assertThat(discResult.getName()).isEqualTo("Eaten Back to Life");
        assertThat(discResult.getTrackList()).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Metal Blade Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void reviewsTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        service.setLoadReviews(true);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Cannibal Corpse", false);
        query.setReleaseName("The Wretched Spawn", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getReviews().isEmpty()).isFalse();
        for (final Review resultReview : discResult.getReviews()) {
            assertThat(resultReview.getAuthor().isEmpty()).isFalse();
            assertThat(resultReview.getContent().isEmpty()).isFalse();
            assertThat(resultReview.getName().isEmpty()).isFalse();
            assertThat(resultReview.getDate().isEmpty()).isFalse();
            assertThat(resultReview.getPercent() >= 0 && resultReview.getPercent() <= 100).isTrue();
            assertThat(resultReview.getId() != 0).isTrue();
            assertThat(discResult).isSameAs(resultReview.getDisc());
        }
        assertThat(discResult.getBandName()).isEqualTo("Cannibal Corpse");
        assertThat(discResult.getName()).isEqualTo("The Wretched Spawn");
        assertThat(discResult.getTrackList()).hasSize(13);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Metal Blade Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void unreleasedAlbumTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService(1, false, false, true);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("At the Gate of Sethu", true);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Nile");
        assertThat(discResult.getName()).isEqualTo("At the Gate of Sethu");
        assertThat(discResult.getTrackList()).hasSize(11);
        for (final Track track : discResult.getTrackList()) {
            assertThat(track.getPlayTime().isEmpty()).isFalse();
        }
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Nuclear Blast");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void metallumExcetionTest() {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        assertThatThrownBy(() -> service.performSearch(query)).isInstanceOf(MetallumException.class);
    }

    @Test
    public void directIDTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setSearchObject(new Disc(666L));
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Suffocation");
        assertThat(discResult.getName()).isEqualTo("Effigy of the Forgotten");
        assertThat(discResult.getTrackList()).hasSize(9);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("R/C Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void instrumentalTrackTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Vikingligr Veldi", false);
        final Disc discResult = service.performSearch(query).get(0);
        assertThat(discResult.getBandName()).isEqualTo("Enslaved");
        assertThat(discResult.getName()).isEqualTo("Vikingligr veldi");
        List<Track> trackList = discResult.getTrackList();
        assertThat(trackList).hasSize(5);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabel().getName()).isEqualTo("Deathlike Silence Productions");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL().isEmpty()).isFalse();
        assertThat(discResult.getAddedBy().isEmpty()).isFalse();
        assertThat(discResult.getAddedOn().isEmpty()).isFalse();
        assertThat(discResult.getModifiedBy().isEmpty()).isFalse();
        assertThat(discResult.getLastModifiedOn().isEmpty()).isFalse();
        Track lastTrack = trackList.get(trackList.size() - 1);
        assertThat(lastTrack.isInstrumental()).isTrue();
    }

    @Test
    public void testMember() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.SPLIT_VIDEO);
        query.setReleaseName("Live & Plugged vol.2", false);
        final Disc discResult = service.performSearch(query).get(0);
        Map<Member, String> members = discResult.getMember();
        boolean foundMember = false;
        for (Member member : members.keySet()) {
            if (member.getId() == 1032L) {
                assertThat("Guitars, Vocals").isEqualTo(members.get(member));
                assertThat(1032L).isEqualTo(member.getId());
                assertThat("Jon Nödtveidt").isEqualTo(member.getName());
                foundMember = true;
                break;
            }
        }
        assertThat(foundMember).isTrue();
    }

    @Test
    public void onlyMiscMemberCategoryButNoMembers() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setSearchObject(new Disc(248488L));
        Disc disc = service.performSearch(query).get(0);

        assertThat(disc.getMember()).isEmpty();
        assertThat(disc.getName()).isEqualTo("Demon's Night");
        assertThat(disc.getBandName()).isEqualTo("Accept");

    }

    @Test
    public void testCollaboration() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setSearchObject(new Disc(586338L));
        Disc disc = service.performSearch(query).get(0);

        assertThat(disc.getType()).isEqualTo(DiscType.COLLABORATION);
        assertThat(disc.getName()).isEqualTo("Chthonic Libations");
        assertThat(disc.getSplitBands())
                .extracting(Band::getId, Band::getName)
                .containsExactly(tuple(7218L, "Nåstrond"), tuple(112532L,"Acherontas"));
        assertThat(disc.getBandName()).isEmpty();
        assertThat(disc.getBand().getId()).isEqualTo(0L);

        List<Track> trackList = disc.getTrackList();
        assertThat(trackList).hasSize(5);
        for (Track track : trackList) {
            Band band = track.getBand();
            assertThat(band.getId()).isEqualTo(0L);
            assertThat(band.getName()).isEmpty();
            assertThat(track.getBandName()).isEqualTo("Nåstrond / Acherontas");
        }
    }


}
