package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.entity.LabelQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchLabelResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class LabelSearchServiceTest {

    @Test
    public void noLabelTest() {
        final LabelSearchService searchService = new LabelSearchService();
        assertThatThrownBy(() -> searchService.getFully(new LabelQuery(""))).isInstanceOf(MetallumException.class);
    }

    @Test
    public void labelNameTest() throws MetallumException {
        final List<SearchLabelResult> result = new LabelSearchService().get(new LabelQuery("Nuclear"));

        assertThat(result).isNotEmpty();
        for (final SearchLabelResult label : result) {
            assertThat(label.getId()).isNotSameAs(0L);
            assertThat(label.getName()).isNotEmpty();
            assertThat(label.getSpecialisation()).isNotEmpty();
        }

        assertThat(result)
                .extracting(SearchLabelResult::getName, SearchLabelResult::getId, SearchLabelResult::getSpecialisation, SearchLabelResult::getCountry)
                .contains(tuple("Atomik Nuclear Desolation Productions (a.k.a. Atomic Nuclear Desolation Productions)", 663L, "Black/Death Metal", Country.CL));
    }

    /**
     * This test exists because there is no date and no user who added this Label
     * http://www.metal-archives.com/labels/Spinefarm_Records/14
     *
     */
    @Test
    public void addedByTest() throws MetallumException {
        final Label label = new LabelSearchService().getById(14L);

        assertThat(label.getLogo()).isNull();
        assertDefaultLabel(label);
    }

    @Test
    public void labelLogoTest() throws MetallumException {
        final Label result = new LabelSearchService(true).getById(1878L);

        assertThat(result.getLogo()).isNotNull();
        assertDefaultLabel(result);
    }

    private void assertDefaultLabel(Label result) {
        assertThat(result.getId()).isNotSameAs(0L);
        assertThat(result.getName().isEmpty()).isFalse();
        assertThat(result.getSpecialisation()).isNotEmpty();
        assertThat(result.getLogoUrl()).isNotNull();
        assertThat(result.getCountry()).isNotNull();
        assertThat(result.getAddedBy()).isNotEmpty();
        assertThat(result.getAddedOn()).isNotEmpty();
        assertThat(result.getModifiedBy()).isNotEmpty();
        assertThat(result.getLastModifiedOn()).isNotEmpty();
    }
}
