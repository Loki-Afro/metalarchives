package de.loki.metallum.core.parser.site.helper.band;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.entity.Band;
import de.loki.metallum.entity.Member;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberParser {
	private enum MemberCategory {
		LAST_KNOWN("Last known lineup"), PAST("Past members"), CURRENT("current lineup"), LIVE("Live musicians");
		private final String[] asString;

		MemberCategory(final String... asString) {
			this.asString = asString;
		}

		public final static MemberCategory getMemberTypeForString(final String possibleMemberCategory) {
			for (final MemberCategory cat : values()) {
				if (MetallumUtil.isStringInArray(possibleMemberCategory, cat.asString)) {
					return cat;
				}
			}
			System.err.println("MemberCategory: " + possibleMemberCategory);
			return null;
		}

	}

	private final Map<Member, String> currentLineupList   = new HashMap<Member, String>();
	private final Map<Member, String> pastLineupList      = new HashMap<Member, String>();
	private final Map<Member, String> liveLineupList      = new HashMap<Member, String>();
	private final Map<Member, String> lastKnownLineupList = new HashMap<Member, String>();

	public final void parse(final String html) {
		final List<Member> members = new ArrayList<Member>();
		if (!html.contains("id=\"band_members\"")) {
			return;
		}
		final String[] cats = html.split("href=\"#band_tab_members_");
		for (int x = 1; x < cats.length; x++) {

			final String cat = cats[x].substring(cats[x].indexOf(">") + 1, cats[x].indexOf("</a>"));
			if (!cat.contains("Complete lineup")) {

				final String catID = cats[x].substring(0, cats[x].indexOf("\""));
				String catHtml = html.substring(html.indexOf("id=\"band_tab_members_" + catID + "\""));

				if (catHtml.contains("<!--")) {
					catHtml = catHtml.substring(0, catHtml.indexOf("<!--"));
				}

				final String[] membersArray = catHtml.split("class=\"lineupTab\">");
				for (int i = 1; i < membersArray.length; i++) {
					final String[] memInfo = membersArray[i].split("</td>");
					final Member member = new Member(parseMemberId(memInfo[0]));
					member.setName(parseMemberName(memInfo[0]));
					final String role = parseMemberRole(memInfo[1]);
					if (memInfo.length > 2) {
						member.setUncategorizedBands(parseMemberBands(memInfo[2]));
					}
					addToMemberList(member, role, cat);
					members.add(member);
				}
			}

		}
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

	private String parseMemberRole(final String htmlPart) {
		String role = htmlPart.substring(htmlPart.indexOf(">") + 1);
		role = role.trim().replaceAll("\\s+", " ");
		return role;
	}

	private long parseMemberId(String htmlPart) {
		htmlPart = htmlPart.substring(htmlPart.indexOf("<a"), htmlPart.indexOf("</a>"));
		String idStr = htmlPart.substring(0, htmlPart.indexOf("\" style"));
		idStr = idStr.substring(idStr.lastIndexOf("/") + 1, idStr.length());
		return Long.parseLong(idStr);
	}

	private String parseMemberName(final String htmlPart) {
		return Jsoup.parse(htmlPart).text();
	}

	private final List<Band> parseMemberBands(final String htmlPart) {
		final List<Band> mbands = new ArrayList<Band>();
		if (!htmlPart.contains("See also:")) {
			return mbands;
		}

		final String[] bands = htmlPart.split("<a");
		for (int i = 1; i < bands.length; i++) {
			final Band band = new Band(0);
			band.setName(bands[i].substring(bands[i].indexOf(">") + 1, bands[i].indexOf("</a>")));

			String idStr = bands[i].substring(bands[i].indexOf("/bands/") + 7);
			idStr = idStr.substring(idStr.indexOf("/") + 1, idStr.indexOf("\">"));
			band.setId(Long.parseLong(idStr));

			mbands.add(band);
		}

		return mbands;
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
