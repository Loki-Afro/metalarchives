package de.loki.metallum.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ===REQUIRES REFACTORING===
 * 
 * metal-archives does now provide a release Type (more or less this class) and a release Format
 * 
 * @author zarathustra
 * 
 */
public enum DiscType {
	FULL_LENGTH("Full-length", 1, false), LIVE_ALBUM("Live album", 2, false), DEMO("Demo", 3, false), SINGLE("Single", 4, false), EP("EP", 5, false), VIDEO("Video", 6, false), BOXED_SET("Boxed Set", 7, false), SPLIT("Split", 8, true),
	/**
	 * will be removed in the future? its marked as legacy at metal-archives?
	 */
	@Deprecated
	VHS("Video/VHS", 9, false), COMPILATION("Compilation", 10, false), SPLIT_VIDEO("Split video", 12, true);

	private final String	realName;
	private final int		searchNumber;
	private final boolean	isSplit;
	private static Logger	logger	= LoggerFactory.getLogger(DiscType.class);

	DiscType(final String realName, final int searchNumber, final boolean isSplitType) {
		this.realName = realName;
		this.searchNumber = searchNumber;
		this.isSplit = isSplitType;
	}

	public static DiscType getTypeDiscTypeForString(String possibleDiscType) {
		possibleDiscType = possibleDiscType.trim();
		for (DiscType type : values()) {
			if (type.realName.equalsIgnoreCase(possibleDiscType)) {
				return type;
			}
		}
		logger.error("Unrecognized DiscType: " + possibleDiscType);
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
