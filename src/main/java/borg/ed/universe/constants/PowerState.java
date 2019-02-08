package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * PowerState
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum PowerState {

	CONTESTED, CONTROLLED, EXPLOITED, HOME_SYSTEM, IN_PREPARE_RADIUS, PREPARED, TURMOIL;

	public static PowerState fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "Contested":
				return PowerState.CONTESTED;
			case "Controlled":
				return PowerState.CONTROLLED;
			case "Exploited":
				return PowerState.EXPLOITED;
			case "HomeSystem":
				return PowerState.HOME_SYSTEM;
			case "InPrepareRadius":
				return PowerState.IN_PREPARE_RADIUS;
			case "Prepared":
				return PowerState.PREPARED;
			case "Turmoil":
				return PowerState.TURMOIL;
			case "Unknown":
				return null;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for PowerState");
			}
		}
	}

}
