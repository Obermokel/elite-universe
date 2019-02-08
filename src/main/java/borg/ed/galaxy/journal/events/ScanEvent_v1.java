package borg.ed.galaxy.journal.events;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.galaxy.data.Coord;
import borg.ed.galaxy.journal.events.ScanEvent.Share;
import borg.ed.galaxy.util.MiscUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * ScanEventOld
 * 
 * @deprecated
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Deprecated
public class ScanEvent_v1 extends AbstractJournalEvent {

	private static final long serialVersionUID = 6435327132176032477L;

	static final Logger logger = LoggerFactory.getLogger(ScanEvent_v1.class);

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

	private Map<String, Number> Materials = null;

	public ScanEvent toNewScanEvent() {
		ScanEvent result = new ScanEvent();
		result.setTimestamp(this.getTimestamp());
		result.setEvent(this.getEvent());
		result.setStarSystem(this.getStarSystem());
		result.setStarPos(this.getStarPos());
		result.setBodyName(this.getBodyName());
		result.setDistanceFromArrivalLS(this.getDistanceFromArrivalLS());
		result.setStarType(this.getStarType());
		result.setPlanetClass(this.getPlanetClass());
		result.setSurfaceTemperature(this.getSurfaceTemperature());
		result.setAge_MY(this.getAge_MY());
		result.setStellarMass(this.getStellarMass());
		result.setLuminosity(this.getLuminosity());
		result.setAbsoluteMagnitude(this.getAbsoluteMagnitude());
		result.setVolcanism(this.getVolcanism());
		result.setAtmosphere(this.getAtmosphere());
		result.setTerraformState(this.getTerraformState());
		result.setMassEM(this.getMassEM());
		result.setRadius(this.getRadius());
		result.setSurfaceGravity(this.getSurfaceGravity());
		result.setSurfacePressure(this.getSurfacePressure());
		result.setOrbitalPeriod(this.getOrbitalPeriod());
		result.setSemiMajorAxis(this.getSemiMajorAxis());
		result.setEccentricity(this.getEccentricity());
		result.setOrbitalInclination(this.getOrbitalInclination());
		result.setPeriapsis(this.getPeriapsis());
		result.setRotationPeriod(this.getRotationPeriod());
		result.setTidalLock(this.getTidalLock());
		result.setAxialTilt(this.getAxialTilt());
		result.setLandable(this.getLandable());
		result.setReserveLevel(this.getReserveLevel());
		result.setRings(this.getRings());
		result.setAtmosphereComposition(this.getAtmosphereComposition());
		result.setMaterials(this.toNewMaterials(this.getMaterials()));
		return result;
	}

	private List<Share> toNewMaterials(Map<String, Number> oldMaterials) {
		if (oldMaterials == null || oldMaterials.isEmpty()) {
			return null;
		} else {
			List<Share> result = new ArrayList<>(oldMaterials.size());
			for (String oldMaterial : oldMaterials.keySet()) {
				Share share = new Share();
				share.setName(oldMaterial);
				share.setPercent(MiscUtil.getAsBigDecimal(oldMaterials.get(oldMaterial)));
				result.add(share);
			}
			return result;
		}
	}

}
