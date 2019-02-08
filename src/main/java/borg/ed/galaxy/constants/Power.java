package borg.ed.galaxy.constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Power
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum Power {

	A_LAVIGNY_DUVAL, AISLING_DUVAL, ARCHON_DELAINE, DENTON_PATREUS, EDMUND_MAHON, FELICIA_WINTERS, LI_YONG_RUI, PRANAV_ANTAL, YURI_GROM, ZACHARAY_HUDSON, ZEMINA_TORVAL;

	public static List<Power> fromJournalValue(Collection<String> values) {
		if (values == null || values.isEmpty()) {
			return null;
		} else {
			List<Power> result = new ArrayList<>();
			for (String value : values) {
				switch (value) {
				case "A. Lavigny-Duval":
					result.add(Power.A_LAVIGNY_DUVAL);
					break;
				case "Aisling Duval":
					result.add(Power.AISLING_DUVAL);
					break;
				case "Archon Delaine":
					result.add(Power.ARCHON_DELAINE);
					break;
				case "Denton Patreus":
					result.add(Power.DENTON_PATREUS);
					break;
				case "Edmund Mahon":
					result.add(EDMUND_MAHON);
					break;
				case "Felicia Winters":
					result.add(Power.FELICIA_WINTERS);
					break;
				case "Li Yong-Rui":
					result.add(Power.LI_YONG_RUI);
					break;
				case "Pranav Antal":
					result.add(Power.PRANAV_ANTAL);
					break;
				case "Yuri Grom":
					result.add(YURI_GROM);
					break;
				case "Zachary Hudson":
					result.add(ZACHARAY_HUDSON);
					break;
				case "Zemina Torval":
					result.add(ZEMINA_TORVAL);
					break;
				default:
					throw new IllegalArgumentException("Unknown value '" + value + "' for Power");
				}
			}
			return result;
		}
	}

}
