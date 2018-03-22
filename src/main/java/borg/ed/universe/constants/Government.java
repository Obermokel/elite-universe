package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * Government
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum Government {

	NONE,
	ANARCHY,
	COMMUNISM,
	CONFEDERACY,
	COOPERATIVE,
	CORPORATE,
	DEMOCRACY,
	ENGINEER,
	DICTATORSHIP,
	FEUDAL,
	PATRONAGE,
	PRISON_COLONY,
	THEOCRACY;

	public static Government fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "$government_None;":
			case "None":
				return Government.NONE;
			case "$government_Anarchy;":
			case "Anarchy":
				return Government.ANARCHY;
			case "$government_Communism;":
			case "Communism":
				return Government.COMMUNISM;
			case "$government_Confederacy;":
			case "Confederacy":
				return Government.CONFEDERACY;
			case "$government_Cooperative;":
			case "Cooperative":
				return Government.COOPERATIVE;
			case "$government_Corporate;":
			case "Corporate":
				return Government.CORPORATE;
			case "$government_Democracy;":
			case "Democracy":
				return Government.DEMOCRACY;
			case "$government_Dictatorship;":
			case "Dictatorship":
				return Government.DICTATORSHIP;
			case "Engineer":
				return Government.ENGINEER;
			case "$government_Feudal;":
			case "Feudal":
				return Government.FEUDAL;
			case "$government_Patronage;":
			case "Patronage":
				return Government.PATRONAGE;
			case "$government_PrisonColony;":
			case "$government_Prison;":
			case "PrisonColony":
			case "Prison Colony":
				return Government.PRISON_COLONY;
			case "$government_Theocracy;":
			case "Theocracy":
				return Government.THEOCRACY;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for Government");
			}
		}
	}

}
