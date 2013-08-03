package de.loki.metallum.core.parser.site.helper.band;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Member;

public class MemberParser {

	private enum MemberCategory {
		LAST_KNOWN("Last known lineup"), PAST("Past members"), CURRENT("current lineup"), LIVE("Live musicians");
		private static final Logger	logger	= Logger.getLogger(MemberCategory.class);
		private final String[]		asString;

		private MemberCategory(final String... asString) {
			this.asString = asString;
		}

		public final static MemberCategory getMemberTypeForString(final String possibleMemberCategory) {
			for (final MemberCategory cat : values()) {
				if (MetallumUtil.isStringInArray(possibleMemberCategory, cat.asString)) {
					return cat;
				}
			}
			logger.error("MemberCategory: " + possibleMemberCategory);
			return null;
		}

	}

	private final Map<Member, String>	currentLineupList	= new HashMap<Member, String>();
	private final Map<Member, String>	pastLineupList		= new HashMap<Member, String>();
	private final Map<Member, String>	liveLineupList		= new HashMap<Member, String>();
	private final Map<Member, String>	lastKnownLineupList	= new HashMap<Member, String>();

	public final void parse(final Document doc) {
		Element bandMembersElem = doc.getElementById("band_members");
		if (bandMembersElem == null) {
			return;
		}
		Elements categoryElements = doc.select("[id~=band_tab_members_.*]");
		for (Element catElem : categoryElements) {
			final String catId = catElem.id();
			if (!catId.endsWith("all")) {
				Elements lineUpRows = catElem.getElementsByClass("lineupRow");
				for (Element lineUpRow : lineUpRows) {
					Member member = new Member();
					member.setId(parseMemberId(lineUpRow));
					member.setName(lineUpRow.getElementsByClass("bold").first().text());
					final String role = parseMemberRole(lineUpRow);
					final String cat = parseMemberCategory(catElem);

//					then we have bands to parse too
					Element possibleLineupBandsRow = lineUpRow.nextElementSibling();
					if (possibleLineupBandsRow != null && possibleLineupBandsRow.className().equals("lineupBandsRow")) {
						List<Band> bands = parseAdditionalMemberBands(possibleLineupBandsRow);
						member.setUncategorizedBands(bands);
					}
					addToMemberList(member, role, cat);
				}
			}
		}
	}

	private List<Band> parseAdditionalMemberBands(final Element lineUpBandsRow) {
		ArrayList<Band> bandList = new ArrayList<Band>();
		for (Element linkElem : lineUpBandsRow.getElementsByTag("a")) {
			Band band = new Band();
			band.setName(linkElem.text());
			String url = linkElem.attr("href");
			String id = url.substring(url.lastIndexOf("/") + 1, url.length());
			band.setId(Long.parseLong(id));
		}
		return bandList;
	}

	private String parseMemberCategory(final Element catElem) {
		String catId = catElem.id();
		Document doc = catElem.ownerDocument();
		Element realCatElem = doc.select("a[href=#" + catId + "]").first();
		return realCatElem.text();
	}

	public final void addToMemberList(final Member memberToAdd, final String role, final String category) {
		final MemberCategory cat = MemberCategory.getMemberTypeForString(category);
		switch (cat) {
			case CURRENT:
				this.currentLineupList.put(memberToAdd, role);
				break;
			case LIVE:
				this.liveLineupList.put(memberToAdd, role);
				break;
			case PAST:
				this.pastLineupList.put(memberToAdd, role);
				break;
			case LAST_KNOWN:
				this.lastKnownLineupList.put(memberToAdd, role);
				break;
			default:
				break;
		}
	}

	private String parseMemberRole(final Element memberRow) {
		Element lastTdTag = memberRow.getElementsByTag("td").last();
		return lastTdTag.text();
	}

	private long parseMemberId(final Element memberRow) {
		String hrefElem = memberRow.getElementsByAttribute("href").first().attr("href");
		hrefElem = hrefElem.substring(hrefElem.lastIndexOf("/") + 1, hrefElem.length());
		return Long.parseLong(hrefElem);
	}

	public final Map<Member, String> getCurrentLineup() {
		return this.currentLineupList;
	}

	public final Map<Member, String> getLiveLieup() {
		return this.liveLineupList;
	}

	public final Map<Member, String> getLastKnownLineup() {
		return this.lastKnownLineupList;
	}

	public final Map<Member, String> getPastLineup() {
		return this.pastLineupList;
	}

}
