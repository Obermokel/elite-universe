package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * StarClass
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum StarClass {

    O,
    B,
    A,
    F,
    G,
    K,
    M,
    //A_BLUE_WHITE_SUPER_GIANT,
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
    //F_WHITE_SUPER_GIANT,
    H,
    //K_ORANGE_GIANT,
    L,
    //M_RED_GIANT,
    //M_RED_SUPER_GIANT,
    MS,
    N,
    //SUPER_MASSIVE_BLACK_HOLE,
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
                    return StarClass.A;
                case "AeBe":
                case "AEBE":
                    return StarClass.AEBE;
                case "C":
                    return StarClass.C;
                case "CJ":
                    return StarClass.CJ;
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
                case "DBZ":
                    return StarClass.DBZ;
                case "DC":
                    return StarClass.DC;
                case "DCV":
                    return StarClass.DCV;
                case "DQ":
                    return StarClass.DQ;
                case "DX":
                    return StarClass.DX;
                case "F_WhiteSuperGiant":
                    return StarClass.F;
                case "H":
                    return StarClass.H;
                case "K_OrangeGiant":
                    return StarClass.K;
                case "L":
                    return StarClass.L;
                case "M_RedGiant":
                    return StarClass.M;
                case "M_RedSuperGiant":
                    return StarClass.M;
                case "MS":
                    return StarClass.MS;
                case "N":
                    return StarClass.N;
                case "S":
                    return StarClass.S;
                case "SupermassiveBlackHole":
                    return StarClass.H;
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

    public String getShortName() {
        return shortName;
    }

}
