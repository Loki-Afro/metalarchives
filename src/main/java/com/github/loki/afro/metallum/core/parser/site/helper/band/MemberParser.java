package com.github.loki.afro.metallum.core.parser.site.helper.band;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.entity.partials.NullBand;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class MemberParser {

    private enum MemberCategory {
        PAST, PAST_LIVE, CURRENT, LIVE;
    }

    private final Map<Member, String> currentLineupList = new HashMap<>();
    private final Map<Member, String> pastLineupList = new HashMap<>();
    private final Map<Member, String> liveLineupList = new HashMap<>();
    private final Map<Member, String> pastLiveLineupList = new HashMap<>();

    private Member currentMember = null;
    private String role = null;
    private MemberCategory memberCategory = null;

    public void parse(Document doc) {
        Element bandTabMembersCurrent = doc.getElementById("band_tab_members_all");
        if (bandTabMembersCurrent != null) {
            Element table = bandTabMembersCurrent.select("table").first();
            if (table != null) {
                parseTable(table);
            }
        }
    }

    private void parseTable(Element table) {
        for (Element row : table.select("tr")) {
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
                        .replaceAll("\\(live\\)", "")
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
                throw new MetallumException("Unknown member row :" + row.text());
            }
        }
        addCurrentMember();
    }

    private void addCurrentMember() {
        Objects.requireNonNull(this.memberCategory);
        addToMemberList(this.currentMember, this.role, this.memberCategory);
        this.currentMember = null;
        this.role = null;
    }

    private static MemberCategory getMemberCategory(String text) {
        MemberCategory memberCategory;
        if ("current".equalsIgnoreCase(text) || "last known".equalsIgnoreCase(text)) {
            memberCategory = MemberCategory.CURRENT;
        } else if ("current (live)".equalsIgnoreCase(text) || "last known (live)".equalsIgnoreCase(text)) {
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
