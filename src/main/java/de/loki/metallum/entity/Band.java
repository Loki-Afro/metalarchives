package de.loki.metallum.entity;

import de.loki.metallum.enums.BandStatus;
import de.loki.metallum.enums.Country;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Band extends AbstractEntity {

	private String                   genre             = "";
	private Country                  country           = Country.ANY;
	private String                   province          = "";
	private BandStatus               status            = null;
	private String                   lyricalThemes     = "";
	private Label                    label             = new Label(0);
	private int                      yearFormedIn      = 0;
	private BufferedImage            photo             = null;
	private BufferedImage            logo              = null;
	private String                   info              = "";
	private List<Disc>               discs             = new ArrayList<Disc>();
	private Map<Member, String>      currentMember     = new HashMap<Member, String>();
	private Map<Member, String>      lastMember        = new HashMap<Member, String>();
	private Map<Member, String>      liveMember        = new HashMap<Member, String>();
	private Map<Member, String>      pastMember        = new HashMap<Member, String>();
	private List<Link>               officalLinks      = new ArrayList<Link>();
	private List<Link>               officalMerchLinks = new ArrayList<Link>();
	private List<Link>               unofficalLinks    = new ArrayList<Link>();
	private List<Link>               tabulatureLinks   = new ArrayList<Link>();
	private List<Link>               labelLinks        = new ArrayList<Link>();
	private Map<Integer, List<Band>> similarArtist     = new HashMap<Integer, List<Band>>();
	private String                   photoUrl          = null;
	private String                   logoUrl           = null;

	/**
	 * DummyBand Constructor
	 */
	public Band() {
		super(0);
	}

	public Band(final long id) {
		super(id);
	}

	public Band(final long id, final String name) {
		super(id, name);
	}

	public final List<Disc> getDiscs() {
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
			this.country = Country.getRightCountryForString(countryStr);
		}
	}

	public void setProvince(final String province) {
		this.province = province;
	}

	public Label getLabel() {
		return this.label;
	}

	public void setLabel(final Label newLabel) {
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

	public void setPhoto(final BufferedImage bandPhoto) {
		this.photo = bandPhoto;
	}

	public void setLogo(final BufferedImage bandLogo) {
		this.logo = bandLogo;

	}

	public void setInfo(final String info) {
		this.info = info;
	}

	public void addToDiscography(final Disc... discs) {
		for (final Disc disc : discs) {
			if (!this.discs.contains(disc)) {
				// if (disc.getBand() != this) {
				// disc.setBand(this);
				// }
				this.discs.add(disc);
			}
		}
	}

	public void setCurrentLineup(final Map<Member, String> currentMember) {
		this.currentMember = currentMember;
	}

	public void setPastLineup(final Map<Member, String> pastMembers) {
		this.pastMember = pastMembers;
	}

	public void setLiveLineup(final Map<Member, String> liveMembers) {
		this.liveMember = liveMembers;
	}

	public void setLastKnownLineup(final Map<Member, String> lastMembers) {
		this.lastMember = lastMembers;
	}

	public void addToReviews(final Review... reviews) {
		for (final Review review : reviews) {
			for (final Disc disc : this.discs) {
				if (disc.getId() == review.getDisc().getId()) {
					disc.addReview(review);
				}
			}
		}
	}

	/**
	 * The band photo is mostly a picture of the band members.
	 *
	 * @return the bandphoto as Image, could be null if there is none!
	 */
	public BufferedImage getPhoto() {
		return this.photo;
	}

	/**
	 * The Band logo is mostly the lettering of the band.
	 *
	 * @return the bandlogo as Image, could be null if there is none!
	 */
	public BufferedImage getLogo() {
		return this.logo;
	}

	public BandStatus getStatus() {
		return this.status;
	}

	/**
	 * To add a Link to the Band.
	 * If there is no suitable Category for the link, the link will not be added to any List.
	 *
	 * @param links doesen't matter what you put in for a Link as long as it has a category.
	 */
	public void addLinks(final Link... links) {
		for (Link link : links) {
			switch (link.getCategory()) {
				case OFFICAL:
					this.officalLinks.add(link);
					break;
				case OFFICAL_MERCH:
					this.officalMerchLinks.add(link);
					break;
				case UNOFFICAL:
					this.unofficalLinks.add(link);
					break;
				case LABEL:
					this.labelLinks.add(link);
					break;
				case TABULATURES:
					this.tabulatureLinks.add(link);
					break;
				default:
					System.err.println("unrecognized linkcategory, the Link will be ignored, this should never happen!");
					System.err.println(link.getName() + "    " + link.getURL());
					break;
			}
		}
	}

	public void addOfficalLinks(final Link... links) {
		for (Link link : links) {
			this.officalLinks.add(link);
		}
	}

	public void addOfficalMerchLinks(final Link... links) {
		for (Link link : links) {
			this.officalMerchLinks.add(link);
		}
	}

	public void addUnofficalLinks(final Link... links) {
		for (Link link : links) {
			this.unofficalLinks.add(link);
		}
	}

	public void addLabelLinks(final Link... links) {
		for (Link link : links) {
			this.labelLinks.add(link);
		}
	}

	public void addTabulatureLinks(final Link... links) {
		for (Link link : links) {
			this.tabulatureLinks.add(link);
		}
	}

	public void addSimliarArtists(final Band band, final int score) {
		List<Band> bandListFromMap = this.similarArtist.get(score);
		if (bandListFromMap == null) {
			bandListFromMap = new ArrayList<Band>();
			bandListFromMap.add(band);
		} else {
			bandListFromMap.add(band);
		}
		this.similarArtist.put(score, bandListFromMap);
	}

	public String getInfo() {
		return this.info;
	}

	public Map<Member, String> getCurrentLineup() {
		return this.currentMember;
	}

	public Map<Member, String> getLastKnownLineup() {
		return this.lastMember;
	}

	public Map<Member, String> getLiveLineup() {
		return this.liveMember;
	}

	public Map<Member, String> getPastLineup() {
		return this.pastMember;
	}

	public List<Link> getOfficalLinks() {
		return this.officalLinks;
	}

	public List<Link> getOfficalMerchLinks() {
		return this.officalMerchLinks;
	}

	public List<Link> getUnofficalLinks() {
		return this.unofficalLinks;
	}

	public List<Link> getLabelLinks() {
		return this.labelLinks;
	}

	public List<Link> getTabulatureLinks() {
		return this.tabulatureLinks;
	}

	public final List<Review> getReviews() {
		final List<Review> reviewList = new ArrayList<Review>();
		for (final Disc disc : this.discs) {
			reviewList.addAll(disc.getReviews());
		}
		return reviewList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + new Long(this.id).hashCode();
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Band other = (Band) obj;
		if (this.id != other.id) {
			return false;
		}
		return this.name.equals(other.name);
	}

	public void setSimliarArtists(final Map<Integer, List<Band>> similarArtists) {
		this.similarArtist = similarArtists;
	}

	public Map<Integer, List<Band>> getSimilarArtists() {
		return this.similarArtist;
	}

	public List<Link> getLinks() {
		List<Link> linkList = new ArrayList<Link>();
		linkList.addAll(this.officalLinks);
		linkList.addAll(this.officalMerchLinks);
		linkList.addAll(this.unofficalLinks);
		linkList.addAll(this.labelLinks);
		linkList.addAll(this.tabulatureLinks);
		return linkList;
	}

	public final boolean hasPhoto() {
		return this.photo != null;
	}

	public final boolean hasLogo() {
		return this.logo != null;
	}

	public final void setDiscs(final List<Disc> discList) {
		this.discs = discList;
	}

	public void setOfficalLinks(final List<Link> officalLinks) {
		this.officalLinks = officalLinks;
	}

	public void setOfficalMerchLinks(final List<Link> officalMerchLinkList) {
		this.officalMerchLinks = officalMerchLinkList;
	}

	public void setUnofficalLinks(final List<Link> list) {
		this.unofficalLinks = list;
	}

	public void setLabelLinks(final List<Link> list) {
		this.labelLinks = list;
	}

	public void setTabulatureLinks(final List<Link> list) {
		this.tabulatureLinks = list;
	}

	public void setLogoUrl(final String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public void setPhotoUrl(final String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getLogoUrl() {
		return this.logoUrl;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

}
