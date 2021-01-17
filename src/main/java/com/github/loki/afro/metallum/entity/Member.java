package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.entity.partials.PartialDisc;
import com.github.loki.afro.metallum.enums.Country;

import java.awt.image.BufferedImage;
import java.util.*;

public class Member extends AbstractEntity {
    // good Example: http://www.metal-archives.com/artists/Kerry_King/267

    private Country country;
    private String realName;
    private List<PartialBand> uncategorizedBands = new ArrayList<>();
    private Integer age;
    private String province;
    private String gender;
    private BufferedImage photo = null;
    private String alternativeName;
    private Map<PartialBand, Map<PartialDisc, String>> guestSessionBands = new LinkedHashMap<>();
    private Map<PartialBand, Map<PartialDisc, String>> activeInBands = new LinkedHashMap<>();
    private Map<PartialBand, Map<PartialDisc, String>> pastBands = new LinkedHashMap<>();
    private Map<PartialBand, Map<PartialDisc, String>> miscBands = new LinkedHashMap<>();
    private final List<Link> linkList = new ArrayList<>();
    private String details;
    private String photoUrl;

    public Member(long id, String name) {
        super(id, name);
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    public void setRealName(final String realName) {
        this.realName = realName;
    }

    /**
     * If we just search for the Member it is not possible to know if these are past Bands.
     *
     * @return uncategorizedBands
     */
    public final List<PartialBand> getUncategorizedBands() {
        return this.uncategorizedBands;
    }

    public String getRealName() {
        return this.realName;
    }

    public Country getCountry() {
        return this.country;
    }

    public final void setUncategorizedBands(final List<PartialBand> memberBands) {
        this.uncategorizedBands = memberBands;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public void setProvince(final String province) {
        this.province = province;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public void setPhoto(final BufferedImage parseMemberImage) {
        this.photo = parseMemberImage;
    }

    @Deprecated
    public void setGuestIn(final Map<PartialBand, Map<PartialDisc, String>> map) {
        this.guestSessionBands = map;
    }

    public void setAlternativeName(final String alternativeName) {
        this.alternativeName = alternativeName;
    }

    @Deprecated
    public void setMiscActivities(final Map<PartialBand, Map<PartialDisc, String>> parseMiscBands) {
        this.miscBands = parseMiscBands;
    }

    @Deprecated
    public void setPastBands(final Map<PartialBand, Map<PartialDisc, String>> parsePastBands) {
        this.pastBands = parsePastBands;
    }

    @Deprecated
    public void setActiveIn(final Map<PartialBand, Map<PartialDisc, String>> parseActiveBands) {
        this.activeInBands = parseActiveBands;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public int getAge() {
        return this.age;
    }

    public String getProvince() {
        return this.province;
    }

    public String getGender() {
        return this.gender;
    }

    public BufferedImage getPhoto() {
        return this.photo;
    }

    public String getAlternativeName() {
        return this.alternativeName;
    }

    @Deprecated
    public Map<PartialBand, Map<PartialDisc, String>> getGuestSessionBands() {
        return this.guestSessionBands;
    }

    @Deprecated
    public Map<PartialBand, Map<PartialDisc, String>> getActiveInBands() {
        return this.activeInBands;
    }

    @Deprecated
    public Map<PartialBand, Map<PartialDisc, String>> getPastBands() {
        return this.pastBands;
    }

    @Deprecated
    public Map<PartialBand, Map<PartialDisc, String>> getMiscBands() {
        return this.miscBands;
    }

    public String getDetails() {
        return this.details;
    }

    public void addLinks(final Link... links) {
        Collections.addAll(this.linkList, links);
    }

    public List<Link> getLinks() {
        return this.linkList;
    }

    public boolean hasPhoto() {
        return this.photo != null;
    }

    public final void setPhotoUrl(final String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public final String getPhotoUrl() {
        return this.photoUrl;
    }

}