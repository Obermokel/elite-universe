package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * StarClass
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum StarClass {

    O, B, A, F, G, K, M, A_BLUE_WHITE_SUPER_GIANT, AeBe, C, CN, D, DA, DAB, DAV, DAZ, DB, DBV, DC, DCV, DQ, DX, F_WHITE_SUPER_GIANT, H, K_ORANGE_GIANT, L, M_RED_GIANT, M_RED_SUPER_GIANT, MS, N, SUPER_MASSIVE_BLACK_HOLE, S, T, TTS, W, WC, WN, WNC, WO, Y;

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
                case "AEBE":
				return StarClass.AeBe;
			case "C":
				return StarClass.C;
			case "CN":
				return StarClass.CN;
			case "D":
				return StarClass.D;
			case "DA":
				return StarClass.DA;
			case "DAB":
				return StarClass.DAB;
			case "DAV":
				return StarClass.DAV;
			case "DAZ":
				return StarClass.DAZ;
			case "DB":
				return StarClass.DB;
                case "DBV":
                    return StarClass.DBV;
			case "DC":
				return StarClass.DC;
			case "DCV":
				return StarClass.DCV;
			case "DQ":
				return StarClass.DQ;
                case "DX":
                    return StarClass.DX;
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
			case "MS":
				return StarClass.MS;
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
			case "W":
				return StarClass.W;
			case "WC":
				return StarClass.WC;
			case "WN":
				return StarClass.WN;
			case "WNC":
				return StarClass.WNC;
			case "WO":
				return StarClass.WO;
			case "Y":
				return StarClass.Y;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for StarClass");
			}
		}
	}

}
