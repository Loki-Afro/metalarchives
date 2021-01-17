package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.entity.partials.PartialLabel;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.LabelStatus;

import java.awt.image.BufferedImage;
import java.util.*;

public class Label extends AbstractEntity {

    private String specialisation;
    private Country country;
    private PartialLabel parentLabel = null;
    private String address;
    private String phoneNumber;
    private LabelStatus status;
    private String foundingDate;
    private List<PartialLabel> subLabels = new ArrayList<>();
    private boolean onlineShopping = false;
    private Link websiteURL;
    private String email;
    private List<Band> currentRoster = new ArrayList<>();
    private Map<Band, List<Disc>> releases = new LinkedHashMap<>();
    private Map<Band, Integer> pastRoster = new LinkedHashMap<>();
    private String details;
    private List<Link> links = new ArrayList<>();
    private BufferedImage logo = null;
    private String logoUrl = null;

    public Label(final long id, final String labelName) {
        super(id, labelName);
    }

    public PartialLabel getParentLabel() {
        return this.parentLabel;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public LabelStatus getStatus() {
        return this.status;
    }

    public String getFoundingDate() {
        return this.foundingDate;
    }

    public List<PartialLabel> getSubLabels() {
        return this.subLabels;
    }

    public Link getWebsiteURL() {
        return this.websiteURL;
    }

    public String getEmail() {
        return this.email;
    }

    public List<Band> getCurrentRoster() {
        return this.currentRoster;
    }

    public Map<Band, Integer> getPastRoster() {
        return this.pastRoster;
    }

    public String getDetails() {
        return this.details;
    }

    public List<Link> getLinks() {
        return this.links;
    }

    public Map<Band, List<Disc>> getReleases() {
        return this.releases;
    }

    public BufferedImage getLogo() {
        return this.logo;
    }

    public String getSpecialisation() {
        return this.specialisation;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setSpecialisation(final String specialisation) {
        this.specialisation = specialisation;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(final LabelStatus parseLabelStatus) {
        this.status = parseLabelStatus;
    }

    public void setFoundingDate(final String foundingDate) {
        this.foundingDate = foundingDate;
    }

    public void setSubLabels(final List<PartialLabel> subLabels) {
        this.subLabels = subLabels;
    }

    public void setOnlineShopping(final boolean onlineShopping) {
        this.onlineShopping = onlineShopping;
    }

    public boolean getHasOnlineShopping() {
        return this.onlineShopping;
    }

    public void setWebSiteURL(final Link websiteURL) {
        this.websiteURL = websiteURL;
    }

    public void setEmail(final String labelEmail) {
        this.email = labelEmail;
    }

    public void setCurrentRoster(final List<Band> currentRooster) {
        this.currentRoster = currentRooster;
    }

    public void setPastRoster(final Map<Band, Integer> map) {
        this.pastRoster = map;
    }

    public void setLinks(final List<Link> links) {
        this.links = links;
    }

    public void setAdditionalNotes(final String additionalNotes) {
        this.details = additionalNotes;
    }

    public void setReleases(final Map<Band, List<Disc>> map) {
        this.releases = map;
    }

    public void setParentLabel(final PartialLabel parseParentLabel) {
        this.parentLabel = parseParentLabel;
    }

    public void setLogo(final BufferedImage logo) {
        this.logo = logo;
    }

    public void addLink(final Link... links) {
        Collections.addAll(this.links, links);
    }

    public boolean hasLogo() {
        return this.logo != null;
    }

    public final void setLogoUrl(final String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public final String getLogoUrl() {
        return this.logoUrl;
    }

}
