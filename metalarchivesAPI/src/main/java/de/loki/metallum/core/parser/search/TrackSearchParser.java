package de.loki.metallum.core.parser.search;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Track;
import de.loki.metallum.enums.DiscType;
import de.loki.metallum.search.SearchRelevance;

/**
 * Parses the data which was gained by the search
 * 
 * @author Zarathustra
 * 
 */
public class TrackSearchParser extends AbstractSearchParser<Track> {

	private static Logger	logger					= Logger.getLogger(TrackSearchParser.class);
	private boolean			isAbleToParseGenre		= false;
	private boolean			isAbleToParseDiscType	= false;
	private boolean			loadLyrics				= false;

	public void setIsAbleToParseGenre(final boolean isAbleToParse) {
		this.isAbleToParseGenre = isAbleToParse;
	}

	public void setIsAbleToParseDiscType(final boolean isAbleToParse) {
		this.isAbleToParseDiscType = isAbleToParse;
	}

	@Override
	protected Track useSpecificSearchParser(final JSONArray hits) throws JSONException {
		// tracks haben die id der disc
		Track track = new Track(parseDiscId(hits.getString(1)));
		track.setDiscName(parseDiscName((hits.getString(1))));
		track.setDiscType(parseAlbumType(hits.getString(2)));
		// we have to do this because if we are searching with an DiscType it will not appear in the
		// JSONArray. So the position will change
		track.setName(parseTitleName(hits.getString(this.isAbleToParseDiscType ? 3 : 2)));
		track.getBand().setName(parseBandName(hits.getString(0)));
		track.getBand().setId(parseBandId(hits.getString(0)));
		track = parseOptionalFields(track, hits);
		return track;
	}

	private Track parseOptionalFields(final Track track, final JSONArray jArray) throws JSONException {
		track.setGenre(this.isAbleToParseGenre ? parseGenre(jArray.getString(jArray.length() - 2)) : "");
		// must be at the last position ever!
		track.setLyrics(parseLyrics(jArray.getString(jArray.length() - 1)));
		return track;
	}

	/**
	 * Parses the lyrics by getting the Id of them. With that Id we'll download the html code and
	 * parse that again.
	 * 
	 * @param hit is always the last hit.
	 * @return the lyrics if existent
	 */
	private final String parseLyrics(final String hit) {
		if (this.loadLyrics) {
			// getting the Lyrics Id!
			String id = hit.substring(hit.indexOf("lyricsLink_") + 11, hit.length());
			id = id.substring(0, id.indexOf("\" title=\""));

			// downloading the Lyrics!
			try {
				String lyricsHtml = Downloader.getHTML(MetallumURL.assembleLyricsURL(Long.parseLong(id))).trim();
				// making it nice and if there are no lyrics there should be nothing to return!
				lyricsHtml = MetallumUtil.parseHtmlWithLineSeperators(lyricsHtml);
				final String lyrics = lyricsHtml.replaceAll("\\(lyrics not available\\)", "");
				return lyrics;
			} catch (final ExecutionException e) {
				logger.error("unanble to get the Lyrics from \"" + hit, e);
			}
		}
		return "";
	}

	private String parseGenre(final String hit) {
		return hit;
	}

	private long parseDiscId(String hit) {
		hit = hit.substring(0, hit.lastIndexOf("\""));
		hit = hit.substring(hit.lastIndexOf("/") + 1, hit.length());
		return Long.parseLong(hit);
	}

	private String parseDiscName(final String hit) {
		return hit.substring(hit.indexOf(">") + 1, hit.indexOf("</a>"));
	}

	private DiscType parseAlbumType(final String hit) {
		return DiscType.getTypeDiscTypeForString(hit);
	}

	private String parseBandName(final String hit) {
		return hit.substring(hit.indexOf(">") + 1, hit.indexOf("</a>"));
	}

	private String parseTitleName(final String hit) {
		return hit;
	}

	public void setLoadLyrics(final boolean loadLyrics) {
		this.loadLyrics = loadLyrics;
	}

	@Override
	protected SearchRelevance getSearchRelevance(final JSONArray htis) throws JSONException {
		return new SearchRelevance(0d);
	}
}
