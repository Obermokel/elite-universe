package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * BodyComposition
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum BodyComposition {

	ICE, METAL, ROCK;

	public static BodyComposition fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "Ice":
				return BodyComposition.ICE;
			case "Metal":
				return BodyComposition.METAL;
			case "Rock":
				return BodyComposition.ROCK;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for BodyComposition");
			}
		}
	}

}
