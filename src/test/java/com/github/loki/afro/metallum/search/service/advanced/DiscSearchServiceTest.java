package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.entity.Review;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.DiscSearchQuery;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DiscSearchServiceTest {

    @Test
    public void objectToLoadTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        service.setObjectsToLoad(0);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Lantlôs", false);
        final List<Disc> discListResult = service.performSearch(query);
        Assert.assertFalse(discListResult.isEmpty());
        for (final Disc discResult : discListResult) {
            Assert.assertEquals("Lantlôs", discResult.getBandName());
            Assert.assertFalse(discResult.getName().isEmpty());
            Assert.assertTrue(discResult.getTrackList().isEmpty());
            Assert.assertNotNull(discResult.getType());
            Assert.assertNotNull(discResult.getLabel());
            Assert.assertNull(discResult.getArtwork());
        }
    }

    @Test
    public void fullLengthTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.FULL_LENGTH);
        query.setReleaseName("Transilvanian Hunger", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Darkthrone", discResult.getBandName());
        Assert.assertEquals("Transilvanian Hunger", discResult.getName());
        Assert.assertEquals(8, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Peaceville Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void vhsTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.VIDEO);
        query.setReleaseName("Live Εσχατον: The Art of Rebellion", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Behemoth", discResult.getBandName());
        Assert.assertEquals("Live Εσχατον: The Art of Rebellion", discResult.getName());
        Assert.assertEquals(12, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.VIDEO, discResult.getType());
        Assert.assertEquals("Metal Mind Productions", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void epTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.EP);
        query.setReleaseName("Hordanes Land", false);
        service.setLoadLyrics(true);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Enslaved", discResult.getBandName());
        Assert.assertEquals("Hordanes Land", discResult.getName());
        Assert.assertEquals(3, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.EP, discResult.getType());
        Assert.assertEquals("Candlelight Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
        Assert.assertFalse(discResult.getTrackList().get(1).getLyrics().isEmpty());
    }

    @Test
    public void dvdTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.VIDEO);
        query.setReleaseName("Return to Yggdrasill", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Enslaved", discResult.getBandName());
        Assert.assertEquals("Return to Yggdrasill - Live in Bergen", discResult.getName());
        Assert.assertEquals(11, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.VIDEO, discResult.getType());
        Assert.assertEquals("Tabu Recordings", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void liveTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.LIVE_ALBUM);
        query.setReleaseName("Live Kreation", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Kreator", discResult.getBandName());
        Assert.assertEquals("Live Kreation", discResult.getName());
        Assert.assertEquals(13, discResult.getTrackListOnDisc(1).size());
        Assert.assertEquals(11, discResult.getTrackListOnDisc(2).size());
        Assert.assertEquals(DiscType.LIVE_ALBUM, discResult.getType());
        Assert.assertEquals("Steamhammer", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void boxedSetTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.BOXED_SET);
        query.setReleaseName("In Memory of Quorthon", true);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Bathory", discResult.getBandName());
        Assert.assertEquals("In Memory of Quorthon", discResult.getName());
        Assert.assertEquals(4, discResult.getTrackListOnDisc(1).size());
        Assert.assertEquals(DiscType.BOXED_SET, discResult.getType());
        Assert.assertEquals("Black Mark Production", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void splitTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.SPLIT);
        query.setReleaseName("Emperor / Hordanes Land", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Emperor", discResult.getSplitBands().get(0).getName());
        Assert.assertEquals("Enslaved", discResult.getSplitBands().get(1).getName());
        Assert.assertEquals("Emperor / Hordanes Land", discResult.getName());
        Assert.assertEquals(7, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.SPLIT, discResult.getType());
        Assert.assertEquals("Candlelight Records", discResult.getLabel().getName());
        Assert.assertTrue(discResult.isSplit());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void compilationTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.COMPILATION);
        query.setReleaseName("Abyssus Abyssum Invocat", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Behemoth", discResult.getBandName());
        Assert.assertEquals("Abyssus Abyssum Invocat", discResult.getName());
        Assert.assertEquals(11, discResult.getTrackListOnDisc(1).size());
        Assert.assertEquals(7, discResult.getTrackListOnDisc(2).size());
        Assert.assertEquals(DiscType.COMPILATION, discResult.getType());
        Assert.assertEquals("Peaceville Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void splitVideoTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.SPLIT_VIDEO);
        query.setReleaseName("Live & Plugged vol.2", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Dimmu Borgir", discResult.getSplitBands().get(0).getName());
        Assert.assertEquals("Dissection", discResult.getSplitBands().get(1).getName());
        Assert.assertEquals("Live & Plugged Vol. 2", discResult.getName());
        Assert.assertEquals(17, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.SPLIT_VIDEO, discResult.getType());
        assertThat(discResult.getLabel().getName(), is("Nuclear Blast"));
        Assert.assertTrue(discResult.isSplit());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void demoTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseTypes(DiscType.DEMO);
        query.setReleaseName("Burzum", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Burzum", discResult.getBandName());
        Assert.assertEquals("Burzum", discResult.getName());
        Assert.assertEquals(2, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.DEMO, discResult.getType());
        Assert.assertEquals("Independent", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void cityTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("A Touch of Medieval Darkness", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Desaster", discResult.getBandName());
        Assert.assertEquals("A Touch of Medieval Darkness", discResult.getName());
        Assert.assertEquals(10, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Merciless Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void countryTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Autumn Aurora", false);
        query.setCountries(Country.UKRAINE);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Drudkh", discResult.getBandName());
        Assert.assertEquals("Autumn Aurora", discResult.getName());
        Assert.assertEquals(6, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals(Country.UKRAINE, discResult.getBand().getCountry());
        Assert.assertEquals("Supernal Music", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void multiCountryTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("The Somberlain", false);
        query.setReleaseTypes(DiscType.FULL_LENGTH);
        query.setCountries(Country.UKRAINE, Country.GERMANY, Country.SWEDEN, Country.TAIWAN);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Dissection", discResult.getBandName());
        Assert.assertEquals("The Somberlain", discResult.getName());
        Assert.assertEquals(11, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("No Fashion Records", discResult.getLabel().getName());
        Assert.assertNotNull(discResult.getBand().getCountry());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void multiDiscTypeTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Alongside Death", false);
        query.setReleaseTypes(DiscType.FULL_LENGTH, DiscType.SPLIT);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("The Black", discResult.getBandName());
        Assert.assertEquals("Alongside Death", discResult.getName());
        Assert.assertEquals(8, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Hells Cargo", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void genreTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Stormblåst", false);
        query.setGenre("Symphonic Black Metal");
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Dimmu Borgir", discResult.getBandName());
        Assert.assertEquals("Stormblåst", discResult.getName());
        Assert.assertEquals(10, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Cacophonous Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
        Assert.assertEquals(discResult.getReleaseDate(), "January 25th, 1996");
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
        Assert.assertEquals("Burzum", discResult.getBandName());
        Assert.assertEquals("Det som engang var", discResult.getName());
        Assert.assertEquals(8, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Cymophane Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
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
        Assert.assertEquals("Burzum", discResult.getBandName());
        Assert.assertEquals("Burzum", discResult.getName());
        Assert.assertEquals(9, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Deathlike Silence Productions", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void labelNameTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Reverend Bizarre", false);
        query.setReleaseTypes(DiscType.FULL_LENGTH);
        query.setLabel("Spikefarm Records", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Reverend Bizarre", discResult.getBandName());
        Assert.assertEquals("II: Crush the Insects", discResult.getName());
        Assert.assertEquals(8, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Spikefarm Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void indieLabelTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Practice Sessions", false);
        query.setLabel("", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Reverend Bizarre", discResult.getBandName());
        Assert.assertEquals("Practice Sessions", discResult.getName());
        Assert.assertEquals(4, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.DEMO, discResult.getType());
        Assert.assertEquals("Independent", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    // NOT possible because there is a bug at metal-archives, exactMatch dosn't work

    @Test
    public void exactReleaseTitleTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("In the Rectory of the Bizarre Reverend", true);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Reverend Bizarre", discResult.getBandName());
        Assert.assertEquals("In the Rectory of the Bizarre Reverend", discResult.getName());
        Assert.assertEquals(6, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Sinister Figure", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
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
        Assert.assertEquals("Reverend Bizarre", discResult.getBandName());
        Assert.assertEquals("II: Crush the Insects", discResult.getName());
        Assert.assertEquals(8, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Spikefarm Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void artworkTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService(true);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Belphegor", false);
        query.setReleaseName("Pestapokalypse VI", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Belphegor", discResult.getBandName());
        Assert.assertEquals("Pestapokalypse VI", discResult.getName());
        Assert.assertEquals(9, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Nuclear Blast", discResult.getLabel().getName());
        Assert.assertNotNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
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
            Assert.assertFalse(resultTrack.getLyrics().isEmpty());
        }
        Assert.assertEquals("Cannibal Corpse", discResult.getBandName());
        Assert.assertEquals("Eaten Back to Life", discResult.getName());
        Assert.assertEquals(11, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Metal Blade Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void reviewsTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        service.setLoadReviews(true);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setBandName("Cannibal Corpse", false);
        query.setReleaseName("The Wretched Spawn", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertFalse(discResult.getReviews().isEmpty());
        for (final Review resultReview : discResult.getReviews()) {
            Assert.assertFalse(resultReview.getAuthor().isEmpty());
            Assert.assertFalse(resultReview.getContent().isEmpty());
            Assert.assertFalse(resultReview.getName().isEmpty());
            Assert.assertFalse(resultReview.getDate().isEmpty());
            Assert.assertTrue(resultReview.getPercent() >= 0 && resultReview.getPercent() <= 100);
            Assert.assertTrue(resultReview.getId() != 0);
            Assert.assertSame(resultReview.getDisc(), discResult);
        }
        Assert.assertEquals("Cannibal Corpse", discResult.getBandName());
        Assert.assertEquals("The Wretched Spawn", discResult.getName());
        Assert.assertEquals(13, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Metal Blade Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void unreleasedAlbumTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService(1, false, false, true);
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("At the Gate of Sethu", true);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Nile", discResult.getBandName());
        Assert.assertEquals("At the Gate of Sethu", discResult.getName());
        Assert.assertEquals(11, discResult.getTrackList().size());
        for (final Track track : discResult.getTrackList()) {
            Assert.assertFalse(track.getPlayTime().isEmpty());
        }
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        assertThat(discResult.getLabel().getName(), is("Nuclear Blast"));
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void metallumExcetionTest() {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        try {
            service.performSearch(query);
            Assert.fail();
        } catch (final MetallumException e) {
            Assert.assertFalse(e.getMessage().isEmpty());
        }

    }

    @Test
    public void directIDTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setSearchObject(new Disc(666L));
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Suffocation", discResult.getBandName());
        Assert.assertEquals("Effigy of the Forgotten", discResult.getName());
        Assert.assertEquals(9, discResult.getTrackList().size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("R/C Records", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
    }

    @Test
    public void instrumentalTrackTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setReleaseName("Vikingligr Veldi", false);
        final Disc discResult = service.performSearch(query).get(0);
        Assert.assertEquals("Enslaved", discResult.getBandName());
        Assert.assertEquals("Vikingligr veldi", discResult.getName());
        List<Track> trackList = discResult.getTrackList();
        Assert.assertEquals(5, trackList.size());
        Assert.assertEquals(DiscType.FULL_LENGTH, discResult.getType());
        Assert.assertEquals("Deathlike Silence Productions", discResult.getLabel().getName());
        Assert.assertNull(discResult.getArtwork());
        Assert.assertFalse(discResult.getArtworkURL().isEmpty());
        Assert.assertFalse(discResult.getAddedBy().isEmpty());
        Assert.assertFalse(discResult.getAddedOn().isEmpty());
        Assert.assertFalse(discResult.getModifiedBy().isEmpty());
        Assert.assertFalse(discResult.getLastModifiedOn().isEmpty());
        Track lastTrack = trackList.get(trackList.size() - 1);
        Assert.assertTrue(lastTrack.isInstrumental());
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
                Assert.assertEquals(members.get(member), "Guitars, Vocals");
                Assert.assertEquals(member.getId(), 1032L);
                Assert.assertEquals(member.getName(), "Jon Nödtveidt");
                foundMember = true;
                break;
            }
        }
        Assert.assertTrue(foundMember);
    }

    @Test
    public void onlyMiscMemberCategoryButNoMembers() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        final DiscSearchQuery query = new DiscSearchQuery();
        query.setSearchObject(new Disc(248488L));
        Disc disc = service.performSearch(query).get(0);

        assertThat(disc.getMember().size(), is(0));
        assertThat(disc.getName(), is("Demon's Night"));
        assertThat(disc.getBandName(), is("Accept"));

    }


}
