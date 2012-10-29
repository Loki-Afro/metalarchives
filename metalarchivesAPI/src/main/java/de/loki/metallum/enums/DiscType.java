package de.loki.metallum.enums;

public enum DiscType {
	FULL_LENGTH("Full-length", 1, false), LIVE_ALBUM("Live album", 2, false), DEMO("Demo", 3, false), SINGLE("Single", 4, false), EP("EP", 5, false), DVD("DVD", 6, false), BOXED_SET("Boxed Set", 7,
			false), SPLIT("Split", 8, true), VHS("Video/VHS", 9, false), COMPILATION("Compilation", 10, false), SPLIT_VIDEO("Split DVD/video", 12, true);

	private final String	realName;
	private final int		searchNumber;
	private final boolean	isSplit;

	private DiscType(String realName, int searchNumber, boolean isSplitType) {
		this.realName = realName;
		this.searchNumber = searchNumber;
		this.isSplit = isSplitType;
	}

	public static DiscType getTypeDiscTypeForString(final String possibleDiscType) {
		for (DiscType type : values()) {
			if (type.realName.equalsIgnoreCase(possibleDiscType)) {
				return type;
			}
		}
		return null;
	}

	public int asSearchNumber() {
		return this.searchNumber;
	}

	public static boolean isSplit(final DiscType type) {
		if (type == null) {
			return false;
		}
		return type.isSplit;
	}
}
