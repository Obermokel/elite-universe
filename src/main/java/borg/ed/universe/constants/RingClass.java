package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * RingClass (such as metallic, rocky)
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum RingClass {

	ICY, METAL_RICH, METALLIC, ROCKY;

	public static RingClass fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "eRingClass_Icy":
				return RingClass.ICY;
			case "eRingClass_MetalRich":
				return RingClass.METAL_RICH;
			case "eRingClass_Metalic":
				return RingClass.METALLIC;
			case "eRingClass_Rocky":
				return RingClass.ROCKY;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for RingClass");
			}
		}
	}

}
