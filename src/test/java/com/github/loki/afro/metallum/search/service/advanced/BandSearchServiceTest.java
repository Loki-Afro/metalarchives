package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.*;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.BandSearchQuery;
import org.junit.jupiter.api.Test;

import com.github.loki.afro.metallum.entity.YearRange.Year;
import java.util.List;
import java.util.Map;

import static com.github.loki.afro.metallum.entity.YearRange.Year.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BandSearchServiceTest {

    @Test
    public void bandNameTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Mournful Congregation", false);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Mournful Congregation").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.AU).isEqualTo(resultBand.getCountry());
        assertThat("Adelaide, South Australia").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1993).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Funeral Doom Metal").isEqualTo(resultBand.getGenre());
        assertThat(resultBand.getLyricalThemes()).isEqualTo("Despair, Desolation, Depression, Mysticism");
        assertThat("Osmose Productions").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("On the Australian and European tour")).isTrue();
        assertThat(resultBand.getInfo().endsWith("Solitude Productions)")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 13, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
        List<Disc> discs = resultBand.getDiscs();
        boolean found = false;
        boolean found2 = false;
        for (Disc disc : discs) {
            if (disc.getName().equals("Weeping")) {
                found = !disc.hasReviews();
            } else if (disc.getName().equals("The Book of Kings")) {
                found2 = disc.hasReviews();
            }
        }
        assertThat(found).isTrue();
        assertThat(found2).isTrue();
    }

    @Test
    public void exactBandNameTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Nile", false);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Nile").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.US).isEqualTo(resultBand.getCountry());
        assertThat("Greenville, South Carolina").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1993).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Brutal/Technical Death Metal").isEqualTo(resultBand.getGenre());
        assertThat("Egyptian mythology, Death, Rituals, Lovecraft").isEqualTo(resultBand.getLyricalThemes());
        assertThat(resultBand.getLabel().getName()).isEqualTo("Nuclear Blast");
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo()).startsWith("In the late 1980s way prior to forming Nile");
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 15, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void testDisputedStatus() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(174L));
        final Band resultBand = service.performSearch(query).get(0);

        assertThat(resultBand.getStatus()).isEqualTo(BandStatus.DISPUTED);
    }

    private synchronized void checkDefaultDisc(final List<Disc> discList, final int expectedSize, final Band bandFromDisc) {
        checkDefaultDisc(discList, expectedSize, bandFromDisc, false);
    }

    private synchronized void checkDefaultDisc(final List<Disc> discList, final int expectedSize, final Band bandFromDisc, final boolean percentAverage) {
        assertThat(discList).isNotEmpty();
        assertThat(discList.size() >= expectedSize).isTrue();
        for (Disc disc : discList) {
            assertThat(bandFromDisc).isSameAs(disc.getBand());
            assertThat(disc.getName()).isNotEmpty();
            assertThat(disc.getId() > 0).isTrue();
            assertThat(disc.getType()).isNotNull();
            assertThat(!disc.getReleaseDate().isEmpty()).isTrue();
            assertThat(disc.getArtwork()).isNull();
            if (percentAverage) {
                assertThat(disc.getReviewPercentAverage() != 0).isTrue();
            } else {
                assertThat(Double.isNaN(disc.getReviewPercentAverage())).isTrue();
            }
        }
    }

    @Test
    public void genreTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Warning", false);
        query.setGenre("Doom Metal");
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Warning").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.GB).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEqualTo("Harlow, Essex, England");
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1994).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Doom Metal").isEqualTo(resultBand.getGenre());
        assertThat("Horror (early); Depression, Relationships (later)").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Unsigned/independent").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() == 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("Formed by Patrick")).isTrue();
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void countryTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("40 Watt Sun", false);
        query.addCountry(Country.GB);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("40 Watt Sun").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.GB).isEqualTo(resultBand.getCountry());
        assertThat("London, England").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2009).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Doom Metal, Atmospheric/Alternative Rock").isEqualTo(resultBand.getGenre());
        assertThat("Relationships, Longing, Introspection").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Radiance Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("Patrick Walker took the band name")).isTrue();
        assertThat(resultBand.getInfo().endsWith("\"Emerald Lies\".")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 1, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void multiCountryTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setObjectsToLoad(0);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Lifelover", false);
        query.addCountry(Country.SE);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Lifelover").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.SE).isEqualTo(resultBand.getCountry());
        assertThat("Stockholm").isEqualTo(resultBand.getProvince());
        assertThat(resultBand.getStatus()).isNull();
        assertThat(resultBand.getYearFormedIn() == 0).isTrue();
        assertThat("Black Metal/Depressive Rock").isEqualTo(resultBand.getGenre());
        assertThat(resultBand.getLyricalThemes().isEmpty()).isTrue();
        assertThat(resultBand.getLabel().getName().isEmpty()).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
    }

    @Test
    public void toYearOfFormationTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setObjectsToLoad(0);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Katatonia", false);
        query.setYearOfFormationTo(1991);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Katatonia").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.SE).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEmpty();
        assertThat(resultBand.getStatus()).isNull();
        assertThat(1991).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Doom/Death Metal (early); Gothic/Alternative/Progressive Rock/Metal (later)").isEqualTo(resultBand.getGenre());
        assertThat(resultBand.getLyricalThemes().isEmpty()).isTrue();
        assertThat(resultBand.getLabel().getName().isEmpty()).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
    }

    @Test
    public void fromYearOfFormationTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Vital Remains", false);
        query.setYearOfFormationTo(1988);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Vital Remains").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.US).isEqualTo(resultBand.getCountry());
        assertThat("Providence, Rhode Island").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1988).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Death Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satanism, Occultism, Anti-Christianity, Death").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Unsigned/independent").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId()).isEqualTo(0);
        assertThat(resultBand.getInfo().startsWith("Compilation ")).isTrue();
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 14, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void fromAndToYearOfFormationTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Triptykon", false);
        query.setYearOfFormationFrom(1988);
        query.setYearOfFormationTo(2010);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Triptykon").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.CH).isEqualTo(resultBand.getCountry());
        assertThat("Zürich").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2008).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre()).isEqualTo("Gothic/Doom/Death/Black Metal");
        assertThat(resultBand.getLyricalThemes()).isEqualTo("Despair, Pain, Depression, Darkness");
        assertThat(resultBand.getLabel().getName()).isEqualTo("Century Media Records");
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("Formed by Tom G. Warrior after his")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void bandStatusTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Nocturnal Depression", false);
        query.addStatus(BandStatus.ACTIVE);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Nocturnal Depression").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.FR).isEqualTo(resultBand.getCountry());
        assertThat("Grenoble, Auvergne-Rhône-Alpes").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2004).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black Metal").isEqualTo(resultBand.getGenre());
        assertThat("Suicide, Sorrow, Despair, Death, Nature").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Sun & Moon Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("Herr Suizid ")).isTrue();
        assertThat(resultBand.getInfo().endsWith("performances.")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 16, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void multiBandStatusTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Nagelfar", false);
        query.addStatus(BandStatus.ACTIVE, BandStatus.SPLIT_UP);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Nagelfar").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat("Aachen, North Rhine-Westphalia").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.SPLIT_UP).isEqualTo(resultBand.getStatus());
        assertThat(1993).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black Metal").isEqualTo(resultBand.getGenre());
        assertThat("Paganism, Mythology, Nature, Seasons").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Ván Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().startsWith("The picture here shows")).isTrue();
        assertThat(resultBand.getInfo().endsWith("...")).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 8, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void onHoldTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Cruel Force", false);
        query.addStatus(BandStatus.ON_HOLD);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Cruel Force").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat("Rhineland-Palatinate").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ON_HOLD).isEqualTo(resultBand.getStatus());
        assertThat(2008).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black/Thrash Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satan, Blasphemy, Heavy Metal Cult").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Heavy Forces Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void lyricalThemesTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setObjectsToLoad(0);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Nocturnal", false);
        query.setLyricalThemes("Satan, Evil, Metal");
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Nocturnal").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEmpty();
        assertThat(resultBand.getStatus()).isNull();
        assertThat(resultBand.getYearFormedIn() == 0).isTrue();
        assertThat("Black/Thrash Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satan, Evil, Metal").isEqualTo(resultBand.getLyricalThemes());
        assertThat(resultBand.getLabel().getName().isEmpty()).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
    }

    @Test
    public void provinceTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setObjectsToLoad(0);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Merciless", false);
        query.setProvince("Strängnäs");
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Merciless").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.SE).isEqualTo(resultBand.getCountry());
        assertThat("Strängnäs, Södermanland").isEqualTo(resultBand.getProvince());
        assertThat(resultBand.getStatus()).isNull();
        assertThat(resultBand.getYearFormedIn() == 0).isTrue();
        assertThat("Death/Thrash Metal").isEqualTo(resultBand.getGenre());
        assertThat(resultBand.getLyricalThemes()).isEmpty();
        assertThat(resultBand.getLabel().getName().isEmpty()).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
    }

    @Test
    public void imagesTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setLoadImages(true);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Cruel Force", false);
        query.addStatus(BandStatus.ON_HOLD);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Cruel Force").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat("Rhineland-Palatinate").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ON_HOLD).isEqualTo(resultBand.getStatus());
        assertThat(2008).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black/Thrash Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satan, Blasphemy, Heavy Metal Cult").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Heavy Forces Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.getInfo().isEmpty()).isTrue();
        assertThat(resultBand.hasLogo()).isTrue();
        assertThat(resultBand.hasPhoto()).isTrue();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void reviewTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setLoadReviews(true);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Cruel Force", false);
        query.addStatus(BandStatus.ON_HOLD);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Cruel Force").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.DE).isEqualTo(resultBand.getCountry());
        assertThat("Rhineland-Palatinate").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ON_HOLD).isEqualTo(resultBand.getStatus());
        assertThat(2008).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Black/Thrash Metal").isEqualTo(resultBand.getGenre());
        assertThat("Satan, Blasphemy, Heavy Metal Cult").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Heavy Forces Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        // TODO enrich the entitys!
        // assertThat(resultBand.hasLogo()).isTrue();
        // assertThat(resultBand.hasPhoto()).isTrue();
        assertThat(resultBand.getReviews().size() >= 2).isTrue();
        for (final Review review : resultBand.getReviews()) {
            // review percent average
            defaultReviewTest(review, resultBand);
        }

        checkDefaultDisc(resultBand.getDiscs(), 4, resultBand, true);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    private void defaultReviewTest(final Review review, final Band band) {
        assertThat(review.getAuthor()).isNotEmpty();
        assertThat(review.getContent()).isNotEmpty();
        assertThat(review.getDate()).isNotEmpty();
        assertThat(review.getId() != 0).isTrue();
        assertThat(review.getName()).isNotEmpty();
//		some jerk said: Over hyped. - 0% 
        assertThat(review.getPercent() >= 0).isTrue();
        // final Disc reviewDisc = review.getDisc();
        // Disc bandDisc = null;
        // for (final Disc bandDiscLoop : band.getDiscs()) {
        // if (reviewDisc.getId() == bandDiscLoop.getId()) {
        // bandDisc = bandDiscLoop;
        // break;
        // }
        // }
        assertThat(band.getDiscs().contains(review.getDisc())).isTrue();

        boolean trueIfReviewIsInADisc = false;
        for (final Disc bandDisc : band.getDiscs()) {
            if (bandDisc.getId() == review.getDisc().getId()) {
                trueIfReviewIsInADisc = bandDisc.getReviews().contains(review);
            }
            if (trueIfReviewIsInADisc) {
                break;
            }
        }
        assertThat(trueIfReviewIsInADisc).isTrue();
    }

    @Test
    public void reviewPercentAverageTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setLoadReadMore(true);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Madness", true);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Madness").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.BR).isEqualTo(resultBand.getCountry());
        assertThat("Piracicaba, São Paulo").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2005).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre()).isNotEmpty();
        assertThat(resultBand.getLyricalThemes()).isNotEmpty();
        assertThat(resultBand.getLabel().getName()).isEqualTo("Murdher Records");
        assertThat(resultBand.getLabel().getId()).isEqualTo(36846L);
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 3, resultBand, true);

        assertThat(resultBand.getInfo().endsWith("(2009).")).isTrue();
        assertThat(resultBand.getInfo().startsWith("Additional discograp")).isTrue();
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
            service.performSearch(query);
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
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Warning").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.GB).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEqualTo("Harlow, Essex, England");
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1994).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre()).isNotEmpty();
        assertThat(resultBand.getLyricalThemes()).isNotEmpty();
        assertThat("Unsigned/independent").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() == 0).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        assertThat(!resultBand.getInfo().isEmpty()).isTrue();
        checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
        final Map<Integer, List<Band>> similarArtists = resultBand.getSimilarArtists();
        checkSimilarArtists(similarArtists);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    private synchronized void checkSimilarArtists(final Map<Integer, List<Band>> similarArtists) {
        for (final List<Band> bandList : similarArtists.values()) {
            for (final Band similarBand : bandList) {
                assertThat(similarBand.getId() != 0).isTrue();
                assertThat(similarBand.getName()).isNotEmpty();
                assertThat(similarBand.getCountry() == Country.ANY).isFalse();
                assertThat(similarBand.getGenre()).isNotEmpty();
            }
        }
    }

    @Test
    public void directIdTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(666L));
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Black Jester").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() == 666).isTrue();
        assertThat(Country.IT).isEqualTo(resultBand.getCountry());
        assertThat("Treviso, Veneto").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1986).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Neoclassical/Progressive Metal").isEqualTo(resultBand.getGenre());
        assertThat("Philosophical and existentialistic themes").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Elevate Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl()).isNotEmpty();
        assertThat(resultBand.getLogoUrl()).isNotEmpty();
        checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
        assertThat(resultBand.getAddedBy()).isNotEmpty();
        assertThat(resultBand.getAddedOn()).isNotEmpty();
        assertThat(resultBand.getModifiedBy()).isNotEmpty();
        assertThat(resultBand.getLastModifiedOn()).isNotEmpty();
    }

    @Test
    public void noDiscographyYetTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(3540273493L));
        final Band resultBand = service.performSearch(query).get(0);

        assertThat(resultBand.getDiscs()).isEmpty();
    }

    @Test
    public void bitmapImageTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setLoadImages(true);
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(4515L));
        final Band resultBand = service.performSearch(query).get(0);

        assertThat(resultBand.getLogo()).isNotNull();
        assertThat(resultBand.getPhoto()).isNotNull();
    }

    @Test
    public void unknownLabelTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(3540382837L));
        final Band resultBand = service.performSearch(query).get(0);

        Label label = resultBand.getLabel();
        assertThat(label.getName()).isEqualTo("Unknown");
        assertThat(label.getId()).isEqualTo(0L);
    }

    @Test
    public void yearsActiveTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(189L));
        final Band resultBand = service.performSearch(query).get(0);

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
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(7L));
        final Band resultBand = service.performSearch(query).get(0);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(of(1987), of(1989), "Nihilist", 14076L),
                YearRange.of(of(1989), of(2014), "Entombed", 7L),
                YearRange.of(of(2016), present(), "Entombed", 7L)
        );
    }

    @Test
    public void yearsActiveWithQuestionMark() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(4515L));
        final Band resultBand = service.performSearch(query).get(0);

        assertThat(resultBand.getYearsActive()).containsExactly(
                YearRange.of(of(1998), unknown(), "Abandoned Grave", 4515L)
        );
    }

    @Test
    public void yearsActiveWithNotAvailable() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(3540477583L));
        final Band resultBand = service.performSearch(query).get(0);

        assertThat(resultBand.getYearsActive()).isEmpty();
    }

   @Test
    public void yearsActiveMultipleQuestionMarks() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(81844L));
        final Band resultBand = service.performSearch(query).get(0);

       assertThat(resultBand.getYearsActive()).containsExactly(
               YearRange.of(of(2001), unknown(), "1000 A.D.", 81844L),
               YearRange.of(unknown(), present(), "Growing", null)
       );
    }

    @Test
    public void yearsActiveStartWithQuestionMark() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(3540413935L));
        final Band resultBand = service.performSearch(query).get(0);

       assertThat(resultBand.getYearsActive()).containsExactly(
               YearRange.of(unknown(), of(2017), "12:06", 3540413935L)
       );
    }

    @Test
    public void yearsActiveChangedNameWithReferenceAtEnd() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(3540293316L));
        final Band resultBand = service.performSearch(query).get(0);

       assertThat(resultBand.getYearsActive()).containsExactly(
               YearRange.of(of(1993), of(2003), "1369", 3540293316L),
               YearRange.of(of(2003), present(), "Blood Tribe", 106025L)
       );
    }

    @Test
    public void yearsActiveToString() throws MetallumException{
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setSearchObject(new Band(98226L));
        final Band resultBand = service.performSearch(query).get(0);
        
        assertThat(resultBand.getYearsActive().stream()
                .map(YearRange::toString))
                .containsExactly(
                        "1992-1994 (as Crashcat)",
                        "1994-? (as Crash for Excess)",
                        "?-? (as 44 X ES$98226)");
    }

}
