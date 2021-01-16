package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.query.entity.LabelQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchLabelResult;
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
        for (String labelName : stringList) {
            // System.out.println("searching with: " + labelName);
            List<SearchLabelResult> labels = new LabelSearchService().get(new LabelQuery(labelName));
            for (SearchLabelResult label : labels) {
//                label.getCountry()
//                ...
            }
            assertThat(labels).isNotEmpty();
        }

    }

}
