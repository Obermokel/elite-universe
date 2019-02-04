package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * ReserveLevel
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum ReserveLevel {

	DEPLETED, LOW, COMMON, MAJOR, PRISTINE;

	public static ReserveLevel fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "DepletedResources":
			case "Depleted":
				return ReserveLevel.DEPLETED;
			case "LowResources":
			case "Low":
				return ReserveLevel.LOW;
			case "CommonResources":
			case "Common":
				return ReserveLevel.COMMON;
			case "MajorResources":
			case "Major":
				return ReserveLevel.MAJOR;
			case "PristineResources":
			case "Pristine":
				return ReserveLevel.PRISTINE;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for ReserveLevel");
			}
		}
	}

}
