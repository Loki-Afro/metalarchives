package com.github.loki.afro.metallum.core.util.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Zarathustra
 */
public final class MetallumURL {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetallumURL.class);

    /**
     * Default constructor.
     */
    private MetallumURL() {

    }

    /**
     * The metal-archives Base-URL.
     */
    public static final String BASEURL = "https://www.metal-archives.com/";

    public enum SearchTyp {
        BAND("bands"), TRACK("songs"), DISC("albums");

        private String searchString;

        SearchTyp(String searchString) {
            this.searchString = searchString;
        }

        public String asSearchString() {
            return this.searchString;
        }
    }

    /**
     * This method gives you an URL String for example: I love you -&gt;
     * I+love+you.
     *
     * @param notURLString The URLString to convert
     * @return the URL String
     */
    public static String asURLString(final String notURLString) {
        final String tempPlus = "IchBinEinPlusZeichen";
        if (notURLString == null || notURLString.isEmpty()) {
            return "&";
        }
        StringBuilder buf = new StringBuilder();
        for (String namePart : notURLString.split("\\s")) {
            buf.append(namePart);
            buf.append(tempPlus);
        }
        // To remove the last "+"
        buf.replace(buf.length() - tempPlus.length(), buf.length(), "");
        return encodeURIComponent(buf.toString()).replaceAll(tempPlus, "+") + "&";
    }

    /**
     * To get the searchURL for the advanced search Services (bands, albums, tracks).
     *
     * @param searchTyp band, album or track
     * @param query     the specific queryString which contains the parameters for the HTTP Get
     * @param startPage startPage
     * @return the assembledURL
     */
    private static String assembleSearchURL(final SearchTyp searchTyp, final String query, final int startPage) {
        return BASEURL + "search/ajax-advanced/searching/" + searchTyp.asSearchString() + "/?" + query + "sEcho=1&iColumns=3&sColumns=&iDisplayStart=" + startPage
                + "&iDisplayLength=200&sNames=%2C%2C";
    }

    public static String assembleBandSearchURL(final String query, final int startPage) {
        return assembleSearchURL(SearchTyp.BAND, query, startPage);
    }

    public static String assembleDiscSearchURL(final String query, final int startPage) {
        return assembleSearchURL(SearchTyp.DISC, query, startPage);
    }

    public static String assembleTrackSearchURL(final String query, final int startPage) {
        return assembleSearchURL(SearchTyp.TRACK, query, startPage);
    }

    public static String assembleReviewFromBandURL(final long bandId) {
        return BASEURL
                + "review/ajax-list-band/id/"
                + bandId
                + "/json/1?_=1328050442591&sEcho=1&iColumns=4&sColumns=&iDisplayStart=0&iDisplayLength=200&sNames=%2C%2C%2C&iSortingCols=1&iSortCol_0=3&sSortDir_0=desc&bSortable_0=true&bSortable_1=true&bSortable_2=true&bSortable_3=true";
    }

    public static String assembleLyricsURL(final long lyricsId) {
        return BASEURL + "release/ajax-view-lyrics/id/" + lyricsId;
    }

    public static String assembleDiscURL(final long discId) {
        return BASEURL + "albums/_/_/" + discId;
    }

    public static String assembleBandURL(final long bandId) {
        return BASEURL + "bands/*/" + bandId;
    }

    public static String assembleBandRecommendationsURL(final long id, final int site) {
        return BASEURL + "band/ajax-recommendations/id/" + id + "/showMoreSimilar/" + site;
    }

    public static String assembleMoreInfoURL(final long id) {
        return BASEURL + "band/read-more/id/" + id;
    }

    public static String assembleBandLinkURL(final long bandId) {
        return BASEURL + "link/ajax-list/type/band/id/" + bandId;
    }

    public static String assembleMemberURL(final long memberId) {
        // Does not always work?! http://www.metal-archives.com/artists/*/27
        return BASEURL + "artists/*/" + memberId;
    }

    public static String assembleMemberReadMoreURL(final long memberId) {
        return BASEURL + "artist/read-more/id/" + memberId;
    }

    public static String assembleGenreSearchURL(final String genreName, final int begin) {
        return BASEURL + "search/ajax-band-search/?field=genre&query=" + encodeURIComponent(genreName) + "&sEcho=1&iColumns=3&sColumns=&iDisplayStart=" + begin + "&iDisplayLength=200&sNames=%2C%2C";
    }

    public static String assembleLyricalThemesSearchURL(final String themesName, final int begin) {
        return BASEURL + "search/ajax-band-search/?field=themes&query=" + encodeURIComponent(themesName) + "&sEcho=1&iColumns=4&sColumns=&iDisplayStart=" + begin + "&iDisplayLength=200&sNames=%2C%2C";
    }

    /**
     * to get an URL to view the discography of a Band example for Bathory:
     * http://www.metal-archives.com/band/discography/id/184/tab/all
     * @param id the DiscographyId
     * @return the URL
     */
    public static String assembleDiscographyURL(long id) {
        return BASEURL + "band/discography/id/" + id + "/tab/all";
    }

    public static String assembleLabelSearchURL(String labelName, int begin) {
        return BASEURL + "search/ajax-label-search/?field=name&query=" + encodeURIComponent(labelName) + "&sEcho=1&iColumns=3&sColumns=&iDisplayStart=" + begin + "&iDisplayLength=200&sNames=%2C%2C";
    }

    /**
     * To assemble a URL to search for that Member/artist whatever
     *
     * @param name of the member/artist whatever
     * @param startPage the startPage
     * @return the searchURL for the member/artist whatever
     */
    public static String assembleMemberSearchURL(String name, int startPage) {
        return BASEURL + "search/ajax-artist-search/?field=alias&query=" + encodeURIComponent(name) + "&sEcho=1&iColumns=4&sColumns=&iDisplayStart=" + startPage
                + "&iDisplayLength=200&sNames=%2C%2C%2C";
    }

    /**
     * To assemble a URL to parse the Reviews of a Disc.
     *
     * @param discId the id from the disc to get the reviews
     * @return will return a URL like this: http://www.metal-archives.com/reviews/_/_/207
     */
    public static String assembleReviewsURL(final long discId) {
        return BASEURL + "reviews/_/_/" + discId;
    }

    /**
     * Assembles a URL where you can get JSon from. It will contain the
     * <b>current</b> bands from the label.
     *
     * @param labelId       the labelId
     * @param numberPerPage how many results do you want.
     * @param alphabetical  true if you want the result alphabetical
     *                      otherwise you'll get it in reverse order.
     * @param sortType      you can sort per Band, Genre or per Country.
     * @return the create URL
     */
    public static String assembleLabelCurrentRoster(final long labelId, final byte numberPerPage, final boolean alphabetical, final int sortType) {
        return BASEURL + "label/ajax-bands/nbrPerPage/" + numberPerPage + "/id/" + labelId
                + "?_=1328866845907&sEcho=1&iColumns=3&sColumns=&iDisplayStart=0&iDisplayLength=100&sNames=%2C%2C&iSortingCols=1&iSortCol_0=" + sortType + "&sSortDir_0="
                + (alphabetical ? "asc" : "desc") + "&bSortable_0=true&bSortable_1=true&bSortable_2=true";
    }

    /**
     * Assembles a URL where you can get JSon from. It will contain the
     * <b>past</b> bands from the label
     * @param labelId       the labelId
     * @param numberPerPage how many results do you want
     * @param alphabetical  true if you want the result alphabetical otherwise you'll get
     *                      it in reverse order
     * @param sortType      you can sort per Band, Genre or per Country
     * @return the create URL
     */
    public static String assembleLabelPastRoster(final long labelId, final byte numberPerPage, final boolean alphabetical, final int sortType) {
        return BASEURL + "label/ajax-bands-past/nbrPerPage/" + numberPerPage + "/id/" + labelId
                + "?_=1328886333320&sEcho=1&iColumns=4&sColumns=&iDisplayStart=0&iDisplayLength=100&sNames=%2C%2C%2C&iSortingCols=1&iSortCol_0=" + sortType + "&sSortDir_0="
                + (alphabetical ? "asc" : "desc") + "&bSortable_0=true&bSortable_1=true&bSortable_2=true&bSortable_3=true";
    }

    public static String assembleLabelReleasesURL(final long labelId, final byte numberPerPage, final boolean alphabetical, final int sortType) {
        return BASEURL + "label/ajax-albums/nbrPerPage/" + numberPerPage + "/id/" + labelId
                + "?_=1328895906635&sEcho=1&iColumns=4&sColumns=&iDisplayStart=0&iDisplayLength=200&sNames=%2C%2C%2C&iSortingCols=1&iSortCol_0=" + sortType + "&sSortDir_0="
                + (alphabetical ? "asc" : "desc") + "&bSortable_0=true&bSortable_1=false&bSortable_2=true&bSortable_3=true";
    }

    public static String assembleMemberLinksURL(final long memberId) {
        return BASEURL + "link/ajax-list/type/person/id/" + memberId;
    }

    public static String encodeURIComponent(final String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This exception should never occur.
            LOGGER.error("This String was not right encoded: " + s, e);
            return s;
        }
    }

    public static String assembleLabelURL(final long labelId) {
        return BASEURL + "labels/_/" + labelId;
    }

    public static String assembleLabelLinkURL(final long labelId) {
        return BASEURL + "link/ajax-list/type/label/id/" + labelId;
    }

}
