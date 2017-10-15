package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * VolcanismType
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum VolcanismType {

	CARBON_DIOXIDE_GEYSERS_VOLCANISM, ROCKY_MAGMA_VOLCANISM, MAJOR_METALLIC_MAGMA_GEYSERS, MAJOR_ROCKY_MAGMA_VOLCANISM, MAJOR_SILICATE_VAPOUR_GEYSERS, MAJOR_WATER_GEYSERS_VOLCANISM, MAJOR_WATER_MAGMA_VOLCANISM, METALLIC_MAGMA_VOLCANISM, MINOR_AMMONIA_MAGMA_VOLCANISM, MINOR_CARBON_DIOXIDE_GEYSERS_VOLCANISM, MINOR_METALLIC_MAGMA_VOLCANISM, MINOR_METHANE_MAGMA_VOLCANISM, MINOR_NITROGEN_MAGMA_VOLCANISM, MINOR_ROCKY_MAGMA_VOLCANISM, MINOR_SILICATE_VAPOUR_GEYSERS_VOLCANISM, MINOR_WATER_GEYSERS_VOLCANISM, MINOR_WATER_MAGMA_VOLCANISM, SILICATE_VAPOUR_GEYSERS_VOLCANISM, WATER_GEYSERS_VOLCANISM, WATER_MAGMA_VOLCANISM;

	public static VolcanismType fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "carbon dioxide geysers volcanism":
				return VolcanismType.CARBON_DIOXIDE_GEYSERS_VOLCANISM;
			case "rocky magma volcanism":
				return VolcanismType.ROCKY_MAGMA_VOLCANISM;
			case "major metallic magma volcanism":
				return VolcanismType.MAJOR_METALLIC_MAGMA_GEYSERS;
			case "major rocky magma volcanism":
				return VolcanismType.MAJOR_ROCKY_MAGMA_VOLCANISM;
			case "major silicate vapour geysers volcanism":
				return VolcanismType.MAJOR_SILICATE_VAPOUR_GEYSERS;
			case "major water geysers volcanism":
				return VolcanismType.MAJOR_WATER_GEYSERS_VOLCANISM;
			case "major water magma volcanism":
				return VolcanismType.MAJOR_WATER_MAGMA_VOLCANISM;
			case "metallic magma volcanism":
				return VolcanismType.METALLIC_MAGMA_VOLCANISM;
			case "minor ammonia magma volcanism":
				return VolcanismType.MINOR_AMMONIA_MAGMA_VOLCANISM;
			case "minor carbon dioxide geysers volcanism":
				return VolcanismType.MINOR_CARBON_DIOXIDE_GEYSERS_VOLCANISM;
			case "minor metallic magma volcanism":
				return VolcanismType.MINOR_METALLIC_MAGMA_VOLCANISM;
			case "minor methane magma volcanism":
				return VolcanismType.MINOR_METHANE_MAGMA_VOLCANISM;
			case "minor nitrogen magma volcanism":
				return VolcanismType.MINOR_NITROGEN_MAGMA_VOLCANISM;
			case "minor rocky magma volcanism":
				return VolcanismType.MINOR_ROCKY_MAGMA_VOLCANISM;
			case "minor silicate vapour geysers volcanism":
				return VolcanismType.MINOR_SILICATE_VAPOUR_GEYSERS_VOLCANISM;
			case "minor water geysers volcanism":
				return VolcanismType.MINOR_WATER_GEYSERS_VOLCANISM;
			case "minor water magma volcanism":
				return VolcanismType.MINOR_WATER_MAGMA_VOLCANISM;
			case "silicate vapour geysers volcanism":
				return VolcanismType.SILICATE_VAPOUR_GEYSERS_VOLCANISM;
			case "water geysers volcanism":
				return VolcanismType.WATER_GEYSERS_VOLCANISM;
			case "water magma volcanism":
				return VolcanismType.WATER_MAGMA_VOLCANISM;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for VolcanismType");
			}
		}
	}

}
