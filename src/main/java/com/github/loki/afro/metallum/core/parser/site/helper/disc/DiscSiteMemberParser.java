package com.github.loki.afro.metallum.core.parser.site.helper.disc;

import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Member;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscSiteMemberParser {
    private final List<Disc.PartialMember> albumLineupList = new ArrayList<>();
    private final List<Disc.PartialMember> guestLineupList = new ArrayList<>();
    private final List<Disc.PartialMember> otherMemberList = new ArrayList<>();
    private final Document doc;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscSiteMemberParser.class);

    private enum MemberCategory {
        ALBUM_LINEUP("Band members"), ALBUM_GUEST("Guest/session musicians", "Guest/Session"), ALBUM_OTHER("Other staff", "Misc. staff", "Miscellaneous staff");
        private final String[] asString;

        MemberCategory(final String... asString) {
            this.asString = asString;
        }

        public static MemberCategory getMemberTypeForString(final String possibleMemberCategory) {
            for (final MemberCategory cat : values()) {
                if (MetallumUtil.isStringInArray(possibleMemberCategory, cat.asString)) {
                    return cat;
                }
            }
            LOGGER.error("MemberCategory: " + possibleMemberCategory);
            return null;
        }

    }

    public DiscSiteMemberParser(final Document htmlDocument) {
        this.doc = htmlDocument;
    }

    public final void parse() {
        Element lineUpElementAll = this.doc.getElementById("album_all_members_lineup");
        String category = null;
//		important when there are no categories or just one, happens often by Split DVDs and such things
        if (lineUpElementAll == null) {
            lineUpElementAll = this.doc.getElementById("album_members_lineup");
//            not 100% sure when this occurs but sometimes there is just "Misc.staff"
            if (lineUpElementAll != null) {
                category = this.doc.getElementsByAttributeValue("href", "#album_members_lineup").first().text();
            } else {
                lineUpElementAll = this.doc.getElementById("album_members_misc");
                category = this.doc.getElementsByAttributeValue("href", "#album_members_misc").first().text();
            }
        }
        Elements tableRows = lineUpElementAll.getElementsByTag("tr");
        for (final Element row : tableRows) {
            if (row.hasClass("lineupHeaders")) {
                category = row.text();
            } else if (row.hasClass("lineupRow")) {
                Disc.PartialMember member = parseMember(row);
                addToMemberList(member, category);
            }
        }
    }

    private Disc.PartialMember parseMember(final Element memberRow) {
        String memberIdStr = "0";
        Element memberLink = memberRow.getElementsByAttribute("href").first();
        if (memberLink != null) {
            memberIdStr = memberLink.attr("href");
            memberIdStr = memberIdStr.substring(memberIdStr.lastIndexOf("/") + 1);
        } else {
            LOGGER.warn("Member without Link detected, please report that; Member = " + memberRow.text());
        }
        String role = memberRow.getElementsByTag("td").last().text();
        String memberName = memberLink != null ? memberLink.text() : memberRow.text();
        return new Disc.PartialMember(Long.parseLong(memberIdStr), memberName, role);
    }

    private void addToMemberList(final Disc.PartialMember memberToAdd, final String category) {
        final MemberCategory cat = MemberCategory.getMemberTypeForString(category);
        switch (cat) {
            case ALBUM_LINEUP:
                this.albumLineupList.add(memberToAdd);
                break;
            case ALBUM_GUEST:
                this.guestLineupList.add(memberToAdd);
                break;
            case ALBUM_OTHER:
                this.otherMemberList.add(memberToAdd);
                break;
            default:
                break;
        }
    }

    public final List<Disc.PartialMember> getLineup() {
        return this.albumLineupList;
    }

    public final List<Disc.PartialMember> getOtherLineup() {
        return this.otherMemberList;
    }

    public final List<Disc.PartialMember> getGuestLineup() {
        return this.guestLineupList;
    }
}
