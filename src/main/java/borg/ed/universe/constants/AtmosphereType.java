package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * AtmosphereType
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum AtmosphereType {

	AMMONIA, AMMONIA_RICH, ARGON, ARGON_RICH, CARBON_DIOXIDE, CARBON_DIOXIDE_RICH, HELIUM, HOT_CARBON_DIOXIDE, HOT_CARBON_DIOXIDE_RICH, HOT_SILICATE_VAPOUR, HOT_SULFUR_DIOXIDE, HOT_THICK_AMMONIA, HOT_THICK_AMMONIA_RICH, HOT_THICK_ARGON_RICH, HOT_THICK_CARBON_DIOXIDE, HOT_THICK_CARBON_DIOXIDE_RICH, HOT_THICK_METALLIC_VAPOUR, HOT_THICK_METHANE, HOT_THICK_METHANE_RICH, HOT_THICK_SILICATE_VAPOUR, HOT_THICK_SULFUR_DIOXIDE, HOT_THICK_WATER, HOT_THICK_WATER_RICH, HOT_THIN_CARBON_DIOXIDE, HOT_THIN_SILICATE_VAPOUR, HOT_THIN_SULFUR_DIOXIDE, HOT_WATER, METHANE, METHANE_RICH, NEON_RICH, NITROGEN, OXYGEN, SULFUR_DIOXIDE, THICK, THICK_AMMONIA, THICK_AMMONIA_RICH, THICK_ARGON, THICK_ARGON_RICH, THICK_CARBON_DIOXIDE, THICK_CARBON_DIOXIDE_RICH, THICK_HELIUM, THICK_METHANE, THICK_METHANE_RICH, THICK_NITROGEN, THICK_SULFUR_DIOXIDE, THICK_NEON_RICH, THICK_WATER, THICK_WATER_RICH, THIN, THIN_AMMONIA, THIN_ARGON, THIN_ARGON_RICH, THIN_CARBON_DIOXIDE, THIN_CARBON_DIOXIDE_RICH, THIN_HELIUM, THIN_METHANE, THIN_METHANE_RICH, THIN_NEON, THIN_NITROGEN, THIN_OXYGEN_RICH, THIN_SULFUR_DIOXIDE, THIN_WATER, THIN_WATER_RICH, WATER, WATER_RICH;

	public static AtmosphereType fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "ammonia atmosphere":
				return AtmosphereType.AMMONIA;
			case "ammonia rich atmosphere":
				return AtmosphereType.AMMONIA_RICH;
			case "argon atmosphere":
				return AtmosphereType.ARGON;
			case "argon rich atmosphere":
				return AtmosphereType.ARGON_RICH;
			case "carbon dioxide atmosphere":
				return AtmosphereType.CARBON_DIOXIDE;
			case "carbon dioxide rich atmosphere":
				return AtmosphereType.CARBON_DIOXIDE_RICH;
			case "helium atmosphere":
				return AtmosphereType.HELIUM;
			case "hot carbon dioxide atmosphere":
				return AtmosphereType.HOT_CARBON_DIOXIDE;
			case "hot carbon dioxide rich atmosphere":
				return AtmosphereType.HOT_CARBON_DIOXIDE_RICH;
			case "hot silicate vapour atmosphere":
				return AtmosphereType.HOT_SILICATE_VAPOUR;
			case "hot sulfur dioxide atmosphere":
			case "hot sulphur dioxide atmosphere":
				return AtmosphereType.HOT_SULFUR_DIOXIDE;
			case "hot thick ammonia atmosphere":
				return AtmosphereType.HOT_THICK_AMMONIA;
			case "hot thick ammonia rich atmosphere":
				return AtmosphereType.HOT_THICK_AMMONIA_RICH;
			case "hot thick argon rich atmosphere":
				return AtmosphereType.HOT_THICK_ARGON_RICH;
			case "hot thick carbon dioxide atmosphere":
				return AtmosphereType.HOT_THICK_CARBON_DIOXIDE;
			case "hot thick carbon dioxide rich atmosphere":
				return AtmosphereType.HOT_THICK_CARBON_DIOXIDE_RICH;
			case "hot thick metallic vapour atmosphere":
				return AtmosphereType.HOT_THICK_METALLIC_VAPOUR;
			case "hot thick methane atmosphere":
				return AtmosphereType.HOT_THICK_METHANE;
			case "hot thick methane rich atmosphere":
				return AtmosphereType.HOT_THICK_METHANE_RICH;
			case "hot thick silicate vapour atmosphere":
				return AtmosphereType.HOT_THICK_SILICATE_VAPOUR;
			case "hot thick sulfur dioxide atmosphere":
			case "hot thick sulphur dioxide atmosphere":
				return AtmosphereType.HOT_THICK_SULFUR_DIOXIDE;
			case "hot thick water atmosphere":
				return AtmosphereType.HOT_THICK_WATER;
			case "hot thick water rich atmosphere":
				return AtmosphereType.HOT_THICK_WATER_RICH;
			case "hot thin carbon dioxide atmosphere":
				return AtmosphereType.HOT_THIN_CARBON_DIOXIDE;
			case "hot thin silicate vapour atmosphere":
				return AtmosphereType.HOT_THIN_SILICATE_VAPOUR;
			case "hot thin sulfur dioxide atmosphere":
			case "hot thin sulphur dioxide atmosphere":
				return AtmosphereType.HOT_THIN_SULFUR_DIOXIDE;
			case "hot water atmosphere":
				return AtmosphereType.HOT_WATER;
			case "methane atmosphere":
				return AtmosphereType.METHANE;
			case "methane rich atmosphere":
				return AtmosphereType.METHANE_RICH;
			case "neon rich atmosphere":
				return AtmosphereType.NEON_RICH;
			case "nitrogen atmosphere":
				return AtmosphereType.NITROGEN;
			case "oxygen atmosphere":
				return AtmosphereType.OXYGEN;
			case "sulfur dioxide atmosphere":
			case "sulphur dioxide atmosphere":
				return AtmosphereType.SULFUR_DIOXIDE;
			case "thick  atmosphere": // TODO wtf?
				return AtmosphereType.THICK;
			case "thick ammonia atmosphere":
				return AtmosphereType.THICK_AMMONIA;
			case "thick ammonia rich atmosphere":
				return AtmosphereType.THICK_AMMONIA_RICH;
			case "thick argon atmosphere":
				return AtmosphereType.THICK_ARGON;
			case "thick argon rich atmosphere":
				return AtmosphereType.THICK_ARGON_RICH;
			case "thick carbon dioxide atmosphere":
				return AtmosphereType.THICK_CARBON_DIOXIDE;
			case "thick carbon dioxide rich atmosphere":
				return AtmosphereType.THICK_CARBON_DIOXIDE_RICH;
			case "thick helium atmosphere":
				return AtmosphereType.THICK_HELIUM;
			case "thick methane atmosphere":
				return AtmosphereType.THICK_METHANE;
			case "thick methane rich atmosphere":
				return AtmosphereType.THICK_METHANE_RICH;
			case "thick nitrogen atmosphere":
				return AtmosphereType.THICK_NITROGEN;
			case "thick sulfur dioxide atmosphere":
				return AtmosphereType.THICK_SULFUR_DIOXIDE;
			case "thin neon rich atmosphere":
				return AtmosphereType.THICK_NEON_RICH;
			case "thick water atmosphere":
				return AtmosphereType.THICK_WATER;
			case "thick water rich atmosphere":
				return AtmosphereType.THICK_WATER_RICH;
			case "thin  atmosphere": // TODO wtf?
				return AtmosphereType.THIN;
			case "thin ammonia atmosphere":
				return AtmosphereType.THIN_AMMONIA;
			case "thin argon atmosphere":
				return AtmosphereType.THIN_ARGON;
			case "thin argon rich atmosphere":
				return AtmosphereType.THIN_ARGON_RICH;
			case "thin carbon dioxide atmosphere":
				return AtmosphereType.THIN_CARBON_DIOXIDE;
			case "thin carbon dioxide rich atmosphere":
				return AtmosphereType.THIN_CARBON_DIOXIDE_RICH;
			case "thin helium atmosphere":
				return AtmosphereType.THIN_HELIUM;
			case "thin methane atmosphere":
				return AtmosphereType.THIN_METHANE;
			case "thin methane rich atmosphere":
				return AtmosphereType.THIN_METHANE_RICH;
			case "thin neon atmosphere":
				return AtmosphereType.THIN_NEON;
			case "thin nitrogen atmosphere":
				return AtmosphereType.THIN_NITROGEN;
			case "thin oxygen atmosphere":
				return AtmosphereType.THIN_OXYGEN_RICH;
			case "thin sulfur dioxide atmosphere":
			case "thin sulphur dioxide atmosphere":
				return AtmosphereType.THIN_SULFUR_DIOXIDE;
			case "thin water atmosphere":
				return AtmosphereType.THIN_WATER;
			case "thin water rich atmosphere":
				return AtmosphereType.THIN_WATER_RICH;
			case "water atmosphere":
				return AtmosphereType.WATER;
			case "water rich atmosphere":
				return AtmosphereType.WATER_RICH;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for AtmosphereType");
			}
		}
	}

}
