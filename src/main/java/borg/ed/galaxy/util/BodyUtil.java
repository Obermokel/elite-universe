package borg.ed.galaxy.util;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.galaxy.constants.PlanetClass;
import borg.ed.galaxy.constants.StarClass;
import borg.ed.galaxy.constants.TerraformingState;
import borg.ed.galaxy.journal.events.ScanEvent;
import borg.ed.galaxy.model.Body;

/**
 * BodyUtil
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public abstract class BodyUtil {

	static final Logger logger = LoggerFactory.getLogger(BodyUtil.class);

	public static final BigDecimal GRAVITATIONAL_CONSTANT = new BigDecimal("0.0000000000667408"); // m³ / (kg * s²)
	public static final BigDecimal EARTH_RADIUS_KM = new BigDecimal("6378"); // km
	public static final BigDecimal EARTH_MASS_KG = new BigDecimal("5974000000000000000000000"); // kg
	public static final BigDecimal EARTH_DENSITY_G_CM3 = new BigDecimal("5.515"); // g/cm³
	public static final BigDecimal EARTH_GRAVITY_M_S2 = new BigDecimal("9.81"); // m/s²

	public static final BigDecimal FACTOR_KM_TO_M = new BigDecimal(1000); // km -> m
	public static final BigDecimal FACTOR_KG_TO_G = new BigDecimal(1000); // kg -> g
	public static final BigDecimal FACTOR_KM3_TO_CM3 = new BigDecimal(100000).pow(3); // km³ -> cm³

	public static BigDecimal calculateGravityG(BigDecimal earthMasses, BigDecimal radiusKm) {
		BigDecimal massKg = earthMasses.multiply(EARTH_MASS_KG);
		BigDecimal gravityM_s2 = (GRAVITATIONAL_CONSTANT.multiply(massKg)).divide(radiusKm.multiply(FACTOR_KM_TO_M).pow(2), 2, BigDecimal.ROUND_HALF_UP); // g = (G * m) / r²
		return gravityM_s2.divide(EARTH_GRAVITY_M_S2, 2, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal calculateDensityG_cm3(BigDecimal earthMasses, BigDecimal radiusKm) {
		BigDecimal massG = earthMasses.multiply(EARTH_MASS_KG).multiply(FACTOR_KG_TO_G);
		BigDecimal volumeKm3 = BigDecimal.valueOf(4.0 / 3.0).multiply(BigDecimal.valueOf(Math.PI)).multiply(radiusKm.pow(3)); // V = 4/3 * PI * r^3
		return massG.divide(volumeKm3.multiply(FACTOR_KM3_TO_CM3), 3, BigDecimal.ROUND_HALF_UP); // d = m / V
	}

	public static String getAbbreviatedType(Body b) {
		return BodyUtil.getAbbreviatedType(b.getStarClass(), b.getPlanetClass(), TerraformingState.TERRAFORMABLE.equals(b.getTerraformingState()));
	}

	public static String getAbbreviatedType(ScanEvent e) {
		StarClass starClass = StarClass.fromJournalValue(e.getStarType());
		PlanetClass planetClass = PlanetClass.fromJournalValue(e.getPlanetClass());
		TerraformingState terraformingState = TerraformingState.fromJournalValue(e.getTerraformState());

		return BodyUtil.getAbbreviatedType(starClass, planetClass, TerraformingState.TERRAFORMABLE.equals(terraformingState));
	}

	public static String getAbbreviatedType(StarClass starClass, PlanetClass planetClass, boolean terraformingCandidate) {
		if (starClass != null) {
			return starClass.getShortName();
		} else if (planetClass != null) {
			return planetClass.getShortName() + (terraformingCandidate ? "-TF" : "");
		} else {
			return "UNKNOWN";
		}
	}

	public static long estimatePayout(Body b) {
		return BodyUtil.estimatePayout(b.getStarClass(), b.getPlanetClass(), TerraformingState.TERRAFORMABLE.equals(b.getTerraformingState()));
	}

	public static long estimatePayout(ScanEvent e) {
		StarClass starClass = StarClass.fromJournalValue(e.getStarType());
		PlanetClass planetClass = PlanetClass.fromJournalValue(e.getPlanetClass());
		TerraformingState terraformingState = TerraformingState.fromJournalValue(e.getTerraformState());

		return BodyUtil.estimatePayout(starClass, planetClass, TerraformingState.TERRAFORMABLE.equals(terraformingState));
	}

	public static long estimatePayout(StarClass starClass, PlanetClass planetClass, boolean terraformingCandidate) {
		if (starClass != null) {
			switch (starClass) {
			case H:
				return 60000;
			case N:
				return 55000;
			case D:
			case DA:
			case DAB:
			case DAV:
			case DAZ:
			case DB:
			case DC:
			case DCV:
			case DQ:
				return 35000;
			default:
				return 3000;
			}
		} else if (planetClass != null) {
			switch (planetClass) {
			case EARTHLIKE_BODY:
				return 650000;
			case WATER_WORLD:
				return terraformingCandidate ? 600000 : 250000;
			case HIGH_METAL_CONTENT_BODY:
				return terraformingCandidate ? 375000 : 34000;
			case ROCKY_BODY:
				return terraformingCandidate ? 180000 : 1000;
			case AMMONIA_WORLD:
				return 320000;
			case METAL_RICH_BODY:
				return 65000;
			case SUDARSKY_CLASS_II_GAS_GIANT:
				return 53000;
			default:
				return 500;
			}
		} else {
			return 0;
		}
	}

}
