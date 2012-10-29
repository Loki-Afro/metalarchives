package de.loki.metallum.core.parser.site.helper.disc;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

import de.loki.metallum.core.util.MetallumUtil;
import de.loki.metallum.entity.Member;

public class DiscSiteMemberParser {
	private final Map<Member, String>	albumLineupList	= new HashMap<Member, String>();
	private final Map<Member, String>	guestLineupList	= new HashMap<Member, String>();
	private final Map<Member, String>	otherMemberList	= new HashMap<Member, String>();

	private enum MemberCategory {
		ALBUM_LINEUP("Band members"), ALBUM_GUEST("Guest/session musicians"), ALBUM_OTHER("Other staff", "Misc. staff");
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
			System.err.println("MemberCategory: " + possibleMemberCategory);
			return null;
		}

	}

	/**
	 * Parses the disc HTML for the Members and adds them to the List which you'll get here.
	 * 
	 * 
	 * @param html the HTML from the DiscSite.
	 * @return a List of Member with various categories from the DiscSite.
	 */
	public void parse(final String html) {
		final String[] cats = html.split("href=\"#album_members_");
		for (int i = 1; i < cats.length; i++) {
			final String cat = cats[i].substring(cats[i].indexOf(">") + 1, cats[i].indexOf("</a>"));
			final String catID = cats[i].substring(0, cats[i].indexOf("\""));
			final String catHtml = cleanCatHtml(html.substring(html.indexOf("id=\"album_members_" + catID + "\"")));
			final String[] memberStringArray = catHtml.split("class=\"lineupTab\">");
			parseMemberArray(memberStringArray, cat);
		}
	}

	public final void addToMemberList(final Member memberToAdd, final String role, final String category) {
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

	private final String cleanCatHtml(final String htmlPart) {
		String catHtml = htmlPart;
		if (htmlPart.contains("<!--")) {
			catHtml = htmlPart.substring(0, htmlPart.indexOf("<!--"));
		}
		return catHtml;
	}

	private final void parseMemberArray(final String[] memberStringArray, final String category) {
		for (int j = 1; j < memberStringArray.length; j++) {
			final Member member = parseMember(memberStringArray[j]);
			addToMemberList(member, parseMemberRole(memberStringArray[j]), category);
		}
	}

	private Member parseMember(final String htmlPart) {
		Member member = new Member(0);
		String[] memInfo = htmlPart.split("</td>");
		String name = parseMemberName(memInfo[0]);
		member.setName(name);
		member.setId(parseMemberId(memInfo[0], name));
		return member;
	}

	private String parseMemberRole(final String htmlPart) {
		String[] memInfo = htmlPart.split("</td>");
		return Jsoup.parse(memInfo[1]).text();
	}

	private long parseMemberId(final String html, final String memberName) {
		String memberId = html.substring(0, html.indexOf("\">" + memberName));
		memberId = memberId.substring(memberId.lastIndexOf("/") + 1, memberId.length());
		return Long.parseLong(memberId);
	}

	private String parseMemberName(final String html) {
		String name = html.substring(0, html.indexOf("</a>"));
		name = name.substring(name.lastIndexOf("\">") + 2);
		return name;
	}

	public final Map<Member, String> getLineupList() {
		return this.albumLineupList;
	}

	public final Map<Member, String> getOtherLinup() {
		return this.otherMemberList;
	}

	public final Map<Member, String> getGuestLineup() {
		return this.guestLineupList;
	}
}
