package de.loki.metallum.search.service.advanced;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.junit.BeforeClass;
import org.junit.Test;

import de.loki.metallum.MetallumException;
import de.loki.metallum.core.util.MetallumLogger;
import de.loki.metallum.entity.Track;
import de.loki.metallum.enums.DiscType;
import de.loki.metallum.search.query.TrackSearchQuery;

public class TrackSearchServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		MetallumLogger.setLogLevel(Level.INFO);
	}

	@Test
	public void songTitleTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setSongTitle("Fucking Wizard", false);
		final Track resultTrack = service.performSearch(query).get(0);
		Assert.assertEquals("Reverend Bizarre", resultTrack.getBandName());
		Assert.assertEquals("II: Crush the Insects", resultTrack.getDiscName());
		Assert.assertEquals("Fucking Wizard", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertTrue(resultTrack.getDiscTyp() != null);
		Assert.assertTrue(resultTrack.getLyrics().isEmpty());
	}

	@Test
	public void songTitleExactMatchTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setSongTitle("Fucking Wizard", true);
		final Track resultTrack = service.performSearch(query).get(0);
		Assert.assertEquals("Reverend Bizarre", resultTrack.getBandName());
		Assert.assertEquals("II: Crush the Insects", resultTrack.getDiscName());
		Assert.assertEquals("Fucking Wizard", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertTrue(resultTrack.getDiscTyp() != null);
		Assert.assertTrue(resultTrack.getLyrics().isEmpty());
	}

	@Test
	public void bandNameTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setBandName("Drudkh", false);
		final List<Track> resultTrackList = service.performSearch(query);
		for (final Track resultTrack : resultTrackList) {
			Assert.assertTrue(resultTrack.getBandName().contains("Drudkh"));
			Assert.assertNotSame(0, resultTrack.getBand().getId());
			Assert.assertNotSame(0, resultTrack.getDisc().getId());
			Assert.assertTrue(resultTrack.getDiscTyp() != null);
			Assert.assertTrue(resultTrack.getLyrics().isEmpty());
		}
	}

	@Test
	public void bandNameExactMatchTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setSongTitle("Cromwell", false);
		query.setBandName("Reverend Bizarre", true);
		final Track resultTrack = service.performSearch(query).get(0);
		Assert.assertEquals("Reverend Bizarre", resultTrack.getBandName());
		Assert.assertEquals("II: Crush the Insects", resultTrack.getDiscName());
		Assert.assertEquals("Cromwell", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertTrue(resultTrack.getDiscTyp() != null);
		Assert.assertTrue(resultTrack.getLyrics().isEmpty());
	}

	@Test
	public void releaseTitleTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setReleaseTitle("Vîrstele Pămîntului", false);
		final List<Track> resultTrackList = service.performSearch(query);
		for (final Track resultTrack : resultTrackList) {
			Assert.assertTrue(resultTrack.getDiscName().contains("Vîrstele Pămîntului"));
			Assert.assertNotSame(0, resultTrack.getBand().getId());
			Assert.assertNotSame(0, resultTrack.getDisc().getId());
			Assert.assertTrue(resultTrack.getDiscTyp() != null);
			Assert.assertTrue(resultTrack.getLyrics().isEmpty());
		}
	}

	@Test
	public void releaseTitleExactTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setReleaseTitle("Vîrstele Pămîntului", true);
		final List<Track> resultTrackList = service.performSearch(query);
		for (final Track resultTrack : resultTrackList) {
			Assert.assertTrue(resultTrack.getDiscName().contains("Vîrstele Pămîntului"));
			Assert.assertNotSame(0, resultTrack.getBand().getId());
			Assert.assertNotSame(0, resultTrack.getDisc().getId());
			Assert.assertTrue(resultTrack.getDiscTyp() != null);
			Assert.assertTrue(resultTrack.getLyrics().isEmpty());
		}
	}

	@Test
	public void singleReleaseTypeTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setSongTitle("Ars Poetica", false);
		query.setDiscType(DiscType.FULL_LENGTH);
		final Track resultTrack = service.performSearch(query).get(0);
		Assert.assertEquals("Drudkh", resultTrack.getBandName());
		Assert.assertEquals("Microcosmos", resultTrack.getDiscName());
		Assert.assertEquals("Ars Poetica", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertTrue(resultTrack.getDiscTyp() == DiscType.FULL_LENGTH);
		Assert.assertTrue(resultTrack.getLyrics().isEmpty());
	}

	@Test
	public void multiReleaseTypeTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setSongTitle("Solitary Endless Path", false);
		query.setDiscTypes(DiscType.FULL_LENGTH, DiscType.DEMO, DiscType.EP);
		final Track resultTrack = service.performSearch(query).get(0);
		Assert.assertEquals("Drudkh", resultTrack.getBandName());
		Assert.assertEquals("Відчуженість (Estrangement)", resultTrack.getDiscName());
		Assert.assertEquals("Самiтня Нескiнченна Тропа (Solitary Endless Path)", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertNotNull(resultTrack.getDiscTyp());
		Assert.assertTrue(resultTrack.getLyrics().isEmpty());
	}

	@Test
	public void lyricsSearchTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setSongTitle("Fucking Wizard", false);
		query.setDiscType(DiscType.FULL_LENGTH);
		query.setLyrics("I'm your saviour, your");
		final Track resultTrack = service.performSearch(query).get(0);
		Assert.assertEquals("Reverend Bizarre", resultTrack.getBandName());
		Assert.assertEquals("II: Crush the Insects", resultTrack.getDiscName());
		Assert.assertEquals("Fucking Wizard", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertTrue(resultTrack.getDiscTyp() == DiscType.FULL_LENGTH);
		Assert.assertTrue(resultTrack.getLyrics().isEmpty());
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
		Assert.assertEquals("Burzum", resultTrack.getBandName());
		Assert.assertEquals("Burzum", resultTrack.getDiscName());
		Assert.assertEquals("War", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertTrue(resultTrack.getDiscTyp() == DiscType.FULL_LENGTH);
		Assert.assertTrue(!resultTrack.getLyrics().isEmpty());
		Assert.assertTrue(resultTrack.getLyrics().startsWith("This is War" + System.getProperty("line.separator")));
		Assert.assertTrue(resultTrack.getLyrics().endsWith(System.getProperty("line.separator") + "War"));
	}

	@Test
	public void cachedLyricsTest() throws MetallumException {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		query.setSongTitle("War", false);
		query.setBandName("Burzum", false);
		query.setReleaseTitle("Burzum", false);
		query.setDiscType(DiscType.FULL_LENGTH);
		final Track resultTrack = service.performSearch(query).get(0);
		Assert.assertEquals("Burzum", resultTrack.getBandName());
		Assert.assertEquals("Burzum", resultTrack.getDiscName());
		Assert.assertEquals("War", resultTrack.getName());
		Assert.assertNotSame(0, resultTrack.getBand().getId());
		Assert.assertNotSame(0, resultTrack.getDisc().getId());
		Assert.assertTrue(resultTrack.getDiscTyp() == DiscType.FULL_LENGTH);
		Assert.assertTrue(!resultTrack.getLyrics().isEmpty());
		Assert.assertTrue(resultTrack.getLyrics().startsWith("This is War" + System.getProperty("line.separator")));
		Assert.assertTrue(resultTrack.getLyrics().endsWith(System.getProperty("line.separator") + "War"));
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
			Assert.assertEquals("Reverend Bizarre", resultTrack.getBandName());
			Assert.assertTrue(!resultTrack.getDiscName().isEmpty());
			Assert.assertEquals("Fucking Wizard", resultTrack.getName());
			Assert.assertNotSame(0, resultTrack.getBand().getId());
			Assert.assertNotSame(0, resultTrack.getDisc().getId());
			Assert.assertNotNull(resultTrack.getDiscTyp());
			Assert.assertTrue(resultTrack.getLyrics().isEmpty());
		}
	}

	@Test
	public void metallumExceptionTest() {
		final TrackSearchService service = new TrackSearchService();
		final TrackSearchQuery query = new TrackSearchQuery();
		try {
			service.performSearch(query);
			Assert.fail();
		} catch (MetallumException e) {
			Assert.assertFalse(e.getMessage().isEmpty());
		}
	}

}
