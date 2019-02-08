package borg.ed.galaxy.constants;

import org.apache.commons.lang.StringUtils;

/**
 * State
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum State {

	NONE,
	BOOM,
	BUST,
	CIVIL_LIBERTY,
	CIVIL_UNREST,
	CIVIL_WAR,
	ELECTION,
	EXPANSION,
	FAMINE,
	INVESTMENT,
	LOCKDOWN,
	OUTBREAK,
	PIRATE_ATTACK,
	RETREAT,
	WAR;

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
			case "CivilLiberty":
			case "Civil Liberty":
				return State.CIVIL_LIBERTY;
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
			case "PirateAttack":
				return State.PIRATE_ATTACK;
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
