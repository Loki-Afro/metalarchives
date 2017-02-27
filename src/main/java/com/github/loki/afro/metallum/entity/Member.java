package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.enums.Country;

import java.awt.image.BufferedImage;
import java.util.*;

public class Member extends AbstractEntity {
    // good Example: http://www.metal-archives.com/artists/Kerry_King/267

    private Country country;
    private String realName;
    private List<Band> uncategorizedBands = new ArrayList<>();
    private int age = 0;
    private String province = "";
    private String gender = "";
    private BufferedImage photo = null;
    private String alternativeName = "";
    private Map<Band, Map<Disc, String>> guestSessionBands = new LinkedHashMap<>();
    private Map<Band, Map<Disc, String>> activeInBands = new LinkedHashMap<>();
    private Map<Band, Map<Disc, String>> pastBands = new LinkedHashMap<>();
    private Map<Band, Map<Disc, String>> miscBands = new LinkedHashMap<>();
    private final List<Link> linkList = new ArrayList<>();
    private String details = "";
    private String photoUrl = "";

    public Member(final long id) {
        super(id);
    }

    public Member() {
        super(0);
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
    public final List<Band> getUncategorizedBands() {
        return this.uncategorizedBands;
    }

    public String getRealName() {
        return this.realName;
    }

    public Country getCountry() {
        return this.country;
    }

    public final void setUncategorizedBands(final List<Band> memberBands) {
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
    public void setGuestIn(final Map<Band, Map<Disc, String>> map) {
        this.guestSessionBands = map;
    }

    public void setAlternativeName(final String alternativeName) {
        this.alternativeName = alternativeName;
    }

    @Deprecated
    public void setMiscActivities(final Map<Band, Map<Disc, String>> parseMiscBands) {
        this.miscBands = parseMiscBands;
    }

    @Deprecated
    public void setPastBands(final Map<Band, Map<Disc, String>> parsePastBands) {
        this.pastBands = parsePastBands;
    }

    @Deprecated
    public void setActiveIn(final Map<Band, Map<Disc, String>> parseActiveBands) {
        this.activeInBands = parseActiveBands;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return this.age;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return this.province;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * @return the photo
     */
    public BufferedImage getPhoto() {
        return this.photo;
    }

    /**
     * @return the alternativeName
     */
    public String getAlternativeName() {
        return this.alternativeName;
    }

    /**
     * @return the guestSessionBands
     */
    @Deprecated
    public Map<Band, Map<Disc, String>> getGuestSessionBands() {
        return this.guestSessionBands;
    }

    @Deprecated
    public Map<Band, Map<Disc, String>> getActiveInBands() {
        return this.activeInBands;
    }

    @Deprecated
    public Map<Band, Map<Disc, String>> getPastBands() {
        return this.pastBands;
    }

    @Deprecated
    public Map<Band, Map<Disc, String>> getMiscBands() {
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