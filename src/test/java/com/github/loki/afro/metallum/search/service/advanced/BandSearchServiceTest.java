package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.BandNotRecognizedByMetallum;
import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.*;
import com.github.loki.afro.metallum.entity.partials.NullBand;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.entity.partials.PartialLabel;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.BandSearchQuery;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.google.common.collect.Iterables;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.loki.afro.metallum.entity.YearRange.Year.*;
import static org.assertj.core.api.Assertions.*;

public class BandSearchServiceTest {

    @Test
    public void partialLabelTest() throws MetallumException {
        Band band = API.getBandById(3540465313L);

        long partialLabelId = band.getPartialLabel().getId();
        Label label = band.getLabel();

        assertThat(partialLabelId).isEqualTo(label.getId());
        assertThat(label.getEmail()).isNotNull();
    }

    @Test
    public void testDiscPartial() throws MetallumException {
        Band band = API.getBandById(112092L);

        List<Band.PartialDisc> discsPartial = band.getDiscsPartial();
        assertThat(discsPartial).hasSizeGreaterThanOrEqualTo(5);


        Band.PartialDisc disc = discsPartial.stream()
                .filter(d -> d.getName().equals("De Occulta Philosophia"))
                .collect(Collectors.toList())
                .get(0);

        assertThat(disc.getId()).isEqualTo(178710L);
        assertThat(disc.getDiscType()).isEqualTo(DiscType.FULL_LENGTH);
        assertThat(disc.getReleaseYear()).isEqualTo(2007);
        assertThat(disc.getReviewCount()).isGreaterThanOrEqualTo(7);
        assertThat(disc.getAverageReviewPercentage()).isGreaterThanOrEqualTo(70);
    }

    @Test
    public void bandNameTest() throws MetallumException {
        final Band resultBand = API.getSingleUniqueBand(BandQuery.byName("Mournful Congregation", false));

        assertThat("Mournful Congregation").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.AU).isEqualTo(resultBand.getCountry());
        assertThat("Adelaide, South Australia").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1993).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Funeral Doom Metal").isEqualTo(resultBand.getGenre());
        assertThat(resultBand.getLyricalThemes()).isEqualTo("Despair, Desolation, Depression, Mysticism");
        assertThat("Osmose Productions").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("On the Australian and European tour")).isTrue();
        assertThat(resultBand.getInfo().endsWith("Solitude Productions)")).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 13);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void exactBandNameTest() throws MetallumException {
        final Band resultBand = Iterables.getFirst(API.getBandsFully(BandQuery.builder().name("Nile").build()), null);

        assertThat("Nile").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.US).isEqualTo(resultBand.getCountry());
        assertThat("Greenville, South Carolina").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1993).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Brutal/Technical Death Metal").isEqualTo(resultBand.getGenre());
        assertThat("Egyptian mythology, Death, Rituals, Lovecraft").isEqualTo(resultBand.getLyricalThemes());
        assertThat(resultBand.getPartialLabel().getName()).isEqualTo("Napalm Records");
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo()).startsWith("In the late 1980");
        assertThat(resultBand.getInfo()).endsWith("...");
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 15);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void testDisputedStatus() throws MetallumException {
        final Band resultBand = API.getBandById(174L);

        assertThat(resultBand.getStatus()).isEqualTo(BandStatus.DISPUTED);
    }

    private void checkDefaultDisc(final List<Band.PartialDisc> discList, final int expectedSize) {
        assertThat(discList).isNotEmpty();
        assertThat(discList.size() >= expectedSize).isTrue();
        for (Band.PartialDisc disc : discList) {
            assertThat(disc.getName()).isNotEmpty();
            assertThat(disc.getId()).isNotEqualTo(0L);
            assertThat(disc.getDiscType()).isNotNull();
            assertThat(disc.getReleaseYear()).isGreaterThan(0);
            Integer averageReviewPercentage = disc.getAverageReviewPercentage();
            if (averageReviewPercentage != null) {
                assertThat(disc.getReviewCount()).isGreaterThan(0);
            } else {
                assertThat(disc.getReviewCount()).isEqualTo(0);
            }
        }
    }

    @Test
    public void genreTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Warning", false);
        query.setGenre("Doom Metal");
        final Band resultBand = service.performSearchAndLoadFully(query).get(0);
        assertThat("Warning").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.GB).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEqualTo("Harlow, Essex, England");
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1994).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Doom Metal").isEqualTo(resultBand.getGenre());
        assertThat("Horror (early); Depression, Relationships (later)").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Unsigned/independent").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() == 0).isTrue();
        assertThat(resultBand.getInfo().contains("Formed by Patrick")).isTrue();
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 6);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void countryTest() throws MetallumException {
        BandQuery bandQuery = BandQuery.builder()
                .name("40 Watt Sun")
                .country(Country.GB)
                .build();

        final Band resultBand = API.getSingleUniqueBand(bandQuery);
        assertThat("40 Watt Sun").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.GB).isEqualTo(resultBand.getCountry());
        assertThat("London, England").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2009).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Doom Metal, Atmospheric/Alternative Rock").isEqualTo(resultBand.getGenre());
        assertThat("Relationships, Longing, Introspection").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Svart Records").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("Patrick Walker took the band's name")).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 1);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void multiCountryTest() throws MetallumException {
        BandQuery bandQuery = BandQuery.builder()
                .name("Lifelover")
                .country(Country.SE)
                .country(Country.AD)
                .build();

        final SearchBandResult resultBand = Iterables.getOnlyElement(new BandSearchService().get(bandQuery));
        assertThat("Lifelover").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(resultBand.getCountry()).contains(Country.SE);
        assertThat(resultBand.getProvince()).contains("Stockholm");
        assertThat(resultBand.getStatus()).isEmpty();
        assertThat(resultBand.getYearFormedIn()).isEmpty();
        assertThat(resultBand.getGenre()).contains("Black Metal/Depressive Rock");
        assertThat(resultBand.getLyricalThemes()).isEmpty();
    }

    @Test
    public void toYearOfFormationTest() throws MetallumException {
        BandQuery bandQuery = BandQuery.byName("Katatonia", false)
                .setYearOfFormationToYear(1991);

        final SearchBandResult resultBand = Iterables.getOnlyElement(new BandSearchService().get(bandQuery));
        assertThat("Katatonia").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId()).isNotEqualTo(0);
        assertThat(resultBand.getCountry()).contains(Country.SE);
        assertThat(resultBand.getProvince()).isEmpty();
        assertThat(resultBand.getStatus()).isEmpty();
        assertThat(resultBand.getYearFormedIn()).contains(1991);
        assertThat(resultBand.getGenre()).contains("Doom/Death Metal (early); Gothic/Alternative/Progressive Rock/Metal (later)");
        assertThat(resultBand.getLyricalThemes()).isEmpty();
    }

    @Test
    public void fromYearOfFormationTest() throws MetallumException {
        BandQuery bandQuery = BandQuery.byName("Vital Remains", false)
                .setYearOfFormationToYear(1988);
        final Band resultBand = API.getSingleUniqueBand(bandQuery);

        assertThat("Vital Remains").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.US).isEqualTo(resultBand.getCountry());
        assertThat("Providence, Rhode Island").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1988).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Death Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satanism, Occultism, Anti-Christianity, Death").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Unsigned/independent").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId()).isEqualTo(0);
        assertThat(resultBand.getInfo().startsWith("Compilation ")).isTrue();
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 14);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void fromAndToYearOfFormationTest() throws MetallumException {
        BandQuery bandQuery = BandQuery.byName("Triptykon", false)
                .setYearOfFormationFromYear(1988)
                .setYearOfFormationToYear(2010);
        final Band resultBand = API.getSingleUniqueBand(bandQuery);

        assertThat("Triptykon").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.CH).isEqualTo(resultBand.getCountry());
        assertThat("Zürich").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2008).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre()).isEqualTo("Gothic/Doom/Death/Black Metal");
        assertThat(resultBand.getLyricalThemes()).isEqualTo("Despair, Pain, Depression, Darkness");
        assertThat(resultBand.getPartialLabel().getName()).isEqualTo("Century Media Records");
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("Formed by Tom G. Warrior after his")).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 3);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void bandStatusTest() throws MetallumException {
        BandQuery bandQuery = BandQuery.builder()
                .name("Nocturnal Depression")
                .status(BandStatus.ACTIVE)
                .build();

        final Band resultBand = API.getSingleUniqueBand(bandQuery);
        assertThat("Nocturnal Depression").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.FR).isEqualTo(resultBand.getCountry());
        assertThat("Grenoble, Auvergne-Rhône-Alpes").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2004).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Atmospheric/Depressive Black Metal").isEqualTo(resultBand.getGenre());
        assertThat("Suicide, Sorrow, Despair, Death, Nature").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Sun & Moon Records").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo()).startsWith("Nocturnal Depression started playing");
//      it currently ends with "<a href=\"https://www.metal-archives.com/labels/Sturmglanz_Black_Metal_Manufaktur/27023\">&lt; a&gt; ... </a>" which is not even valid html ...
//        assertThat(resultBand.getInfo()).endsWith("Silence of Cold Forest (2017)");
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 16);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void multiBandStatusTest() throws MetallumException {
        BandQuery bandQuery = BandQuery.builder()
                .name("Nagelfar")
                .status(BandStatus.ACTIVE)
                .status(BandStatus.SPLIT_UP)
                .build();

        final Band resultBand = API.getSingleUniqueBand(bandQuery);
        assertThat("Nagelfar").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat("Aachen, North Rhine-Westphalia").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.SPLIT_UP).isEqualTo(resultBand.getStatus());
        assertThat(1993).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black Metal").isEqualTo(resultBand.getGenre());
        assertThat("Paganism, Mythology, Nature, Seasons").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Ván Records").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("The picture here shows")).isTrue();
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 8);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void lyricalThemesTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Nocturnal", false);
        query.setLyricalThemes("Satanism, Death, Evil, Violence, Metal");
        final SearchBandResult resultBand = service.performSearch(query).get(0);
        assertThat("Nocturnal").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId()).isNotEqualTo(0);
        assertThat(resultBand.getCountry()).contains(Country.DE);
        assertThat(resultBand.getProvince()).isEmpty();
        assertThat(resultBand.getStatus()).isEmpty();
        assertThat(resultBand.getYearFormedIn()).isEmpty();
        assertThat(resultBand.getGenre()).contains("Black/Thrash Metal");
        assertThat(resultBand.getLyricalThemes()).contains("Satanism, Death, Evil, Violence, Metal");
    }

    @Test
    public void provinceTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Merciless", false);
        query.setProvince("Strängnäs");
        final SearchBandResult resultBand = service.performSearch(query).get(0);
        assertThat(resultBand.getName()).isEqualTo("Merciless");
        assertThat(resultBand.getId()).isNotEqualTo(0);
        assertThat(resultBand.getCountry()).contains(Country.SE);
        assertThat(resultBand.getProvince()).contains("Strängnäs, Södermanland");
        assertThat(resultBand.getStatus()).isEmpty();
        assertThat(resultBand.getYearFormedIn()).isEmpty();
        assertThat(resultBand.getGenre()).contains("Death/Thrash Metal");
        assertThat(resultBand.getLyricalThemes()).isEmpty();
    }

    @Test
    public void imagesTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        BandQuery bandQuery = BandQuery.builder()
                .name("Cruel Force")
                .status(BandStatus.ACTIVE)
                .build();

        final Band resultBand = Iterables.getOnlyElement(service.getFully(bandQuery));
        assertThat("Cruel Force").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEqualTo("Rheinzabern, (Rhineland-Palatinate) (early); Mannheim (Baden Württemberg) (later)");
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2008).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black/Thrash Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satan, Blasphemy, Heavy Metal Cult").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Shadow Kingdom Records").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isFalse();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        assertThat(resultBand.getPhoto()).isNotNull();
        assertThat(resultBand.getLogo()).isNotNull();
        checkDefaultDisc(resultBand.getDiscsPartial(), 4);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void reviewPercentAverageTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setLoadReadMore(true);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Madness", true);
        query.setProvince("Piracicaba, São Paulo");
        final Band resultBand = service.performSearchAndLoadFully(query).get(0);
        assertThat("Madness").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.BR).isEqualTo(resultBand.getCountry());
        assertThat("Piracicaba, São Paulo").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2005).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre()).isNotEmpty();
        assertThat(resultBand.getLyricalThemes()).isNotEmpty();
        assertThat(resultBand.getPartialLabel().getName()).isEqualTo("Murdher Records");
        assertThat(resultBand.getPartialLabel().getId()).isEqualTo(36846L);
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 3);

        assertThat(resultBand.getInfo().endsWith("(2009).")).isTrue();
        assertThat(resultBand.getInfo().startsWith("Not to be confused with")).isTrue();
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void metallumExceptionTest() {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        try {
            service.performSearchAndLoadFully(query);
        } catch (final MetallumException e) {
            assertThat(e.getMessage()).isNotEmpty();
        }
    }

    @Test
    public void similarArtistTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setLoadSimilar(true);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Warning", true);
        query.setGenre("Doom Metal");
        final Band resultBand = service.performSearchAndLoadFully(query).get(0);
        assertThat("Warning").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.GB).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEqualTo("Harlow, Essex, England");
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1994).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre()).isNotEmpty();
        assertThat(resultBand.getLyricalThemes()).isNotEmpty();
        assertThat("Unsigned/independent").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() == 0).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        assertThat(!resultBand.getInfo().isEmpty()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 6);
        final Map<Integer, List<Band.SimilarBand>> similarArtists = resultBand.getSimilarArtists();
        checkSimilarArtists(similarArtists);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    private synchronized void checkSimilarArtists(final Map<Integer, List<Band.SimilarBand>> similarArtists) {
        for (final List<Band.SimilarBand> bandList : similarArtists.values()) {
            for (final Band.SimilarBand similarBand : bandList) {
                assertThat(similarBand.getId() != 0).isTrue();
                assertThat(similarBand.getName()).isNotEmpty();
                assertThat(similarBand.getCountry()).isNotNull();
                assertThat(similarBand.getGenre()).isNotEmpty();
            }
        }
    }

    @Test
    public void directIdTest() throws MetallumException {
        final Band resultBand = API.getBandById(666L);

        assertThat("Black Jester").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() == 666).isTrue();
        assertThat(Country.IT).isEqualTo(resultBand.getCountry());
        assertThat("Treviso, Veneto").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1986).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Neoclassical/Progressive Metal").isEqualTo(resultBand.getGenre());
        assertThat("Philosophical and existentialistic themes").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Elevate Records").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        checkDefaultDisc(resultBand.getDiscsPartial(), 3);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void noDiscographyYetTest() throws MetallumException {
        final Band resultBand = API.getBandById(3540273493L);

        assertThat(resultBand.getDiscsPartial()).isEmpty();
    }

    @Test
    public void bitmapImageTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final Band resultBand = service.getById(4515L);

        assertThat(resultBand.getLogo()).isNotNull();
        assertThat(resultBand.getPhoto()).isNotNull();
    }

    @Test
    public void unknownLabelTest() throws MetallumException {
        final Band resultBand = API.getBandById(3540382837L);

        PartialLabel label = resultBand.getPartialLabel();
        assertThat(label.getName()).isEqualTo("Unknown");
        assertThat(label.getId()).isEqualTo(0L);
    }

    @Test
    public void yearsActiveTest() throws MetallumException {
        final Band resultBand = API.getBandById(189L);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(of(1989), of(1990), "Ulceration", null),
                YearRange.of(of(1990), of(1990), "Fear the Factory", null),
                YearRange.of(of(1990), of(2002), "Fear Factory", 189L),
                YearRange.of(of(2002), of(2006), "Fear Factory", 189L),
                YearRange.of(of(2009), of(2017), "Fear Factory", 189L),
                YearRange.of(of(2020), present(), "Fear Factory", 189L)
        );

    }

    @Test
    public void yearsActiveWithLinksTest() throws MetallumException {
        final Band resultBand = API.getBandById(7L);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(of(1987), of(1987), "Brainwarp", null),
                YearRange.of(of(1987), of(1989), "Nihilist", 14076L),
                YearRange.of(of(1989), of(2014), "Entombed", 7L),
                YearRange.of(of(2016), present(), "Entombed", 7L)
        );
    }

    @Test
    public void yearsActiveWithQuestionMark() throws MetallumException {
        final Band resultBand = API.getBandById(4515L);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(of(1998), unknown(), "Abandoned Grave", 4515L)
        );
    }

    @Test
    public void yearsActiveWithNotAvailable() throws MetallumException {
        final Band resultBand = API.getBandById(3540477583L);

        assertThat(resultBand.getYearsActive()).isEmpty();
    }

    @Test
    public void yearsActiveMultipleQuestionMarks() throws MetallumException {
        final Band resultBand = API.getBandById(81844L);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(of(2001), unknown(), "1000 A.D.", 81844L),
                YearRange.of(unknown(), present(), "Growing", null)
        );
    }

    @Test
    public void yearsActiveStartWithQuestionMark() throws MetallumException {
        final Band resultBand = API.getBandById(3540413935L);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(unknown(), of(2017), "12:06", 3540413935L)
        );
    }

    @Test
    public void yearsActiveChangedNameWithReferenceAtEnd() throws MetallumException {
        final Band resultBand = API.getBandById(3540293316L);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(of(1993), of(2003), "1369", 3540293316L),
                YearRange.of(of(2003), present(), "Blood Tribe", 106025L)
        );
    }

    @Test
    public void yearsActiveToString() throws MetallumException {
        final Band resultBand = API.getBandById(98226L);

        assertThat(resultBand.getYearsActive().stream()
                .map(YearRange::toString))
                .containsExactly(
                        "1992-1994 (as Crashcat)",
                        "1994-? (as Crash for Excess)",
                        "?-? (as 44 X ES$98226)");
    }


    @Test
    public void splitAlbumLyricsWithoutBand() throws MetallumException {
//        here AC/DC and The Black Crowes are not on metallum yet have lyrics on that disc
        Disc discById = API.getDiscById(379007L);

        List<Track> trackList = discById.getTrackList();
        List<String> lyrics = trackList.stream()
                .map(Track::getLyrics)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        assertThat(lyrics).hasSize(13);
        assertThat(lyrics).allMatch(s -> !Strings.isNullOrEmpty(s));

        PartialBand nullBand = trackList.get(12).getBand();
        assertThat(nullBand.getName()).isEqualTo("AC/DC");
        assertThatThrownBy(nullBand::load)
                .isInstanceOf(BandNotRecognizedByMetallum.class);
    }


    @Test
    public void splitAlbumLyricsWithBandButPartlyNoLyrics() throws MetallumException {
//        here reverend bizarre has lyrics but orodruin has not
        Disc discById = API.getDiscById(38148L);

        List<Optional<String>> lyrics = discById.getTrackList().stream()
                .map(Track::getLyrics)
                .collect(Collectors.toList());

        assertThat(lyrics).hasSize(3);
        assertThat(lyrics.get(0)).isPresent();
        assertThat(lyrics.get(1)).isNotPresent();
        assertThat(lyrics.get(2)).isNotPresent();
    }


    @Test
    public void getLineUp() throws MetallumException {
        Band bandById = API.getBandById(3540325435L);

        assertThat(bandById.getCurrentMembers())
                .size().isEqualTo(3)
                .returnToIterable()
                .extracting(Band.PartialMember::getName, Band.PartialMember::getId, Band.PartialMember::getRole)
                .contains(tuple("Odalv", 440L, "Drums (2010-present)"),
                        tuple("Vrolok", 431L, "Vocals, Bass, Vargan, Shakuhachi (2010-present)"),
                        tuple("Helg", 33725L, "Vocals, Guitars, Keyboards (2010-present)"));

        assertThat(bandById.getCurrentLiveMembers())
                .size().isEqualTo(1)
                .returnToIterable()
                .extracting(Band.PartialMember::getName, Band.PartialMember::getId, Band.PartialMember::getRole)
                .contains(
                        tuple("Goreon", 759332L, "Guitars (2019-present)"));


        assertThat(bandById.getPastMembers()).isEmpty();
        assertThat(bandById.getPastLiveMembers()).isEmpty();
    }

    @Test
    public void getLineUp2() throws MetallumException {
        Band bandById = API.getBandById(125L);

        assertThat(bandById.getCurrentMembers()).hasSize(4);
        assertThat(bandById.getCurrentLiveMembers()).hasSize(0);
        assertThat(bandById.getPastMembers()).hasSize(4);
        assertThat(bandById.getPastLiveMembers()).hasSize(5);
        assertThat(bandById.getCurrentMembers())
                .extracting(Band.PartialMember::getName,
                        Band.PartialMember::getId,
                        Band.PartialMember::getRole,
                        pbm -> pbm.getBands().stream()
                                .sorted(Comparator.comparing(PartialBand::getId))
                                .collect(Collectors.toList()))
                .contains(tuple("Lars Ulrich", 187L, "Drums (1981-present)", List.of()),
                        tuple("Kirk Hammett", 175L, "Guitars (lead), Vocals (backing) (1983-present)",
                                List.of(new NullBand("Kirk Hammett"),
                                        new NullBand("The Wedding Band"),
                                        new NullBand("Spastik Children"),
                                        new PartialBand(173L, "Exodus"))
                        ));
    }


    @Test
    public void getLineUp3() throws MetallumException {
        Band bandById = API.getBandById(67L);

        assertThat(bandById.getCurrentMembers()).hasSize(5);
        assertThat(bandById.getPastMembers()).hasSize(15);
        assertThat(bandById.getCurrentLiveMembers()).hasSize(1);
        assertThat(bandById.getPastLiveMembers()).hasSize(4);

        assertThat(bandById.getPastMembers())
                .extracting(Band.PartialMember::getName,
                        Band.PartialMember::getId,
                        Band.PartialMember::getRole,
                        pbm -> pbm.getBands().stream()
                                .sorted(Comparator.comparing(PartialBand::getId))
                                .collect(Collectors.toList()))
                .contains(tuple("Euronymous", 38L, "Guitars (1984-1993), Vocals (1984-1986)",
                                List.of(new NullBand("Horn"),
                                        new NullBand("L.E.G.O."),
                                        new PartialBand(41211L, "Checker Patrol"))
                        ),
                        tuple("Dead", 41L, "Vocals (1988-1991)",
                                List.of(new NullBand("Ohlin Metal"),
                                        new NullBand("Scapegoat"),
                                        new PartialBand(6967L, "Morbid"))
                        ));
    }

    @Test
    public void getLastKnownLineUp() throws MetallumException {
        // slayer, split-up
        Band bandById = API.getBandById(72L);

        assertThat(bandById.getCurrentMembers()).hasSize(3);
        assertThat(bandById.getPastMembers()).hasSize(3);
        assertThat(bandById.getCurrentLiveMembers()).hasSize(1);
        assertThat(bandById.getPastLiveMembers()).hasSize(5);


        assertThat(bandById.getPastMembers())
                .extracting(Band.PartialMember::getName,
                        Band.PartialMember::getId,
                        Band.PartialMember::getRole,
                        pbm -> pbm.getBands().stream()
                                .sorted(Comparator.comparing(PartialBand::getId))
                                .collect(Collectors.toList()))
                .contains(tuple("Jeff Hanneman", 265L, "Guitars (1981-2013)",
                                List.of(new NullBand("Pap Smear"))
                        ),
                        tuple("Jon Dette", 819L, "Drums (1996-1997)",
                                List.of(
                                        new NullBand("Animetal USA"),
                                        new NullBand("Pushed"),
                                        new PartialBand(4L, "Iced Earth"),
                                        new PartialBand(70L, "Testament"),
                                        new PartialBand(169L, "Anthrax"),
                                        new PartialBand(320L, "Impellitteri"),
                                        new PartialBand(378L, "Heathen"),
                                        new PartialBand(1232L, "Evildead"),
                                        new PartialBand(3365L, "HavocHate"),
                                        new PartialBand(4578L, "Killing Machine"),
                                        new PartialBand(33323L, "Volbeat"),
                                        new PartialBand(44583L, "Terror"),
                                        new PartialBand(49705L, "Temple of Brutality"),
                                        new PartialBand(71769L, "Chaotic Realm"),
                                        new PartialBand(3540335339L, "Apocalypse"),
                                        new PartialBand(3540388054L, "Metal Machine"),
                                        new PartialBand(3540415139L, "Meshiaak")
                                ))
                );
    }

}
