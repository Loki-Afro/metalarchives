package com.github.loki.afro.metallum.core.parser.site.helper.band;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.entity.partials.NullBand;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MemberParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberParser.class);

    private enum MemberCategory {
        COMPLETE, PAST, PAST_LIVE, CURRENT, LIVE;
    }

    private final Map<Member, String> currentLineupList = new HashMap<>();
    private final Map<Member, String> pastLineupList = new HashMap<>();
    private final Map<Member, String> liveLineupList = new HashMap<>();
    private final Map<Member, String> pastLiveLineupList = new HashMap<>();

    private Member currentMember = null;
    private String role = null;
    private MemberCategory memberCategory = null;

    public void parse(Document doc) {
        Element bandMembers = doc.getElementById("band_members");
        if (bandMembers != null) {
            Element bandTabMembersCurrent = doc.getElementById("band_tab_members_all");
            if (bandTabMembersCurrent != null) {

                Element table = bandTabMembersCurrent.select("table").first();
                if (table != null) {

                    Elements rows = table.select("tr");
                    for (Element row : rows) {
                        Elements columns = row.select("td");
                        if (row.hasClass("lineupHeaders")) {
                            if (this.currentMember != null) {
                                addCurrentMember();
                            }
                            String text = row.text();
                            this.memberCategory = getMemberCategory(text);
                        } else if (row.hasClass("lineupRow")) {
                            if (this.currentMember != null) {
                                addCurrentMember();
                            }
                            Element firstColumn = columns.get(0);
                            String href = firstColumn.select("a").attr("href");
                            String memberName = firstColumn.text();
                            this.currentMember = new Member(parseIdFromUrl(href), memberName);
                            this.role = columns.get(1).text();
                        } else if (row.hasClass("lineupBandsRow")) {
                            Objects.requireNonNull(this.currentMember);
                            List<PartialBand> bands = new ArrayList<>();
                            String[] bandStrings = row.html().replaceAll("</td>", "")
                                    .replaceAll("<td.*?>", "")
                                    .replaceAll("\\(R\\.I\\.P\\. \\d\\d\\d\\d\\) ", "")
                                    .replaceAll("See also: ", "")
                                    .replaceAll("ex-", "")
                                    .split(",");
                            for (String bandStr : bandStrings) {
                                bandStr = bandStr.trim();
                                if (bandStr.startsWith("<a href")) {
                                    long bandId = parseIdFromUrl(bandStr.replaceAll("<a href=\"(http.+\\d+)\".*", "$1"));
                                    String name = bandStr.replaceAll(".+\">(.+)</a>", "$1");
                                    bands.add(new PartialBand(bandId, name));
                                } else {
                                    bands.add(new NullBand(bandStr));
                                }
                            }
                            this.currentMember.setUncategorizedBands(bands);
                        } else {
                            throw new IllegalStateException("tbd");
                        }
                    }
                    addCurrentMember();
                }
            }
        }
    }

    private void addCurrentMember() {
        Objects.requireNonNull(this.memberCategory);
        addToMemberList(this.currentMember, this.role, this.memberCategory);
        this.currentMember = null;
        this.role = null;
    }

    private static MemberCategory getMemberCategory(String text) {
        MemberCategory memberCategory;
        if ("current".equalsIgnoreCase(text)) {
            memberCategory = MemberCategory.CURRENT;
        } else if ("current (live)".equalsIgnoreCase(text)) {
            memberCategory = MemberCategory.LIVE;
        } else if ("past".equalsIgnoreCase(text)) {
            memberCategory = MemberCategory.PAST;
        } else if ("past (live)".equalsIgnoreCase(text)) {
            memberCategory = MemberCategory.PAST_LIVE;
        } else {
            throw new IllegalStateException("unknown category: " + text);
        }
        return memberCategory;
    }

    private long parseIdFromUrl(String element) {
        String memberId = element.substring(element.lastIndexOf("/") + 1);
        return Long.parseLong(memberId);
    }

    public final void parse(final String html) {
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
                    long memberId = parseIdFromUrl(memInfo[0]);
                    String memberName = parseMemberName(memInfo[0]);
                    final Member member = new Member(memberId, memberName);
                    final String role = parseMemberRole(memInfo[1]);
                    if (memInfo.length > 2) {
                        member.setUncategorizedBands(parseMemberBands(memInfo[2]));
                    }
//                    addToMemberList(member, role, cat);
                }
            }

        }
    }

    private void addToMemberList(final Member memberToAdd, final String role, final MemberCategory category) {
        switch (category) {
            case CURRENT:
                this.currentLineupList.put(memberToAdd, role);
                break;
            case LIVE:
                this.liveLineupList.put(memberToAdd, role);
                break;
            case PAST:
                this.pastLineupList.put(memberToAdd, role);
                break;
            case PAST_LIVE:
                this.pastLiveLineupList.put(memberToAdd, role);
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

    private String parseMemberName(final String htmlPart) {
        return Jsoup.parse(htmlPart).text();
    }

    private List<PartialBand> parseMemberBands(final String htmlPart) {
        final List<PartialBand> mBands = new ArrayList<>();
        if (!htmlPart.contains("See also:")) {
            return mBands;
        }

        final String[] bands = htmlPart.split("<a");
        for (int i = 1; i < bands.length; i++) {
            String name = bands[i].substring(bands[i].indexOf(">") + 1, bands[i].indexOf("</a>"));

            String idStr = bands[i].substring(bands[i].indexOf("/bands/") + 7);
            idStr = idStr.substring(idStr.indexOf("/") + 1, idStr.indexOf("\">"));
            long id = Long.parseLong(idStr);

            mBands.add(new PartialBand(id, name));
        }

        return mBands;
    }

    public final Map<Member, String> getCurrentLineup() {
        return this.currentLineupList;
    }

    public final Map<Member, String> getCurrentLiveLineup() {
        return this.liveLineupList;
    }

    public final Map<Member, String> getPastLineup() {
        return this.pastLineupList;
    }

    public Map<Member, String> getPastLiveLineupList() {
        return pastLiveLineupList;
    }
}
