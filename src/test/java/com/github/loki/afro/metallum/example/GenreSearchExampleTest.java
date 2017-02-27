package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.query.GenreSearchQuery;
import com.github.loki.afro.metallum.search.service.GenreSearchService;
import org.junit.Assert;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.List;

public class GenreSearchExampleTest {

    @Ignore
    public void test() throws MetallumException {
        List<String> stringList = new ArrayList<>();
        stringList.add("Death Metal");
        stringList.add("Death Metal");
        // stringList.add("Black Metal");
        for (String genreName : stringList) {
            GenreSearchQuery query = new GenreSearchQuery();
            GenreSearchService service = new GenreSearchService();
            service.setObjectsToLoad(5);
            query.setGenre(genreName);
            service.performSearch(query);
            Assert.assertFalse(service.getResultAsList().isEmpty());
        }

    }
}
