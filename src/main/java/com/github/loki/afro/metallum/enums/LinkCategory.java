package com.github.loki.afro.metallum.enums;

public enum LinkCategory {
    OFFICIAL("Official"), OFFICIAL_MERCH("Official merchandise"), UNOFFICIAL("Unofficial"), TABLATURES("Tablatures"), LABEL("Labels"), ANY("");

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
        System.err.println("possible LinkCategory " + possibleCategory);
        return ANY;
    }

    public String asCategoryName() {
        return this.linkCategoryAsName;
    }
}
