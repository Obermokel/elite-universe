package borg.ed.galaxy.constants;

import org.apache.commons.lang.StringUtils;

/**
 * PlanetClass
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum PlanetClass {

	AMMONIA_WORLD("AW"),
	EARTHLIKE_BODY("ELW"),
	GAS_GIANT_WITH_AMMONIA_BASED_LIFE("GG-AL"),
	GAS_GIANT_WITH_WATER_BASED_LIFE("GG-WL"),
	HELIUM_GAS_GIANT("GG-H"),
	HELIUM_RICH_GAS_GIANT("GG-HR"),
	HIGH_METAL_CONTENT_BODY("HMC"),
	ICY_BODY("Icy"),
	METAL_RICH_BODY("MR"),
	ROCKY_BODY("Rocky"),
	ROCKY_ICY_BODY("Ricer"),
	SUDARSKY_CLASS_I_GAS_GIANT("GG-I"),
	SUDARSKY_CLASS_II_GAS_GIANT("GG-II"),
	SUDARSKY_CLASS_III_GAS_GIANT("GG-III"),
	SUDARSKY_CLASS_IV_GAS_GIANT("GG-IV"),
	SUDARSKY_CLASS_V_GAS_GIANT("GG-V"),
	WATER_GIANT("WG"),
	WATER_WORLD("WW");

	private final String shortName;

	private PlanetClass(String shortName) {
		this.shortName = shortName;
	}

	public static PlanetClass fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "Ammonia world":
				return PlanetClass.AMMONIA_WORLD;
			case "Earthlike body":
			case "Earth-like world":
				return PlanetClass.EARTHLIKE_BODY;
			case "Gas giant with ammonia based life":
			case "Gas giant with ammonia-based life":
				return PlanetClass.GAS_GIANT_WITH_AMMONIA_BASED_LIFE;
			case "Gas giant with water based life":
			case "Gas giant with water-based life":
				return PlanetClass.GAS_GIANT_WITH_WATER_BASED_LIFE;
			case "Helium gas giant":
				return PlanetClass.HELIUM_GAS_GIANT;
			case "Helium rich gas giant":
			case "Helium-rich gas giant":
				return PlanetClass.HELIUM_RICH_GAS_GIANT;
			case "High metal content body":
			case "High metal content world":
				return PlanetClass.HIGH_METAL_CONTENT_BODY;
			case "Icy body":
				return PlanetClass.ICY_BODY;
			case "Metal rich body":
			case "Metal-rich body":
				return PlanetClass.METAL_RICH_BODY;
			case "Rocky body":
				return PlanetClass.ROCKY_BODY;
			case "Rocky ice body":
			case "Rocky ice world":
			case "Rocky Ice world":
				return PlanetClass.ROCKY_ICY_BODY;
			case "Sudarsky class I gas giant":
			case "Class I gas giant":
				return PlanetClass.SUDARSKY_CLASS_I_GAS_GIANT;
			case "Sudarsky class II gas giant":
			case "Class II gas giant":
				return PlanetClass.SUDARSKY_CLASS_II_GAS_GIANT;
			case "Sudarsky class III gas giant":
			case "Class III gas giant":
				return PlanetClass.SUDARSKY_CLASS_III_GAS_GIANT;
			case "Sudarsky class IV gas giant":
			case "Class IV gas giant":
				return PlanetClass.SUDARSKY_CLASS_IV_GAS_GIANT;
			case "Sudarsky class V gas giant":
			case "Class V gas giant":
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

	public String getShortName() {
		return shortName;
	}

}
