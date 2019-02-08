package borg.ed.galaxy.journal.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.galaxy.data.Coord;
import lombok.Getter;
import lombok.Setter;

/**
 * ScanEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class ScanEvent extends AbstractJournalEvent {

	private static final long serialVersionUID = -7473062323064854951L;

	static final Logger logger = LoggerFactory.getLogger(ScanEvent.class);

	public static final String SCAN_TYPE_BASIC = "Basic";
	public static final String SCAN_TYPE_DETAILED = "Detailed";
	public static final String SCAN_TYPE_NAV_BEACON = "NavBeacon";
	public static final String SCAN_TYPE_NAV_BEACON_DETAIL = "NavBeaconDetail";
	public static final String SCAN_TYPE_AUTO_SCAN = "AutoScan";

	private String ScanType = null;

	private String StarSystem = null;

	private Coord StarPos = null;

	private String BodyName = null;

	private BigDecimal DistanceFromArrivalLS = null;

	private String StarType = null;

	private String PlanetClass = null;

	private BigDecimal SurfaceTemperature = null;

	private BigDecimal Age_MY = null;

	private BigDecimal StellarMass = null;

	private String Luminosity = null;

	private BigDecimal AbsoluteMagnitude = null;

	private String Volcanism = null;

	private String Atmosphere = null;

	private String TerraformState = null;

	private BigDecimal MassEM = null;

	private BigDecimal Radius = null;

	private BigDecimal SurfaceGravity = null;

	private BigDecimal SurfacePressure = null;

	private BigDecimal OrbitalPeriod = null;

	private BigDecimal SemiMajorAxis = null;

	private BigDecimal Eccentricity = null;

	private BigDecimal OrbitalInclination = null;

	private BigDecimal Periapsis = null;

	private BigDecimal RotationPeriod = null;

	private Boolean TidalLock = null;

	private BigDecimal AxialTilt = null;

	private Boolean Landable = null;

	private String ReserveLevel = null;

	private List<ScanEvent.Ring> Rings = null;

	private List<ScanEvent.Share> AtmosphereComposition = null;

	private List<ScanEvent.Share> Materials = null;

	@Getter
	@Setter
	public static class Ring implements Serializable {

		private static final long serialVersionUID = 6416215918361818244L;

		private String Name = null;

		private String RingClass = null;

		private BigDecimal MassMT = null;

		private BigDecimal InnerRad = null;

		private BigDecimal OuterRad = null;

	}

	@Getter
	@Setter
	public static class Share implements Serializable {

		private static final long serialVersionUID = -6615336876011953688L;

		private String Name = null;

		private BigDecimal Percent = null;

	}

	@Override
	public String toString() {
		if (StringUtils.isNotEmpty(this.StarType)) {
			return super.toString() + " (Body: " + this.BodyName + " | " + this.StarType + ")";
		} else if (StringUtils.isNotEmpty(this.PlanetClass)) {
			return super.toString() + " (Body: " + this.BodyName + " | " + this.PlanetClass + ")";
		} else {
			return super.toString() + " (Body: " + this.BodyName + ")";
		}
	}

	public boolean isDetailedScan() {
		if (this.getScanType() == null) {
			return true; // Old event
		} else if (ScanEvent.SCAN_TYPE_DETAILED.equals(this.getScanType()) || ScanEvent.SCAN_TYPE_NAV_BEACON_DETAIL.equals(this.getScanType())) {
			return true; // Obviously detailed
		} else if (ScanEvent.SCAN_TYPE_AUTO_SCAN.equals(this.getScanType())) {
			return this.getSurfaceTemperature() != null || StringUtils.isNotEmpty(this.getStarType()); // According to the manual surface temp is only included in detailed scans
		}

		return false;
	}

}
