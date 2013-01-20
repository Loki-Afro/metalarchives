package de.loki.metallum.search.service.advanced;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.junit.BeforeClass;
import org.junit.Test;

import de.loki.metallum.MetallumException;
import de.loki.metallum.core.util.MetallumLogger;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.entity.Review;
import de.loki.metallum.enums.BandStatus;
import de.loki.metallum.enums.Country;
import de.loki.metallum.search.query.BandSearchQuery;

public class BandSearchServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		MetallumLogger.setLogLevel(Level.INFO);
	}

	@Test
	public void bandNameTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Mournful Congregation", false);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Mournful Congregation");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.AUSTRALIA);
		Assert.assertEquals(resultBand.getProvince(), "Adelaide, South Australia");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 1993);
		Assert.assertEquals(resultBand.getGenre(), "Funeral Doom Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Despair, Desolation, Depression");
		Assert.assertEquals(resultBand.getLabel().getName(), "Weird Truth Productions");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("On the Australian and European tour"));
		Assert.assertTrue(resultBand.getInfo().endsWith("Solitude Productions)"));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 13, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
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
		Assert.assertTrue(found);
		Assert.assertTrue(found2);
	}

	@Test
	public void exactBandNameTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nile", false);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Nile");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.UNITED_STATES);
		Assert.assertEquals(resultBand.getProvince(), "Greenville, South Carolina");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 1993);
		Assert.assertEquals(resultBand.getGenre(), "Brutal/Technical Death Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Egyptian Mythology, Death, Torture, H.P. Lovecraft");
		Assert.assertEquals(resultBand.getLabel().getName(), "Nuclear Blast");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("Many people mistake members of Nile as being Egyptia"));
		Assert.assertTrue(resultBand.getInfo().endsWith("..."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 17, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	private synchronized void checkDefaultDisc(final List<Disc> discList, final int expectedSize, final Band bandFromDisc) {
		checkDefaultDisc(discList, expectedSize, bandFromDisc, false);
	}

	private synchronized void checkDefaultDisc(final List<Disc> discList, final int expectedSize, final Band bandFromDisc, final boolean percentAverage) {
		Assert.assertFalse(discList.isEmpty());
		Assert.assertTrue(discList.size() >= expectedSize);
		for (Disc disc : discList) {
			Assert.assertSame(disc.getBand(), bandFromDisc);
			Assert.assertFalse(disc.getName().isEmpty());
			Assert.assertTrue(disc.getId() > 0);
			Assert.assertNotNull(disc.getType());
			Assert.assertTrue(!disc.getReleaseDate().isEmpty());
			Assert.assertNull(disc.getArtwork());
			if (percentAverage) {
				Assert.assertTrue(disc.getReviewPercentAverage() != 0);
			} else {
				Assert.assertEquals(disc.getReviewPercentAverage(), Float.NaN);
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
		Assert.assertEquals(resultBand.getName(), "Warning");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.UNITED_KINGDOM);
		Assert.assertEquals(resultBand.getProvince(), "Harlow, Essex");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.SPLIT_UP);
		Assert.assertEquals(resultBand.getYearFormedIn(), 1994);
		Assert.assertEquals(resultBand.getGenre(), "Doom Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Horror (early), depression, relationships");
		Assert.assertEquals(resultBand.getLabel().getName(), "Cyclone Empire");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("Warning were formed by Patrick"));
		Assert.assertTrue(resultBand.getInfo().endsWith("..."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void countryTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("40 Watt Sun", false);
		query.addCountry(Country.UNITED_KINGDOM);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "40 Watt Sun");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.UNITED_KINGDOM);
		Assert.assertEquals(resultBand.getProvince(), "London");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2009);
		Assert.assertEquals(resultBand.getGenre(), "Doom Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Relationships, Longing");
		Assert.assertEquals(resultBand.getLabel().getName(), "Cyclone Empire");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("The band name"));
		Assert.assertTrue(resultBand.getInfo().endsWith("\"Emerald Lies\"."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 1, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void cacheCommonTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		// we already searched for this band.
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("40 Watt Sun", false);
		query.addCountry(Country.UNITED_KINGDOM);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "40 Watt Sun");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.UNITED_KINGDOM);
		Assert.assertEquals(resultBand.getProvince(), "London");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2009);
		Assert.assertEquals(resultBand.getGenre(), "Doom Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Relationships, Longing");
		Assert.assertEquals(resultBand.getLabel().getName(), "Cyclone Empire");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("The band name"));
		Assert.assertTrue(resultBand.getInfo().endsWith("\"Emerald Lies\"."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 1, resultBand);
	}

	@Test
	public void multiCountryTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Lifelover", false);
		query.addCountry(Country.SWEDEN);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Lifelover");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.SWEDEN);
		Assert.assertEquals(resultBand.getProvince(), "Stockholm");
		Assert.assertNull(resultBand.getStatus());
		Assert.assertTrue(resultBand.getYearFormedIn() == 0);
		Assert.assertEquals(resultBand.getGenre(), "Black Metal/Depressive Rock");
		Assert.assertTrue(resultBand.getLyricalThemes().isEmpty());
		Assert.assertTrue(resultBand.getLabel().getName().isEmpty());
		Assert.assertTrue(resultBand.getInfo().isEmpty());
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void toYearOfFormationTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Katatonia", false);
		query.setYearOfFormationTo(1991);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Katatonia");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.SWEDEN);
		Assert.assertEquals(resultBand.getProvince(), "");
		Assert.assertNull(resultBand.getStatus());
		Assert.assertTrue(resultBand.getYearFormedIn() == 1991);
		Assert.assertEquals(resultBand.getGenre(), "Doom/Death Metal (early), Depressive Rock/Metal (later)");
		Assert.assertTrue(resultBand.getLyricalThemes().isEmpty());
		Assert.assertTrue(resultBand.getLabel().getName().isEmpty());
		Assert.assertTrue(resultBand.getInfo().isEmpty());
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void fromYearOfFormationTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Vital Remains", false);
		query.setYearOfFormationTo(1988);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Vital Remains");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.UNITED_STATES);
		Assert.assertEquals(resultBand.getProvince(), "Providence, Rhode Island");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 1988);
		Assert.assertEquals(resultBand.getGenre(), "Death Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Satanism, Occultism, Anti-Christianity, Death");
		Assert.assertEquals(resultBand.getLabel().getName(), "Century Media Records");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("Vital Remains participated"));
		Assert.assertTrue(resultBand.getInfo().endsWith("A Tribute ..."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 14, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void fromAndToYearOfFormationTestTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Triptykon", false);
		query.setYearOfFormationFrom(1988);
		query.setYearOfFormationTo(2010);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Triptykon");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.SWITZERLAND);
		Assert.assertEquals(resultBand.getProvince(), "Zurich");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2008);
		Assert.assertEquals(resultBand.getGenre(), "Gothic/Doom Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Despair, Human Pain, Dark Thoughts");
		Assert.assertEquals(resultBand.getLabel().getName(), "Prowling Death Records");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("Formed by Tom G. Warrior after his"));
		Assert.assertTrue(resultBand.getInfo().endsWith("Celtic Frost in May 2008."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void bandStatusTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nocturnal Depression", false);
		query.addStatus(BandStatus.ACTIV);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Nocturnal Depression");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.FRANCE);
		Assert.assertEquals(resultBand.getProvince(), "Grenoble, Rhône-Alpes");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2004);
		Assert.assertEquals(resultBand.getGenre(), "Black Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Suicide, Sorrow, Despair, Death, Nature");
		Assert.assertEquals(resultBand.getLabel().getName(), "Avantgarde Music");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("Herr Suizid "));
		Assert.assertTrue(resultBand.getInfo().endsWith("band."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 16, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void multiBandStatusTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nagelfar", false);
		query.addStatus(BandStatus.ACTIV, BandStatus.SPLIT_UP);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Nagelfar");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.GERMANY);
		Assert.assertEquals(resultBand.getProvince(), "Aachen, North Rhine-Westphalia");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.SPLIT_UP);
		Assert.assertEquals(resultBand.getYearFormedIn(), 1993);
		Assert.assertEquals(resultBand.getGenre(), "Black Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Paganism, Mythology, Nature, Seasons");
		Assert.assertEquals(resultBand.getLabel().getName(), "Ván Records");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().startsWith("The picture here shows"));
		Assert.assertTrue(resultBand.getInfo().endsWith("..."));
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 8, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void onHoldTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Cruel Force", false);
		query.addStatus(BandStatus.ON_HOLD);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Cruel Force");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.GERMANY);
		Assert.assertEquals(resultBand.getProvince(), "Rhineland-Palatinate");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ON_HOLD);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2008);
		Assert.assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Satan, Blasphemy, Heavy Metal Cult");
		Assert.assertEquals(resultBand.getLabel().getName(), "Heavy Forces Records");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().isEmpty());
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void lyricalThemesTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nocturnal", false);
		query.setLyricalThemes("Satan, Evil, Metal");
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Nocturnal");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.GERMANY);
		Assert.assertEquals(resultBand.getProvince(), "");
		Assert.assertNull(resultBand.getStatus());
		Assert.assertTrue(resultBand.getYearFormedIn() == 0);
		Assert.assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Satan, Evil, Metal");
		Assert.assertTrue(resultBand.getLabel().getName().isEmpty());
		Assert.assertTrue(resultBand.getInfo().isEmpty());
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void provinceTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Merciless", false);
		query.setProvince("Strängnäs");
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Merciless");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.SWEDEN);
		Assert.assertEquals(resultBand.getProvince(), "Strängnäs");
		Assert.assertNull(resultBand.getStatus());
		Assert.assertTrue(resultBand.getYearFormedIn() == 0);
		Assert.assertEquals(resultBand.getGenre(), "Death/Thrash Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "");
		Assert.assertTrue(resultBand.getLabel().getName().isEmpty());
		Assert.assertTrue(resultBand.getInfo().isEmpty());
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void imagesTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setLoadImages(true);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Cruel Force", false);
		query.addStatus(BandStatus.ON_HOLD);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Cruel Force");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.GERMANY);
		Assert.assertEquals(resultBand.getProvince(), "Rhineland-Palatinate");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ON_HOLD);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2008);
		Assert.assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Satan, Blasphemy, Heavy Metal Cult");
		Assert.assertEquals(resultBand.getLabel().getName(), "Heavy Forces Records");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertTrue(resultBand.getInfo().isEmpty());
		Assert.assertTrue(resultBand.hasLogo());
		Assert.assertTrue(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void reviewTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setLoadReviews(true);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Cruel Force", false);
		query.addStatus(BandStatus.ON_HOLD);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Cruel Force");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.GERMANY);
		Assert.assertEquals(resultBand.getProvince(), "Rhineland-Palatinate");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ON_HOLD);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2008);
		Assert.assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Satan, Blasphemy, Heavy Metal Cult");
		Assert.assertEquals(resultBand.getLabel().getName(), "Heavy Forces Records");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		// TODO enrich the entitys!
		// Assert.assertTrue(resultBand.hasLogo());
		// Assert.assertTrue(resultBand.hasPhoto());
		Assert.assertTrue(resultBand.getReviews().size() >= 2);
		for (final Review review : resultBand.getReviews()) {
			// review percent average
			defaultReviewTest(review, resultBand);
		}

		checkDefaultDisc(resultBand.getDiscs(), 4, resultBand, true);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	private void defaultReviewTest(final Review review, final Band band) {
		Assert.assertFalse(review.getAuthor().isEmpty());
		Assert.assertFalse(review.getContet().isEmpty());
		Assert.assertFalse(review.getDate().isEmpty());
		Assert.assertTrue(review.getId() == 0);
		Assert.assertFalse(review.getName().isEmpty());
		Assert.assertTrue(review.getPercent() != 0);
		// final Disc reviewDisc = review.getDisc();
		// Disc bandDisc = null;
		// for (final Disc bandDiscLoop : band.getDiscs()) {
		// if (reviewDisc.getId() == bandDiscLoop.getId()) {
		// bandDisc = bandDiscLoop;
		// break;
		// }
		// }
		Assert.assertTrue(band.getDiscs().contains(review.getDisc()));

		boolean trueIfReviewIsInADisc = false;
		for (final Disc bandDisc : band.getDiscs()) {
			if (bandDisc.getId() == review.getDisc().getId()) {
				trueIfReviewIsInADisc = bandDisc.getReviews().contains(review);
			}
			if (trueIfReviewIsInADisc) {
				break;
			}
		}
		Assert.assertTrue(trueIfReviewIsInADisc);
	}

	@Test
	public void reviewPercentAverageTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setLoadReadMore(true);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Madness", true);
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Madness");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.BRAZIL);
		Assert.assertEquals(resultBand.getProvince(), "Piracicaba, São Paulo");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		Assert.assertEquals(resultBand.getYearFormedIn(), 2005);
		Assert.assertFalse(resultBand.getGenre().isEmpty());
		Assert.assertFalse(resultBand.getLyricalThemes().isEmpty());
		Assert.assertEquals(resultBand.getLabel().getName(), "Unsigned/independent");
		Assert.assertTrue(resultBand.getLabel().getId() == 0);
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 3, resultBand, true);

		Assert.assertTrue(resultBand.getInfo().endsWith("(2009)"));
		Assert.assertTrue(resultBand.getInfo().startsWith("Additional discograp"));
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void metallumExceptionTest() {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		try {
			service.performSearch(query);
		} catch (final MetallumException e) {
			Assert.assertFalse(e.getMessage().isEmpty());
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
		Assert.assertEquals(resultBand.getName(), "Warning");
		Assert.assertTrue(resultBand.getId() != 0);
		Assert.assertEquals(resultBand.getCountry(), Country.UNITED_KINGDOM);
		Assert.assertEquals(resultBand.getProvince(), "Harlow, Essex");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.SPLIT_UP);
		Assert.assertEquals(resultBand.getYearFormedIn(), 1994);
		Assert.assertFalse(resultBand.getGenre().isEmpty());
		Assert.assertFalse(resultBand.getLyricalThemes().isEmpty());
		Assert.assertEquals(resultBand.getLabel().getName(), "Cyclone Empire");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		Assert.assertTrue(!resultBand.getInfo().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
		final Map<Integer, List<Band>> similarArtists = resultBand.getSimilarArtists();
		checkSimilarArtists(resultBand, similarArtists);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	private synchronized void checkSimilarArtists(final Band band, final Map<Integer, List<Band>> similarArtists) {
		for (final List<Band> bandList : similarArtists.values()) {
			for (final Band similarBand : bandList) {
				Assert.assertTrue(similarBand.getId() != 0);
				Assert.assertFalse(similarBand.getName().isEmpty());
				Assert.assertFalse(similarBand.getCountry() == Country.ANY);
				Assert.assertFalse(similarBand.getGenre().isEmpty());
			}
		}
	}

	@Test
	public void directIdTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setSearchObject(new Band(666L));
		final Band resultBand = service.performSearch(query).get(0);
		Assert.assertEquals(resultBand.getName(), "Black Jester");
		Assert.assertTrue(resultBand.getId() == 666);
		Assert.assertEquals(resultBand.getCountry(), Country.ITALY);
		Assert.assertEquals(resultBand.getProvince(), "Treviso, Veneto");
		Assert.assertEquals(resultBand.getStatus(), BandStatus.SPLIT_UP);
		Assert.assertEquals(resultBand.getYearFormedIn(), 1991);
		Assert.assertEquals(resultBand.getGenre(), "Neoclassical/Progressive Metal");
		Assert.assertEquals(resultBand.getLyricalThemes(), "Philosophical and existentialistic themes");
		Assert.assertEquals(resultBand.getLabel().getName(), "Elevate Records");
		Assert.assertTrue(resultBand.getLabel().getId() != 0);
		Assert.assertFalse(resultBand.hasLogo());
		Assert.assertFalse(resultBand.hasPhoto());
		Assert.assertFalse(resultBand.getPhotoUrl().isEmpty());
		Assert.assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
		Assert.assertFalse(resultBand.getAddedBy().isEmpty());
		Assert.assertFalse(resultBand.getAddedOn().isEmpty());
		Assert.assertFalse(resultBand.getModifiedBy().isEmpty());
		Assert.assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

}
