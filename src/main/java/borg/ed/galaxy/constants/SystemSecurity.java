package borg.ed.galaxy.constants;

import org.apache.commons.lang.StringUtils;

/**
 * SystemSecurity
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum SystemSecurity {

	ANARCHY, LAWLESS, LOW, MEDIUM, HIGH;

	public static SystemSecurity fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "$GAlAXY_MAP_INFO_state_anarchy;":
			case "Anarchy":
				return SystemSecurity.ANARCHY;
			case "$GALAXY_MAP_INFO_state_lawless;":
			case "Lawless":
				return SystemSecurity.LAWLESS;
			case "$SYSTEM_SECURITY_low;":
			case "Low":
				return SystemSecurity.LOW;
			case "$SYSTEM_SECURITY_medium;":
			case "Medium":
				return SystemSecurity.MEDIUM;
			case "$SYSTEM_SECURITY_high;":
			case "High":
				return SystemSecurity.HIGH;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for SystemSecurity");
			}
		}
	}

}
