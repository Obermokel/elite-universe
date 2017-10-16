package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * Element (such as iron, nickel)
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum Element {

	ANTIMONY, ARSENIC, CADMIUM, CARBON, CHROMIUM, GERMANIUM, IRON, MANGANESE, MERCURY, MOLYBDENUM, NICKEL, NIOBIUM, PHOSPHORUS, POLONIUM, RUTHENIUM, SELENIUM, SULPHUR, TECHNETIUM, TELLURIUM, TIN, TUNGSTEN, VANADIUM, YTTRIUM, ZINC, ZIRCONIUM;

	public static Element fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "antimony":
				return Element.ANTIMONY;
			case "arsenic":
				return Element.ARSENIC;
			case "cadmium":
				return Element.CADMIUM;
			case "carbon":
				return Element.CARBON;
			case "chromium":
				return Element.CHROMIUM;
			case "germanium":
				return Element.GERMANIUM;
			case "iron":
				return Element.IRON;
			case "manganese":
				return Element.MANGANESE;
			case "mercury":
				return Element.MERCURY;
			case "molybdenum":
				return Element.MOLYBDENUM;
			case "nickel":
				return Element.NICKEL;
			case "niobium":
				return Element.NIOBIUM;
			case "phosphorus":
				return Element.PHOSPHORUS;
			case "polonium":
				return Element.POLONIUM;
			case "ruthenium":
				return Element.RUTHENIUM;
			case "selenium":
				return Element.SELENIUM;
			case "sulphur":
				return Element.SULPHUR;
			case "technetium":
				return Element.TECHNETIUM;
			case "tellurium":
				return Element.TELLURIUM;
			case "tin":
				return Element.TIN;
			case "tungsten":
				return Element.TUNGSTEN;
			case "vanadium":
				return Element.VANADIUM;
			case "yttrium":
				return Element.YTTRIUM;
			case "zinc":
				return Element.ZINC;
			case "zirconium":
				return Element.ZIRCONIUM;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for Element");
			}
		}
	}

}
