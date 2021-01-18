package com.github.loki.afro.metallum.search;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.search.query.entity.*;
import com.github.loki.afro.metallum.search.service.LabelSearchService;
import com.github.loki.afro.metallum.search.service.MemberSearchService;
import com.github.loki.afro.metallum.search.service.advanced.BandSearchService;
import com.github.loki.afro.metallum.search.service.advanced.DiscSearchService;
import com.google.common.collect.Iterables;

public abstract class API {
    private API() {
//        util
    }

    public static Iterable<SearchBandResult> getBands(BandQuery bandQuery) {
        return new BandSearchService().get(bandQuery);
    }

    public static Iterable<Band> getBandsFully(BandQuery bandQuery) {
        return new BandSearchService().getFully(bandQuery);
    }

    public static Band getSingleUniqueBand(BandQuery bandQuery) throws MetallumException {
        return new BandSearchService().getSingleUniqueByQuery(bandQuery);
    }

    public static Band getBandById(long id) throws MetallumException {
        return new BandSearchService().getById(id);
    }

    public static Iterable<SearchDiscResult> getDiscs(DiscQuery query) {
        return new DiscSearchService().get(query);
    }

    public static Iterable<Disc> getDiscsFully(DiscQuery query) {
        return new DiscSearchService().getFully(query);
    }

    public static Disc getSingleUniqueDisc(DiscQuery discQuery) throws MetallumException {
        return Iterables.getOnlyElement(getDiscsFully(discQuery));
    }

    public static Disc getDiscById(long id) throws MetallumException {
        return new DiscSearchService().getById(id);
    }

    public static Iterable<SearchMemberResult> getMemberByName(String name) {
        return new MemberSearchService().get(new MemberQuery(name));
    }

    public static Member getMemberById(long id) {
        return new MemberSearchService().getById(id);
    }

    public static Label getLabelById(long id) {
        return new LabelSearchService().getById(id);
    }

}
