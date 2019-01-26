package borg.ed.universe.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.journal.events.AbstractSystemJournalEvent_v3_2;
import borg.ed.universe.journal.events.AbstractSystemJournalEvent_v3_2.Faction;
import borg.ed.universe.journal.events.FSDJumpEvent;
import borg.ed.universe.journal.events.LocationEvent;
import borg.ed.universe.journal.events.ScanEvent;
import borg.ed.universe.journal.events.ScanEvent.Share;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.Body.AtmosphereShare;
import borg.ed.universe.model.Body.MaterialShare;
import borg.ed.universe.model.Body.Ring;
import borg.ed.universe.model.FsdJump;
import borg.ed.universe.model.Location;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;
import borg.ed.universe.model.StarSystem.FactionPresence;
import borg.ed.universe.service.UniverseService;

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

	public StarSystem abstractSystemJournalEventToStarSystem(AbstractSystemJournalEvent_v3_2 event) {
		StarSystem result = new StarSystem();

		result.setId(null);
		result.setEddbId(null);
		result.setEdsmId(null);
		result.setCreatedAt(null);
		result.setUpdatedAt(null);
		result.setCoord(event.getStarPos());
		result.setName(event.getStarSystem());
		result.setPopulation(event.getPopulation());
		result.setGovernment(Government.fromJournalValue(event.getSystemGovernment()));
		result.setAllegiance(Allegiance.fromJournalValue(event.getSystemAllegiance()));
		result.setSecurity(SystemSecurity.fromJournalValue(event.getSystemSecurity()));
		result.setEconomy(Economy.fromJournalValue(event.getSystemEconomy()));
		result.setPowers(Power.fromJournalValue(event.getPowers()));
		result.setPowerState(PowerState.fromJournalValue(event.getPowerplayState()));
		result.setNeedsPermit(null); // Manually edited
		result.setControllingMinorFactionName(event.getSystemFaction());
		result.setMinorFactionPresences(this.factionsToFactionPresences(event.getFactions()));
		result.setState(this.lookupState(result.getMinorFactionPresences(), result.getControllingMinorFactionName()));

		return result;
	}

	@SuppressWarnings("unchecked")
	private List<FactionPresence> factionsToFactionPresences(List<Faction> list) {
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			List<FactionPresence> result = new ArrayList<>(list.size());
			for (Faction factionData : list) {
				FactionPresence factionPresence = new FactionPresence();
				factionPresence.setName(factionData.getName());
				if (factionData.getRecoveringStates() == null) {
					factionPresence.setRecoveringStates(null);
				} else {
					List<State> recoveringStates = new ArrayList<>(factionData.getRecoveringStates().size());
					for (borg.ed.universe.journal.events.FSDJumpEvent.Faction.State stateData : factionData.getRecoveringStates()) {
						recoveringStates.add(State.fromJournalValue(stateData.getState()));
					}
					factionPresence.setRecoveringStates(recoveringStates);
				}
				factionPresence.setState(State.fromJournalValue(factionData.getFactionState()));
				if (factionData.getPendingStates() == null) {
					factionPresence.setPendingStates(null);
				} else {
					List<State> pendingStates = new ArrayList<>(factionData.getPendingStates().size());
					for (borg.ed.universe.journal.events.FSDJumpEvent.Faction.State stateData : factionData.getPendingStates()) {
						pendingStates.add(State.fromJournalValue(stateData.getState()));
					}
					factionPresence.setPendingStates(pendingStates);
				}
				factionPresence.setInfluence(factionData.getInfluence());
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
	public List<MinorFaction> abstractSystemJournalEventToMinorFactions(AbstractSystemJournalEvent_v3_2 event) {
		if (event.getFactions() == null || event.getFactions().isEmpty()) {
			return null;
		} else {
			List<MinorFaction> result = new ArrayList<>(event.getFactions().size());
			for (Faction factionData : event.getFactions()) {
				MinorFaction minorFaction = new MinorFaction();

				minorFaction.setId(null);
				minorFaction.setEddbId(null);
				minorFaction.setEdsmId(null);
				minorFaction.setCreatedAt(null);
				minorFaction.setUpdatedAt(null);
				minorFaction.setHomeSystemId(null); // Manually edited
				minorFaction.setCoord(null); // Manually edited
				minorFaction.setName(factionData.getName());
				minorFaction.setGovernment(Government.fromJournalValue(factionData.getGovernment()));
				minorFaction.setAllegiance(Allegiance.fromJournalValue(factionData.getAllegiance()));
				minorFaction.setState(State.fromJournalValue(factionData.getFactionState()));
				minorFaction.setIsPlayerFaction(null); // Manually edited

				result.add(minorFaction);
			}
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public Body scanToBody(ScanEvent event) {
		Body result = new Body();

		if (StringUtils.isNotEmpty(event.getStarSystem())) {
			try {
				StarSystem starSystem = this.universeService.findStarSystemByName(event.getStarSystem());
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
		result.setCoord(event.getStarPos());
		result.setName(event.getBodyName());
		result.setDistanceToArrival(event.getDistanceFromArrivalLS());
		result.setStarClass(StarClass.fromJournalValue(event.getStarType()));
		result.setPlanetClass(PlanetClass.fromJournalValue(event.getPlanetClass()));
		result.setSurfaceTemperature(event.getSurfaceTemperature());
		result.setAge(event.getAge_MY());
		result.setSolarMasses(event.getStellarMass()); // TODO Convert
		result.setVolcanismType(VolcanismType.fromJournalValue(event.getVolcanism()));
		result.setAtmosphereType(AtmosphereType.fromJournalValue(event.getAtmosphere()));
		result.setTerraformingState(TerraformingState.fromJournalValue(event.getTerraformState()));
		result.setEarthMasses(event.getMassEM()); // TODO Convert
		result.setRadius(event.getRadius() == null ? null : event.getRadius().divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP)); // m -> km
		result.setGravity(event.getSurfaceGravity() == null ? null : event.getSurfaceGravity().divide(new BigDecimal(9.81), 2, BigDecimal.ROUND_HALF_UP)); // m/s² -> G
		result.setSurfacePressure(event.getSurfacePressure());
		result.setOrbitalPeriod(event.getOrbitalPeriod());
		result.setSemiMajorAxis(event.getSemiMajorAxis());
		result.setOrbitalEccentricity(event.getEccentricity());
		result.setOrbitalInclination(event.getOrbitalInclination());
		result.setArgOfPeriapsis(event.getPeriapsis());
		result.setRotationalPeriod(event.getRotationPeriod());
		result.setTidallyLocked(event.getTidalLock());
		result.setAxisTilt(event.getAxialTilt());
		result.setIsLandable(event.getLandable());
		result.setReserves(ReserveLevel.fromJournalValue(event.getReserveLevel()));
		result.setRings(this.ringsToRings(event.getRings()));
		result.setAtmosphereShares(this.sharesToAtmosphereShares(event.getAtmosphereComposition()));
		result.setMaterialShares(this.sharesToMaterialShares(event.getMaterials()));

		return result;
	}

	private List<Ring> ringsToRings(List<borg.ed.universe.journal.events.ScanEvent.Ring> list) {
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			List<Ring> result = new ArrayList<>(list.size());
			for (borg.ed.universe.journal.events.ScanEvent.Ring ringData : list) {
				Ring ring = new Ring();
				ring.setName(ringData.getName());
				ring.setRingClass(RingClass.fromJournalValue(ringData.getRingClass()));
				ring.setMassMT(ringData.getMassMT());
				ring.setInnerRadius(ringData.getInnerRad());
				ring.setOuterRadius(ringData.getOuterRad());
				result.add(ring);
			}
			return result;
		}
	}

	private List<AtmosphereShare> sharesToAtmosphereShares(List<Share> list) {
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			List<AtmosphereShare> result = new ArrayList<>(list.size());
			for (Share data : list) {
				AtmosphereShare share = new AtmosphereShare();
				share.setName(BodyAtmosphere.fromJournalValue(data.getName()));
				share.setPercent(data.getPercent());
				result.add(share);
			}
			return result;
		}
	}

	private List<MaterialShare> sharesToMaterialShares(List<Share> list) {
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			List<MaterialShare> result = new ArrayList<>(list.size());
			for (Share data : list) {
				MaterialShare share = new MaterialShare();
				share.setName(Element.fromJournalValue(data.getName()));
				share.setPercent(data.getPercent());
				result.add(share);
			}
			return result;
		}
	}

	public FsdJump fsdJump(FSDJumpEvent event) {
		FsdJump result = new FsdJump();

		result.setTimestamp(Date.from(event.getTimestamp().toInstant()));
		result.setCoord(event.getStarPos());
		result.setStarSystem(event.getStarSystem());
		result.setFaction(event.getSystemFaction());
		result.setAllegiance(Allegiance.fromJournalValue(event.getSystemAllegiance()));
		result.setState(State.fromJournalValue(event.getFactionState()));

		return result;
	}

	public Location location(LocationEvent event) {
		Location result = new Location();

		result.setTimestamp(Date.from(event.getTimestamp().toInstant()));
		result.setDocked(event.getDocked());
		result.setCoord(event.getStarPos());
		result.setStarSystem(event.getStarSystem());
		result.setBody(event.getBody());
		result.setStation(event.getStationName());
		result.setFaction(event.getSystemFaction());
		result.setAllegiance(Allegiance.fromJournalValue(event.getSystemAllegiance()));
		result.setState(State.fromJournalValue(event.getFactionState()));

		return result;
	}

}
