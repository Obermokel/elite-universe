package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VolcanismType
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum VolcanismType {

	AMMONIA_MAGMA,
	CARBON_DIOXIDE_GEYSERS,
	MAJOR_CARBON_DIOXIDE_GEYSERS, // very rare!!!
	MAJOR_METALLIC_MAGMA,
	MAJOR_ROCKY_MAGMA,
	MAJOR_SILICATE_VAPOUR_GEYSERS,
	MAJOR_WATER_GEYSERS,
	MAJOR_WATER_MAGMA,
	METALLIC_MAGMA,
	METHANE_MAGMA,
	MINOR_AMMONIA_MAGMA,
	MINOR_CARBON_DIOXIDE_GEYSERS,
	MINOR_METALLIC_MAGMA,
	MINOR_METHANE_MAGMA,
	MINOR_NITROGEN_MAGMA,
	MINOR_ROCKY_MAGMA,
	MINOR_SILICATE_VAPOUR_GEYSERS,
	MINOR_WATER_GEYSERS,
	MINOR_WATER_MAGMA,
	NITROGEN_MAGMA,
	ROCKY_MAGMA,
	SILICATE_VAPOUR_GEYSERS,
	WATER_GEYSERS,
	WATER_MAGMA;

	static final Logger logger = LoggerFactory.getLogger(VolcanismType.class);

	public static VolcanismType fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value.toLowerCase()) {
			case "no volcanism":
				return null;
			case "ammonia magma volcanism":
			case "ammonia magma":
				return VolcanismType.AMMONIA_MAGMA;
			case "carbon dioxide geysers volcanism":
			case "carbon dioxide geysers":
				return VolcanismType.CARBON_DIOXIDE_GEYSERS;
			case "major carbon dioxide geysers volcanism":
			case "major carbon dioxide geysers":
				return VolcanismType.MAJOR_CARBON_DIOXIDE_GEYSERS;
			case "major metallic magma volcanism":
			case "major metallic magma":
				return VolcanismType.MAJOR_METALLIC_MAGMA;
			case "major rocky magma volcanism":
			case "major rocky magma":
				return VolcanismType.MAJOR_ROCKY_MAGMA;
			case "major silicate vapour geysers volcanism":
			case "major silicate vapour geysers":
				return VolcanismType.MAJOR_SILICATE_VAPOUR_GEYSERS;
			case "major water geysers volcanism":
			case "major water geysers":
				return VolcanismType.MAJOR_WATER_GEYSERS;
			case "major water magma volcanism":
			case "major water magma":
				return VolcanismType.MAJOR_WATER_MAGMA;
			case "metallic magma volcanism":
			case "metallic magma":
				return VolcanismType.METALLIC_MAGMA;
			case "methane magma volcanism":
			case "methane magma":
				return VolcanismType.METHANE_MAGMA;
			case "minor ammonia magma volcanism":
			case "minor ammonia magma":
				return VolcanismType.MINOR_AMMONIA_MAGMA;
			case "minor carbon dioxide geysers volcanism":
			case "minor carbon dioxide geysers":
				return VolcanismType.MINOR_CARBON_DIOXIDE_GEYSERS;
			case "minor metallic magma volcanism":
			case "minor metallic magma":
				return VolcanismType.MINOR_METALLIC_MAGMA;
			case "minor methane magma volcanism":
			case "minor methane magma":
				return VolcanismType.MINOR_METHANE_MAGMA;
			case "minor nitrogen magma volcanism":
			case "minor nitrogen magma":
				return VolcanismType.MINOR_NITROGEN_MAGMA;
			case "minor rocky magma volcanism":
			case "minor rocky magma":
				return VolcanismType.MINOR_ROCKY_MAGMA;
			case "minor silicate vapour geysers volcanism":
			case "minor silicate vapour geysers":
				return VolcanismType.MINOR_SILICATE_VAPOUR_GEYSERS;
			case "minor water geysers volcanism":
			case "minor water geysers":
				return VolcanismType.MINOR_WATER_GEYSERS;
			case "minor water magma volcanism":
			case "minor water magma":
				return VolcanismType.MINOR_WATER_MAGMA;
			case "nitrogen magma volcanism":
			case "nitrogen magma":
				return NITROGEN_MAGMA;
			case "rocky magma volcanism":
			case "rocky magma":
				return VolcanismType.ROCKY_MAGMA;
			case "silicate vapour geysers volcanism":
			case "silicate vapour geysers":
				return VolcanismType.SILICATE_VAPOUR_GEYSERS;
			case "water geysers volcanism":
			case "water geysers":
				return VolcanismType.WATER_GEYSERS;
			case "water magma volcanism":
			case "water magma":
				return VolcanismType.WATER_MAGMA;
			default:
				logger.warn("Unknown value '" + value + "' for VolcanismType");
				return null;
			}
		}
	}

}
