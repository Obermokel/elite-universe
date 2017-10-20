package borg.ed.universe.eddn;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import borg.ed.universe.converter.JournalConverter;
import borg.ed.universe.data.Coord;
import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.exceptions.SuspiciousDataException;
import borg.ed.universe.journal.events.AbstractJournalEvent;
import borg.ed.universe.journal.events.FSDJumpEvent;
import borg.ed.universe.journal.events.ScanEvent;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;
import borg.ed.universe.repository.BodyRepository;
import borg.ed.universe.repository.MinorFactionRepository;
import borg.ed.universe.repository.StarSystemRepository;
import borg.ed.universe.service.UniverseService;

/**
 * EddnElasticUpdater
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class EddnElasticUpdater implements EddnUpdateListener {

    static final Logger logger = LoggerFactory.getLogger(EddnElasticUpdater.class);

    @Autowired
    private UniverseService universeService = null;

    @Autowired
    private JournalConverter journalConverter = null;

    @Autowired
    private StarSystemRepository starSystemRepository = null;

    @Autowired
    private BodyRepository bodyRepository = null;

    @Autowired
    private MinorFactionRepository minorFactionRepository = null;

    @Override
    public void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event) {
        try {
            ZonedDateTime nowPlusTenMinutes = ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(10);

            if (event == null) {
                // NOOP
            } else if (event.getTimestamp().isAfter(nowPlusTenMinutes)) {
                logger.warn("Received data from the future: " + event.getTimestamp() + " > " + nowPlusTenMinutes + ", uploaderID=" + uploaderID);
            } else if (event instanceof FSDJumpEvent) {
                this.handleFsdJump(gatewayTimestamp, uploaderID, (FSDJumpEvent) event);
            } else if (event instanceof ScanEvent) {
                this.handleScan(gatewayTimestamp, uploaderID, (ScanEvent) event);
            } else {
                //logger.warn("Unknown journal event: " + event);
            }
        } catch (NonUniqueResultException e) {
            logger.error("Elasticsearch update failed", e);
        }
    }

    void handleFsdJump(ZonedDateTime gatewayTimestamp, String uploaderID, FSDJumpEvent event) throws NonUniqueResultException {
        try {
            this.readStarSystem(uploaderID, event);
            this.readMinorFactions(uploaderID, event);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }

    private void readStarSystem(String uploaderID, FSDJumpEvent event) throws NonUniqueResultException {
        StarSystem currentStarSystem = this.journalConverter.fsdJumpToStarSystem(event);
        StarSystem existingStarSystem = this.universeService.findStarSystemByName(currentStarSystem.getName());

        if (existingStarSystem == null) {
            currentStarSystem.setCreatedAt(Date.from(event.getTimestamp().toInstant()));
            currentStarSystem.setUpdatedAt(Date.from(event.getTimestamp().toInstant()));
            currentStarSystem.setFirstDiscoveredBy(uploaderID);

            this.starSystemRepository.index(currentStarSystem);

            logger.info(String.format(Locale.US, "New star system discovered by %s: '%s' %,.1f Ly from Sol", uploaderID, currentStarSystem.getName(), currentStarSystem.getCoord().distanceTo(new Coord())));

            // TODO See if there is another starsystem at the same coords. If so, it may have been renamed. Delete the old, including bodies, stations etc.
        } else if (!Date.from(event.getTimestamp().toInstant()).after(existingStarSystem.getUpdatedAt())) {
            logger.trace("Received outdated information for {}", existingStarSystem);
        } else {
            this.updateStarSystem(existingStarSystem, currentStarSystem);
            existingStarSystem.setUpdatedAt(Date.from(event.getTimestamp().toInstant()));

            this.starSystemRepository.index(existingStarSystem);

            logger.trace("Updated {}", existingStarSystem);
        }
    }

    private void readMinorFactions(String uploaderID, FSDJumpEvent event) throws NonUniqueResultException {
        List<MinorFaction> currentMinorFactions = this.journalConverter.fsdJumpToMinorFactions(event);

        if (currentMinorFactions != null) {
            for (MinorFaction currentMinorFaction : currentMinorFactions) {
                MinorFaction existingMinorFaction = this.universeService.findMinorFactionByName(currentMinorFaction.getName());

                if (existingMinorFaction == null) {
                    currentMinorFaction.setCreatedAt(Date.from(event.getTimestamp().toInstant()));
                    currentMinorFaction.setUpdatedAt(Date.from(event.getTimestamp().toInstant()));

                    this.minorFactionRepository.index(currentMinorFaction);

                    logger.info(String.format(Locale.US, "New minor faction discovered by %s: '%s'", uploaderID, currentMinorFaction.getName()));

                    // TODO See if there is another starsystem at the same coords. If so, it may have been renamed. Delete the old, including bodies, stations etc.
                } else if (!Date.from(event.getTimestamp().toInstant()).after(existingMinorFaction.getUpdatedAt())) {
                    logger.trace("Received outdated information for {}", existingMinorFaction);
                } else {
                    this.updateMinorFaction(existingMinorFaction, currentMinorFaction);
                    existingMinorFaction.setUpdatedAt(Date.from(event.getTimestamp().toInstant()));

                    this.minorFactionRepository.index(existingMinorFaction);

                    logger.trace("Updated {}", existingMinorFaction);
                }
            }
        }
    }

    private void updateStarSystem(StarSystem oldData, StarSystem newData) {
        //oldData.setCoord(newData.getCoord());
        //oldData.setName(newData.getName());
        oldData.setPopulation(newData.getPopulation());
        oldData.setGovernment(newData.getGovernment());
        oldData.setAllegiance(newData.getAllegiance());
        oldData.setState(newData.getState());
        oldData.setSecurity(newData.getSecurity());
        oldData.setEconomy(newData.getEconomy());
        oldData.setPowers(newData.getPowers());
        oldData.setPowerState(newData.getPowerState());
        oldData.setPopulation(newData.getPopulation());
        //oldData.setNeedsPermit(newData.getNeedsPermit());
        oldData.setControllingMinorFactionName(newData.getControllingMinorFactionName());
        oldData.setMinorFactionPresences(newData.getMinorFactionPresences());
    }

    private void updateMinorFaction(MinorFaction oldData, MinorFaction newData) {
        //oldData.setHomeSystemId(newData.getHomeSystemId());
        //oldData.setCoord(newData.getCoord());
        //oldData.setName(newData.getName());
        oldData.setGovernment(newData.getGovernment());
        oldData.setAllegiance(newData.getAllegiance());
        if (newData.getState() != borg.ed.universe.constants.State.NONE) {
            oldData.setState(newData.getState());
        }
        //oldData.setIsPlayerFaction(newData.getIsPlayerFaction());
    }

    void handleScan(ZonedDateTime gatewayTimestamp, String uploaderID, ScanEvent event) throws NonUniqueResultException {
        try {
            this.readBody(uploaderID, event);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }

    private void readBody(String uploaderID, ScanEvent event) throws NonUniqueResultException {
        Body currentBody = this.journalConverter.scanToBody(event);
        Body existingBody = this.universeService.findBodyByName(currentBody.getName());

        if (existingBody == null) {
            currentBody.setCreatedAt(Date.from(event.getTimestamp().toInstant()));
            currentBody.setUpdatedAt(Date.from(event.getTimestamp().toInstant()));
            currentBody.setFirstDiscoveredBy(uploaderID);

            this.bodyRepository.index(currentBody);

            if (currentBody.getStarClass() != null) {
                logger.info(String.format(Locale.US, "New class %s star discovered by %s: '%s' %,.1f Ly from Sol", currentBody.getStarClass(), uploaderID, currentBody.getName(), currentBody.getCoord().distanceTo(new Coord())));
            } else {
                logger.info(String.format(Locale.US, "New %s discovered by %s: '%s' %,.1f Ly from Sol", currentBody.getPlanetClass(), uploaderID, currentBody.getName(), currentBody.getCoord().distanceTo(new Coord())));
            }

            // TODO See if there is another starsystem at the same coords. If so, it may have been renamed. Delete the old, including bodies, stations etc.
        } else if (!Date.from(event.getTimestamp().toInstant()).after(existingBody.getUpdatedAt())) {
            logger.trace("Received outdated information for {}", existingBody);
        } else {
            this.updateBody(existingBody, currentBody);
            existingBody.setUpdatedAt(Date.from(event.getTimestamp().toInstant()));

            this.bodyRepository.index(existingBody);

            logger.trace("Updated {}", existingBody);
        }
    }

    private void updateBody(Body oldData, Body newData) {
        //oldData.setStarSystemId(newData.getStarSystemId());
        //oldData.setStarSystemName(newData.getStarSystemName());
        //oldData.setCoord(newData.getCoord());
        //oldData.setName(newData.getName());
        oldData.setDistanceToArrival(newData.getDistanceToArrival());
        oldData.setStarClass(newData.getStarClass());
        oldData.setPlanetClass(newData.getPlanetClass());
        oldData.setSurfaceTemperature(newData.getSurfaceTemperature());
        oldData.setAge(newData.getAge());
        oldData.setSolarMasses(newData.getSolarMasses());
        oldData.setVolcanismType(newData.getVolcanismType());
        oldData.setAtmosphereType(newData.getAtmosphereType());
        oldData.setTerraformingState(newData.getTerraformingState());
        oldData.setEarthMasses(newData.getEarthMasses());
        oldData.setRadius(newData.getRadius());
        oldData.setGravity(newData.getGravity());
        oldData.setSurfacePressure(newData.getSurfacePressure());
        oldData.setOrbitalPeriod(newData.getOrbitalPeriod());
        oldData.setOrbitalInclination(newData.getOrbitalInclination());
        oldData.setArgOfPeriapsis(newData.getArgOfPeriapsis());
        oldData.setRotationalPeriod(newData.getRotationalPeriod());
        oldData.setTidallyLocked(newData.getTidallyLocked());
        oldData.setAxisTilt(newData.getAxisTilt());
        oldData.setIsLandable(newData.getIsLandable());
        oldData.setReserves(newData.getReserves());
        oldData.setRings(newData.getRings());
        oldData.setAtmosphereShares(newData.getAtmosphereShares());
        oldData.setMaterialShares(newData.getMaterialShares());
    }

}
