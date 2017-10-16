package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * BodyAtmosphere
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum BodyAtmosphere {

	AMMONIA, ARGON, CARBON_DIOXIDE, HELIUM, HYDROGEN, IRON, METHANE, NEON, NITROGEN, OXYGEN, SILICATES, SULPHUR_DIOXIDE, WATER;

	public static BodyAtmosphere fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "Ammonia":
				return BodyAtmosphere.AMMONIA;
			case "Argon":
				return BodyAtmosphere.ARGON;
			case "CarbonDioxide":
				return BodyAtmosphere.CARBON_DIOXIDE;
			case "Helium":
				return BodyAtmosphere.HELIUM;
			case "Hydrogen":
				return BodyAtmosphere.HYDROGEN;
			case "Iron":
				return BodyAtmosphere.IRON;
			case "Methane":
				return BodyAtmosphere.METHANE;
			case "Neon":
				return BodyAtmosphere.NEON;
			case "Nitrogen":
				return BodyAtmosphere.NITROGEN;
			case "Oxygen":
				return BodyAtmosphere.OXYGEN;
			case "Silicates":
				return BodyAtmosphere.SILICATES;
			case "SulphurDioxide":
				return BodyAtmosphere.SULPHUR_DIOXIDE;
			case "Water":
				return BodyAtmosphere.WATER;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for BodyAtmosphere");
			}
		}
	}

}
