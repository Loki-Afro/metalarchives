package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.query.LyricalThemesSearchQuery;
import com.github.loki.afro.metallum.search.service.LyricalThemesSearchService;
import org.junit.Assert;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.List;

public class LyricalThemesExampleTest {

    @Ignore
    public void test() throws MetallumException {

        List<String> stringList = new ArrayList<>();
        stringList.add("Love");
        stringList.add("Sex");
        for (String lyricalThemes : stringList) {
            LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
            LyricalThemesSearchService service = new LyricalThemesSearchService();
            query.setLyricalThemes(lyricalThemes);
            service.performSearch(query);
            Assert.assertFalse(service.getResultAsList().isEmpty());
        }

    }

}
