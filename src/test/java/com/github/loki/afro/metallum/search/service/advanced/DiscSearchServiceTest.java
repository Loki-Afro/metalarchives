package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.entity.Review;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class DiscSearchServiceTest {


    @Test
    public void objectToLoadTest() throws MetallumException {
        final Iterable<SearchDiscResult> discListResult = new DiscSearchService().get(DiscQuery.builder()
                .bandName("Lantlôs")
                .build());

        assertThat(discListResult)
                .extracting(SearchDiscResult::getBandName)
                .contains("Lantlôs");
    }

    @Test
    public void fullLengthTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Transilvanian Hunger")
                .discType(DiscType.FULL_LENGTH)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Darkthrone");
        assertThat(discResult.getName()).isEqualTo("Transilvanian Hunger");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Peaceville Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void vhsTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Live Εσχατον: The Art of Rebellion")
                .discType(DiscType.VIDEO)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Behemoth");
        assertThat(discResult.getName()).isEqualTo("Live Εσχατον: The Art of Rebellion");
        assertThat(discResult.getTrackList()).hasSize(12);
        assertThat(discResult.getType()).isEqualTo(DiscType.VIDEO);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Metal Mind Productions");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void epTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        service.setLoadLyrics(true);

        DiscQuery query = DiscQuery.builder()
                .name("Hordanes Land")
                .discType(DiscType.EP)
                .build();
        final Disc discResult = service.getSingleUniqueByQuery(query);

        assertThat(discResult.getBandName()).isEqualTo("Enslaved");
        assertThat(discResult.getName()).isEqualTo("Hordanes Land");
        assertThat(discResult.getTrackList()).hasSize(3);
        assertThat(discResult.getType()).isEqualTo(DiscType.EP);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Candlelight Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
        assertThat(discResult.getTrackList().get(1).getLyrics()).isNotEmpty();
    }

    @Test
    public void dvdTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Return to Yggdrasill")
                .discType(DiscType.VIDEO)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Enslaved");
        assertThat(discResult.getName()).isEqualTo("Return to Yggdrasill - Live in Bergen");
        assertThat(discResult.getTrackList()).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.VIDEO);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Tabu Recordings");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void liveTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Live Kreation")
                .discType(DiscType.LIVE_ALBUM)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Kreator");
        assertThat(discResult.getName()).isEqualTo("Live Kreation");
        assertThat(discResult.getTrackListOnDisc(1)).hasSize(13);
        assertThat(discResult.getTrackListOnDisc(2)).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.LIVE_ALBUM);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Steamhammer");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void boxedSetTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("In Memory of Quorthon")
                .discType(DiscType.BOXED_SET)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Bathory");
        assertThat(discResult.getName()).isEqualTo("In Memory of Quorthon");
        assertThat(discResult.getTrackListOnDisc(1)).hasSize(4);
        assertThat(discResult.getType()).isEqualTo(DiscType.BOXED_SET);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Black Mark Production");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void splitSearchTest() throws MetallumException {
        Iterable<SearchDiscResult> result = API.getDiscs(DiscQuery.builder()
                .name("Thousands of Moons Ago / The Gates")
                .build());

        SearchDiscResult searchDiscResult = Iterables.getOnlyElement(result);

        assertThat(searchDiscResult.getId()).isNotEqualTo(0L);
        assertThat(searchDiscResult.getReleaseDate()).isEmpty();
        assertThat(searchDiscResult.getBandCountry()).isEmpty();
        assertThat(searchDiscResult.getDiscType()).contains(DiscType.SPLIT);

        assertThat(searchDiscResult.getBandId()).isEqualTo(0L);
        assertThat(searchDiscResult.getBandName()).isEmpty();
        assertThat(searchDiscResult.getSplitBands())
                .extracting(PartialBand::getId, PartialBand::getName)
                .containsExactly(
                        tuple(9344L, "Drudkh"),
                        tuple(107837L, "Winterfylleth"));
    }

    @Test
    public void splitTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Emperor / Hordanes Land")
                .discType(DiscType.SPLIT)
                .build());

        assertThat(discResult.getSplitBands().get(0).getName()).isEqualTo("Emperor");
        assertThat(discResult.getSplitBands().get(1).getName()).isEqualTo("Enslaved");
        assertThat(discResult.getBandName()).isEqualTo("Emperor / Enslaved");
        assertThat(discResult.getBand()).isNull();
        assertThat(discResult.getName()).isEqualTo("Emperor / Hordanes Land");
        assertThat(discResult.getTrackList()).hasSize(7);
        assertThat(discResult.getType()).isEqualTo(DiscType.SPLIT);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Candlelight Records");
        assertThat(discResult.isSplit()).isTrue();
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void compilationTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Abyssus Abyssum Invocat")
                .discType(DiscType.COMPILATION)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Behemoth");
        assertThat(discResult.getName()).isEqualTo("Abyssus Abyssum Invocat");
        assertThat(discResult.getTrackListOnDisc(1)).hasSize(11);
        assertThat(discResult.getTrackListOnDisc(2)).hasSize(7);
        assertThat(discResult.getType()).isEqualTo(DiscType.COMPILATION);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Peaceville Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void splitVideoTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Live & Plugged vol.2")
                .discType(DiscType.SPLIT_VIDEO)
                .build());

        assertThat(discResult.getSplitBands())
                .extracting(PartialBand::getId, PartialBand::getName)
                .containsExactly(tuple(69L, "Dimmu Borgir"),
                        tuple(183L, "Dissection"));

        assertThat(discResult.getTrackList())
                .extracting(Track::getName, t -> t.getBand().getId(), Track::getBandName)
                .contains(
                        tuple("Mourning Palace", 69L, "Dimmu Borgir"),
                        tuple("The Somberlain", 183L, "Dissection"));

        assertThat(discResult.getName()).isEqualTo("Live & Plugged Vol. 2");
        assertThat(discResult.getTrackList()).hasSize(17);
        assertThat(discResult.getType()).isEqualTo(DiscType.SPLIT_VIDEO);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Nuclear Blast");
        assertThat(discResult.isSplit()).isTrue();
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void demoTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Burzum")
                .exactNameMatch(true)
                .discType(DiscType.DEMO)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Burzum");
        assertThat(discResult.getName()).isEqualTo("Burzum");
        assertThat(discResult.getTrackList()).hasSize(2);
        assertThat(discResult.getType()).isEqualTo(DiscType.DEMO);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Independent");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void cityTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("A Touch of Medieval Darkness")
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Desaster");
        assertThat(discResult.getName()).isEqualTo("A Touch of Medieval Darkness");
        assertThat(discResult.getTrackList()).hasSize(10);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Merciless Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void countryTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Autumn Aurora")
                .country(Country.UA)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Drudkh");
        assertThat(discResult.getName()).isEqualTo("Autumn Aurora");
        assertThat(discResult.getTrackList()).hasSize(6);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Supernal Music");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void multiCountryTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("The Somberlain")
                .discType(DiscType.FULL_LENGTH)
                .countries(Sets.newHashSet(Country.UA, Country.DE, Country.SE, Country.TW))
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Dissection");
        assertThat(discResult.getName()).isEqualTo("The Somberlain");
        assertThat(discResult.getTrackList()).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("No Fashion Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void multiDiscTypeTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Alongside Death")
                .discTypes(Sets.newHashSet(DiscType.FULL_LENGTH, DiscType.SPLIT))
                .build());

        assertThat(discResult.getBandName()).isEqualTo("The Black");
        assertThat(discResult.getName()).isEqualTo("Alongside Death");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Hells Cargo");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void genreTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .name("Stormblåst")
                .genre("Symphonic Black Metal")
                .build();

        final Disc discResult = Iterables.getFirst(API.getDiscsFully(query), null);

        assertThat(discResult.getBandName()).isEqualTo("Dimmu Borgir");
        assertThat(discResult.getName()).isEqualTo("Stormblåst");
        assertThat(discResult.getTrackList()).hasSize(10);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Cacophonous Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
        assertThat("January 25th, 1996").isEqualTo(discResult.getReleaseDate());
    }

    @Test
    public void releaseDateFromTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .name("Det Som Engang Var")
                .fromMonth(1)
                .fromYear(1993)
                .toYear(2007)
                .build();

        final Disc discResult = API.getSingleUniqueDisc(query);

        assertThat(discResult.getBandName()).isEqualTo("Burzum");
        assertThat(discResult.getName()).isEqualTo("Det som engang var");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Cymophane Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void releaseDateToTest() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Burzum")
                .toMonth(4)
                .toYear(1993)
                .discType(DiscType.FULL_LENGTH)
                .build());

        assertThat(discResult.getBandName()).isEqualTo("Burzum");
        assertThat(discResult.getName()).isEqualTo("Burzum");
        assertThat(discResult.getTrackList()).hasSize(9);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Deathlike Silence Productions");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void labelNameTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .bandName("Reverend Bizarre")
                .discType(DiscType.FULL_LENGTH)
                .labelName("Spikefarm Records")
                .build();

        final Disc discResult = new DiscSearchService().getFully(query).iterator().next();

        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("II: Crush the Insects");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Spikefarm Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void indieLabelTest() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        DiscQuery query = DiscQuery.builder().name("Practice Sessions").labelName("").build();
        final Disc discResult = service.getFully(query).iterator().next();
        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("Practice Sessions");
        assertThat(discResult.getTrackList()).hasSize(4);
        assertThat(discResult.getType()).isEqualTo(DiscType.DEMO);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Independent");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    // NOT possible because there is a bug at metal-archives, exactMatch dosn't work

    @Test
    public void exactReleaseTitleTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .name("In the Rectory of the Bizarre Reverend")
                .exactNameMatch(true)
                .build();
        final Disc discResult = API.getSingleUniqueDisc(query);

        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("In the Rectory of the Bizarre Reverend");
        assertThat(discResult.getTrackList()).hasSize(6);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Sinister Figure");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    // NOT possible because there is a bug at metal-archives, exactMatch dosn't work
    // Also a bug to search for smth. with ":"

    @Test
    public void exactBandNameTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .bandName("Reverend Bizarre")
                .exactBandNameMatch(true)
                .name("II: Crush the Insects")
                .build();
        final Disc discResult = API.getSingleUniqueDisc(query);

        assertThat(discResult.getBandName()).isEqualTo("Reverend Bizarre");
        assertThat(discResult.getName()).isEqualTo("II: Crush the Insects");
        assertThat(discResult.getTrackList()).hasSize(8);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Spikefarm Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void artworkTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .bandName("Belphegor")
                .name("Pestapokalypse VI")
                .build();
        final Disc discResult = Iterables.getFirst(new DiscSearchService(true).getFully(query), null);

        assertThat(discResult.getBandName()).isEqualTo("Belphegor");
        assertThat(discResult.getName()).isEqualTo("Pestapokalypse VI");
        assertThat(discResult.getTrackList()).hasSize(9);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Nuclear Blast");
        assertThat(discResult.getArtwork()).isNotNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void lyricsTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .bandName("Cannibal Corpse")
                .name("Eaten Back to Life")
                .discType(DiscType.FULL_LENGTH)
                .build();
        final Disc discResult = new DiscSearchService(false, true).getSingleUniqueByQuery(query);

        for (final Track resultTrack : discResult.getTrackList()) {
            assertThat(resultTrack.getLyrics()).isNotEmpty();
        }
        assertThat(discResult.getBandName()).isEqualTo("Cannibal Corpse");
        assertThat(discResult.getName()).isEqualTo("Eaten Back to Life");
        assertThat(discResult.getTrackList()).hasSize(11);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Metal Blade Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void reviewsTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .bandName("Cannibal Corpse")
                .name("The Wretched Spawn")
                .build();
        final Disc discResult = new DiscSearchService(false, false).getSingleUniqueByQuery(query);

        List<Review> reviews = discResult.getReviews();
        assertThat(reviews).isNotEmpty();
        for (final Review resultReview : reviews) {
            assertThat(resultReview.getAuthor()).isNotEmpty();
            assertThat(resultReview.getContent()).isNotEmpty();
            assertThat(resultReview.getName()).isNotEmpty();
            assertThat(resultReview.getDate()).isNotEmpty();
            assertThat(resultReview.getPercent() >= 0 && resultReview.getPercent() <= 100).isTrue();
            assertThat(resultReview.getId() != 0).isTrue();
        }
        assertThat(discResult.getBandName()).isEqualTo("Cannibal Corpse");
        assertThat(discResult.getName()).isEqualTo("The Wretched Spawn");
        assertThat(discResult.getTrackList()).hasSize(13);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Metal Blade Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void unreleasedAlbumTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .name("At the Gate of Sethu")
                .exactNameMatch(true)
                .build();
        final Disc discResult = new DiscSearchService(false, true).getSingleUniqueByQuery(query);

        assertThat(discResult.getBandName()).isEqualTo("Nile");
        assertThat(discResult.getName()).isEqualTo("At the Gate of Sethu");
        assertThat(discResult.getTrackList()).hasSize(11);
        for (final Track track : discResult.getTrackList()) {
            assertThat(track.getPlayTime()).isNotEmpty();
        }
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Nuclear Blast");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void metallumExcetionTest() {
        assertThatThrownBy(() -> API.getDiscsFully(DiscQuery.builder().build()).iterator().next()).isInstanceOf(MetallumException.class);
    }

    @Test
    public void directIDTest() throws MetallumException {
        final Disc discResult = API.getDiscById(666L);

        assertThat(discResult.getBandName()).isEqualTo("Suffocation");
        assertThat(discResult.getName()).isEqualTo("Effigy of the Forgotten");
        assertThat(discResult.getTrackList()).hasSize(9);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("R/C Records");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void instrumentalTrackTest() throws MetallumException {
        DiscQuery query = DiscQuery.builder()
                .name("Vikingligr Veldi")
                .build();
        final Disc discResult = new DiscSearchService(false, true).getSingleUniqueByQuery(query);

        assertThat(discResult.getBandName()).isEqualTo("Enslaved");
        assertThat(discResult.getName()).isEqualTo("Vikingligr veldi");
        List<Track> trackList = discResult.getTrackList();
        assertThat(trackList).hasSize(5);
        assertThat(discResult.getType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(discResult.getLabelPartial().getName()).isEqualTo("Deathlike Silence Productions");
        assertThat(discResult.getArtwork()).isNull();
        assertThat(discResult.getArtworkURL()).isNotEmpty();
        assertThat(discResult.getAddedBy()).isNotEmpty();
        assertThat(discResult.getAddedOn()).isNotEmpty();
        assertThat(discResult.getModifiedBy()).isNotEmpty();
        assertThat(discResult.getLastModifiedOn()).isNotEmpty();
        Track lastTrack = trackList.get(trackList.size() - 1);
        assertThat(lastTrack.isInstrumental()).isTrue();
    }

    @Test
    public void testMember() throws MetallumException {
        final Disc discResult = API.getSingleUniqueDisc(DiscQuery.builder()
                .name("Live & Plugged vol.2")
                .discType(DiscType.SPLIT_VIDEO)
                .build());

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

    @Disabled("unable to find another example of only misc members")
    @Test
    public void onlyMiscMemberCategoryButNoMembers() throws MetallumException {
        final Disc disc = API.getDiscById(248488L);

        assertThat(disc.getMember()).isEmpty();
        assertThat(disc.getName()).isEqualTo("Demon's Night");
        assertThat(disc.getBandName()).isEqualTo("Accept");

    }

    @Test
    public void testCollaboration() throws MetallumException {
        final Disc disc = API.getDiscById(586338L);

        assertThat(disc.getType()).isEqualTo(DiscType.COLLABORATION);
        assertThat(disc.getName()).isEqualTo("Chthonic Libations");
        assertThat(disc.getSplitBands())
                .extracting(PartialBand::getId, PartialBand::getName)
                .containsExactly(tuple(7218L, "Nåstrond"), tuple(112532L, "Acherontas"));
        assertThat(disc.getBandName()).isEqualTo("Nåstrond / Acherontas");
        assertThat(disc.getBand()).isNull();

        List<Track> trackList = disc.getTrackList();
        assertThat(trackList).hasSize(5);
        for (Track track : trackList) {
            assertThat(track.getBand()).isNull();
            assertThat(track.getBandName()).isEqualTo("Nåstrond / Acherontas");
        }
    }

    @Test
    public void testImageUrlWithoutFileSuffix() throws MetallumException {
        final DiscSearchService service = new DiscSearchService();
        service.setLoadImages(true);

        Disc disc = service.getById(64384L);

        assertThat(disc.getArtworkURL()).isNotEmpty();
        assertThat(disc.getArtwork()).isNotNull();
    }

    @Test
    public void splitBandsTest() {
        Disc disc = API.getDiscById(347457L);
        assertThat(disc.getSplitBands()).extracting(PartialBand::getId, PartialBand::getName)
                .containsExactly(
                        tuple(97L, "Judas Priest"),
                        tuple(198L, "Accept"),
                        tuple(15424L, "Scorpions"),
                        tuple(0L, "New Race"),
                        tuple(0L, "Hawkwind")
                );

        assertThat(disc.getTrackList())
                .extracting(Track::getBandName, t -> t.getBand().getId(), Track::getName)
                .contains(
                        tuple("Judas Priest", 97L, "Diamonds and Rust (Joan Baez cover)"),
                        tuple("Scorpions", 15424L, "It All Depends"),
                        tuple("Hawkwind", 0L, "Motorhead")
                );
    }


}
