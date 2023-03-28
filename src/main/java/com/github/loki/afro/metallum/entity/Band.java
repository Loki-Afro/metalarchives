package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.entity.partials.PartialImage;
import com.github.loki.afro.metallum.entity.partials.PartialLabel;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Band extends AbstractEntity {

    private static final Logger logger = LoggerFactory.getLogger(Band.class);

    private String genre;
    private Country country;
    private String province;
    private BandStatus status = null;
    private String lyricalThemes;
    private PartialLabel label;
    private Integer yearFormedIn;
    private PartialImage partialPhoto;
    private PartialImage partialLogo;
    private String info;
    private List<PartialDisc> discs = new ArrayList<>();
    private Map<Member, String> currentMembers = new HashMap<>();
    private Map<Member, String> currentLiveMembers = new HashMap<>();
    private Map<Member, String> pastMembers = new HashMap<>();
    private Map<Member, String> pastLiveMembers = new HashMap<>();
    private List<Link> officialLinks = new ArrayList<>();
    private List<Link> officialMerchLinks = new ArrayList<>();
    private List<Link> unofficialLinks = new ArrayList<>();
    private List<Link> tablatureLinks = new ArrayList<>();
    private List<Link> labelLinks = new ArrayList<>();
    private Map<Integer, List<SimilarBand>> similarArtist = new HashMap<>();
    private TreeSet<YearRange> yearsActive = new TreeSet<>();

    public Band(final long id, final String name) {
        super(id, name);
    }

    public final List<Disc> getDiscs() {
        return this.discs.stream()
                .map(PartialDisc::load)
                .collect(Collectors.toList());
    }

    public List<PartialDisc> getDiscsPartial() {
        return this.discs;
    }

    public int getYearFormedIn() {
        return this.yearFormedIn;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public void setLyricalThemes(final String themes) {
        this.lyricalThemes = themes;
    }

    public void setStatus(final BandStatus stat) {
        this.status = stat;
    }

    public void setLocation(final String countryStr, final String province) {
        setCountry(countryStr);
        setProvince(province);
    }

    public void setCountry(final String countryStr) {
        if (!countryStr.trim().isEmpty()) {
            this.country = Country.ofMetallumDisplayName(countryStr);
        }
    }

    public void setProvince(final String province) {
        this.province = province;
    }

    public Label getLabel() {
        return this.label.load();
    }

    public PartialLabel getPartialLabel() {
        return this.label;
    }

    public void setLabel(final PartialLabel newLabel) {
        this.label = newLabel;
    }

    public String getGenre() {
        return this.genre;
    }

    public Country getCountry() {
        return this.country;
    }

    public BandStatus getBandStatus() {
        return this.status;
    }

    public String getLyricalThemes() {
        return this.lyricalThemes;
    }

    public String getLocation() {
        return this.country.getFullName() + ", " + getProvince();
    }

    public String getProvince() {
        return this.province;
    }

    public void setYearFormedIn(final int year) {
        this.yearFormedIn = year;
    }

    public void setPhoto(final PartialImage bandPhoto) {
        this.partialPhoto = bandPhoto;
    }

    public void setLogo(final PartialImage bandLogo) {
        this.partialLogo = bandLogo;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

    public void setCurrentMembers(final Map<Member, String> currentMember) {
        this.currentMembers = currentMember;
    }

    public void setPastMembers(final Map<Member, String> pastMembers) {
        this.pastMembers = pastMembers;
    }

    public void setCurrentLiveMembers(final Map<Member, String> liveMembers) {
        this.currentLiveMembers = liveMembers;
    }

    public Optional<PartialImage> getPartialPhoto() {
        return Optional.ofNullable(partialPhoto);
    }

    public Optional<PartialImage> getPartialLogo() {
        return Optional.ofNullable(partialLogo);
    }

    public Optional<byte[]> getPhoto() {
        return Optional.ofNullable(partialPhoto).map(PartialImage::load);
    }

    public Optional<byte[]> getLogo() {
        return Optional.ofNullable(partialLogo).map(PartialImage::load);
    }

    public BandStatus getStatus() {
        return this.status;
    }

    /**
     * To add a Link to the Band.
     * If there is no suitable Category for the link, the link will not be added to any List.
     *
     * @param links does not matter what you put in for a Link as long as it has a category.
     */
    public void addLinks(final Link... links) {
        for (Link link : links) {
            switch (link.getCategory()) {
                case OFFICIAL:
                    this.officialLinks.add(link);
                    break;
                case OFFICIAL_MERCH:
                    this.officialMerchLinks.add(link);
                    break;
                case UNOFFICIAL:
                    this.unofficialLinks.add(link);
                    break;
                case LABEL:
                    this.labelLinks.add(link);
                    break;
                case TABLATURES:
                    this.tablatureLinks.add(link);
                    break;
                default:
                    logger.error("unrecognized link-category, the Link will be ignored, this should never happen!");
                    logger.error(link.getName() + "    " + link.getURL());
                    break;
            }
        }
    }

    public void addOfficialLinks(final Link... links) {
        Collections.addAll(this.officialLinks, links);
    }

    public void addOfficialMerchLinks(final Link... links) {
        Collections.addAll(this.officialMerchLinks, links);
    }

    public void addUnofficialLinks(final Link... links) {
        Collections.addAll(this.unofficialLinks, links);
    }

    public void addLabelLinks(final Link... links) {
        Collections.addAll(this.labelLinks, links);
    }

    public void addTablatureLinks(final Link... links) {
        Collections.addAll(this.tablatureLinks, links);
    }

    public void addSimilarArtists(final SimilarBand band, final int score) {
        List<SimilarBand> bandListFromMap = this.similarArtist.get(score);
        if (bandListFromMap == null) {
            bandListFromMap = new ArrayList<>();
            bandListFromMap.add(band);
        } else {
            bandListFromMap.add(band);
        }
        this.similarArtist.put(score, bandListFromMap);
    }

    public String getInfo() {
        return this.info;
    }

    public Map<Member, String> getCurrentMembers() {
        return currentMembers;
    }

    public Map<Member, String> getCurrentLiveMembers() {
        return currentLiveMembers;
    }

    public Map<Member, String> getPastLiveMembers() {
        return pastLiveMembers;
    }

    public void setPastLiveMembers(Map<Member, String> pastLiveMembers) {
        this.pastLiveMembers = pastLiveMembers;
    }

    public Map<Member, String> getPastMembers() {
        return pastMembers;
    }

    public List<Link> getOfficialLinks() {
        return this.officialLinks;
    }

    public List<Link> getOfficialMerchLinks() {
        return this.officialMerchLinks;
    }

    public List<Link> getUnofficialLinks() {
        return this.unofficialLinks;
    }

    public List<Link> getLabelLinks() {
        return this.labelLinks;
    }

    public List<Link> getTablatureLinks() {
        return this.tablatureLinks;
    }

    public void setSimilarArtists(final Map<Integer, List<SimilarBand>> similarArtists) {
        this.similarArtist = similarArtists;
    }

    public Map<Integer, List<SimilarBand>> getSimilarArtists() {
        return this.similarArtist;
    }

    public List<Link> getLinks() {
        List<Link> linkList = new ArrayList<>();
        linkList.addAll(this.officialLinks);
        linkList.addAll(this.officialMerchLinks);
        linkList.addAll(this.unofficialLinks);
        linkList.addAll(this.labelLinks);
        linkList.addAll(this.tablatureLinks);
        return linkList;
    }

    public final boolean hasPhoto() {
        return this.partialPhoto != null;
    }

    public final boolean hasLogo() {
        return this.partialLogo != null;
    }

    public final void setDiscs(final List<PartialDisc> discList) {
        this.discs = discList;
    }

    public void setOfficialLinks(final List<Link> officialLinks) {
        this.officialLinks = officialLinks;
    }

    public void setOfficialMerchLinks(final List<Link> officialMerchLinkList) {
        this.officialMerchLinks = officialMerchLinkList;
    }

    public void setUnofficialLinks(final List<Link> list) {
        this.unofficialLinks = list;
    }

    public void setLabelLinks(final List<Link> list) {
        this.labelLinks = list;
    }

    public void setTablatureLinks(final List<Link> list) {
        this.tablatureLinks = list;
    }

    public TreeSet<YearRange> getYearsActive() {
        return yearsActive;
    }

    public void setYearsActive(TreeSet<YearRange> yearsActive) {
        this.yearsActive = yearsActive;
    }

    public static class SimilarBand extends PartialBand {
        @Getter
        private final Country country;
        @Getter
        private final String genre;

        public SimilarBand(long id, String name, Country country, String genre) {
            super(id, name);
            this.country = country;
            this.genre = genre;
        }
    }

    public static class PartialDisc extends com.github.loki.afro.metallum.entity.partials.PartialDisc {

        @Getter
        private final DiscType discType;
        @Getter
        private final int releaseYear;
        @Getter
        private final int reviewCount;
        @Getter
        private final Integer averageReviewPercentage;

        public PartialDisc(long id, String name, DiscType discType, int releaseYear, int reviewCount, Integer averageReviewPercentage) {
            super(id, name);
            this.discType = discType;
            this.releaseYear = releaseYear;
            this.reviewCount = reviewCount;
            this.averageReviewPercentage = averageReviewPercentage;
        }

    }
}
