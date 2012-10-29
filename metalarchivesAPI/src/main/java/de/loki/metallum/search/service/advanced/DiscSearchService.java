package de.loki.metallum.search.service.advanced;

import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.parser.search.DiscSearchParser;
import de.loki.metallum.core.parser.site.DiscSiteParser;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.entity.Track;
import de.loki.metallum.search.AbstractSearchService;

/**
 * Represents the advanced search for bands Here we'll search and try to parse the Result of our
 * Search.
 * 
 * How to use this: Setup a DiscSearchQuery and just call performSearch(DiscSearchQuery query)
 * 
 * This will also call the Parser to finally get a clear result. <br>
 * <br>
 * <b>What this class does not do:</b> It does not fill the resultList with the whole data what you
 * can get from the metal-archives<br>
 * <br>
 * 
 * <b>Actually it does just search!</b>
 * 
 * 
 * @see http://www.metal-archives.com/search/advanced/#albums
 * 
 * 
 * @author Zarathustra
 * 
 */
public class DiscSearchService extends AbstractSearchService<Disc> {

	private boolean	loadImages	= false;
	private boolean	loadReviews	= false;
	private boolean	loadLyrics	= false;

	/**
	 * Constructs a default DiscSearchService.
	 * - object to load = 5
	 * - load review = false
	 * - load image = false
	 */
	public DiscSearchService() {
		this(5, false, false, false);
	}

	/**
	 * Constructs a DiscSearchService, where<br>
	 * - object to load = 5<br>
	 * and load Reviews as false
	 * 
	 * @param loadImages see {@code setLoadImages}
	 */
	public DiscSearchService(final boolean loadImages) {
		this(5, loadImages, false, false);
	}

	/**
	 * Constructs a DiscSearchService.<br>
	 * <br>
	 * 
	 * @param objectToLoad see {@code setObjectsToLoad}
	 * @param loadImages see {@code setLoadImages}
	 * @param loadReviews see {@code setLoadReviews}
	 */
	public DiscSearchService(final int objectToLoad, final boolean loadImages, final boolean loadReviews, final boolean loadLyrics) {
		this.objectToLoad = objectToLoad;
		this.loadImages = loadImages;
		this.loadReviews = loadReviews;
		this.loadLyrics = loadLyrics;
	}

	@Override
	protected DiscSiteParser getSiteParser(final Disc disc) throws ExecutionException {
		return new DiscSiteParser(disc, this.loadImages, this.loadReviews, this.loadLyrics);
	}

	@Override
	protected DiscSearchParser getSearchParser() {
		return new DiscSearchParser();
	}

	public void setLoadImages(boolean loadImages) {
		this.loadImages = loadImages;
	}

	public void setLoadReviews(boolean loadReviews) {
		this.loadReviews = loadReviews;
	}

	public void setloadLyrics(final boolean loadLyrics) {
		this.loadLyrics = loadLyrics;
	}

	@Override
	public void setObjectsToLoad(final int objectToLoad) {
		super.setObjectsToLoad(objectToLoad);
	}

	@Override
	protected final boolean hasAllInformation(final Disc entityFromCache) {
		if (this.loadImages && !entityFromCache.hasArtwork()) {
			return false;
		}

		if (this.loadReviews && entityFromCache.getReviews().isEmpty()) {
			return false;
		}

		if (this.loadLyrics) {
			for (final Track track : entityFromCache.getTrackList()) {
				if (!track.getLyrics().isEmpty()) {
					return true;
				}
			}
			return false;
		}

		return true;
	}

}
