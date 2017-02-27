package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.core.parser.search.MemberSearchParser;
import com.github.loki.afro.metallum.core.parser.site.MemberSiteParser;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.search.AbstractSearchService;

import java.util.concurrent.ExecutionException;

/**
 * <pre>
 * This class represents the simple Member search.
 *
 * This service is configuratable to enhance the speed of the parser.
 * You'll also have less requests to the metal-archives.
 *
 * Configuratable prameters:
 *  - if you want to have the related <b>links</b>, if available (default <i>true</i>)
 *  - if you want to have the member <b>details</b>, if available (default <i>true</i>)
 *  - if you want to have the member <b>photo</b>, if available (default <i>false</i>)
 * </pre>
 *
 * @author Zarathustra
 */
public class MemberSearchService extends AbstractSearchService<Member> {
    private boolean loadImages = false;
    private boolean loadLinks = true;
    private boolean loadDetails = true;

    /**
     * Constructs a default MemberSearchService.
     * You'll not download Memberimages.
     * You'll download Memberlinks.
     * You'll download Memberdetails.
     */
    public MemberSearchService() {
        this(0, false, true, true);
    }

    /**
     * @param objectToLoad the id of the member to load if known
     * @param loadImages  true if you care about the photo, false otherwise
     * @param loadLinks   true if you care about the links, false otherwise
     * @param loadDetails true if you care about the details, false otherwise
     */
    public MemberSearchService(final int objectToLoad, final boolean loadImages, final boolean loadLinks, final boolean loadDetails) {
        this.objectToLoad = objectToLoad;
        this.loadLinks = loadLinks;
        this.loadDetails = loadDetails;
        this.loadImages = loadImages;
    }

    @Override
    protected final MemberSiteParser getSiteParser(Member member) throws ExecutionException {
        return new MemberSiteParser(member, this.loadImages, this.loadLinks, this.loadDetails);
    }

    @Override
    protected final MemberSearchParser getSearchParser() {
        return new MemberSearchParser();
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

}
