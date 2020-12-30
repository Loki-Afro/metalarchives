package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.LinkCategory;
import com.github.loki.afro.metallum.search.query.MemberSearchQuery;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberSearchTest {
    // TODO to test: -objectToLoad

    @Test
    public void noMemberNameTest() {
        final MemberSearchService searchService = new MemberSearchService();
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("");
        assertThatThrownBy(() -> searchService.performSearch(query)).isInstanceOf(MetallumException.class);
    }

    @Test
    public void searchNameTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService();
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("Fenriz");
        final List<Member> result = searchService.performSearch(query);
        assertThat(result.isEmpty()).isFalse();
        defaultSearchMemberCheck(result);
    }

    @Test
    public void photoTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(1, true, false, false);
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("Eicca Toppinen");
        final List<Member> result = searchService.performSearch(query);
        assertThat(result.isEmpty()).isFalse();
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        assertThat(member.getPhotoUrl().isEmpty()).isFalse();
        assertThat(member.getPhoto()).isNotNull();
    }

    @Test
    public void detailsTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(1, false, false, true);
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("George \"Corpsegrinder\" Fisher");
        final List<Member> result = searchService.performSearch(query);
        assertThat(result.isEmpty()).isFalse();
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        assertThat(member.getPhotoUrl().isEmpty()).isFalse();
        assertThat(member.getDetails().startsWith("George")).isTrue();
        assertThat(member.getDetails().endsWith("Forgotten.")).isTrue();
    }

    @Test
    public void linksTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(1, false, true, false);
        final MemberSearchQuery query = new MemberSearchQuery();
        query.setMemberName("Tom G. Warrior");
        final List<Member> result = searchService.performSearch(query);
        assertThat(result.isEmpty()).isFalse();
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        assertThat(member.getPhotoUrl().isEmpty()).isFalse();
        defaulLinkTest(member.getLinks());
    }

    private void defaulLinkTest(final Collection<Link> links) {
        for (final Link link : links) {
            assertThat(link.getName().isEmpty()).isFalse();
            assertThat(link.getURL().isEmpty()).isFalse();
            assertThat(LinkCategory.ANY).isNotSameAs(link.getCategory());
        }
    }

    private void defaultSearchMemberCheck(final Member member) {
        assertThat(member.getId()).isNotSameAs(0L);
        assertThat(member.getName().isEmpty()).isFalse();
        assertThat(member.getRealName()).isNotNull();
        assertThat(member.getCountry()).isNotSameAs(Country.ANY);
    }

    private void defaultSearchMemberCheck(final Collection<Member> membersToTest) {
        for (final Member member : membersToTest) {
            defaultSearchMemberCheck(member);
        }
    }
}
