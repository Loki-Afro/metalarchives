package com.github.loki.afro.metallum.search.query.entity;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public interface IQuery {

    boolean isValid();

    String assembleQueryUrl(int page);

    static String asPair(String parameterName, Optional<String> property) {
        return parameterName + "=" + MetallumURL.asURLString(property.orElse(""));
    }

    static <X> String getForQuery(String name, Collection<X> collection, Function<X, Object> toString) {
        final StringBuilder buf = new StringBuilder();
        for (final X type : collection) {
            buf.append(name + "[]=" + toString.apply(type) + "&");
        }
        return buf.toString();
    }
}
