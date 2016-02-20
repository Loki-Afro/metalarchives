package de.loki.metallum.core.parser.site.helper.disc;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.entity.Member;

public class DiscSiteMemberParser {
	private final Map<Member, String>	albumLineupList	= new HashMap<Member, String>();
	private final Map<Member, String>	guestLineupList	= new HashMap<Member, String>();
	private final Map<Member, String>	otherMemberList	= new HashMap<Member, String>();
	private final Document				doc;
	private static Logger				logger			= Logger.getLogger(DiscSiteMemberParser.class);

	private enum MemberCategory {
		ALBUM_LINEUP("Band members"), ALBUM_GUEST("Guest/session musicians", "Guest/Session"), ALBUM_OTHER("Other staff", "Misc. staff", "Miscellaneous staff");
		private final String[]	asString;

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

	public DiscSiteMemberParser(final Document htmlDocument) {
		this.doc = htmlDocument;
	}

	public final void parse() {
		Element lineUpElementAll = this.doc.getElementById("album_all_members_lineup");
		String category = null;
//		importent when there are not categories or just one, happens often by Split DVDs and such things 
		if (lineUpElementAll == null) {
			lineUpElementAll = this.doc.getElementById("album_members_lineup");
			category = this.doc.getElementsByAttributeValue("href", "#album_members_lineup").first().text();
		}
		Elements tableRows = lineUpElementAll.getElementsByTag("tr");
		for (Element row : tableRows) {
			if (row.hasClass("lineupHeaders")) {
				category = row.text();
			} else if (row.hasClass("lineupRow")) {
				Member member = parseMember(row);
				String role = row.getElementsByTag("td").last().text();
				addToMemberList(member, role, category);
			}
		}
	}

	private final Member parseMember(final Element memberRow) {
		String memberIdStr = "0";
		Element memberLink = memberRow.getElementsByAttribute("href").first();
		if (memberLink != null) {
			memberIdStr = memberLink.attr("href");
			memberIdStr = memberIdStr.substring(memberIdStr.lastIndexOf("/") + 1, memberIdStr.length());
		} else {
			logger.warn("Member without Link detected, please report that; Member = " + memberRow.text());
		}
		Member member = new Member(Long.parseLong(memberIdStr));
		member.setName(memberLink != null ? memberLink.text() : memberRow.text());
		return member;
	}

	private final void addToMemberList(final Member memberToAdd, final String role, final String category) {
		final MemberCategory cat = MemberCategory.getMemberTypeForString(category);
		switch (cat) {
			case ALBUM_LINEUP:
				this.albumLineupList.put(memberToAdd, role);
				break;
			case ALBUM_GUEST:
				this.guestLineupList.put(memberToAdd, role);
				break;
			case ALBUM_OTHER:
				this.otherMemberList.put(memberToAdd, role);
				break;
			default:
				break;
		}
	}

	public final Map<Member, String> getLineup() {
		return this.albumLineupList;
	}

	public final Map<Member, String> getOtherLinup() {
		return this.otherMemberList;
	}

	public final Map<Member, String> getGuestLineup() {
		return this.guestLineupList;
	}
}
