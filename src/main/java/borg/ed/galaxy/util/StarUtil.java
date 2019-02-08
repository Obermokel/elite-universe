package borg.ed.galaxy.util;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.galaxy.constants.StarClass;

/**
 * StarUtil
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public abstract class StarUtil {

	static final Logger logger = LoggerFactory.getLogger(MiscUtil.class);

	public static Color starClassToColor(StarClass starClass) {
		if (starClass == null) {
			return new Color(80, 80, 80);
		} else if (StarClass.O.equals(starClass)) {
			return new Color(207, 218, 235);
		} else if (StarClass.B.equals(starClass)) {
			return new Color(200, 219, 227);
		} else if (StarClass.A.equals(starClass)) {
			return new Color(202, 201, 205);
		} else if (StarClass.F.equals(starClass)) {
			return new Color(210, 196, 144);
		} else if (StarClass.G.equals(starClass)) {
			return new Color(233, 194, 125);
		} else if (StarClass.K.equals(starClass)) {
			return new Color(242, 152, 79);
		} else if (StarClass.M.equals(starClass)) {
			return new Color(221, 125, 61);
		} else if (StarClass.Y.equals(starClass)) {
			return new Color(58, 18, 20);
		} else if (StarClass.L.equals(starClass)) {
			return new Color(144, 14, 45);
		} else if (StarClass.T.equals(starClass)) {
			return new Color(72, 8, 41);
		} else if (StarClass.MS.equals(starClass) || StarClass.S.equals(starClass)) {
			return new Color(229, 129, 62);
		} else if (StarClass.TTS.equals(starClass)) {
			return new Color(239, 217, 90);
		} else if (StarClass.AEBE.equals(starClass)) {
			return new Color(247, 244, 101);
		} else if (StarClass.N.equals(starClass)) {
			return new Color(224, 224, 255);
		} else if (StarClass.H.equals(starClass)) {
			return new Color(0, 0, 0);
		} else if (starClass.name().startsWith("D")) {
			return new Color(224, 212, 224); // White dwarf
		} else if (starClass.name().startsWith("C")) {
			return new Color(219, 203, 26); // Carbon star
		} else if (starClass.name().startsWith("W")) {
			return new Color(166, 214, 219); // Wolf Rayet star
		} else {
			logger.warn("Unknown star class '" + starClass + "'");
			return new Color(255, 0, 255);
		}
	}

}
