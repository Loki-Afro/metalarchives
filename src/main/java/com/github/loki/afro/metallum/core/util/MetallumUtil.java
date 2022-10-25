package com.github.loki.afro.metallum.core.util;

import com.google.api.client.util.Strings;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public final class MetallumUtil {
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");
    private final static String[] daySuffixes =
            // 0 1 2 3 4 5 6 7 8 9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    // 10 11 12 13 14 15 16 17 18 19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    // 20 21 22 23 24 25 26 27 28 29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    // 30 31
                    "th", "st"};
    private static final Logger LOGGER = LoggerFactory.getLogger(MetallumUtil.class);

    private MetallumUtil() {

    }

    public static boolean isNotBlank(Optional<String> optional) {
        if (optional.isPresent()) {
            String s = optional.get();
            return !Strings.isNullOrEmpty(s);
        } else {
            return false;
        }
    }

    private final static String getDayOfMonthSuffix(final int day) {
        return daySuffixes[day];
    }

    public final static Date getMetallumDate(final String possibleDate) {
        final String day = possibleDate.replaceAll("\\w+?\\s", "").replaceAll("\\w{2},\\s\\d+", "");
        final int dayLength = day.length();
        SimpleDateFormat sdf;
        if (dayLength > 2) {
            // April 1995
            sdf = new SimpleDateFormat("MMMMM yyyy");
        } else {
            sdf = new SimpleDateFormat("MMMMM " + (day.length() > 1 ? "dd" : "d") + "'" + MetallumUtil.getDayOfMonthSuffix(Integer.parseInt(day)) + ",' " + "yyyy", Locale.ENGLISH);
        }
        try {
            return sdf.parse(possibleDate);
        } catch (ParseException e) {
            LOGGER.error("Unable to parse date " + possibleDate, e);
        }
        return null;
    }

    /**
     * This method exists because {@link String#trim()} )} does not remove all whitespaces.
     * So called No Break-Spaces ({@code &nbsp;})!<br>
     * Removes all leading and trailing \\u00A0.<br>
     *
     * @param stringWithWhiteSpaces with \\u00A0 Characters
     * @return the clean String
     * @see <a href="http://www.fileformat.info/info/unicode/char/202f/index.htm"></a>
     * @see <a href="http://www.vineetmanohar.com/2009/06/how-to-trim-no-break-space-when-parsing-html"></a>
     */
    public static String trimNoBreakSpaces(final String stringWithWhiteSpaces) {
        // (\\.|!|\\?)+(\\s|\\z)
        // final String[] words = stringWithWhiteSpaces.split("\\S");
        // for (int i = 0; i < words.length; i++) {
        // if (!words[i].isEmpty()) {
        //
        // String word = words[i];
        // word = word.replaceAll("[\\s\\u00A0]+$|^[\\s\\u00A0]+", "");
        // System.out.println(word);
        // }
        // }
        return stringWithWhiteSpaces.replaceAll("[\\s\\u00A0]+$|^[\\s\\u00A0]+", "");
    }

    /**
     * @param html the HTML String with tags
     * @return a HTML clean String (parsed), but with line separators
     */
    public static String htmlToPlainText(final String html) {
        final StringBuilder strBuf = new StringBuilder();
        String cleanHtml = Jsoup.parse(html.replaceAll("(?i)<br[^>]*>", "br2n")).text();
        for (final String strPart : cleanHtml.split("br2n")) {
            strBuf.append(strPart.trim());
            strBuf.append(LINE_SEPARATOR);
        }
        return strBuf.toString().trim();
    }

    public static boolean isStringInArray(final String control, final String... test) {
        for (String aTest : test) {
            if (aTest.equalsIgnoreCase(control)) {
                return true;
            }
        }
        return false;
    }

    // :(( not possible because we cannot make a generic Array see
    // http://stackoverflow.com/questions/2927391/whats-the-reason-i-cant-create-generic-array-types-in-java
    // private static <E> E[] asArray(List<E> list) {
    // E[] array = new E[list.size()];
    // return list.toArray(array);
    // }

}
