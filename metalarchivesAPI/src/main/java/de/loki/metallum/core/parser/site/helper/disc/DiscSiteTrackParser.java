package de.loki.metallum.core.parser.site.helper.disc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Track;

public class DiscSiteTrackParser {
	private static Logger	logger	= Logger.getLogger(DiscSiteTrackParser.class);
	private final Document	doc;

	public DiscSiteTrackParser(final String html) {
		this.doc = Jsoup.parse(html);
	}

	private boolean parseIsInstrumental(final Element row) {
		Element lastTd = row.getElementsByTag("td").last();
		if (lastTd.text().contains("instrumental")) {
			return true;
		} else {
			return false;
		}
	}

	private String parseLyrics(final Element row, final long trackId) {
		if (row.getElementById("lyricsButton" + trackId) == null) {
			return "";
		}
		try {
			String lyricsHTML = Downloader.getHTML(MetallumURL.assembleLyricsURL(trackId));
			lyricsHTML = lyricsHTML.replaceAll("<br />", System.getProperty("line.separator")).replaceAll("(lyrics not available)", "");
			return lyricsHTML.trim();
		} catch (ExecutionException e) {
			logger.error("unanble to get lyrics from \"" + trackId + "\"", e);
		}
		return "";
	}

	/*
	 * only called when isSplit is true
	 */
	private String parseBandName(final Element row) {
		String bandName = row.getElementsByTag("td").get(1).text();
		bandName = bandName.substring(0, bandName.indexOf(" - "));
		return bandName;
	}

	private int parseTrackNumber(final Element row) {
		String trackNo = row.getElementsByTag("td").get(0).text();
		return Integer.parseInt(trackNo.replaceAll("\\.", ""));
	}

	public Track[] parse(final boolean isSplit, final boolean loadLyrics) {
		Element tabeElement = this.doc.select("table[class$=table_lyrics]").first();
//		final int discCount = tabeElement.select("td[colspan=4]").size();
		int counter = 1;
		boolean foundFirstFirstTrack = false;
		Elements rows = tabeElement.select("tr[class~=(even|odd)]");
		List<Track> trackList = new ArrayList<Track>();
		for (Element row : rows) {
			long trackId = parseTrackId(row);
			Track track = new Track(trackId);
			track.setName(parseTrackTitle(row, isSplit));
			track.setPlayTime(parsePlayTime(row));
			int trackNumber = parseTrackNumber(row);
			track.setTrackNumber(trackNumber);
			if (foundFirstFirstTrack && trackNumber == 1) {
				counter++;
			} else if (trackNumber == 1 && !foundFirstFirstTrack) {
				foundFirstFirstTrack = true;
			}
			track.setDiscNumber(counter);
//			because otherwise the bandname is always the same
			if (isSplit) {
				track.setBandName(parseBandName(row));
			}
			track.setInstrumental(parseIsInstrumental(row));
			if (loadLyrics) {
				track.setLyrics(parseLyrics(row, trackId));
			}
			trackList.add(track);

		}
		return trackList.toArray(new Track[trackList.size()]);
	}

	private long parseTrackId(final Element row) {
//		<a name="5767" class="anchor"> </a>
		String idStr = row.select("a[name~=\\d.*]").first().attr("name");
		return Long.parseLong(idStr);
	}

	private String parseTrackTitle(final Element row, final boolean isSplit) {
		String title = row.getElementsByTag("td").get(1).text();
		if (isSplit) {
//			because the result is smth like: BnadName - TitleName
			title = title.substring(title.indexOf(" - ") + 3, title.length());
			return title;
		} else {
			return title;
		}
	}

	private String parsePlayTime(final Element row) {
		return row.getElementsByTag("td").get(2).text();
	}

}
