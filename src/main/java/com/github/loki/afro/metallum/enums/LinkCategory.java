package com.github.loki.afro.metallum.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LinkCategory {
    OFFICIAL("Official"),
    OFFICIAL_MERCH("Official merchandise"),
    UNOFFICIAL("Unofficial"),
    TABLATURES("Tablatures"),
    LABEL("Labels"),
    UNLISTED_BANDS("Unlisted bands");

    private static final Logger logger = LoggerFactory.getLogger(LinkCategory.class);

    private final String linkCategoryAsName;

    LinkCategory(String linkCatName) {
        this.linkCategoryAsName = linkCatName;
    }

    public static LinkCategory getLinkCategoryForString(final String possibleCategory) {
        for (LinkCategory type : values()) {
            if (type.linkCategoryAsName.equalsIgnoreCase(possibleCategory)) {
                return type;
            }
        }
        logger.error("possible LinkCategory  " + possibleCategory);
        return null;
    }

    public String asCategoryName() {
        return this.linkCategoryAsName;
    }
}
