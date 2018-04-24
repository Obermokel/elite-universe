package borg.ed.universe.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.constants.TerraformingState;
import borg.ed.universe.journal.events.ScanEvent;
import borg.ed.universe.model.Body;

/**
 * BodyUtil
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public abstract class BodyUtil {

    static final Logger logger = LoggerFactory.getLogger(BodyUtil.class);

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
