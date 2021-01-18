package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.core.parser.search.DiscSearchParser;
import com.github.loki.afro.metallum.core.parser.site.DiscSiteParser;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import com.google.common.collect.Iterables;

import java.util.function.Function;

import static com.github.loki.afro.metallum.core.util.MetallumUtil.isNotBlank;

public class DiscSearchService extends AbstractSearchService<Disc, DiscQuery, SearchDiscResult> {

    private boolean loadImages;
    private boolean loadLyrics;

    /**
     * Constructs a default DiscSearchService.
     * - load review = false
     * - load image = false
     */
    public DiscSearchService() {
        this(false, false);
    }

    /**
     * Constructs a DiscSearchService, where<br>
     * load Reviews is false
     *
     * @param loadImages see {@code setLoadImages}
     */
    public DiscSearchService(final boolean loadImages) {
        this(loadImages, false);
    }

    /**
     * Constructs a DiscSearchService.<br>
     * <br>
     *
     * @param loadImages   see {@code setLoadImages}
     * @param loadLyrics see {@code setLoadLyrics}
     */
    public DiscSearchService(final boolean loadImages, final boolean loadLyrics) {
        this.loadImages = loadImages;
        this.loadLyrics = loadLyrics;
    }

    public void setLoadImages(boolean loadImages) {
        this.loadImages = loadImages;
    }

    public void setLoadLyrics(final boolean loadLyrics) {
        this.loadLyrics = loadLyrics;
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
    protected Function<Long, Disc> getById() {
        return id -> new DiscSiteParser(id, this.loadImages, this.loadLyrics).parse();
    }

    @Override
    protected void enrichParsedEntity(DiscQuery query, final SearchDiscResult result) {
        if (query.getDiscTypes().size() == 1) {
            final DiscType discType = Iterables.getOnlyElement(query.getDiscTypes());
            result.setDiscType(discType);
        }
        if (query.getCountries().size() == 1) {
            final Country country = Iterables.getOnlyElement(query.getCountries());
            result.setBandCountry(country);
        }
    }

}
