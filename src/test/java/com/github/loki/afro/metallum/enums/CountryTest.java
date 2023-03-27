package com.github.loki.afro.metallum.enums;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class CountryTest {

    @Test
    public void testCountries() {
        // this way we get the same error handing
        String html = Downloader.getHTML(MetallumURL.BASEURL + "search/advanced/searching/bands");
        Document doc = Jsoup.parse(html);
        Elements options = doc.getElementById("country").getElementsByTag("option");
        if (options.isEmpty()) {
            fail("no countries found at all");
        }
        for (Element element : options) {
            String isoCode = element.attr("value");
            String countryName = element.text();
            if (!countryName.contains("Any")) {
                Country result = Country.ofMetallumDisplayName(countryName);
                assertThat(result.getShortForm()).isEqualTo(isoCode);
                assertThat(result).isNotNull();
            }
        }
    }

}