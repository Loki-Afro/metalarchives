package de.loki.metallum.core.parser.site.helper.band;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Band;
import de.loki.metallum.enums.Country;

public class SimilarArtistsParser {
	private final Document	document;

	public SimilarArtistsParser(final long id) throws ExecutionException {
		String html = Downloader.getHTML(MetallumURL.assembleBandRecommendationsURL(id, 1));
		this.document = Jsoup.parse(html);
	}

	public Map<Integer, List<Band>> parse() {
//		multimap: key is score, multiple bands can have the same score
		final Map<Integer, List<Band>> out = new TreeMap<Integer, List<Band>>();
		Element tableElem = this.document.getElementById("artist_list");
		Elements trElements = tableElem.getElementsByTag("tr");
//		removing the tableheader
		trElements.remove(0);
		trElements.remove(trElements.size() - 1);
		for (Element tr : trElements) {
			Elements tdElemnts = tr.getElementsByTag("td");
			Band band = parseBand(tdElemnts);
			addToMap(out, Integer.parseInt(tdElemnts.last().text()), band);
		}
		return out;
	}

	private Band parseBand(final Elements tdElemnts) {
		Band band = new Band();
		band.setName((tdElemnts.get(0).text()));
		band.setId(parseBandId((tdElemnts.get(0).html())));
		band.setCountry(Country.getRightCountryForString(tdElemnts.get(1).text()));
		band.setGenre(tdElemnts.get(2).text());
		return band;
	}

	private Map<Integer, List<Band>> addToMap(final Map<Integer, List<Band>> themap, final int key, final Band value) {
		List<Band> bandListFromMap = themap.get(key);
		if (bandListFromMap == null) {
			bandListFromMap = new ArrayList<Band>();
			bandListFromMap.add(value);
		} else {
			bandListFromMap.add(value);
		}
		themap.put(key, bandListFromMap);
		return themap;
	}

	private long parseBandId(final String htmlPart) {
		String strId = htmlPart.substring(0, htmlPart.lastIndexOf("\">"));
		strId = strId.substring(strId.lastIndexOf("/") + 1, strId.length());
		return Long.parseLong(strId);
	}
}
