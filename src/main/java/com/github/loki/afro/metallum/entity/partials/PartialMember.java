package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.search.API;

public class PartialMember extends IdentifiablePartial<Member> {
    public PartialMember(long id, String name) {
        super(id, name);
    }

    @Override
    public Member load() {
        return API.getMemberById(getId());
    }
}
