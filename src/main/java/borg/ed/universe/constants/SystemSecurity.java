package borg.ed.universe.constants;

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
				return SystemSecurity.ANARCHY;
			case "$GALAXY_MAP_INFO_state_lawless;":
				return SystemSecurity.LAWLESS;
			case "$SYSTEM_SECURITY_low;":
				return SystemSecurity.LOW;
			case "$SYSTEM_SECURITY_medium;":
				return SystemSecurity.MEDIUM;
			case "$SYSTEM_SECURITY_high;":
				return SystemSecurity.HIGH;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for SystemSecurity");
			}
		}
	}

}
