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
import borg.ed.universe.constants.Economy;
import borg.ed.universe.constants.Government;
import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.Power;
import borg.ed.universe.constants.PowerState;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.constants.State;
import borg.ed.universe.constants.SystemSecurity;
import borg.ed.universe.constants.TerraformingState;
import borg.ed.universe.constants.VolcanismType;
import borg.ed.universe.data.Coord;
import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.model.Body;
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
		result.setReserves(null); // Only available in scan event
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
		result.setCoord(this.starPosToCoord((List<Number>) journalData.get("StarPos")));
		result.setName(MiscUtil.getAsString(journalData.get("StarSystem")));
		result.setDistanceToArrival(MiscUtil.getAsBigDecimal(journalData.get("DistanceFromArrivalLS")));
		result.setStarClass(StarClass.fromJournalValue(MiscUtil.getAsString(journalData.get("StarType"))));
		result.setPlanetClass(PlanetClass.fromJournalValue(MiscUtil.getAsString(journalData.get("PlanetClass"))));
		result.setSurfaceTemperature(MiscUtil.getAsBigDecimal(journalData.get("SurfaceTemperature")));
		result.setAge(MiscUtil.getAsBigDecimal(journalData.get("Age_MY")));
		result.setSolarMasses(MiscUtil.getAsBigDecimal(journalData.get("StellarMass"))); // TODO Convert
		result.setVolcanismType(VolcanismType.fromJournalValue(MiscUtil.getAsString(journalData.get("Volcanism"))));
		result.setAtmosphereType(AtmosphereType.fromJournalValue(MiscUtil.getAsString(journalData.get("Atmosphere"))));
		result.setTerraformingState(TerraformingState.fromJournalValue(MiscUtil.getAsString(journalData.get("TerraformState"))));
		result.setEarthMasses(MiscUtil.getAsBigDecimal(journalData.get("MassEM"))); // TODO Convert
		result.setRadius(MiscUtil.getAsBigDecimal(journalData.get("Radius"))); // TODO Unit
		result.setGravity(MiscUtil.getAsBigDecimal(journalData.get("SurfaceGravity")));
		result.setSurfacePressure(MiscUtil.getAsBigDecimal(journalData.get("SurfacePressure")));
		result.setOrbitalPeriod(MiscUtil.getAsBigDecimal(journalData.get("OrbitalPeriod")));
		result.setSemiMajorAxis(MiscUtil.getAsBigDecimal(journalData.get("SemiMajorAxis")));
		result.setOrbitalEccentricity(MiscUtil.getAsBigDecimal(journalData.get("Eccentricity")));
		result.setOrbitalInclination(MiscUtil.getAsBigDecimal(journalData.get("OrbitalInclination")));
		result.setArgOfPeriapsis(MiscUtil.getAsBigDecimal(journalData.get("Periapsis")));
		result.setRotationalPeriod(MiscUtil.getAsBigDecimal(journalData.get("RotationPeriod")));
		result.setTidallyLocked(MiscUtil.getAsBoolean(journalData.get("TidalLock")));
		result.setAxisTilt(MiscUtil.getAsBigDecimal(journalData.get("AxialTilt")));
		result.setIsLandable(MiscUtil.getAsBoolean(journalData.get("Landable")));

		return result;
	}

}
