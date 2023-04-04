package com.github.loki.afro.metallum.core.parser.site.helper.band;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.partials.NullBand;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MemberParser {

    private enum MemberCategory {
        CURRENT("band_tab_members_current"), PAST("band_tab_members_past"), LIVE("band_tab_members_live"), PAST_LIVE(null);

        private final String elementId;

        MemberCategory(String elementId) {
            this.elementId = elementId;
        }
        static Set<MemberCategory> directlyReferencedCategories() {
            return Set.of(CURRENT, PAST, LIVE);
        }
    }

    private final List<Band.PartialMember> currentLineupList = new ArrayList<>();
    private final List<Band.PartialMember> pastLineupList = new ArrayList<>();
    private final List<Band.PartialMember> liveLineupList = new ArrayList<>();
    private final List<Band.PartialMember> pastLiveLineupList = new ArrayList<>();

    private CurrentlyParsedMember currentMember = null;
    private MemberCategory memberCategory = null;

    public void parse(Document doc) {
        for (MemberCategory category : MemberCategory.directlyReferencedCategories()) {
            Element foundCategoryElement = doc.getElementById(category.elementId);
            if (foundCategoryElement != null) {
                Element table = foundCategoryElement.select("table").first();
                if (table != null) {
                    parseTable(table, category);
                }
            }
        }
    }

    private void parseTable(Element table, MemberCategory category) {
        this.memberCategory = category;
        for (Element row : table.select("tr")) {
            Elements columns = row.select("td");
            if (category == MemberCategory.LIVE && row.hasClass("lineupHeaders")) {
                if (this.currentMember != null) {
                    addCurrentMember();
                }
                // past comes always after current
                if ("past".equalsIgnoreCase(row.text())) {
                    this.memberCategory = MemberCategory.PAST_LIVE;
                }
            } else if (row.hasClass("lineupRow")) {
                if (this.currentMember != null) {
                    addCurrentMember();
                }
                Element firstColumn = columns.get(0);
                String href = firstColumn.select("a").attr("href");
                String memberName = firstColumn.text();
                this.currentMember = new CurrentlyParsedMember(parseIdFromUrl(href), memberName, columns.get(1).text());
            } else if (row.hasClass("lineupBandsRow")) {
                Objects.requireNonNull(this.currentMember);
                this.currentMember.bands = parseBandRow(row);
            } else {
                throw new MetallumException("Unknown member row :" + row.text());
            }
        }
        addCurrentMember();
    }

    private static class CurrentlyParsedMember {
        private final long id;
        private final String name;
        private final String role;
        private List<PartialBand> bands = new ArrayList<>();

        public CurrentlyParsedMember(long id, String name, String role) {
            this.name = name;
            this.id = id;
            this.role = role;
        }

        Band.PartialMember toPartialMember() {
            return new Band.PartialMember(id, name, this.role, bands);
        }
    }

    private List<PartialBand> parseBandRow(Element row) {
        List<PartialBand> bands = new ArrayList<>();
        String[] bandStrings = row.html()
                .replace("</td>", "")
                .replaceAll("\\(R\\.I\\.P\\. \\d\\d\\d\\d\\) ", "")
                .replace("See also: ", "")
                .replace("ex-", "")
                .replace("(live)", "")
                .replaceAll("<td[^>]*+>", "")
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
        return bands;
    }

    private void addCurrentMember() {
        Objects.requireNonNull(this.memberCategory);
        addToMemberList(this.currentMember.toPartialMember(), this.memberCategory);
        this.currentMember = null;
    }

    private long parseIdFromUrl(String element) {
        String memberId = element.substring(element.lastIndexOf("/") + 1);
        return Long.parseLong(memberId);
    }

    private void addToMemberList(final Band.PartialMember memberToAdd, final MemberCategory category) {
        switch (category) {
            case CURRENT:
                this.currentLineupList.add(memberToAdd);
                break;
            case LIVE:
                this.liveLineupList.add(memberToAdd);
                break;
            case PAST:
                this.pastLineupList.add(memberToAdd);
                break;
            case PAST_LIVE:
                this.pastLiveLineupList.add(memberToAdd);
                break;
            default:
                break;
        }
    }

    public final List<Band.PartialMember> getCurrentLineup() {
        return this.currentLineupList;
    }

    public final List<Band.PartialMember> getCurrentLiveLineup() {
        return this.liveLineupList;
    }

    public final List<Band.PartialMember> getPastLineup() {
        return this.pastLineupList;
    }

    public List<Band.PartialMember> getPastLiveLineupList() {
        return pastLiveLineupList;
    }
}
