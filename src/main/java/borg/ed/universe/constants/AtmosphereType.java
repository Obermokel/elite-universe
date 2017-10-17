package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * AtmosphereType
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum AtmosphereType {

	AMMONIA, AMMONIA_RICH, ARGON, ARGON_RICH, CARBON_DIOXIDE, CARBON_DIOXIDE_RICH, HELIUM, HOT_CARBON_DIOXIDE, HOT_CARBON_DIOXIDE_RICH, HOT_SILICATE_VAPOUR, HOT_SULFUR_DIOXIDE, HOT_THICK_AMMONIA, HOT_THICK_AMMONIA_RICH, HOT_THICK_ARGON_RICH, HOT_THICK_CARBON_DIOXIDE, HOT_THICK_CARBON_DIOXIDE_RICH, HOT_THICK_METALLIC_VAPOUR, HOT_THICK_METHANE, HOT_THICK_METHANE_RICH, HOT_THICK_SILICATE_VAPOUR, HOT_THICK_SULFUR_DIOXIDE, HOT_THICK_WATER, HOT_THICK_WATER_RICH, HOT_THIN_CARBON_DIOXIDE, HOT_THIN_SILICATE_VAPOUR, HOT_THIN_SULFUR_DIOXIDE, HOT_WATER, METHANE, METHANE_RICH, NEON, NEON_RICH, NITROGEN, OXYGEN, SULFUR_DIOXIDE, THICK, THICK_AMMONIA, THICK_AMMONIA_RICH, THICK_ARGON, THICK_ARGON_RICH, THICK_CARBON_DIOXIDE, THICK_CARBON_DIOXIDE_RICH, THICK_HELIUM, THICK_METHANE, THICK_METHANE_RICH, THICK_NITROGEN, THICK_SULFUR_DIOXIDE, THICK_NEON_RICH, THICK_WATER, THICK_WATER_RICH, THIN, THIN_AMMONIA, THIN_ARGON, THIN_ARGON_RICH, THIN_CARBON_DIOXIDE, THIN_CARBON_DIOXIDE_RICH, THIN_HELIUM, THIN_METHANE, THIN_METHANE_RICH, THIN_NEON, THIN_NITROGEN, THIN_OXYGEN_RICH, THIN_SULFUR_DIOXIDE, THIN_WATER, THIN_WATER_RICH, WATER, WATER_RICH;

	public static AtmosphereType fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "No atmosphere":
			case "Metallic vapour": // GIGO
			case "Suitable for water based life": // GIGO
			case "Silicate vapour": // GIGO
			case "Ammonia and oxygen": // GIGO
				return null;
			case "ammonia atmosphere":
			case "Ammonia":
				return AtmosphereType.AMMONIA;
			case "ammonia rich atmosphere":
			case "Ammonia-rich":
				return AtmosphereType.AMMONIA_RICH;
			case "argon atmosphere":
			case "Argon":
				return AtmosphereType.ARGON;
			case "argon rich atmosphere":
			case "Argon-rich":
				return AtmosphereType.ARGON_RICH;
			case "carbon dioxide atmosphere":
			case "Carbon dioxide":
				return AtmosphereType.CARBON_DIOXIDE;
			case "carbon dioxide rich atmosphere":
			case "Carbon dioxide-rich":
				return AtmosphereType.CARBON_DIOXIDE_RICH;
			case "helium atmosphere":
			case "Helium":
				return AtmosphereType.HELIUM;
			case "hot carbon dioxide atmosphere":
			case "Hot carbon dioxide":
				return AtmosphereType.HOT_CARBON_DIOXIDE;
			case "hot carbon dioxide rich atmosphere":
			case "Hot carbon dioxide-rich":
				return AtmosphereType.HOT_CARBON_DIOXIDE_RICH;
			case "hot silicate vapour atmosphere":
			case "Hot silicate vapour":
				return AtmosphereType.HOT_SILICATE_VAPOUR;
			case "hot sulfur dioxide atmosphere":
			case "hot sulphur dioxide atmosphere":
				return AtmosphereType.HOT_SULFUR_DIOXIDE;
			case "hot thick ammonia atmosphere":
			case "Hot thick ammonia":
				return AtmosphereType.HOT_THICK_AMMONIA;
			case "hot thick ammonia rich atmosphere":
			case "Hot thick ammonia-rich":
				return AtmosphereType.HOT_THICK_AMMONIA_RICH;
			case "hot thick argon rich atmosphere":
			case "Hot thick argon-rich":
				return AtmosphereType.HOT_THICK_ARGON_RICH;
			case "hot thick carbon dioxide atmosphere":
			case "Hot thick carbon dioxide":
				return AtmosphereType.HOT_THICK_CARBON_DIOXIDE;
			case "hot thick carbon dioxide rich atmosphere":
			case "Hot thick carbon dioxide-rich":
				return AtmosphereType.HOT_THICK_CARBON_DIOXIDE_RICH;
			case "hot thick metallic vapour atmosphere":
			case "Hot thick metallic vapour":
				return AtmosphereType.HOT_THICK_METALLIC_VAPOUR;
			case "hot thick methane atmosphere":
			case "Hot thick methane":
				return AtmosphereType.HOT_THICK_METHANE;
			case "hot thick methane rich atmosphere":
			case "Hot thick methane-rich":
				return AtmosphereType.HOT_THICK_METHANE_RICH;
			case "hot thick silicate vapour atmosphere":
			case "Hot thick silicate vapour":
				return AtmosphereType.HOT_THICK_SILICATE_VAPOUR;
			case "hot thick sulfur dioxide atmosphere":
			case "hot thick sulphur dioxide atmosphere":
				return AtmosphereType.HOT_THICK_SULFUR_DIOXIDE;
			case "hot thick water atmosphere":
			case "Hot thick water":
				return AtmosphereType.HOT_THICK_WATER;
			case "hot thick water rich atmosphere":
			case "Hot thick water-rich":
				return AtmosphereType.HOT_THICK_WATER_RICH;
			case "hot thin carbon dioxide atmosphere":
			case "Hot thin carbon dioxide":
				return AtmosphereType.HOT_THIN_CARBON_DIOXIDE;
			case "hot thin silicate vapour atmosphere":
			case "Hot thin silicate vapour":
				return AtmosphereType.HOT_THIN_SILICATE_VAPOUR;
			case "hot thin sulfur dioxide atmosphere":
			case "hot thin sulphur dioxide atmosphere":
				return AtmosphereType.HOT_THIN_SULFUR_DIOXIDE;
			case "hot water atmosphere":
			case "Hot water":
				return AtmosphereType.HOT_WATER;
			case "methane atmosphere":
			case "Methane":
				return AtmosphereType.METHANE;
			case "methane rich atmosphere":
			case "Methane-rich":
				return AtmosphereType.METHANE_RICH;
			case "neon atmosphere":
			case "Neon":
				return AtmosphereType.NEON;
			case "neon rich atmosphere":
			case "Neon-rich":
				return AtmosphereType.NEON_RICH;
			case "nitrogen atmosphere":
			case "Nitrogen":
				return AtmosphereType.NITROGEN;
			case "oxygen atmosphere":
			case "Oxygen":
				return AtmosphereType.OXYGEN;
			case "sulfur dioxide atmosphere":
			case "sulphur dioxide atmosphere":
			case "Sulphur dioxide":
				return AtmosphereType.SULFUR_DIOXIDE;
			case "thick  atmosphere": // TODO wtf?
				return AtmosphereType.THICK;
			case "thick ammonia atmosphere":
			case "Thick ammonia":
				return AtmosphereType.THICK_AMMONIA;
			case "thick ammonia rich atmosphere":
			case "Thick ammonia-rich":
				return AtmosphereType.THICK_AMMONIA_RICH;
			case "thick argon atmosphere":
			case "Thick argon":
				return AtmosphereType.THICK_ARGON;
			case "thick argon rich atmosphere":
			case "Thick argon-rich":
				return AtmosphereType.THICK_ARGON_RICH;
			case "thick carbon dioxide atmosphere":
			case "Thick carbon dioxide":
				return AtmosphereType.THICK_CARBON_DIOXIDE;
			case "thick carbon dioxide rich atmosphere":
			case "Thick carbon dioxide-rich":
				return AtmosphereType.THICK_CARBON_DIOXIDE_RICH;
			case "thick helium atmosphere":
			case "Thick helium":
				return AtmosphereType.THICK_HELIUM;
			case "thick methane atmosphere":
			case "Thick methane":
				return AtmosphereType.THICK_METHANE;
			case "thick methane rich atmosphere":
			case "Thick methane-rich":
				return AtmosphereType.THICK_METHANE_RICH;
			case "thick nitrogen atmosphere":
			case "Thick nitrogen":
				return AtmosphereType.THICK_NITROGEN;
			case "thick sulfur dioxide atmosphere":
			case "Thick sulfur dioxide":
				return AtmosphereType.THICK_SULFUR_DIOXIDE;
			case "thin neon rich atmosphere":
			case "Thin neon-rich":
				return AtmosphereType.THICK_NEON_RICH;
			case "thick water atmosphere":
			case "Thick water":
				return AtmosphereType.THICK_WATER;
			case "thick water rich atmosphere":
			case "Thick water-rich":
				return AtmosphereType.THICK_WATER_RICH;
			case "thin  atmosphere": // TODO wtf?
				return AtmosphereType.THIN;
			case "thin ammonia atmosphere":
			case "Thin ammonia":
				return AtmosphereType.THIN_AMMONIA;
			case "thin argon atmosphere":
			case "Thin argon":
				return AtmosphereType.THIN_ARGON;
			case "thin argon rich atmosphere":
			case "Thin argon-rich":
				return AtmosphereType.THIN_ARGON_RICH;
			case "thin carbon dioxide atmosphere":
			case "Thin carbon dioxide":
				return AtmosphereType.THIN_CARBON_DIOXIDE;
			case "thin carbon dioxide rich atmosphere":
			case "Thin carbon dioxide-rich":
				return AtmosphereType.THIN_CARBON_DIOXIDE_RICH;
			case "thin helium atmosphere":
			case "Thin helium":
				return AtmosphereType.THIN_HELIUM;
			case "thin methane atmosphere":
			case "Thin methane":
				return AtmosphereType.THIN_METHANE;
			case "thin methane rich atmosphere":
			case "Thin methane-rich":
				return AtmosphereType.THIN_METHANE_RICH;
			case "thin neon atmosphere":
			case "Thin neon":
				return AtmosphereType.THIN_NEON;
			case "thin nitrogen atmosphere":
			case "Thin nitrogen":
				return AtmosphereType.THIN_NITROGEN;
			case "thin oxygen atmosphere":
			case "Thin oxygen":
				return AtmosphereType.THIN_OXYGEN_RICH;
			case "thin sulfur dioxide atmosphere":
			case "thin sulphur dioxide atmosphere":
			case "Thin sulphur dioxide":
				return AtmosphereType.THIN_SULFUR_DIOXIDE;
			case "thin water atmosphere":
			case "Thin water":
				return AtmosphereType.THIN_WATER;
			case "thin water rich atmosphere":
			case "Thin water-rich":
				return AtmosphereType.THIN_WATER_RICH;
			case "water atmosphere":
			case "Water":
				return AtmosphereType.WATER;
			case "water rich atmosphere":
			case "Water-rich":
				return AtmosphereType.WATER_RICH;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for AtmosphereType");
			}
		}
	}

}
