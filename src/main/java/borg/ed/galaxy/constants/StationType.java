package borg.ed.galaxy.constants;

import org.apache.commons.lang.StringUtils;

/**
 * StationType
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum StationType {

	ASTEROID_BASE, CORIOLIS, MEGASHIP, OCELLUS, ORBIS, OUTPOST, PLANETARY_OUTPOST, PLANETARY_PORT;

	public static StationType fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "AsteroidBase":
				return StationType.ASTEROID_BASE;
			case "Coriolis":
				return StationType.CORIOLIS;
			case "MegaShip":
				return StationType.MEGASHIP;
			case "Ocellus":
				return StationType.OCELLUS;
			case "Orbis":
				return StationType.ORBIS;
			case "Outpost":
				return StationType.OUTPOST;
			case "CraterOutpost":
				return StationType.PLANETARY_OUTPOST;
			case "CraterPort":
				return StationType.PLANETARY_PORT;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for StationType");
			}
		}
	}

	public boolean isPlanetary() {
		return this == PLANETARY_OUTPOST || this == PLANETARY_PORT;
	}

	public String getMaxLandingPadSize() {
		if (this == OUTPOST) {
			return "M";
		} else {
			return "L";
		}
	}

}
