package de.loki.metallum.core.parser.site.helper.disc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Track;

public final class DiscSiteTrackParser {
	private static Logger	logger	= Logger.getLogger(DiscSiteTrackParser.class);
	private CountDownLatch	doneSignal;
	private final Document	doc;
	private final boolean	isSplit;
	private final boolean	loadLyrics;

	public DiscSiteTrackParser(final Document doc, final boolean isSplit, final boolean loadLyrics) {
		this.doc = doc;
		this.isSplit = isSplit;
		this.loadLyrics = loadLyrics;
	}

	private boolean parseIsInstrumental(final Element row) {
		Element lastTd = row.getElementsByTag("td").last();
		if (lastTd.text().contains("instrumental")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * if doneSignal is not initialized this method will initialize it with the given count of threads,
	 * 
	 * @param trackCount count of tracks to parse
	 */
	private void lazyInitLache(final int trackCount) {
		if (this.doneSignal == null) {
			this.doneSignal = new CountDownLatch(trackCount);
		}
	}

	private void parseLyrics2(final Element row, final long trackId, final Track trackToModify) {
		Runnable runable = new DownloadLyricsRunnable(trackToModify, trackId);
		new Thread(runable).start();
	}

	private final class DownloadLyricsRunnable implements Runnable {

		private final Track	trackToModify;
		private final long	trackId;

		public DownloadLyricsRunnable(final Track trackToModify, final long trackId) {
			this.trackToModify = trackToModify;
			this.trackId = trackId;
		}

		@Override
		public void run() {
			try {
				String lyricsHTML = Downloader.getHTML(MetallumURL.assembleLyricsURL(this.trackId));
				lyricsHTML = lyricsHTML.replaceAll("<br />", System.getProperty("line.separator")).replaceAll("(lyrics not available)", "");
				this.trackToModify.setLyrics(lyricsHTML.trim());
			} catch (ExecutionException e) {
				logger.error("unanble to get lyrics from \"" + this.trackId + "\"", e);
			} finally {
				signalDoneCountDown();
			}
		}
	}

	private void signalDoneCountDown() {
		if (this.doneSignal != null) {
			DiscSiteTrackParser.this.doneSignal.countDown();
		}
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

	public Track[] parse() {
		Element tabeElement = this.doc.select("table[class$=table_lyrics]").first();
//		final int discCount = tabeElement.select("td[colspan=4]").size();
		int counter = 1;
		boolean foundFirstFirstTrack = false;
		Elements rows = tabeElement.select("tr[class~=(even|odd)]");
		List<Track> trackList = new ArrayList<Track>();
		for (Element row : rows) {
			long trackId = parseTrackId(row);
			Track track = new Track(trackId);
			track.setName(parseTrackTitle(row, this.isSplit));
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
			if (this.isSplit) {
				track.setBandName(parseBandName(row));
			}
			track.setInstrumental(parseIsInstrumental(row));
			if (this.loadLyrics && row.getElementById("lyricsButton" + trackId) != null) {
				lazyInitLache(rows.size());
				parseLyrics2(row, trackId, track);
			} else {
				signalDoneCountDown();
			}
			trackList.add(track);

		}
		waitUntilEverythingIsDone(rows.size());
		return trackList.toArray(new Track[trackList.size()]);
	}

	private void waitUntilEverythingIsDone(final int trackCount) {
		if (this.loadLyrics && this.doneSignal != null) {
			try {
//				as fallback we wait here 6 seconds for each track if that fails smth went wrong
				this.doneSignal.await(trackCount * 6, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.fatal("Please, please report this error: Thread Lock failed while downloading lyrics", e);
			}
		}
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
