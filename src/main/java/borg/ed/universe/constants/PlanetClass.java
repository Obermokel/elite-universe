package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * PlanetClass
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum PlanetClass {

	AMMONIA_WORLD, EARTHLIKE_BODY, GAS_GIANT_WITH_AMMONIA_BASED_LIFE, GAS_GIANT_WITH_WATER_BASED_LIFE, HELIUM_RICH_GAS_GIANT, HIGH_METAL_CONTENT_BODY, ICY_BODY, METAL_RICH_BODY, ROCKY_BODY, ROCKY_ICY_BODY, SUDARSKY_CLASS_I_GAS_GIANT, SUDARSKY_CLASS_II_GAS_GIANT, SUDARSKY_CLASS_III_GAS_GIANT, SUDARSKY_CLASS_IV_GAS_GIANT, SUDARSKY_CLASS_V_GAS_GIANT, WATER_GIANT, WATER_WORLD;

	public static PlanetClass fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "Ammonia world":
				return PlanetClass.AMMONIA_WORLD;
			case "Earthlike body":
				return PlanetClass.EARTHLIKE_BODY;
			case "Gas giant with ammonia based life":
				return PlanetClass.GAS_GIANT_WITH_AMMONIA_BASED_LIFE;
			case "Gas giant with water based life":
				return PlanetClass.GAS_GIANT_WITH_WATER_BASED_LIFE;
			case "Helium rich gas giant":
				return PlanetClass.HELIUM_RICH_GAS_GIANT;
			case "High metal content body":
				return PlanetClass.HIGH_METAL_CONTENT_BODY;
			case "Icy body":
				return PlanetClass.ICY_BODY;
			case "Metal rich body":
				return PlanetClass.METAL_RICH_BODY;
			case "Rocky body":
				return PlanetClass.ROCKY_BODY;
			case "Rocky ice body":
				return PlanetClass.ROCKY_ICY_BODY;
			case "Sudarsky class I gas giant":
				return PlanetClass.SUDARSKY_CLASS_I_GAS_GIANT;
			case "Sudarsky class II gas giant":
				return PlanetClass.SUDARSKY_CLASS_II_GAS_GIANT;
			case "Sudarsky class III gas giant":
				return PlanetClass.SUDARSKY_CLASS_III_GAS_GIANT;
			case "Sudarsky class IV gas giant":
				return PlanetClass.SUDARSKY_CLASS_IV_GAS_GIANT;
			case "Sudarsky class V gas giant":
				return PlanetClass.SUDARSKY_CLASS_V_GAS_GIANT;
			case "Water giant":
				return PlanetClass.WATER_GIANT;
			case "Water world":
				return PlanetClass.WATER_WORLD;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for PlanetClass");
			}
		}
	}

}
