package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.query.LabelSearchQuery;
import com.github.loki.afro.metallum.search.service.LabelSearchService;
import org.junit.jupiter.api.Disabled;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LabelSearchExampleTest {

    @Disabled
    public void test() throws MetallumException {
        List<String> stringList = new ArrayList<>();
        stringList.add("Deathlike Silence");
        stringList.add("Apparitia Recordings");
        stringList.add("Metal Blade Records");
        stringList.add("Nuclear");
        stringList.add("Century Media Records");
        stringList.add("");
        for (String labelName : stringList) {
            // System.out.println("searching with: " + labelName);
            LabelSearchQuery query = new LabelSearchQuery();
            LabelSearchService service = new LabelSearchService();
            query.setLabelName(labelName);
            service.performSearch(query);
            assertThat(service.getResultAsList()).isNotEmpty();
        }

    }

}
