package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.core.parser.search.MemberSearchParser;
import com.github.loki.afro.metallum.core.parser.site.MemberSiteParser;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.MemberQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchMemberResult;

import java.util.function.Function;


public class MemberSearchService extends AbstractSearchService<Member, MemberQuery, SearchMemberResult> {
    private boolean loadImages;
    private boolean loadLinks;
    private boolean loadDetails;

    /**
     * Constructs a default MemberSearchService.
     * You'll not download Member-images.
     * You'll download Member-links.
     * You'll download Member-details.
     */
    public MemberSearchService() {
        this(false, true, true);
    }

    /**
     * @param loadImages  true if you care about the photo, false otherwise
     * @param loadLinks   true if you care about the links, false otherwise
     * @param loadDetails true if you care about the details, false otherwise
     */
    public MemberSearchService(final boolean loadImages, final boolean loadLinks, final boolean loadDetails) {
        this.loadLinks = loadLinks;
        this.loadDetails = loadDetails;
        this.loadImages = loadImages;
    }

    /**
     * If you want to have the photo of the Member you have to invoke this method with true. <br>
     * To go more into detail:<br>
     * The parser will download the Image and make it a <br>
     * <br>
     * Default false
     *
     * @param loadImages true if you care about the photo, false otherwise
     */
    public final void setLoadImages(final boolean loadImages) {
        this.loadImages = loadImages;
    }

    /**
     * The links of a Member, like his official Site. <br>
     * <br>
     * An Example is Kerry King <br>
     * <br>
     * Default true
     *
     * @param loadMemberLinks true if you care about the links, false otherwise
     */
    public final void setLoadMemberLinks(final boolean loadMemberLinks) {
        this.loadLinks = loadMemberLinks;
    }

    /**
     * The details are a more detailed description of the Member. <br>
     * <br>
     * Default true.
     *
     * @param loadDetails true if you care about the details, false otherwise
     */
    public final void setLoadDetails(final boolean loadDetails) {
        this.loadDetails = loadDetails;
    }

    @Override
    protected final MemberSearchParser getSearchParser(MemberQuery memberQuery) {
        return new MemberSearchParser();
    }

    @Override
    protected Function<Long, Member> getById() {
        return id -> new MemberSiteParser(id, this.loadImages, this.loadLinks, this.loadDetails).parse();
    }

}
