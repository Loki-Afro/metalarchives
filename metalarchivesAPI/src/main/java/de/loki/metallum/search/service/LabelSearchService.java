package de.loki.metallum.search.service;

import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.parser.search.LabelSearchParser;
import de.loki.metallum.core.parser.site.LabelSiteParser;
import de.loki.metallum.entity.Label;
import de.loki.metallum.search.AbstractSearchService;

public class LabelSearchService extends AbstractSearchService<Label> {

	private boolean						loadImages			= false;
	private LabelSiteParser.PARSE_STYLE	loadCurrentRooster	= LabelSiteParser.PARSE_STYLE.NONE;
	private LabelSiteParser.PARSE_STYLE	loadPastRooster		= LabelSiteParser.PARSE_STYLE.NONE;
	private LabelSiteParser.PARSE_STYLE	loadReleases		= LabelSiteParser.PARSE_STYLE.NONE;
	private boolean						loadLinks			= false;

	public LabelSearchService() {
		this(0, false);
	}

	public LabelSearchService(final int objectToLoad, final boolean loadImages) {
		this.objectToLoad = objectToLoad;
		this.loadImages = loadImages;
	}

	@Override
	protected LabelSearchParser getSearchParser() {
		return new LabelSearchParser();
	}

	@Override
	protected LabelSiteParser getSiteParser(final Label label) throws ExecutionException {
		return new LabelSiteParser(label, this.loadImages, this.loadLinks, this.loadCurrentRooster, this.loadPastRooster, this.loadReleases);
	}

	public void setLoadImages(boolean loadImages) {
		this.loadImages = loadImages;
	}

	public void setLoadCurrentRooster(LabelSiteParser.PARSE_STYLE style) {
		this.loadCurrentRooster = style;
	}

	public void setLoadPstRooster(LabelSiteParser.PARSE_STYLE style) {
		this.loadPastRooster = style;
	}

	public void setLoadReleases(LabelSiteParser.PARSE_STYLE style) {
		this.loadReleases = style;
	}

	public void setLoadLinks(final boolean loadLinks) {
		this.loadLinks = loadLinks;
	}

	@Override
	protected final boolean hasAllInformation(final Label entityFromCache) {
		if (this.loadImages && !entityFromCache.hasLogo()) {
			return false;
		}

		if (this.loadLinks && entityFromCache.getLinks().isEmpty()) {
			return false;
		}

		// TODO PARSE Style accessable from LabelSearchService to use the Cache.
		if (this.loadCurrentRooster != LabelSiteParser.PARSE_STYLE.NONE && entityFromCache.getCurrentRoser().isEmpty()) {
			return false;
		}

		if (this.loadPastRooster != LabelSiteParser.PARSE_STYLE.NONE && entityFromCache.getReleases().isEmpty()) {
			return false;
		}

		if (this.loadReleases != LabelSiteParser.PARSE_STYLE.NONE && entityFromCache.getPastRoster().isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public void setObjectsToLoad(final int objectToLoad) {
		super.setObjectsToLoad(objectToLoad);
	}

}
