package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * StarClass
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum StarClass {

	O,
	O_GIANT,
	O_SUPER_GIANT,
	B,
	B_GIANT,
	B_SUPER_GIANT,
	A,
	A_GIANT,
	A_SUPER_GIANT,
	F,
	F_GIANT,
	F_SUPER_GIANT,
	G,
	G_GIANT,
	G_SUPER_GIANT,
	K,
	K_GIANT,
	K_SUPER_GIANT,
	M,
	M_GIANT,
	M_SUPER_GIANT,
	AEBE,
	C,
	CJ,
	CN,
	D,
	DA,
	DAB,
	DAV,
	DAZ,
	DB,
	DBV,
	DBZ,
	DC,
	DCV,
	DQ,
	DX,
	H,
	H_SUPER_MASSIVE,
	L,
	MS,
	N,
	S,
	T,
	TTS,
	W,
	WC,
	WN,
	WNC,
	WO,
	Y;

	private final String shortName;

	private StarClass() {
		this.shortName = this.name();
	}

	public static StarClass fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "O":
			case "O (Blue-White) Star":
				return StarClass.O;
			case "B":
			case "B (Blue-White) Star":
				return StarClass.B;
			case "A":
			case "A (Blue-White) Star":
				return StarClass.A;
			case "A_BlueWhiteSuperGiant":
			case "A (Blue-White super giant) Star":
				return StarClass.A_SUPER_GIANT;
			case "F":
			case "F (White) Star":
				return StarClass.F;
			case "F_WhiteSuperGiant":
			case "F (White super giant) Star":
				return StarClass.F_SUPER_GIANT;
			case "G":
			case "G (White-Yellow) Star":
				return StarClass.G;
			case "G (White-Yellow super giant) Star":
				return StarClass.G_SUPER_GIANT;
			case "K":
			case "K (Yellow-Orange) Star":
				return StarClass.K;
			case "K_OrangeGiant":
			case "K (Yellow-Orange giant) Star":
				return StarClass.K_GIANT;
			case "M":
			case "M (Red dwarf) Star":
				return StarClass.M;
			case "M_RedGiant":
			case "M (Red giant) Star":
				return StarClass.M_GIANT;
			case "M_RedSuperGiant":
			case "M (Red super giant) Star":
				return StarClass.M_SUPER_GIANT;
			case "AeBe":
			case "AEBE":
			case "Herbig Ae/Be Star":
				return StarClass.AEBE;
			case "C":
			case "C Star":
				return StarClass.C;
			case "CJ":
			case "CJ Star":
				return StarClass.CJ;
			case "CN":
			case "CN Star":
				return StarClass.CN;
			case "D":
			case "White Dwarf (D) Star":
				return StarClass.D;
			case "DA":
			case "White Dwarf (DA) Star":
				return StarClass.DA;
			case "DAB":
			case "White Dwarf (DAB) Star":
				return StarClass.DAB;
			case "DAV":
			case "White Dwarf (DAV) Star":
				return StarClass.DAV;
			case "DAZ":
			case "White Dwarf (DAZ) Star":
				return StarClass.DAZ;
			case "DB":
			case "White Dwarf (DB) Star":
				return StarClass.DB;
			case "DBV":
			case "White Dwarf (DBV) Star":
				return StarClass.DBV;
			case "DBZ":
			case "White Dwarf (DBZ) Star":
				return StarClass.DBZ;
			case "DC":
			case "White Dwarf (DC) Star":
				return StarClass.DC;
			case "DCV":
			case "White Dwarf (DCV) Star":
				return StarClass.DCV;
			case "DQ":
			case "White Dwarf (DQ) Star":
				return StarClass.DQ;
			case "DX":
			case "White Dwarf (DX) Star":
				return StarClass.DX;
			case "H":
			case "Black Hole":
				return StarClass.H;
			case "SupermassiveBlackHole":
			case "Supermassive Black Hole":
				return StarClass.H_SUPER_MASSIVE;
			case "L":
			case "L (Brown dwarf) Star":
				return StarClass.L;
			case "MS":
			case "MS-type Star":
				return StarClass.MS;
			case "N":
			case "Neutron Star":
				return StarClass.N;
			case "S":
			case "S-type Star":
				return StarClass.S;
			case "T":
			case "T (Brown dwarf) Star":
				return StarClass.T;
			case "TTS":
			case "T Tauri Star":
				return StarClass.TTS;
			case "W":
			case "Wolf-Rayet Star":
				return StarClass.W;
			case "WC":
			case "Wolf-Rayet C Star":
				return StarClass.WC;
			case "WN":
			case "Wolf-Rayet N Star":
				return StarClass.WN;
			case "WNC":
			case "Wolf-Rayet NC Star":
				return StarClass.WNC;
			case "WO":
			case "Wolf-Rayet O Star":
				return StarClass.WO;
			case "Y":
			case "Y (Brown dwarf) Star":
				return StarClass.Y;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for StarClass");
			}
		}
	}

	public String getShortName() {
		return shortName;
	}

	public boolean isScoopable() {
		return this == O || this == B || this == A || this == F || this == G || this == K || this == M || //
				this == O_GIANT || this == B_GIANT || this == A_GIANT || this == F_GIANT || this == G_GIANT || this == K_GIANT || this == M_GIANT || //
				this == O_SUPER_GIANT || this == B_SUPER_GIANT || this == A_SUPER_GIANT || this == F_SUPER_GIANT || this == G_SUPER_GIANT || this == K_SUPER_GIANT || this == M_SUPER_GIANT;
	}

}
