package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.MemberQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchMemberResult;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberSearchServiceTest {

    @Test
    public void test() throws MetallumException {
        List<SearchMemberResult> members = API.getMemberByName("Sami");

        for (SearchMemberResult member : members) {
            assertThat(member.getName()).isNotNull();
            assertThat(member.getId()).isNotEqualTo(0);
//            assertThat(member.getCountry()).isNotNull(); some may actually be null
            assertThat(member.getBands()).isNotNull();
        }
    }

    @Test
    public void byIdTest() throws MetallumException {
        Member member = API.getMemberById(1417L);

        assertThat(member.getRealName()).isEqualTo("Sami Albert Hynninen");
        assertThat(member.getCountry()).isEqualTo(Country.FI);
        assertThat(member.getGender()).isEqualTo("Male");
        assertThat(member.getProvince()).isEqualTo("Lohja, Uusimaa");
        assertThat(member.getAge()).isGreaterThanOrEqualTo(44);
        assertThat(member.getPhoto()).isNull();
        assertThat(member.getPhotoUrl()).isNotEmpty();
        assertThat(member.getAlternativeName()).isNull();
        assertThat(member.getDetails()).isNotEmpty();
        assertThat(member.getLinks()).isNotEmpty();
        assertThat(member.getUncategorizedBands()).isEmpty();
        assertThat(member.getActiveInBands()).isNotEmpty();
        assertThat(member.getPastBands()).isNotEmpty();
        assertThat(member.getMiscBands()).isNotEmpty();
        assertThat(member.getGuestSessionBands()).isNotEmpty();
    }

    @Test
    public void noMemberNameTest() {
        assertThatThrownBy(() -> API.getMemberByName("")).isInstanceOf(MetallumException.class);
    }

    @Test
    public void searchNameTest() throws MetallumException {
        final List<SearchMemberResult> result = API.getMemberByName("Fenriz");
        assertThat(result.isEmpty()).isFalse();
        defaultSearchMemberCheck(result);
    }

    @Test
    public void photoTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(true, false, false);
        final List<Member> result = searchService.getFully(new MemberQuery("Eicca Toppinen"));
        assertThat(result.isEmpty()).isFalse();
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        assertThat(member.getPhotoUrl().isEmpty()).isFalse();
        assertThat(member.getPhoto()).isNotNull();
    }

    @Test
    public void detailsTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(false, false, true);
        final List<Member> result = searchService.getFully(new MemberQuery("George \"Corpsegrinder\" Fisher"));
        assertThat(result.isEmpty()).isFalse();
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        assertThat(member.getPhotoUrl().isEmpty()).isFalse();
        assertThat(member.getDetails().startsWith("George")).isTrue();
        assertThat(member.getDetails().endsWith("Forgotten.")).isTrue();
    }

    @Test
    public void linksTest() throws MetallumException {
        final MemberSearchService searchService = new MemberSearchService(false, true, false);
        final List<Member> result = searchService.getFully(new MemberQuery("Tom G. Warrior"));
        assertThat(result.isEmpty()).isFalse();
        final Member member = result.get(0);
        defaultSearchMemberCheck(member);
        assertThat(member.getPhotoUrl()).isNotEmpty();
        defaulLinkTest(member.getLinks());
        assertThat(member.getRealName()).isNotEmpty();
    }

    private void defaulLinkTest(final Collection<Link> links) {
        for (final Link link : links) {
            assertThat(link.getName()).isNotEmpty();
            assertThat(link.getURL()).isNotEmpty();
            assertThat(link.getCategory()).isNotNull();
        }
    }

    private void defaultSearchMemberCheck(final Member member) {
        defaultSearchMemberCheck(member.getId(), member.getName(), member.getCountry());
    }

    private void defaultSearchMemberCheck(long id, String name, Country country) {
        assertThat(id).isNotEqualTo(0L);
        assertThat(name).isNotEmpty();
        assertThat(country).isNotNull();
    }

    private void defaultSearchMemberCheck(final Collection<SearchMemberResult> membersToTest) {
        for (final SearchMemberResult member : membersToTest) {
            defaultSearchMemberCheck(member.getId(), member.getName(), member.getCountry());
        }
    }
}
