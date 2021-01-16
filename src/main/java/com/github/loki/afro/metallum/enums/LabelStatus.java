package com.github.loki.afro.metallum.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LabelStatus {
    ACTIVE("active"), UNKNOWN("unknown"), CLOSED("closed"), CHANGED_NAME("changed name");
    private final String asString;

    private static final Logger logger = LoggerFactory.getLogger(LabelStatus.class);

    LabelStatus(final String asString) {
        this.asString = asString;
    }

    public static LabelStatus getLabelStatusForString(String possibleStatus) {
        possibleStatus = possibleStatus.trim();
        for (LabelStatus type : values()) {
            if (type.asString.equalsIgnoreCase(possibleStatus)) {
                return type;
            }
        }
        logger.error("Unrecognized LabelStatus: " + possibleStatus);
        return null;
    }

    public String asString() {
        return this.asString;
    }
}
