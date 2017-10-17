package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * State
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum State {

	NONE, BOOM, BUST, CIVIL_UNREST, CIVIL_WAR, ELECTION, EXPANSION, FAMINE, INVESTMENT, LOCKDOWN, OUTBREAK, RETREAT, WAR;

	public static State fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "None":
				return State.NONE;
			case "Boom":
				return State.BOOM;
			case "Bust":
				return State.BUST;
			case "CivilUnrest":
			case "Civil Unrest":
				return State.CIVIL_UNREST;
			case "CivilWar":
			case "Civil War":
				return State.CIVIL_WAR;
			case "Election":
				return State.ELECTION;
			case "Expansion":
				return State.EXPANSION;
			case "Famine":
				return State.FAMINE;
			case "Investment":
				return State.INVESTMENT;
			case "Lockdown":
				return State.LOCKDOWN;
			case "Outbreak":
				return State.OUTBREAK;
			case "Retreat":
				return State.RETREAT;
			case "War":
				return State.WAR;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for State");
			}
		}
	}

}
