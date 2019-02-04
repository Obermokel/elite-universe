package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * Economy
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum Economy {

	NONE, AGRICULTURAL, COLONY, DAMAGED, EXTRACTION, HIGH_TECH, INDUSTRIAL, MILITARY, PRISON, REFINERY, REPAIR, RESCUE, SERVICE, TERRAFORMING, TOURISM;

	public static Economy fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "$economy_None;":
			case "None":
				return Economy.NONE;
			case "$economy_Agri;":
			case "Agriculture":
				return Economy.AGRICULTURAL;
			case "$economy_Colony;":
			case "Colony":
				return Economy.COLONY;
			case "$economy_Damaged;":
			case "Damaged":
				return Economy.DAMAGED;
			case "$economy_Extraction;":
			case "Extraction":
				return Economy.EXTRACTION;
			case "$economy_HighTech;":
			case "High Tech":
				return HIGH_TECH;
			case "$economy_Industrial;":
			case "Industrial":
				return Economy.INDUSTRIAL;
			case "$economy_Military;":
			case "Military":
				return Economy.MILITARY;
			case "$economy_Prison;":
			case "Prison":
				return Economy.PRISON;
			case "$economy_Refinery;":
			case "Refinery":
				return Economy.REFINERY;
			case "$economy_Repair;":
			case "Repair":
				return Economy.REPAIR;
			case "$economy_Rescue;":
			case "Rescue":
				return Economy.RESCUE;
			case "$economy_Service;":
			case "Service":
				return Economy.SERVICE;
			case "$economy_Terraforming;":
			case "Terraforming":
				return Economy.TERRAFORMING;
			case "$economy_Tourism;":
			case "Tourism":
				return Economy.TOURISM;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for Economy");
			}
		}
	}

}
