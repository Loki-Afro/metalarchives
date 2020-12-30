package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.query.LyricalThemesSearchQuery;
import com.github.loki.afro.metallum.search.service.LyricalThemesSearchService;
import org.junit.jupiter.api.Disabled;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LyricalThemesExampleTest {

    @Disabled
    public void test() throws MetallumException {

        List<String> stringList = new ArrayList<>();
        stringList.add("Love");
        stringList.add("Sex");
        for (String lyricalThemes : stringList) {
            LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
            LyricalThemesSearchService service = new LyricalThemesSearchService();
            query.setLyricalThemes(lyricalThemes);
            service.performSearch(query);
            assertThat(service.getResultAsList()).isNotEmpty();
        }

    }

}
