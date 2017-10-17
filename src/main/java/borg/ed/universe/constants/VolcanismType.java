package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * VolcanismType
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum VolcanismType {

	CARBON_DIOXIDE_GEYSERS,
	ROCKY_MAGMA,
	MAJOR_METALLIC_MAGMA,
	MAJOR_ROCKY_MAGMA,
	MAJOR_SILICATE_VAPOUR_GEYSERS,
	MAJOR_WATER_GEYSERS,
	MAJOR_WATER_MAGMA,
	METALLIC_MAGMA,
	MINOR_AMMONIA_MAGMA,
	MINOR_CARBON_DIOXIDE_GEYSERS,
	MINOR_METALLIC_MAGMA,
	MINOR_METHANE_MAGMA,
	MINOR_NITROGEN_MAGMA,
	MINOR_ROCKY_MAGMA,
	MINOR_SILICATE_VAPOUR_GEYSERS,
	MINOR_WATER_GEYSERS,
	MINOR_WATER_MAGMA,
	SILICATE_VAPOUR_GEYSERS,
	WATER_GEYSERS,
	WATER_MAGMA;

	public static VolcanismType fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "No volcanism":
			case "Iron magma": // TODO From EDDB
			case "Silicate magma": // TODO From EDDB
			case "Nitrogen magma": // TODO From EDDB
			case "Methane magma": // TODO From EDDB
			case "Ammonia magma": // TODO From EDDB
				return null;
			case "carbon dioxide geysers volcanism":
			case "Carbon dioxide geysers":
				return VolcanismType.CARBON_DIOXIDE_GEYSERS;
			case "rocky magma volcanism":
			case "Rocky Magma":
				return VolcanismType.ROCKY_MAGMA;
			case "major metallic magma volcanism":
			case "Major Metallic Magma":
				return VolcanismType.MAJOR_METALLIC_MAGMA;
			case "major rocky magma volcanism":
			case "Major Rocky Magma":
				return VolcanismType.MAJOR_ROCKY_MAGMA;
			case "major silicate vapour geysers volcanism":
			case "Major Silicate Vapour Geysers":
				return VolcanismType.MAJOR_SILICATE_VAPOUR_GEYSERS;
			case "major water geysers volcanism":
			case "Major Water Geysers":
				return VolcanismType.MAJOR_WATER_GEYSERS;
			case "major water magma volcanism":
			case "Major Water Magma":
				return VolcanismType.MAJOR_WATER_MAGMA;
			case "metallic magma volcanism":
			case "Metallic Magma":
				return VolcanismType.METALLIC_MAGMA;
			case "minor ammonia magma volcanism":
			case "Minor Ammonia Magma":
				return VolcanismType.MINOR_AMMONIA_MAGMA;
			case "minor carbon dioxide geysers volcanism":
			case "Minor Carbon Dioxide Geysers":
				return VolcanismType.MINOR_CARBON_DIOXIDE_GEYSERS;
			case "minor metallic magma volcanism":
			case "Minor Metallic Magma":
				return VolcanismType.MINOR_METALLIC_MAGMA;
			case "minor methane magma volcanism":
			case "Minor Methane Magma":
				return VolcanismType.MINOR_METHANE_MAGMA;
			case "minor nitrogen magma volcanism":
			case "Minor Nitrogen Magma":
				return VolcanismType.MINOR_NITROGEN_MAGMA;
			case "minor rocky magma volcanism":
			case "Minor Rocky Magma":
				return VolcanismType.MINOR_ROCKY_MAGMA;
			case "minor silicate vapour geysers volcanism":
			case "Minor Silicate Vapour Geysers":
				return VolcanismType.MINOR_SILICATE_VAPOUR_GEYSERS;
			case "minor water geysers volcanism":
			case "Minor Water Geysers":
				return VolcanismType.MINOR_WATER_GEYSERS;
			case "minor water magma volcanism":
			case "Minor Water Magma":
				return VolcanismType.MINOR_WATER_MAGMA;
			case "silicate vapour geysers volcanism":
			case "Silicate vapour geysers":
				return VolcanismType.SILICATE_VAPOUR_GEYSERS;
			case "water geysers volcanism":
			case "Water geysers":
				return VolcanismType.WATER_GEYSERS;
			case "water magma volcanism":
			case "Water magma":
				return VolcanismType.WATER_MAGMA;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for VolcanismType");
			}
		}
	}

}
