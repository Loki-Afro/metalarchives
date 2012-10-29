package de.loki.metallum.core.parser.site.helper.disc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Track;

public class DiscSiteTrackParser {
	private static Logger	logger	= Logger.getLogger(DiscSiteTrackParser.class);

	public Track[] parse(final String html, final boolean isSplit, final boolean loadLyrics) {
		final String[] tracks = prepareHtml(html);
		final List<Track> trackList = new ArrayList<Track>();
		int trackNo = 0;
		// boolean isSplit = html.contains("<dd>Split</dd>");
		String discNo = "";
		for (int i = 1; i < tracks.length - 1; i += 2) {
			if (tracks[i].contains("<td colspan=\"4\">")) {
				discNo = tracks[i].substring(tracks[i].indexOf("<td") + 3, tracks[i].indexOf("</td>"));
				discNo = discNo.substring(discNo.indexOf(">") + 1);
				i--;
				trackNo = 0;
				continue;
			}
			if (tracks[i].contains("<td align=\"right\"><strong>")) {
				i--;
				continue;
			}
			final Track track = new Track();
			final String[] trackDetails = tracks[i].split("<td");

			track.setId(parseTrackId(trackDetails[1]));
			track.setName(parseTrackTitle(trackDetails[2], isSplit));
			track.setPlayTime(parsePlayTime(trackDetails[3]));

			if (isSplit) {
				track.setBandName(parseBandName(trackDetails[2]));
			} else if (discNo != null && !discNo.isEmpty()) {
				track.setDiscNumber(Integer.parseInt(discNo.replaceAll("[^0-9]", "")));
			}

			// if (trackDetails.length < 5 || trackDetails[4].contains("instrumental")) {
			if (trackDetails[4].contains("instrumental")) {
				track.setInstrumental(true);
				// } else if (loadLyrics && trackDetails[4].contains("Show lyrics")) {
			} else if (loadLyrics && trackDetails[4].contains("toggleLyrics")) {
				track.setLyrics(parseLyrics(trackDetails[4]));
			}

			track.setTrackNumber(++trackNo);
			trackList.add(track);
		}
		Track[] trackArray = new Track[trackList.size()];
		return trackList.toArray(trackArray);
	}

	private String[] prepareHtml(final String html) {
		String tracksHtml = html.substring(html.indexOf("table_lyrics"));
		tracksHtml = tracksHtml.substring(tracksHtml.indexOf(">") + 1, tracksHtml.indexOf("</table>"));
		return tracksHtml.split("<tr");
	}

	private String parseLyrics(final String hitWithId) {
		String lyricsId = hitWithId.substring(hitWithId.indexOf("toggleLyrics(") + 13, hitWithId.length());
		lyricsId = lyricsId.substring(0, lyricsId.indexOf(");"));

		try {
			// loading the Lyrics!
			final String lyricsHtml = Downloader.getHTML(MetallumURL.assembleLyricsURL(Long.parseLong(lyricsId))).trim();
			final String lyrics = lyricsHtml.replaceAll("<br />", System.getProperty("line.separator")).replaceAll("(lyrics not available)", "");
			return lyrics;
		} catch (final ExecutionException e) {
			logger.error("unanble to get lyrics from \"" + hitWithId + "\"", e);
		}
		// making it nice and if there are no lyrics there should be nothing to return!
		return "";
	}

	private long parseTrackId(final String html) {
		String id = html.substring(html.indexOf("name=\"") + 6);
		id = id.substring(0, id.indexOf("\""));
		return Long.parseLong(id);
	}

	private String parseBandName(final String html) {
		String name = Jsoup.parse(html).text();
		name = name.substring(2, name.indexOf(" - "));
		return name;
	}

	private String parseTrackTitle(final String html, final boolean isSplit) {
		String title = Jsoup.parse(html).text();
		title = title.substring(2, title.length());
		if (!isSplit) {
			return title;
		} else {
			title = title.substring(title.indexOf(" - ") + 3, title.length());
			return title;
		}
	}

	private String parsePlayTime(final String html) {
		String playtime = html.substring(html.indexOf(">") + 1);
		playtime = playtime.substring(0, playtime.indexOf("</td>"));
		return playtime;
	}
}
