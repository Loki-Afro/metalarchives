package de.loki.metallum.entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.loki.metallum.enums.Country;
import de.loki.metallum.enums.LabelStatus;

public class Label extends AbstractEntity {

	private String					specialisation	= "";
	private Country					country			= Country.ANY;
	private Label					parentLabel		= null;
	private String					address			= "";
	private String					phoneNumber		= "";
	private LabelStatus				status			= LabelStatus.ANY;
	private String					foundingDate	= "";
	private List<Label>				subLabels		= new ArrayList<Label>();
	private boolean					onlineShopping	= false;
	private Link					websiteURL		= new Link();
	private String					email			= "";
	private List<Band>				currentRoser	= new ArrayList<Band>();
	private Map<Band, List<Disc>>	releases		= new LinkedHashMap<Band, List<Disc>>();
	private Map<Band, Integer>		pastRoster		= new LinkedHashMap<Band, Integer>();
	private String					details			= "";
	private List<Link>				links			= new ArrayList<Link>();
	private BufferedImage			logo			= null;
	private String					logoUrl			= null;

	public Label(final long id) {
		super(id);
	}

	public Label(final long id, final String labelName) {
		super(id, labelName);
	}

	public Label() {
		super(0, "");
	}

	/**
	 * @return the parentLabel
	 */
	public Label getParentLabel() {
		return this.parentLabel;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * @return the status
	 */
	public LabelStatus getStatus() {
		return this.status;
	}

	/**
	 * @return the foundingDate
	 */
	public String getFoundingDate() {
		return this.foundingDate;
	}

	/**
	 * @return the subLabels
	 */
	public List<Label> getSubLabels() {
		return this.subLabels;
	}

	/**
	 * @return the websiteURL
	 */
	public Link getWebsiteURL() {
		return this.websiteURL;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the currentRoser
	 */
	public List<Band> getCurrentRoser() {
		return this.currentRoser;
	}

	/**
	 * @return the pastRoster
	 */
	public Map<Band, Integer> getPastRoster() {
		return this.pastRoster;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return this.details;
	}

	/**
	 * @return the links
	 */
	public List<Link> getLinks() {
		return this.links;
	}

	/**
	 * @return the releases
	 */
	public Map<Band, List<Disc>> getReleases() {
		return this.releases;
	}

	/**
	 * @return the logo
	 */
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

	public void setSubLabels(final List<Label> subLabels) {
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
		this.currentRoser = currentRooster;
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

	public void setParentLabel(final Label parseParentLabel) {
		this.parentLabel = parseParentLabel;
	}

	public void setLogo(final BufferedImage logo) {
		this.logo = logo;
	}

	public void addLink(final Link... links) {
		for (Link link : links) {
			this.links.add(link);
		}
	}

	public boolean hasLogo() {
		if (this.logo != null) {
			return true;
		}
		return false;
	}

	public final void setLogoUrl(final String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public final String getLogoUrl() {
		return this.logoUrl;
	}

}
