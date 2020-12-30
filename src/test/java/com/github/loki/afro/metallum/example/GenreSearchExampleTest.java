package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.query.GenreSearchQuery;
import com.github.loki.afro.metallum.search.service.GenreSearchService;
import org.junit.jupiter.api.Disabled;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GenreSearchExampleTest {

    @Disabled
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
            assertThat(service.getResultAsList().isEmpty()).isFalse();
        }

    }
}
