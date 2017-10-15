package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * Economy
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum Economy {

	NONE, AGRICULTURAL, COLONY, EXTRACTION, HIGH_TECH, INDUSTRIAL, MILITARY, REFINERY, SERVICE, TERRAFORMING, TOURISM;

	public static Economy fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "$economy_None;":
				return Economy.NONE;
			case "$economy_Agri;":
				return Economy.AGRICULTURAL;
			case "$economy_Colony;":
				return Economy.COLONY;
			case "$economy_Extraction;":
				return Economy.EXTRACTION;
			case "$economy_HighTech;":
				return HIGH_TECH;
			case "$economy_Industrial;":
				return Economy.INDUSTRIAL;
			case "$economy_Military;":
				return Economy.MILITARY;
			case "$economy_Refinery;":
				return Economy.REFINERY;
			case "$economy_Service;":
				return Economy.SERVICE;
			case "$economy_Terraforming;":
				return Economy.TERRAFORMING;
			case "$economy_Tourism;":
				return Economy.TOURISM;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for Economy");
			}
		}
	}

}
