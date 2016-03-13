package de.loki.metallum.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum BandStatus {
	ACTIV(1, "Active"), ON_HOLD(2, "On hold"), SPLIT_UP(3, "Split-up"), UNKNOWN(4, "Unknown"), CHANGED_NAME(5, "Changed name");

	private final int    searchNumber;
	private final String asString;
	private static Logger logger = LoggerFactory.getLogger(BandStatus.class);

	BandStatus(final int searchNumber, final String asString) {
		this.searchNumber = searchNumber;
		this.asString = asString;
	}

	public int asSearchNumber() {
		return this.searchNumber;
	}

	public static BandStatus getTypeBandStatusForString(String possibleStatus) {
		possibleStatus = possibleStatus.trim();
		for (BandStatus type : values()) {
			if (type.asString.equalsIgnoreCase(possibleStatus)) {
				return type;
			}
		}
		logger.error("Unrecognized BandStatus: " + possibleStatus);

		return null;
	}

	public String asString() {
		return this.asString;
	}

}
