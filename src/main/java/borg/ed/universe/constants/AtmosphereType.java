package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AtmosphereType
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum AtmosphereType {

	AMMONIA,
	AMMONIA_RICH,
	AMMONIA_AND_OXYGEN,
	ARGON,
	ARGON_RICH,
	CARBON_DIOXIDE,
	CARBON_DIOXIDE_RICH,
	HELIUM,
	HOT_CARBON_DIOXIDE,
	HOT_CARBON_DIOXIDE_RICH,
	HOT_SILICATE_VAPOUR,
	HOT_SULFUR_DIOXIDE,
	HOT_THICK_AMMONIA,
	HOT_THICK_AMMONIA_RICH,
	HOT_THICK_ARGON,
	HOT_THICK_ARGON_RICH,
	HOT_THICK_CARBON_DIOXIDE,
	HOT_THICK_CARBON_DIOXIDE_RICH,
	HOT_THICK_METALLIC_VAPOUR,
	HOT_THICK_METHANE,
	HOT_THICK_METHANE_RICH,
	HOT_THICK_NITROGEN,
	HOT_THICK_SILICATE_VAPOUR,
	HOT_THICK_SULFUR_DIOXIDE,
	HOT_THICK_WATER,
	HOT_THICK_WATER_RICH,
	HOT_THIN_CARBON_DIOXIDE,
	HOT_THIN_SILICATE_VAPOUR,
	HOT_THIN_SULFUR_DIOXIDE,
	HOT_WATER,
	HOT_WATER_RICH,
	METHANE,
	METHANE_RICH,
	NEON,
	NEON_RICH,
	NITROGEN,
	OXYGEN,
	SULFUR_DIOXIDE,
	THICK,
	THICK_AMMONIA,
	THICK_AMMONIA_RICH,
	THICK_AMMONIA_AND_OXYGEN,
	THICK_ARGON,
	THICK_ARGON_RICH,
	THICK_CARBON_DIOXIDE,
	THICK_CARBON_DIOXIDE_RICH,
	THICK_HELIUM,
	THICK_METHANE,
	THICK_METHANE_RICH,
	THICK_NITROGEN,
	THICK_SULFUR_DIOXIDE,
	THICK_NEON_RICH,
	THICK_WATER,
	THICK_WATER_RICH,
	THIN,
	THIN_AMMONIA,
	THIN_AMMONIA_AND_OXYGEN,
	THIN_ARGON,
	THIN_ARGON_RICH,
	THIN_CARBON_DIOXIDE,
	THIN_CARBON_DIOXIDE_RICH,
	THIN_HELIUM,
	THIN_METHANE,
	THIN_METHANE_RICH,
	THIN_NEON,
	THIN_NITROGEN,
	THIN_OXYGEN_RICH,
	THIN_SULFUR_DIOXIDE,
	THIN_WATER,
	THIN_WATER_RICH,
	WATER,
	WATER_RICH;

	static final Logger logger = LoggerFactory.getLogger(AtmosphereType.class);

	public static AtmosphereType fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value.toLowerCase()) {
			case "no atmosphere":
				return null;
			case "ammonia atmosphere":
			case "ammonia":
				return AtmosphereType.AMMONIA;
			case "ammonia rich atmosphere":
			case "ammonia-rich":
				return AtmosphereType.AMMONIA_RICH;
			case "ammonia and oxygen":
				return AtmosphereType.AMMONIA_AND_OXYGEN;
			case "argon atmosphere":
			case "argon":
				return AtmosphereType.ARGON;
			case "argon rich atmosphere":
			case "argon-rich":
				return AtmosphereType.ARGON_RICH;
			case "carbon dioxide atmosphere":
			case "carbon dioxide":
				return AtmosphereType.CARBON_DIOXIDE;
			case "carbon dioxide rich atmosphere":
			case "carbon dioxide-rich":
				return AtmosphereType.CARBON_DIOXIDE_RICH;
			case "helium atmosphere":
			case "helium":
				return AtmosphereType.HELIUM;
			case "hot carbon dioxide atmosphere":
			case "hot carbon dioxide":
				return AtmosphereType.HOT_CARBON_DIOXIDE;
			case "hot carbon dioxide rich atmosphere":
			case "hot carbon dioxide-rich":
				return AtmosphereType.HOT_CARBON_DIOXIDE_RICH;
			case "hot silicate vapour atmosphere":
			case "hot silicate vapour":
				return AtmosphereType.HOT_SILICATE_VAPOUR;
			case "hot sulfur dioxide atmosphere":
			case "hot sulphur dioxide atmosphere":
			case "hot sulphur dioxide":
				return AtmosphereType.HOT_SULFUR_DIOXIDE;
			case "hot thick ammonia atmosphere":
			case "hot thick ammonia":
				return AtmosphereType.HOT_THICK_AMMONIA;
			case "hot thick ammonia rich atmosphere":
			case "hot thick ammonia-rich":
				return AtmosphereType.HOT_THICK_AMMONIA_RICH;
			case "hot thick argon atmosphere":
			case "hot thick argon":
				return AtmosphereType.HOT_THICK_ARGON;
			case "hot thick argon rich atmosphere":
			case "hot thick argon-rich":
				return AtmosphereType.HOT_THICK_ARGON_RICH;
			case "hot thick carbon dioxide atmosphere":
			case "hot thick carbon dioxide":
				return AtmosphereType.HOT_THICK_CARBON_DIOXIDE;
			case "hot thick carbon dioxide rich atmosphere":
			case "hot thick carbon dioxide-rich":
				return AtmosphereType.HOT_THICK_CARBON_DIOXIDE_RICH;
			case "hot thick metallic vapour atmosphere":
			case "hot thick metallic vapour":
				return AtmosphereType.HOT_THICK_METALLIC_VAPOUR;
			case "hot thick methane atmosphere":
			case "hot thick methane":
				return AtmosphereType.HOT_THICK_METHANE;
			case "hot thick methane rich atmosphere":
			case "hot thick methane-rich":
				return AtmosphereType.HOT_THICK_METHANE_RICH;
			case "hot thick nitrogen atmosphere":
			case "hot thick nitrogen":
				return AtmosphereType.HOT_THICK_NITROGEN;
			case "hot thick silicate vapour atmosphere":
			case "hot thick silicate vapour":
				return AtmosphereType.HOT_THICK_SILICATE_VAPOUR;
			case "hot thick sulfur dioxide atmosphere":
			case "hot thick sulphur dioxide atmosphere":
			case "hot thick sulphur dioxide":
				return AtmosphereType.HOT_THICK_SULFUR_DIOXIDE;
			case "hot thick water atmosphere":
			case "hot thick water":
				return AtmosphereType.HOT_THICK_WATER;
			case "hot thick water rich atmosphere":
			case "hot thick water-rich":
				return AtmosphereType.HOT_THICK_WATER_RICH;
			case "hot thin carbon dioxide atmosphere":
			case "hot thin carbon dioxide":
				return AtmosphereType.HOT_THIN_CARBON_DIOXIDE;
			case "hot thin silicate vapour atmosphere":
			case "hot thin silicate vapour":
				return AtmosphereType.HOT_THIN_SILICATE_VAPOUR;
			case "hot thin sulfur dioxide atmosphere":
			case "hot thin sulphur dioxide atmosphere":
			case "hot thin sulphur dioxide":
				return AtmosphereType.HOT_THIN_SULFUR_DIOXIDE;
			case "hot water atmosphere":
			case "hot water":
				return AtmosphereType.HOT_WATER;
			case "hot water rich atmosphere":
				return AtmosphereType.HOT_WATER_RICH;
			case "methane atmosphere":
			case "methane":
				return AtmosphereType.METHANE;
			case "methane rich atmosphere":
			case "methane-rich":
				return AtmosphereType.METHANE_RICH;
			case "neon atmosphere":
			case "neon":
				return AtmosphereType.NEON;
			case "neon rich atmosphere":
			case "neon-rich":
				return AtmosphereType.NEON_RICH;
			case "nitrogen atmosphere":
			case "nitrogen":
				return AtmosphereType.NITROGEN;
			case "oxygen atmosphere":
			case "oxygen":
				return AtmosphereType.OXYGEN;
			case "sulfur dioxide atmosphere":
			case "sulphur dioxide atmosphere":
			case "sulphur dioxide":
				return AtmosphereType.SULFUR_DIOXIDE;
			case "thick  atmosphere": // TODO wtf?
				return AtmosphereType.THICK;
			case "thick ammonia atmosphere":
			case "thick ammonia":
				return AtmosphereType.THICK_AMMONIA;
			case "thick ammonia rich atmosphere":
			case "thick ammonia-rich":
				return AtmosphereType.THICK_AMMONIA_RICH;
			case "thick ammonia and oxygen":
				return AtmosphereType.THICK_AMMONIA_AND_OXYGEN;
			case "thick argon atmosphere":
			case "thick argon":
				return AtmosphereType.THICK_ARGON;
			case "thick argon rich atmosphere":
			case "thick argon-rich":
				return AtmosphereType.THICK_ARGON_RICH;
			case "thick carbon dioxide atmosphere":
			case "thick carbon dioxide":
				return AtmosphereType.THICK_CARBON_DIOXIDE;
			case "thick carbon dioxide rich atmosphere":
			case "thick carbon dioxide-rich":
				return AtmosphereType.THICK_CARBON_DIOXIDE_RICH;
			case "thick helium atmosphere":
			case "thick helium":
				return AtmosphereType.THICK_HELIUM;
			case "thick methane atmosphere":
			case "thick methane":
				return AtmosphereType.THICK_METHANE;
			case "thick methane rich atmosphere":
			case "thick methane-rich":
				return AtmosphereType.THICK_METHANE_RICH;
			case "thick nitrogen atmosphere":
			case "thick nitrogen":
				return AtmosphereType.THICK_NITROGEN;
			case "thick sulfur dioxide atmosphere":
			case "thick sulphur dioxide atmosphere":
			case "thick sulfur dioxide":
			case "thick sulphur dioxide":
				return AtmosphereType.THICK_SULFUR_DIOXIDE;
			case "thin neon rich atmosphere":
			case "thin neon-rich":
				return AtmosphereType.THICK_NEON_RICH;
			case "thick water atmosphere":
			case "thick water":
				return AtmosphereType.THICK_WATER;
			case "thick water rich atmosphere":
			case "thick water-rich":
				return AtmosphereType.THICK_WATER_RICH;
			case "thin  atmosphere": // TODO wtf?
				return AtmosphereType.THIN;
			case "thin ammonia atmosphere":
			case "thin ammonia":
				return AtmosphereType.THIN_AMMONIA;
			case "thin ammonia and oxygen":
				return AtmosphereType.THIN_AMMONIA_AND_OXYGEN;
			case "thin argon atmosphere":
			case "thin argon":
				return AtmosphereType.THIN_ARGON;
			case "thin argon rich atmosphere":
			case "thin argon-rich":
				return AtmosphereType.THIN_ARGON_RICH;
			case "thin carbon dioxide atmosphere":
			case "thin carbon dioxide":
				return AtmosphereType.THIN_CARBON_DIOXIDE;
			case "thin carbon dioxide rich atmosphere":
			case "thin carbon dioxide-rich":
				return AtmosphereType.THIN_CARBON_DIOXIDE_RICH;
			case "thin helium atmosphere":
			case "thin helium":
				return AtmosphereType.THIN_HELIUM;
			case "thin methane atmosphere":
			case "thin methane":
				return AtmosphereType.THIN_METHANE;
			case "thin methane rich atmosphere":
			case "thin methane-rich":
				return AtmosphereType.THIN_METHANE_RICH;
			case "thin neon atmosphere":
			case "thin neon":
				return AtmosphereType.THIN_NEON;
			case "thin nitrogen atmosphere":
			case "thin nitrogen":
				return AtmosphereType.THIN_NITROGEN;
			case "thin oxygen atmosphere":
			case "thin oxygen":
				return AtmosphereType.THIN_OXYGEN_RICH;
			case "thin sulfur dioxide atmosphere":
			case "thin sulphur dioxide atmosphere":
			case "thin sulphur dioxide":
				return AtmosphereType.THIN_SULFUR_DIOXIDE;
			case "thin water atmosphere":
			case "thin water":
				return AtmosphereType.THIN_WATER;
			case "thin water rich atmosphere":
			case "thin water-rich":
				return AtmosphereType.THIN_WATER_RICH;
			case "water atmosphere":
			case "water":
				return AtmosphereType.WATER;
			case "water rich atmosphere":
			case "water-rich":
				return AtmosphereType.WATER_RICH;
			case "suitable for water-based life":
			case "silicate vapour":
			case "ammonia magma":
			case "metallic vapour":
				return null; // TODO Strange...
			default:
				logger.warn("Unknown value '" + value + "' for AtmosphereType");
				return null;
			}
		}
	}

}
