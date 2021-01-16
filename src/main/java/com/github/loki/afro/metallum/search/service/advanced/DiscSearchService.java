package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.core.parser.search.DiscSearchParser;
import com.github.loki.afro.metallum.core.parser.site.DiscSiteParser;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.SearchRelevance;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.SortedMap;

import static com.github.loki.afro.metallum.core.util.MetallumUtil.isNotBlank;

public class DiscSearchService extends AbstractSearchService<Disc, DiscQuery, SearchDiscResult> {

    private boolean loadImages;
    private boolean loadReviews;
    private boolean loadLyrics;

    /**
     * Constructs a default DiscSearchService.
     * - load review = false
     * - load image = false
     */
    public DiscSearchService() {
        this(false, false, false);
    }

    /**
     * Constructs a DiscSearchService, where<br>
     * load Reviews is false
     *
     * @param loadImages see {@code setLoadImages}
     */
    public DiscSearchService(final boolean loadImages) {
        this(loadImages, false, false);
    }

    /**
     * Constructs a DiscSearchService.<br>
     * <br>
     *
     * @param loadImages   see {@code setLoadImages}
     * @param loadReviews  see {@code setLoadReviews}
     * @param loadLyrics see {@code setLoadLyrics}
     */
    public DiscSearchService(final boolean loadImages, final boolean loadReviews, final boolean loadLyrics) {
        this.loadImages = loadImages;
        this.loadReviews = loadReviews;
        this.loadLyrics = loadLyrics;
    }

    public void setLoadImages(boolean loadImages) {
        this.loadImages = loadImages;
    }

    public void setLoadReviews(boolean loadReviews) {
        this.loadReviews = loadReviews;
    }

    public void setLoadLyrics(final boolean loadLyrics) {
        this.loadLyrics = loadLyrics;
    }

    @Override
    protected DiscSiteParser getSiteParser(final long entityId) {
        return new DiscSiteParser(entityId, this.loadImages, this.loadReviews, this.loadLyrics);
    }

    @Override
    protected DiscSearchParser getSearchParser(DiscQuery discQuery) {
        DiscSearchParser discSearchParser = new DiscSearchParser();
        discSearchParser.setIsAbleToParseDate(discQuery.isAbleToParseDate());

        discSearchParser.setIsAbleToParseDiscType(discQuery.isAbleToParseDiscType());
        discSearchParser.setIsAbleToParseGenre(isNotBlank(discQuery.getGenre()));
        discSearchParser.setIsAbleToParseLabel(isNotBlank(discQuery.getLabelName()));
        discSearchParser.setIsAbleToParseCountry(discQuery.isAbleToParseCountry());
        discSearchParser.setIsAbleToParseProvince(isNotBlank(discQuery.getBandProvince()));
        return discSearchParser;
    }

    @Override
    protected SortedMap<SearchRelevance, List<SearchDiscResult>> enrichParsedEntity(DiscQuery query, final SortedMap<SearchRelevance, List<SearchDiscResult>> resultMap) {
        if (query.getDiscTypes().size() == 1) {
            final DiscType discType = Iterables.getOnlyElement(query.getDiscTypes());
            for (final List<SearchDiscResult> discList : resultMap.values()) {
                for (final SearchDiscResult disc : discList) {
                    // if there is a discType we are overwriting it!
                    disc.setDiscType(discType);
                }
            }
        }
        if (query.getCountries().size() == 1) {
            final Country country = Iterables.getOnlyElement(query.getCountries());
            for (final List<SearchDiscResult> discList : resultMap.values()) {
                for (final SearchDiscResult disc : discList) {
                    disc.setBandCountry(country);
                }
            }
        }
        return resultMap;
    }

}
