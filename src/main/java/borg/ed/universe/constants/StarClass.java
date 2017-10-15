package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * StarClass
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum StarClass {

	O, B, A, F, G, K, M, A_BLUE_WHITE_SUPER_GIANT, AeBe, DA, DAB, DAZ, DB, DC, DCV, DQ, F_WHITE_SUPER_GIANT, H, K_ORANGE_GIANT, L, M_RED_GIANT, M_RED_SUPER_GIANT, N, SUPER_MASSIVE_BLACK_HOLE, S, T, TTS, WN, WNC, Y;

	public static StarClass fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "O":
				return StarClass.O;
			case "B":
				return StarClass.B;
			case "A":
				return StarClass.A;
			case "F":
				return StarClass.F;
			case "G":
				return StarClass.G;
			case "K":
				return StarClass.K;
			case "M":
				return StarClass.M;
			case "A_BlueWhiteSuperGiant":
				return StarClass.A_BLUE_WHITE_SUPER_GIANT;
			case "AeBe":
				return StarClass.AeBe;
			case "DA":
				return StarClass.DA;
			case "DAB":
				return StarClass.DAB;
			case "DAZ":
				return StarClass.DAZ;
			case "DB":
				return StarClass.DB;
			case "DC":
				return StarClass.DC;
			case "DCV":
				return StarClass.DCV;
			case "DQ":
				return StarClass.DQ;
			case "F_WhiteSuperGiant":
				return StarClass.F_WHITE_SUPER_GIANT;
			case "H":
				return StarClass.H;
			case "K_OrangeGiant":
				return StarClass.K_ORANGE_GIANT;
			case "L":
				return StarClass.L;
			case "M_RedGiant":
				return StarClass.M_RED_GIANT;
			case "M_RedSuperGiant":
				return StarClass.M_RED_SUPER_GIANT;
			case "N":
				return StarClass.N;
			case "S":
				return StarClass.S;
			case "SupermassiveBlackHole":
				return StarClass.SUPER_MASSIVE_BLACK_HOLE;
			case "T":
				return StarClass.T;
			case "TTS":
				return StarClass.TTS;
			case "WN":
				return StarClass.WN;
			case "WNC":
				return StarClass.WNC;
			case "Y":
				return StarClass.Y;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for StarClass");
			}
		}
	}

}
