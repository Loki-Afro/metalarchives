package com.github.loki.afro.metallum.enums;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class CountryTest {

    @Test
    public void testCountries() throws IOException {
        Document doc = Jsoup.parse(new URL(MetallumURL.BASEURL + "search/advanced/searching/bands"), (int) TimeUnit.SECONDS.toMillis(20));
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
                assertThat(result).isNotEqualTo(Country.ANY);
            }
        }
    }

}