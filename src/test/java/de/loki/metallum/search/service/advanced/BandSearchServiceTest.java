package de.loki.metallum.search.service.advanced;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.junit.Test;

import de.loki.metallum.MetallumException;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.entity.Review;
import de.loki.metallum.enums.BandStatus;
import de.loki.metallum.enums.Country;
import de.loki.metallum.search.query.BandSearchQuery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BandSearchServiceTest {

	@Test
	public void bandNameTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Mournful Congregation", false);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Mournful Congregation");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.AUSTRALIA);
		assertEquals(resultBand.getProvince(), "Adelaide, South Australia");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 1993);
		assertEquals(resultBand.getGenre(), "Funeral Doom Metal");
		assertEquals("Despair, Desolation, Depression, Mysticism", resultBand.getLyricalThemes());
		assertEquals(resultBand.getLabel().getName(), "Weird Truth Productions");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().startsWith("On the Australian and European tour"));
		assertTrue(resultBand.getInfo().endsWith("Solitude Productions)"));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 13, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
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
		assertTrue(found);
		assertTrue(found2);
	}

	@Test
	public void exactBandNameTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nile", false);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Nile");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.UNITED_STATES);
		assertEquals(resultBand.getProvince(), "Greenville, South Carolina");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 1993);
		assertEquals(resultBand.getGenre(), "Brutal/Technical Death Metal");
		assertEquals(resultBand.getLyricalThemes(), "Egyptian Mythology, Death, Rituals, H.P. Lovecraft");
		assertEquals("Nuclear Blast", resultBand.getLabel().getName());
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().startsWith("Despite the odd misconception that"));
		assertTrue(resultBand.getInfo().endsWith("..."));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 15, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	private synchronized void checkDefaultDisc(final List<Disc> discList, final int expectedSize, final Band bandFromDisc) {
		checkDefaultDisc(discList, expectedSize, bandFromDisc, false);
	}

	private synchronized void checkDefaultDisc(final List<Disc> discList, final int expectedSize, final Band bandFromDisc, final boolean percentAverage) {
		assertFalse(discList.isEmpty());
		assertTrue(discList.size() >= expectedSize);
		for (Disc disc : discList) {
			Assert.assertSame(disc.getBand(), bandFromDisc);
			assertFalse(disc.getName().isEmpty());
			assertTrue(disc.getId() > 0);
			Assert.assertNotNull(disc.getType());
			assertTrue(!disc.getReleaseDate().isEmpty());
			Assert.assertNull(disc.getArtwork());
			if (percentAverage) {
				assertTrue(disc.getReviewPercentAverage() != 0);
			} else {
                assertTrue(Double.isNaN(disc.getReviewPercentAverage()));
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
		assertEquals(resultBand.getName(), "Warning");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.UNITED_KINGDOM);
		assertEquals("Harlow, Essex, England", resultBand.getProvince());
		assertEquals(resultBand.getStatus(), BandStatus.SPLIT_UP);
		assertEquals(resultBand.getYearFormedIn(), 1994);
		assertEquals(resultBand.getGenre(), "Doom Metal");
		assertEquals(resultBand.getLyricalThemes(), "Horror (early), Depression, Relationships");
		assertEquals(resultBand.getLabel().getName(), "Cyclone Empire");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().startsWith("Formed by Patrick"));
		assertTrue(resultBand.getInfo().endsWith("..."));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void countryTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("40 Watt Sun", false);
		query.addCountry(Country.UNITED_KINGDOM);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "40 Watt Sun");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.UNITED_KINGDOM);
		assertEquals(resultBand.getProvince(), "London, England");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 2009);
		assertEquals(resultBand.getGenre(), "Doom Metal");
		assertEquals(resultBand.getLyricalThemes(), "Relationships, Longing, Introspection");
		assertEquals(resultBand.getLabel().getName(), "Radiance Records");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().startsWith("Patrick Walker took the band name"));
		assertTrue(resultBand.getInfo().endsWith("\"Emerald Lies\"."));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 1, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void multiCountryTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Lifelover", false);
		query.addCountry(Country.SWEDEN);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Lifelover");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.SWEDEN);
		assertEquals(resultBand.getProvince(), "Stockholm");
		Assert.assertNull(resultBand.getStatus());
		assertTrue(resultBand.getYearFormedIn() == 0);
		assertEquals(resultBand.getGenre(), "Black Metal/Depressive Rock");
		assertTrue(resultBand.getLyricalThemes().isEmpty());
		assertTrue(resultBand.getLabel().getName().isEmpty());
		assertTrue(resultBand.getInfo().isEmpty());
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void toYearOfFormationTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Katatonia", false);
		query.setYearOfFormationTo(1991);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Katatonia");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.SWEDEN);
		assertEquals(resultBand.getProvince(), "");
		Assert.assertNull(resultBand.getStatus());
		assertTrue(resultBand.getYearFormedIn() == 1991);
		assertEquals(resultBand.getGenre(), "Doom/Death Metal (early), Depressive Rock/Metal (later)");
		assertTrue(resultBand.getLyricalThemes().isEmpty());
		assertTrue(resultBand.getLabel().getName().isEmpty());
		assertTrue(resultBand.getInfo().isEmpty());
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void fromYearOfFormationTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Vital Remains", false);
		query.setYearOfFormationTo(1988);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Vital Remains");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.UNITED_STATES);
		assertEquals(resultBand.getProvince(), "Providence, Rhode Island");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 1988);
		assertEquals(resultBand.getGenre(), "Death Metal");
		assertEquals(resultBand.getLyricalThemes(), "Satanism, Occultism, Anti-Christianity, Death");
		assertEquals(resultBand.getLabel().getName(), "Unsigned/independent");
		assertTrue(resultBand.getLabel().getId() == 0);
		assertTrue(resultBand.getInfo().startsWith("Vital Remains participated"));
		assertTrue(resultBand.getInfo().endsWith("..."));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 14, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void fromAndToYearOfFormationTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Triptykon", false);
		query.setYearOfFormationFrom(1988);
		query.setYearOfFormationTo(2010);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Triptykon");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.SWITZERLAND);
		assertEquals(resultBand.getProvince(), "Zurich");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 2008);
		assertEquals("Gothic/Doom/Death/Black Metal", resultBand.getGenre());
		assertEquals("Despair, Pain, Depression, Darkness", resultBand.getLyricalThemes());
		assertEquals("Century Media Records", resultBand.getLabel().getName());
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().startsWith("Formed by Tom G. Warrior after his"));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void bandStatusTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nocturnal Depression", false);
		query.addStatus(BandStatus.ACTIV);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Nocturnal Depression");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.FRANCE);
		assertEquals(resultBand.getProvince(), "Grenoble, Rhône-Alpes");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 2004);
		assertEquals(resultBand.getGenre(), "Black Metal");
		assertEquals(resultBand.getLyricalThemes(), "Suicide, Sorrow, Despair, Death, Nature");
		assertEquals(resultBand.getLabel().getName(), "Avantgarde Music");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().startsWith("Herr Suizid "));
		assertTrue(resultBand.getInfo().endsWith("band."));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 16, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void multiBandStatusTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nagelfar", false);
		query.addStatus(BandStatus.ACTIV, BandStatus.SPLIT_UP);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Nagelfar");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.GERMANY);
		assertEquals(resultBand.getProvince(), "Aachen, North Rhine-Westphalia");
		assertEquals(resultBand.getStatus(), BandStatus.SPLIT_UP);
		assertEquals(resultBand.getYearFormedIn(), 1993);
		assertEquals(resultBand.getGenre(), "Black Metal");
		assertEquals(resultBand.getLyricalThemes(), "Paganism, Mythology, Nature, Seasons");
		assertEquals(resultBand.getLabel().getName(), "Ván Records");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().startsWith("The picture here shows"));
		assertTrue(resultBand.getInfo().endsWith("..."));
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 8, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void onHoldTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Cruel Force", false);
		query.addStatus(BandStatus.ON_HOLD);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Cruel Force");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.GERMANY);
		assertEquals(resultBand.getProvince(), "Rhineland-Palatinate");
		assertEquals(resultBand.getStatus(), BandStatus.ON_HOLD);
		assertEquals(resultBand.getYearFormedIn(), 2008);
		assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		assertEquals(resultBand.getLyricalThemes(), "Satan, Blasphemy, Heavy Metal Cult");
		assertEquals(resultBand.getLabel().getName(), "Heavy Forces Records");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().isEmpty());
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void lyricalThemesTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Nocturnal", false);
		query.setLyricalThemes("Satan, Evil, Metal");
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Nocturnal");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.GERMANY);
		assertEquals(resultBand.getProvince(), "");
		Assert.assertNull(resultBand.getStatus());
		assertTrue(resultBand.getYearFormedIn() == 0);
		assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		assertEquals(resultBand.getLyricalThemes(), "Satan, Evil, Metal");
		assertTrue(resultBand.getLabel().getName().isEmpty());
		assertTrue(resultBand.getInfo().isEmpty());
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void provinceTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setObjectsToLoad(0);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Merciless", false);
		query.setProvince("Strängnäs");
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Merciless");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.SWEDEN);
		assertEquals(resultBand.getProvince(), "Strängnäs");
		Assert.assertNull(resultBand.getStatus());
		assertTrue(resultBand.getYearFormedIn() == 0);
		assertEquals(resultBand.getGenre(), "Death/Thrash Metal");
		assertEquals(resultBand.getLyricalThemes(), "");
		assertTrue(resultBand.getLabel().getName().isEmpty());
		assertTrue(resultBand.getInfo().isEmpty());
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
	}

	@Test
	public void imagesTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setLoadImages(true);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Cruel Force", false);
		query.addStatus(BandStatus.ON_HOLD);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Cruel Force");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.GERMANY);
		assertEquals(resultBand.getProvince(), "Rhineland-Palatinate");
		assertEquals(resultBand.getStatus(), BandStatus.ON_HOLD);
		assertEquals(resultBand.getYearFormedIn(), 2008);
		assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		assertEquals(resultBand.getLyricalThemes(), "Satan, Blasphemy, Heavy Metal Cult");
		assertEquals(resultBand.getLabel().getName(), "Heavy Forces Records");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertTrue(resultBand.getInfo().isEmpty());
		assertTrue(resultBand.hasLogo());
		assertTrue(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 4, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void reviewTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setLoadReviews(true);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Cruel Force", false);
		query.addStatus(BandStatus.ON_HOLD);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Cruel Force");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.GERMANY);
		assertEquals(resultBand.getProvince(), "Rhineland-Palatinate");
		assertEquals(resultBand.getStatus(), BandStatus.ON_HOLD);
		assertEquals(resultBand.getYearFormedIn(), 2008);
		assertEquals(resultBand.getGenre(), "Black/Thrash Metal");
		assertEquals(resultBand.getLyricalThemes(), "Satan, Blasphemy, Heavy Metal Cult");
		assertEquals(resultBand.getLabel().getName(), "Heavy Forces Records");
		assertTrue(resultBand.getLabel().getId() != 0);
		// TODO enrich the entitys!
		// assertTrue(resultBand.hasLogo());
		// assertTrue(resultBand.hasPhoto());
		assertTrue(resultBand.getReviews().size() >= 2);
		for (final Review review : resultBand.getReviews()) {
			// review percent average
			defaultReviewTest(review, resultBand);
		}

		checkDefaultDisc(resultBand.getDiscs(), 4, resultBand, true);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	private void defaultReviewTest(final Review review, final Band band) {
		assertFalse(review.getAuthor().isEmpty());
		assertFalse(review.getContet().isEmpty());
		assertFalse(review.getDate().isEmpty());
		assertTrue(review.getId() != 0);
		assertFalse(review.getName().isEmpty());
//		some jerk said: Over hyped. - 0% 
		assertTrue(review.getPercent() >= 0);
		// final Disc reviewDisc = review.getDisc();
		// Disc bandDisc = null;
		// for (final Disc bandDiscLoop : band.getDiscs()) {
		// if (reviewDisc.getId() == bandDiscLoop.getId()) {
		// bandDisc = bandDiscLoop;
		// break;
		// }
		// }
		assertTrue(band.getDiscs().contains(review.getDisc()));

		boolean trueIfReviewIsInADisc = false;
		for (final Disc bandDisc : band.getDiscs()) {
			if (bandDisc.getId() == review.getDisc().getId()) {
				trueIfReviewIsInADisc = bandDisc.getReviews().contains(review);
			}
			if (trueIfReviewIsInADisc) {
				break;
			}
		}
		assertTrue(trueIfReviewIsInADisc);
	}

	@Test
	public void reviewPercentAverageTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		service.setLoadReadMore(true);
		final BandSearchQuery query = new BandSearchQuery();
		query.setBandName("Madness", true);
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Madness");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.BRAZIL);
		assertEquals(resultBand.getProvince(), "Piracicaba, São Paulo");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 2005);
		assertFalse(resultBand.getGenre().isEmpty());
		assertFalse(resultBand.getLyricalThemes().isEmpty());
		assertEquals("Murdher Records", resultBand.getLabel().getName());
		assertEquals(36846L, resultBand.getLabel().getId());
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 3, resultBand, true);

		assertTrue(resultBand.getInfo().endsWith("(2009)"));
		assertTrue(resultBand.getInfo().startsWith("Additional discograp"));
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	@Test
	public void metallumExceptionTest() {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		try {
			service.performSearch(query);
		} catch (final MetallumException e) {
			assertFalse(e.getMessage().isEmpty());
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
		assertEquals(resultBand.getName(), "Warning");
		assertTrue(resultBand.getId() != 0);
		assertEquals(resultBand.getCountry(), Country.UNITED_KINGDOM);
		assertEquals("Harlow, Essex, England", resultBand.getProvince());
		assertEquals(resultBand.getStatus(), BandStatus.SPLIT_UP);
		assertEquals(resultBand.getYearFormedIn(), 1994);
		assertFalse(resultBand.getGenre().isEmpty());
		assertFalse(resultBand.getLyricalThemes().isEmpty());
		assertEquals(resultBand.getLabel().getName(), "Cyclone Empire");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		assertTrue(!resultBand.getInfo().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 6, resultBand);
		final Map<Integer, List<Band>> similarArtists = resultBand.getSimilarArtists();
		checkSimilarArtists(similarArtists);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

	private synchronized void checkSimilarArtists(final Map<Integer, List<Band>> similarArtists) {
		for (final List<Band> bandList : similarArtists.values()) {
			for (final Band similarBand : bandList) {
				assertTrue(similarBand.getId() != 0);
				assertFalse(similarBand.getName().isEmpty());
				assertFalse(similarBand.getCountry() == Country.ANY);
				assertFalse(similarBand.getGenre().isEmpty());
			}
		}
	}

	@Test
	public void directIdTest() throws MetallumException {
		final BandSearchService service = new BandSearchService();
		final BandSearchQuery query = new BandSearchQuery();
		query.setSearchObject(new Band(666L));
		final Band resultBand = service.performSearch(query).get(0);
		assertEquals(resultBand.getName(), "Black Jester");
		assertTrue(resultBand.getId() == 666);
		assertEquals(resultBand.getCountry(), Country.ITALY);
		assertEquals(resultBand.getProvince(), "Treviso, Veneto");
		assertEquals(resultBand.getStatus(), BandStatus.ACTIV);
		assertEquals(resultBand.getYearFormedIn(), 1991);
		assertEquals(resultBand.getGenre(), "Neoclassical/Progressive Metal");
		assertEquals(resultBand.getLyricalThemes(), "Philosophical and existentialistic themes");
		assertEquals(resultBand.getLabel().getName(), "Elevate Records");
		assertTrue(resultBand.getLabel().getId() != 0);
		assertFalse(resultBand.hasLogo());
		assertFalse(resultBand.hasPhoto());
		assertFalse(resultBand.getPhotoUrl().isEmpty());
		assertFalse(resultBand.getLogoUrl().isEmpty());
		checkDefaultDisc(resultBand.getDiscs(), 3, resultBand);
		assertFalse(resultBand.getAddedBy().isEmpty());
		assertFalse(resultBand.getAddedOn().isEmpty());
		assertFalse(resultBand.getModifiedBy().isEmpty());
		assertFalse(resultBand.getLastModifiedOn().isEmpty());
	}

}
