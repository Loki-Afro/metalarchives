package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.entity.Review;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.BandSearchQuery;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class BandSearchServiceTest {

    @Test
    public void bandNameTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Mournful Congregation", false);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Mournful Congregation").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.AUSTRALIA).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 13, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(Country.UNITED_STATES).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 15, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(discList.isEmpty()).isFalse();
        assertThat(discList.size() >= expectedSize).isTrue();
        for (Disc disc : discList) {
            assertThat(bandFromDisc).isSameAs(disc.getBand());
            assertThat(disc.getName().isEmpty()).isFalse();
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
        assertThat(Country.UNITED_KINGDOM).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void countryTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("40 Watt Sun", false);
        query.addCountry(Country.UNITED_KINGDOM);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("40 Watt Sun").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.UNITED_KINGDOM).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 1, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void multiCountryTest() throws MetallumException {
        final BandSearchService service = new BandSearchService();
        service.setObjectsToLoad(0);
        final BandSearchQuery query = new BandSearchQuery();
        query.setBandName("Lifelover", false);
        query.addCountry(Country.SWEDEN);
        final Band resultBand = service.performSearch(query).get(0);
        assertThat("Lifelover").isEqualTo(resultBand.getName());
        assertThat(resultBand.getId() != 0).isTrue();
        assertThat(Country.SWEDEN).isEqualTo(resultBand.getCountry());
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
        assertThat(Country.SWEDEN).isEqualTo(resultBand.getCountry());
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
        assertThat(Country.UNITED_STATES).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 14, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(Country.SWITZERLAND).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(Country.FRANCE).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 16, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(Country.GERMANY).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 8, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(Country.GERMANY).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(Country.GERMANY).isEqualTo(resultBand.getCountry());
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
        assertThat(Country.SWEDEN).isEqualTo(resultBand.getCountry());
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
        assertThat(Country.GERMANY).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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
        assertThat(Country.GERMANY).isEqualTo(resultBand.getCountry());
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
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
    }

    private void defaultReviewTest(final Review review, final Band band) {
        assertThat(review.getAuthor().isEmpty()).isFalse();
        assertThat(review.getContent().isEmpty()).isFalse();
        assertThat(review.getDate().isEmpty()).isFalse();
        assertThat(review.getId() != 0).isTrue();
        assertThat(review.getName().isEmpty()).isFalse();
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
        assertThat(Country.BRAZIL).isEqualTo(resultBand.getCountry());
        assertThat("Piracicaba, São Paulo").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(2005).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre().isEmpty()).isFalse();
        assertThat(resultBand.getLyricalThemes().isEmpty()).isFalse();
        assertThat(resultBand.getLabel().getName()).isEqualTo("Murdher Records");
        assertThat(resultBand.getLabel().getId()).isEqualTo(36846L);
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 3, resultBand, true);

        assertThat(resultBand.getInfo().endsWith("(2009).")).isTrue();
        assertThat(resultBand.getInfo().startsWith("Additional discograp")).isTrue();
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void metallumExceptionTest() {
        final BandSearchService service = new BandSearchService();
        final BandSearchQuery query = new BandSearchQuery();
        try {
            service.performSearch(query);
        } catch (final MetallumException e) {
            assertThat(e.getMessage().isEmpty()).isFalse();
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
        assertThat(Country.UNITED_KINGDOM).isEqualTo(resultBand.getCountry());
        assertThat(resultBand.getProvince()).isEqualTo("Harlow, Essex, England");
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1994).isEqualTo(resultBand.getYearFormedIn());
        assertThat(resultBand.getGenre().isEmpty()).isFalse();
        assertThat(resultBand.getLyricalThemes().isEmpty()).isFalse();
        assertThat("Unsigned/independent").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() == 0).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        assertThat(!resultBand.getInfo().isEmpty()).isTrue();
        checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
        final Map<Integer, List<Band>> similarArtists = resultBand.getSimilarArtists();
        checkSimilarArtists(similarArtists);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
    }

    private synchronized void checkSimilarArtists(final Map<Integer, List<Band>> similarArtists) {
        for (final List<Band> bandList : similarArtists.values()) {
            for (final Band similarBand : bandList) {
                assertThat(similarBand.getId() != 0).isTrue();
                assertThat(similarBand.getName().isEmpty()).isFalse();
                assertThat(similarBand.getCountry() == Country.ANY).isFalse();
                assertThat(similarBand.getGenre().isEmpty()).isFalse();
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
        assertThat(Country.ITALY).isEqualTo(resultBand.getCountry());
        assertThat("Treviso, Veneto").isEqualTo(resultBand.getProvince());
        assertThat(BandStatus.ACTIVE).isEqualTo(resultBand.getStatus());
        assertThat(1986).isEqualTo(resultBand.getYearFormedIn());
        assertThat("Neoclassical/Progressive Metal").isEqualTo(resultBand.getGenre());
        assertThat("Philosophical and existentialistic themes").isEqualTo(resultBand.getLyricalThemes());
        assertThat("Elevate Records").isEqualTo(resultBand.getLabel().getName());
        assertThat(resultBand.getLabel().getId() != 0).isTrue();
        assertThat(resultBand.hasLogo()).isFalse();
        assertThat(resultBand.hasPhoto()).isFalse();
        assertThat(resultBand.getPhotoUrl().isEmpty()).isFalse();
        assertThat(resultBand.getLogoUrl().isEmpty()).isFalse();
        checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
        assertThat(resultBand.getAddedBy().isEmpty()).isFalse();
        assertThat(resultBand.getAddedOn().isEmpty()).isFalse();
        assertThat(resultBand.getModifiedBy().isEmpty()).isFalse();
        assertThat(resultBand.getLastModifiedOn().isEmpty()).isFalse();
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

}
