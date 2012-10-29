package de.loki.metallum.enums;

public enum LabelStatus {
	ANY(""), ACTIVE("active"), UNKNOWN("unknown"), CLOSED("closed"), CHANGED_NAME("changed name");
	private final String asString;

	private LabelStatus(String asString) {
		this.asString = asString;
	}

	public static LabelStatus getLabelStatusForString(final String possibleStatus) {
		for (LabelStatus type : values()) {
			if (type.asString.equalsIgnoreCase(possibleStatus)) {
				return type;
			}
		}
		System.err.println("LabelStatus: " + possibleStatus);
		return ANY;
	}

	public String asString() {
		return this.asString;
	}
}
