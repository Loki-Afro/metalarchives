package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.search.query.LabelSearchQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LabelSearchTest {
    // TODO to test: -objectToLoad

    @Test
    public void noLabelTest() {
        final LabelSearchService searchService = new LabelSearchService();
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("");
        assertThatThrownBy(() -> searchService.performSearch(query)).isInstanceOf(MetallumException.class);
    }

    @Test
    public void labelNameTest() throws MetallumException {
        final LabelSearchService searchService = new LabelSearchService();
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("Nuclear");
        final List<Label> result = searchService.performSearch(query);
        assertThat(result).isNotEmpty();
        for (final Label label : result) {
            assertThat(label.getId()).isNotSameAs(0L);
            assertThat(label.getName().isEmpty()).isFalse();
            assertThat(label.getSpecialisation().isEmpty()).isFalse();
            assertThat(label.getCountry()).isNotNull();
        }
    }

    /**
     * This test exists because there is no date and no user who added this Label
     * http://www.metal-archives.com/labels/Spinefarm_Records/14
     *
     */
    @Test
    public void addedByTest() throws MetallumException {
        final LabelSearchService searchService = new LabelSearchService();
        searchService.setObjectsToLoad(1);
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("Spinefarm Records");
        final Label label = searchService.performSearch(query).get(0);
        assertThat(label.getId()).isNotSameAs(0L);
        assertThat(label.getName().isEmpty()).isFalse();
        assertThat(label.getSpecialisation().isEmpty()).isFalse();
        assertThat(label.getCountry()).isNotNull();
        assertThat(label.getAddedBy().isEmpty()).isFalse();
        assertThat(label.getAddedOn().isEmpty()).isFalse();
        assertThat(label.getModifiedBy().isEmpty()).isFalse();
        assertThat(label.getLastModifiedOn().isEmpty()).isFalse();
    }

    @Test
    public void labelLogoTest() throws MetallumException {
        final LabelSearchService searchService = new LabelSearchService(1, true);
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("Deathlike Silence");
        final Label result = searchService.performSearch(query).get(0);
        assertThat(result.getId()).isNotSameAs(0L);
        assertThat(result.getName().isEmpty()).isFalse();
        assertThat(result.getSpecialisation().isEmpty()).isFalse();
        assertThat(result.getCountry()).isNotNull();
        assertThat(result.getAddedBy().isEmpty()).isFalse();
        assertThat(result.getAddedOn().isEmpty()).isFalse();
        assertThat(result.getModifiedBy().isEmpty()).isFalse();
        assertThat(result.getLastModifiedOn().isEmpty()).isFalse();
        assertThat(result.getLogo()).isNotNull();
        assertThat(result.getLogoUrl().isEmpty()).isFalse();

    }
}
