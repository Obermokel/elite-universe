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
			case "Antimony":
				return Element.ANTIMONY;
			case "arsenic":
			case "Arsenic":
				return Element.ARSENIC;
			case "cadmium":
			case "Cadmium":
				return Element.CADMIUM;
			case "carbon":
			case "Carbon":
				return Element.CARBON;
			case "chromium":
			case "Chromium":
				return Element.CHROMIUM;
			case "germanium":
			case "Germanium":
				return Element.GERMANIUM;
			case "iron":
			case "Iron":
				return Element.IRON;
			case "manganese":
			case "Manganese":
				return Element.MANGANESE;
			case "mercury":
			case "Mercury":
				return Element.MERCURY;
			case "molybdenum":
			case "Molybdenum":
				return Element.MOLYBDENUM;
			case "nickel":
			case "Nickel":
				return Element.NICKEL;
			case "niobium":
			case "Niobium":
				return Element.NIOBIUM;
			case "phosphorus":
			case "Phosphorus":
				return Element.PHOSPHORUS;
			case "polonium":
			case "Polonium":
				return Element.POLONIUM;
			case "ruthenium":
			case "Ruthenium":
				return Element.RUTHENIUM;
			case "selenium":
			case "Selenium":
				return Element.SELENIUM;
			case "sulphur":
			case "Sulphur":
				return Element.SULPHUR;
			case "technetium":
			case "Technetium":
				return Element.TECHNETIUM;
			case "tellurium":
			case "Tellurium":
				return Element.TELLURIUM;
			case "tin":
			case "Tin":
				return Element.TIN;
			case "tungsten":
			case "Tungsten":
				return Element.TUNGSTEN;
			case "vanadium":
			case "Vanadium":
				return Element.VANADIUM;
			case "yttrium":
			case "Yttrium":
				return Element.YTTRIUM;
			case "zinc":
			case "Zinc":
				return Element.ZINC;
			case "zirconium":
			case "Zirconium":
				return Element.ZIRCONIUM;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for Element");
			}
		}
	}

}
