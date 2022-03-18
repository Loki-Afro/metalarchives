package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.*;
import com.github.loki.afro.metallum.entity.partials.PartialLabel;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.BandSearchQuery;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.loki.afro.metallum.entity.YearRange.Year.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.getInfo().startsWith("Formed by Patrick")).isTrue();
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat("Black Metal").isEqualTo(resultBand.getGenre());
        assertThat("Suicide, Sorrow, Despair, Death, Nature").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Sun & Moon Records").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo()).startsWith("Nocturnal Depression started playing");
        assertThat(resultBand.getInfo()).endsWith("Silence of Cold Forest (2017)");
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        service.setLoadImages(true);
        BandQuery bandQuery = BandQuery.builder()
                .name("Cruel Force")
                .status(BandStatus.ACTIVE)
                .build();

        final Band resultBand = Iterables.getOnlyElement(service.getFully(bandQuery));
        assertThat("Cruel Force").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEqualTo("Mannheim, Baden-Württemberg (later); Rheinzabern, (Rhineland-Palatinate) (early)");
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2008).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black/Thrash Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satan, Blasphemy, Heavy Metal Cult").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Heavy Forces Records").isEqualTo(resultBand.getPartialLabel().getName());
        assertThat(resultBand.getPartialLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
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
        service.setLoadImages(true);
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

}
