package borg.ed.universe.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import borg.ed.universe.constants.Allegiance;
import borg.ed.universe.constants.AtmosphereType;
import borg.ed.universe.constants.BodyAtmosphere;
import borg.ed.universe.constants.Economy;
import borg.ed.universe.constants.Element;
import borg.ed.universe.constants.Government;
import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.Power;
import borg.ed.universe.constants.PowerState;
import borg.ed.universe.constants.ReserveLevel;
import borg.ed.universe.constants.RingClass;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.constants.State;
import borg.ed.universe.constants.SystemSecurity;
import borg.ed.universe.constants.TerraformingState;
import borg.ed.universe.constants.VolcanismType;
import borg.ed.universe.data.Coord;
import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.Body.AtmosphereShare;
import borg.ed.universe.model.Body.MaterialShare;
import borg.ed.universe.model.Body.Ring;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;
import borg.ed.universe.model.StarSystem.FactionPresence;
import borg.ed.universe.service.UniverseService;
import borg.ed.universe.util.MiscUtil;

/**
 * JournalConverter
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Service
public class JournalConverter {

	static final Logger logger = LoggerFactory.getLogger(JournalConverter.class);

	@Autowired
	private UniverseService universeService = null;

	@SuppressWarnings("unchecked")
	public StarSystem fsdJumpToStarSystem(Map<String, Object> journalData) {
		StarSystem result = new StarSystem();

		result.setId(null);
		result.setEddbId(null);
		result.setEdsmId(null);
		result.setCreatedAt(null);
		result.setUpdatedAt(null);
		result.setCoord(this.starPosToCoord((List<Number>) journalData.get("StarPos")));
		result.setName(MiscUtil.getAsString(journalData.get("StarSystem")));
		result.setPopulation(MiscUtil.getAsBigDecimal(journalData.get("Population")));
		result.setGovernment(Government.fromJournalValue(MiscUtil.getAsString(journalData.get("SystemGovernment"))));
		result.setAllegiance(Allegiance.fromJournalValue(MiscUtil.getAsString(journalData.get("SystemAllegiance"))));
		result.setSecurity(SystemSecurity.fromJournalValue(MiscUtil.getAsString(journalData.get("SystemSecurity"))));
		result.setEconomy(Economy.fromJournalValue(MiscUtil.getAsString(journalData.get("SystemEconomy"))));
		result.setPowers(Power.fromJournalValue((List<String>) (journalData.get("Powers"))));
		result.setPowerState(PowerState.fromJournalValue(MiscUtil.getAsString(journalData.get("PowerplayState"))));
		result.setNeedsPermit(null); // Manually edited
		result.setControllingMinorFactionName(MiscUtil.getAsString(journalData.get("SystemFaction")));
		result.setMinorFactionPresences(this.factionsToFactionPresences((List<Map<String, Object>>) journalData.get("Factions")));
		result.setState(this.lookupState(result.getMinorFactionPresences(), result.getControllingMinorFactionName()));

		return result;
	}

	@SuppressWarnings("unchecked")
	private List<FactionPresence> factionsToFactionPresences(List<Map<String, Object>> factions) {
		if (factions == null || factions.isEmpty()) {
			return null;
		} else {
			List<FactionPresence> result = new ArrayList<>(factions.size());
			for (Map<String, Object> factionData : factions) {
				FactionPresence factionPresence = new FactionPresence();
				factionPresence.setName(MiscUtil.getAsString(factionData.get("Name")));
				if (factionData.get("RecoveringStates") == null) {
					factionPresence.setRecoveringStates(null);
				} else {
					List<Map<String, Object>> statesData = (List<Map<String, Object>>) factionData.get("RecoveringStates");
					List<State> recoveringStates = new ArrayList<>(statesData.size());
					for (Map<String, Object> stateData : statesData) {
						recoveringStates.add(State.fromJournalValue(MiscUtil.getAsString(stateData.get("State"))));
					}
					factionPresence.setRecoveringStates(recoveringStates);
				}
				factionPresence.setState(State.fromJournalValue(MiscUtil.getAsString(factionData.get("FactionState"))));
				if (factionData.get("PendingStates") == null) {
					factionPresence.setPendingStates(null);
				} else {
					List<Map<String, Object>> statesData = (List<Map<String, Object>>) factionData.get("PendingStates");
					List<State> pendingStates = new ArrayList<>(statesData.size());
					for (Map<String, Object> stateData : statesData) {
						pendingStates.add(State.fromJournalValue(MiscUtil.getAsString(stateData.get("State"))));
					}
					factionPresence.setPendingStates(pendingStates);
				}
				factionPresence.setInfluence(MiscUtil.getAsBigDecimal(factionData.get("Influence")));
				result.add(factionPresence);
			}
			return result;
		}
	}

	private State lookupState(List<FactionPresence> minorFactionPresences, String controllingMinorFactionName) {
		if (minorFactionPresences != null && controllingMinorFactionName != null) {
			for (FactionPresence factionPresence : minorFactionPresences) {
				if (controllingMinorFactionName.equals(factionPresence.getName())) {
					return factionPresence.getState();
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MinorFaction> fsdJumpToMinorFactions(Map<String, Object> journalData) {
		List<Map<String, Object>> factions = (List<Map<String, Object>>) journalData.get("Factions");

		if (factions == null || factions.isEmpty()) {
			return null;
		} else {
			List<MinorFaction> result = new ArrayList<>(factions.size());
			for (Map<String, Object> factionData : factions) {
				MinorFaction minorFaction = new MinorFaction();

				minorFaction.setId(null);
				minorFaction.setEddbId(null);
				minorFaction.setEdsmId(null);
				minorFaction.setCreatedAt(null);
				minorFaction.setUpdatedAt(null);
				minorFaction.setHomeSystemId(null); // Manually edited
				minorFaction.setCoord(null); // Manually edited
				minorFaction.setName(MiscUtil.getAsString(factionData.get("Name")));
				minorFaction.setGovernment(Government.fromJournalValue(MiscUtil.getAsString(journalData.get("Government"))));
				minorFaction.setAllegiance(Allegiance.fromJournalValue(MiscUtil.getAsString(journalData.get("Allegiance"))));
				minorFaction.setState(State.fromJournalValue(MiscUtil.getAsString(journalData.get("FactionState"))));
				minorFaction.setIsPlayerFaction(null); // Manually edited

				result.add(minorFaction);
			}
			return result;
		}
	}

	private Coord starPosToCoord(List<Number> xyz) {
		return xyz == null ? null : new Coord(xyz.get(0).floatValue(), xyz.get(1).floatValue(), xyz.get(2).floatValue());
	}

	@SuppressWarnings("unchecked")
	public Body scanToBody(Map<String, Object> journalData) {
		Body result = new Body();

		String starSystemName = MiscUtil.getAsString(journalData.get("StarSystem"));
		if (StringUtils.isNotEmpty(starSystemName)) {
			try {
				StarSystem starSystem = this.universeService.findStarSystemByName(starSystemName);
				if (starSystem != null) {
					result.setStarSystemId(starSystem.getId());
					result.setStarSystemName(starSystem.getName());
				}
			} catch (NonUniqueResultException e) {
				logger.warn("NonUniqueResultException: " + e.getMessage() + "\n\t" + e.getOthers());
			}
		}

		result.setId(null);
		result.setEddbId(null);
		result.setEdsmId(null);
		result.setCreatedAt(null);
		result.setUpdatedAt(null);
		result.setCoord(this.starPosToCoord((List<Number>) journalData.remove("StarPos")));
		result.setName(MiscUtil.getAsString(journalData.remove("BodyName")));
		result.setDistanceToArrival(MiscUtil.getAsBigDecimal(journalData.remove("DistanceFromArrivalLS")));
		result.setStarClass(StarClass.fromJournalValue(MiscUtil.getAsString(journalData.remove("StarType"))));
		result.setPlanetClass(PlanetClass.fromJournalValue(MiscUtil.getAsString(journalData.remove("PlanetClass"))));
		result.setSurfaceTemperature(MiscUtil.getAsBigDecimal(journalData.remove("SurfaceTemperature")));
		result.setAge(MiscUtil.getAsBigDecimal(journalData.remove("Age_MY")));
		result.setSolarMasses(MiscUtil.getAsBigDecimal(journalData.remove("StellarMass"))); // TODO Convert
		result.setVolcanismType(VolcanismType.fromJournalValue(MiscUtil.getAsString(journalData.remove("Volcanism"))));
		result.setAtmosphereType(AtmosphereType.fromJournalValue(MiscUtil.getAsString(journalData.remove("Atmosphere"))));
		result.setTerraformingState(TerraformingState.fromJournalValue(MiscUtil.getAsString(journalData.remove("TerraformState"))));
		result.setEarthMasses(MiscUtil.getAsBigDecimal(journalData.remove("MassEM"))); // TODO Convert
		result.setRadius(MiscUtil.getAsBigDecimal(journalData.remove("Radius"))); // TODO Unit
		result.setGravity(MiscUtil.getAsBigDecimal(journalData.remove("SurfaceGravity")));
		result.setSurfacePressure(MiscUtil.getAsBigDecimal(journalData.remove("SurfacePressure")));
		result.setOrbitalPeriod(MiscUtil.getAsBigDecimal(journalData.remove("OrbitalPeriod")));
		result.setSemiMajorAxis(MiscUtil.getAsBigDecimal(journalData.remove("SemiMajorAxis")));
		result.setOrbitalEccentricity(MiscUtil.getAsBigDecimal(journalData.remove("Eccentricity")));
		result.setOrbitalInclination(MiscUtil.getAsBigDecimal(journalData.remove("OrbitalInclination")));
		result.setArgOfPeriapsis(MiscUtil.getAsBigDecimal(journalData.remove("Periapsis")));
		result.setRotationalPeriod(MiscUtil.getAsBigDecimal(journalData.remove("RotationPeriod")));
		result.setTidallyLocked(MiscUtil.getAsBoolean(journalData.remove("TidalLock")));
		result.setAxisTilt(MiscUtil.getAsBigDecimal(journalData.remove("AxialTilt")));
		result.setIsLandable(MiscUtil.getAsBoolean(journalData.remove("Landable")));
		result.setReserves(ReserveLevel.fromJournalValue(MiscUtil.getAsString(journalData.remove("ReserveLevel"))));
		result.setRings(this.ringsToRings((List<Map<String, Object>>) journalData.remove("Rings")));
		result.setAtmosphereShares(this.atmosphereCompositionToAtmosphereShares((List<Map<String, Object>>) journalData.remove("AtmosphereComposition")));
		result.setMaterialShares(this.materialsToMaterialShares((List<Map<String, Object>>) journalData.remove("Materials")));
		//result.setCompositionShares(this.materialsToMaterialShares((List<Map<String, Object>>) journalData.remove("Materials")));

		journalData.remove("timestamp");
		journalData.remove("event");
		journalData.remove("StarSystem");
		journalData.remove("AbsoluteMagnitude");
		journalData.remove("Luminosity");
		journalData.remove("AtmosphereType");
		if (!journalData.isEmpty())
			logger.info("Scan remaining: " + journalData);

		return result;
	}

	private List<Ring> ringsToRings(List<Map<String, Object>> rings) {
		if (rings == null || rings.isEmpty()) {
			return null;
		} else {
			List<Ring> result = new ArrayList<>(rings.size());
			for (Map<String, Object> ringData : rings) {
				Ring ring = new Ring();
				ring.setName(MiscUtil.getAsString(ringData.get("Name")));
				ring.setRingClass(RingClass.fromJournalValue(MiscUtil.getAsString(ringData.get("RingClass"))));
				ring.setMassMT(MiscUtil.getAsBigDecimal(ringData.get("MassMT")));
				ring.setInnerRadius(MiscUtil.getAsBigDecimal(ringData.get("InnerRad")));
				ring.setOuterRadius(MiscUtil.getAsBigDecimal(ringData.get("OuterRad")));
				result.add(ring);
			}
			return result;
		}
	}

	private List<AtmosphereShare> atmosphereCompositionToAtmosphereShares(List<Map<String, Object>> dataList) {
		if (dataList == null || dataList.isEmpty()) {
			return null;
		} else {
			List<AtmosphereShare> result = new ArrayList<>(dataList.size());
			for (Map<String, Object> data : dataList) {
				AtmosphereShare ring = new AtmosphereShare();
				ring.setName(BodyAtmosphere.fromJournalValue(MiscUtil.getAsString(data.get("Name"))));
				ring.setPercent(MiscUtil.getAsBigDecimal(data.get("Percent")));
				result.add(ring);
			}
			return result;
		}
	}

	private List<MaterialShare> materialsToMaterialShares(List<Map<String, Object>> dataList) {
		if (dataList == null || dataList.isEmpty()) {
			return null;
		} else {
			List<MaterialShare> result = new ArrayList<>(dataList.size());
			for (Map<String, Object> data : dataList) {
				MaterialShare ring = new MaterialShare();
				ring.setName(Element.fromJournalValue(MiscUtil.getAsString(data.get("Name"))));
				ring.setPercent(MiscUtil.getAsBigDecimal(data.get("Percent")));
				result.add(ring);
			}
			return result;
		}
	}

}
