package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.search.query.LabelSearchQuery;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class LabelSearchTest {
    // TODO to test: -objectToLoad

    @Test
    public void noLabelTest() {
        final LabelSearchService searchService = new LabelSearchService();
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("");
        try {
            searchService.performSearch(query);
            Assert.fail();
        } catch (MetallumException e) {
            Assert.assertFalse(e.getMessage().isEmpty());
        }
    }

    @Test
    public void labelNameTest() throws MetallumException {
        final LabelSearchService searchService = new LabelSearchService();
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("Nuclear");
        final List<Label> result = searchService.performSearch(query);
        Assert.assertFalse(result.isEmpty());
        for (final Label label : result) {
            Assert.assertNotSame(0L, label.getId());
            Assert.assertFalse(label.getName().isEmpty());
            Assert.assertFalse(label.getSpecialisation().isEmpty());
            Assert.assertNotNull(label.getCountry());
        }
    }

    /**
     * This test exists because there is no date and no user who added this Label
     * http://www.metal-archives.com/labels/Spinefarm_Records/14
     *
     * @throws MetallumException
     */
    @Test
    public void addedByTest() throws MetallumException {
        final LabelSearchService searchService = new LabelSearchService();
        searchService.setObjectsToLoad(1);
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("Spinefarm Records");
        final Label label = searchService.performSearch(query).get(0);
        Assert.assertNotSame(0L, label.getId());
        Assert.assertFalse(label.getName().isEmpty());
        Assert.assertFalse(label.getSpecialisation().isEmpty());
        Assert.assertNotNull(label.getCountry());
        Assert.assertFalse(label.getAddedBy().isEmpty());
        Assert.assertFalse(label.getAddedOn().isEmpty());
        Assert.assertFalse(label.getModifiedBy().isEmpty());
        Assert.assertFalse(label.getLastModifiedOn().isEmpty());
    }

    @Test
    public void labelLogoTest() throws MetallumException {
        final LabelSearchService searchService = new LabelSearchService(1, true);
        final LabelSearchQuery query = new LabelSearchQuery();
        query.setLabelName("Deathlike Silence");
        final Label result = searchService.performSearch(query).get(0);
        Assert.assertNotSame(0L, result.getId());
        Assert.assertFalse(result.getName().isEmpty());
        Assert.assertFalse(result.getSpecialisation().isEmpty());
        Assert.assertNotNull(result.getCountry());
        Assert.assertFalse(result.getAddedBy().isEmpty());
        Assert.assertFalse(result.getAddedOn().isEmpty());
        Assert.assertFalse(result.getModifiedBy().isEmpty());
        Assert.assertFalse(result.getLastModifiedOn().isEmpty());
        Assert.assertNotNull(result.getLogo());
        Assert.assertFalse(result.getLogoUrl().isEmpty());

    }
}
