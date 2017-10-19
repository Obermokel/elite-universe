package borg.ed.universe.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.constants.TerraformingState;
import borg.ed.universe.model.Body;

/**
 * BodyUtil
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public abstract class BodyUtil {

	static final Logger logger = LoggerFactory.getLogger(BodyUtil.class);

	public static long estimatePayout(Body body) {
		return BodyUtil.estimatePayout(body.getStarClass(), body.getPlanetClass(), TerraformingState.TERRAFORMABLE.equals(body.getTerraformingState()));
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
				return 630000;
			case WATER_WORLD:
				return terraformingCandidate ? 700000 : 300000;
			case HIGH_METAL_CONTENT_BODY:
				return terraformingCandidate ? 410000 : 34000;
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
