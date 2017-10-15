package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * Government
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum Government {

	NONE, ANARCHY, COMMUNISM, CONFEDERACY, COOPERATIVE, CORPORATE, DEMOCRACY, DICTATORSHIP, FEUDAL, PATRONAGE, PRISON_COLONY, THEOCRACY;

	public static Government fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "$government_None;":
				return Government.NONE;
			case "$government_Anarchy;":
				return Government.ANARCHY;
			case "$government_Communism;":
				return Government.COMMUNISM;
			case "$government_Confederacy;":
				return Government.CONFEDERACY;
			case "$government_Cooperative;":
				return Government.COOPERATIVE;
			case "$government_Corporate;":
				return Government.CORPORATE;
			case "$government_Democracy;":
				return Government.DEMOCRACY;
			case "$government_Dictatorship;":
				return Government.DICTATORSHIP;
			case "$government_Feudal;":
				return Government.FEUDAL;
			case "$government_Patronage;":
				return Government.PATRONAGE;
			case "$government_PrisonColony;":
				return Government.PRISON_COLONY;
			case "$government_Theocracy;":
				return Government.THEOCRACY;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for Government");
			}
		}
	}

}