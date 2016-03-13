package de.loki.metallum.core.parser.search;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Member;
import de.loki.metallum.enums.Country;
import de.loki.metallum.search.SearchRelevance;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the data which was gained by the search
 *
 * @author Zarathustra
 */
public class MemberSearchParser extends AbstractSearchParser<Member> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberSearchParser.class);

	@Override
	protected Member useSpecificSearchParser(final JSONArray hits) throws JSONException {
		Member member = new Member(0);
		member.setId(parseId(hits.getString(0)));
		member.setName(parseName(hits.getString(0)));
		member.setAlternativeName(parseAlternativeName(hits.getString(0)));
		member.setRealName(parseRealName(hits.getString(1)));
		member.setCountry(parseCountry(hits.getString(2)));
		member.setUncategorizedBands(parseBands(hits.getString(3)));
		return member;
	}

	private String parseAlternativeName(final String hit) {
		String akaName = "";
		if (hit.contains("(a.k.a")) {
			akaName = hit.substring(hit.indexOf("(a.k.a. ") + 8, hit.length() - 1);
			akaName = Jsoup.parse(akaName).text();
		}
		return akaName;
	}

	private long parseId(String hit) {
		hit = hit.substring(0, hit.indexOf("\">"));
		hit = hit.substring(hit.lastIndexOf("/") + 1, hit.length());
		return Long.parseLong(hit);
	}

	private List<Band> parseBands(final String hit) {
		LOGGER.debug("new hit: " + hit);
		List<Band> bandList = new ArrayList<Band>();
		Document doc = Jsoup.parse(hit);
		Elements links = doc.getElementsByAttribute("href");
		for (Element link : links) {
			String hrefAttr = link.attr("href");
			String bandId = hrefAttr.substring(hrefAttr.lastIndexOf("/") + 1, hrefAttr.length());
//			because there are Members which do not have any band O.o 
//			see FENRIZ - http://www.metal-archives.com/artists/FENRIZ/407865
			if (!bandId.isEmpty()) {
				Band band = new Band(Long.parseLong(bandId));
				band.setName(MetallumUtil.trimNoBreakSpaces(link.text()));
				LOGGER.debug("adding new Band to Member: " + band);
				bandList.add(band);
			}
		}
//		String[] bandSplit = hit.split(", ");
//		for (int i = 0; i < bandSplit.length; i++) {
//			if (!bandSplit[i].contains("etc.")) {
//				LOGGER.info(bandSplit[i]);
//				String bandId = bandSplit[i].substring(0, bandSplit[i].indexOf("\">"));
//				bandId = bandId.substring(bandId.lastIndexOf("/") + 1, bandId.length());
////				because there are Members which do not have any band O.o 
////				see FENRIZ - http://www.metal-archives.com/artists/FENRIZ/407865
//				if (!bandId.isEmpty()) {
//					Band band = new Band(Long.parseLong(bandId));
//					band.setName(parseBandName(bandSplit[i]));
//					bandList.add(band);
//				}
//			}
//		}
		return bandList;
	}

//	private String parseBandName(final String hit) {
//		return MetallumUtil.trimNoBreakSpaces(Jsoup.parse(hit).text());
//	}

	private String parseRealName(final String hit) {
		return hit;
	}

	/**
	 * Parses the name without the alternativ name.
	 * Example:
	 * put in <a href="http://www.metal-archives.com/artists/%2232%22_Kondo/182832">"32" Kondo</a>
	 * (a.k.a. <em>32</em>)
	 * and you'll get: "32" Kondo
	 *
	 * @param hit the link
	 * @return the name name of the link without the alternative name
	 */
	private String parseName(final String hit) {
		String name = Jsoup.parse(hit).text();
		if (!name.contains("a.k.a")) {
			return name;
		} else {
			return nameWithoutAKA(name);
		}
	}

	private String nameWithoutAKA(final String name) {
		return name.replaceAll("\\s\\(a\\.k\\.a\\..*", "");
	}

	private Country parseCountry(final String hit) {
		return Country.getRightCountryForString(hit);
	}

	@Override
	protected SearchRelevance getSearchRelevance(final JSONArray htis) throws JSONException {
		// they do all have the same searchrelevance!
		return new SearchRelevance(0d);
	}

}
