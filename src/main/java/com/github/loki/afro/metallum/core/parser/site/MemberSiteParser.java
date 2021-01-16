package com.github.loki.afro.metallum.core.parser.site;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.core.parser.site.helper.LinkParser;
import com.github.loki.afro.metallum.core.parser.site.helper.member.BandParser;
import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.entity.Partial;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MemberSiteParser extends AbstractSiteParser<Member> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberSiteParser.class);

    private final boolean loadReadMore;

    public MemberSiteParser(final long entityId, final boolean loadImages, final boolean loadMemberLinks, final boolean loadReadMore) {
        super(entityId, loadImages, loadMemberLinks);
        this.loadReadMore = loadReadMore;
    }

    @Override
    public final Member parse() {
        String name = parseName();
        Member member = new Member(this.entityId, name);
        member.setRealName(parseRealName());
        member.setAge(parseAge());
        member.setCountry(parseCountry());
        member.setProvince(parseProvince());
        member.setGender(parseGender());
        final String imageUrl = parseImageUrl();
        member.setPhotoUrl(imageUrl);
        member.setPhoto(parseImage(imageUrl));
        member.setGuestIn(parseGuestSessionBands());
        member.setActiveIn(parseActiveBands());
        member.setPastBands(parsePastBands());
        member.setMiscActivities(parseMiscBands());
        member.setDetails(parseDetails());
        member.addLinks(parseLinks());
        member = parseModifications(member);
        return member;
    }

    private final String parseName() {
        return this.html.substring(this.html.indexOf("<h1 class=\"band_member_name\">") + 29, this.html.indexOf("</h1>"));
    }

    private final String parseRealName() {
        String[] discDetails = this.html.substring(this.html.indexOf("<dl class=\"float_left\"")).split("<dd>");
        String realName = discDetails[1];
        realName = realName.substring(0, realName.indexOf("</dd>"));
        return MetallumUtil.parseHtmlWithLineSeparators(realName);
    }

    private final int parseAge() {
        final String[] discDetails = this.html.substring(this.html.indexOf("<dl class=\"float_left\"")).split("<dd>");
        String age = discDetails[2];
        if (age.contains("N/A")) {
            return 0;
        } else {
            age = age.substring(0, age.indexOf("</dd>")).trim();
            if (age.contains("(born")) {
                age = age.replaceAll("\\(born.*", "").trim();
            }
            return Integer.parseInt(age);
        }
    }

    private final Country parseCountry() {
        final String details = this.html.substring(this.html.indexOf("<dl class=\"float_right\""));
        final String[] memberDetails = details.split("<dd>");
        if (memberDetails[1].contains("N/A")) {
            return null;
        }
        final String country = memberDetails[1].substring(memberDetails[1].indexOf("\">") + 2, memberDetails[1].indexOf("</a>"));
        return Country.ofMetallumDisplayName(country);
    }

    private String parseProvince() {
        final String details = this.html.substring(this.html.indexOf("<dl class=\"float_right\""));
        final String[] memberDetails = details.split("<dd>");
        if (Jsoup.parse(memberDetails[1]).text().matches(".*\\(.+\\).*")) {
            String province = Jsoup.parse(memberDetails[1]).text();
            province = province.substring(province.indexOf(" (") + 2, province.indexOf(") "));
            return province;
        }
        return "";
    }

    private final String parseGender() {
        String details = this.html.substring(this.html.indexOf("<dl class=\"float_right\""));
        String[] memberDetails = details.split("<dd>");
        return memberDetails[2].substring(0, memberDetails[2].indexOf("</dd>"));
    }

    private Map<Partial, Map<Partial, String>> parseActiveBands() {
        final BandParser parser = new BandParser();
        return parser.parse(this.html, BandParser.Mode.ACTIVE);
    }

    private Map<Partial, Map<Partial, String>> parsePastBands() {
        final BandParser parser = new BandParser();
        return parser.parse(this.html, BandParser.Mode.PAST);
    }

    private Map<Partial, Map<Partial, String>> parseGuestSessionBands() {
        final BandParser parser = new BandParser();
        return parser.parse(this.html, BandParser.Mode.GUEST);
    }

    // 2007 Equally Destructive (Single) Design, above the band name
    private Map<Partial, Map<Partial, String>> parseMiscBands() {
        final BandParser parser = new BandParser();
        return parser.parse(this.html, BandParser.Mode.MISC);
    }

    private Link[] parseLinks() {
        if (this.loadLinks) {
            try {
                final LinkParser parser = new LinkParser(this.entityId, LinkParser.MEMBER_PARSER);
                return parser.parse();
            } catch (final ExecutionException e) {
                LOGGER.error("Unable to parse links of " + this.entityId, e);
            }
        }
        return new Link[0];
    }

    private final String parseImageUrl() {
        String imageUrl = null;
        if (this.html.contains("<div class=\"member_img\">")) {
            imageUrl = this.html.substring(this.html.indexOf("<div class=\"member_img\""));
            imageUrl = imageUrl.substring(imageUrl.indexOf("href=\"") + 6);
            imageUrl = imageUrl.substring(0, imageUrl.indexOf("\""));
        }
        return imageUrl;

    }

    private final BufferedImage parseImage(final String imageUrl) {
        if (this.loadImage && imageUrl != null) {
            try {
                return Downloader.getImage(imageUrl);
            } catch (final MetallumException e) {
                throw new MetallumException("Unable get photo for " + this.entityId, e);

            }
        }
        return null;
    }

    private String parseReadMore() {
        String html = "";
        try {
            html = Downloader.getHTML(MetallumURL.assembleMemberReadMoreURL(this.entityId));
        } catch (final MetallumException e) {
            throw new MetallumException("Unable get \"read more\" for " + this.entityId, e);
        }
        return MetallumUtil.parseHtmlWithLineSeparators(html);
    }

    private final String parseDetails() {
        if (this.loadReadMore && this.html.contains("class=\"btn_read_more")) {
            return parseReadMore();
        }
        String biography = this.html.substring(this.html.indexOf("<div class=\"clear band_comment\">"));
        biography = biography.substring(0, biography.indexOf("</div>"));
        // To keep the headlines formatted
        biography = biography.replaceAll("</?h.*?>", "<br><br>");
        biography = MetallumUtil.parseHtmlWithLineSeparators(biography);
        return biography;
    }

    @Override
    protected final String getSiteURL() {
        return MetallumURL.assembleMemberURL(this.entityId);
    }

}
