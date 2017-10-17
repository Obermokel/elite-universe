package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * Allegiance
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum Allegiance {

	INDEPENDENT, ALLIANCE, EMPIRE, FEDERATION, PILOTS_FEDERATION, PLAYER_PILOTS;

	public static Allegiance fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "None":
				return null;
			case "Independent":
				return Allegiance.INDEPENDENT;
			case "Alliance":
				return Allegiance.ALLIANCE;
			case "Empire":
				return Allegiance.EMPIRE;
			case "Federation":
				return Allegiance.FEDERATION;
			case "PilotsFederation":
				return Allegiance.PILOTS_FEDERATION;
			case "PlayerPilots":
				return Allegiance.PLAYER_PILOTS;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for Allegiance");
			}
		}
	}

}
