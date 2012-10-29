package de.loki.metallum.enums;

public enum BandStatus {
	ACTIV(1, "Active"), ON_HOLD(2, "On hold"), SPLIT_UP(3, "Split-up"), UNKNOWN(4, "Unknown"), CHANGED_NAME(5, "Changed name");

	private final int		searchNumber;
	private final String	asString;

	private BandStatus(int searchNumber, String asString) {
		this.searchNumber = searchNumber;
		this.asString = asString;
	}

	public int asSearchNumber() {
		return this.searchNumber;
	}

	public static BandStatus getTypeBandStatusForString(String possibleStatus) {
		for (BandStatus type : values()) {
			if (type.asString.equalsIgnoreCase(possibleStatus)) {
				return type;
			}
		}
		System.err.println("PossibleBandStatus: " + possibleStatus);
		return null;
	}

	public String asString() {
		return this.asString;
	}

}
