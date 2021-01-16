package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.google.api.client.util.Strings;
import lombok.Getter;
import lombok.Setter;

public class MemberQuery implements IQuery {

    @Getter
    @Setter
    private  String name;

    public MemberQuery(String name) {
        this.name = name;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(getName());
    }

    @Override
    public String assembleQueryUrl(int page) {
        return MetallumURL.assembleMemberSearchURL(getName(), page);
    }
}
