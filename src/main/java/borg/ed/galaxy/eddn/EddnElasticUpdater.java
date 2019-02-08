package borg.ed.galaxy.eddn;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import borg.ed.galaxy.converter.JournalConverter;
import borg.ed.galaxy.exceptions.NonUniqueResultException;
import borg.ed.galaxy.journal.events.AbstractJournalEvent;
import borg.ed.galaxy.journal.events.AbstractSystemJournalEvent;
import borg.ed.galaxy.journal.events.DockedEvent;
import borg.ed.galaxy.journal.events.ScanEvent;
import borg.ed.galaxy.model.Body;
import borg.ed.galaxy.model.MinorFaction;
import borg.ed.galaxy.model.StarSystem;
import borg.ed.galaxy.model.Station;
import borg.ed.galaxy.repository.BodyRepository;
import borg.ed.galaxy.repository.MinorFactionRepository;
import borg.ed.galaxy.repository.StarSystemRepository;
import borg.ed.galaxy.repository.StationRepository;
import borg.ed.galaxy.service.GalaxyService;

/**
 * EddnElasticUpdater
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class EddnElasticUpdater implements EddnUpdateListener {

	static final Logger logger = LoggerFactory.getLogger(EddnElasticUpdater.class);

	@Autowired
	private GalaxyService galaxyService = null;

	@Autowired
	private JournalConverter journalConverter = null;

	@Autowired
	private StarSystemRepository starSystemRepository = null;

	@Autowired
	private BodyRepository bodyRepository = null;

	@Autowired
	private MinorFactionRepository minorFactionRepository = null;

	@Autowired
	private StationRepository stationRepository = null;

	private boolean updateMinorFactions = true;

	@Override
	public void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event) {
		ZonedDateTime nowPlusTenMinutes = ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(10);

		if (event == null || event.getTimestamp() == null) {
			// NOOP
		} else if (event.getTimestamp().isAfter(nowPlusTenMinutes)) {
			logger.warn("Received data from the future: " + event.getTimestamp() + " > " + nowPlusTenMinutes + ", uploaderID=" + uploaderID);
		} else if (event instanceof AbstractSystemJournalEvent) {
			this.handleAbstractSystemJournalEvent((AbstractSystemJournalEvent) event);
		} else if (event instanceof ScanEvent) {
			this.handleScan((ScanEvent) event);
		} else if (event instanceof DockedEvent) {
			this.handleDocked((DockedEvent) event);
		} else {
			logger.warn("Unknown journal event: " + event);
		}
	}

	void handleAbstractSystemJournalEvent(AbstractSystemJournalEvent event) {
		try {
			this.updateStarSystem(event);
			this.updateMinorFactions(event);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void updateStarSystem(AbstractSystemJournalEvent event) {
		try {
			this.galaxyService.findStarSystemByName(event.getStarSystem());
		} catch (NonUniqueResultException e) {
			logger.warn("Duplicate star system. Will delete all of them: " + e.getOthers());
			for (String id : e.getOtherIds()) {
				this.starSystemRepository.deleteById(id);
			}
		}

		StarSystem starSystem = this.journalConverter.abstractSystemJournalEventToStarSystem(event);
		this.starSystemRepository.index(starSystem);
	}

	private void updateMinorFactions(AbstractSystemJournalEvent event) {
		if (this.isUpdateMinorFactions()) {
			List<MinorFaction> minorFactions = this.journalConverter.abstractSystemJournalEventToMinorFactions(event);

			if (minorFactions != null) {
				for (MinorFaction minorFaction : minorFactions) {
					this.minorFactionRepository.index(minorFaction);
				}
			}
		}
	}

	void handleScan(ScanEvent event) {
		try {
			this.updateBody(event);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void updateBody(ScanEvent event) {
		boolean starSystemExists = false;

		try {
			if (this.galaxyService.findStarSystemByName(event.getStarSystem()) != null) {
				starSystemExists = true;
			}
		} catch (NonUniqueResultException e) {
			logger.warn("Duplicate star system. Will delete all of them: " + e.getOthers());
			for (String id : e.getOtherIds()) {
				this.starSystemRepository.deleteById(id);
			}
		}

		if (!starSystemExists) {
			//logger.warn("Star system '" + event.getStarSystem() + "' not found");

			// Create a dummy star system
			StarSystem starSystem = new StarSystem();
			starSystem.setId(StarSystem.generateId(event.getStarPos()));
			starSystem.setUpdatedAt(Date.from(event.getTimestamp().toInstant()));
			starSystem.setCoord(event.getStarPos());
			starSystem.setName(event.getStarSystem());
			starSystem.setPopulation(BigDecimal.ZERO);
			this.starSystemRepository.index(starSystem);
		}

		// Only update with detailed scan events
		if (event.isDetailedScan()) {
			Body body = this.journalConverter.scanEventToBody(event);
			this.bodyRepository.index(body);
		}
	}

	void handleDocked(DockedEvent event) {
		try {
			this.updateStation(event);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void updateStation(DockedEvent event) {
		Station station = this.journalConverter.dockedEventToStation(event);
		this.stationRepository.index(station);
	}

	public boolean isUpdateMinorFactions() {
		return updateMinorFactions;
	}

	public void setUpdateMinorFactions(boolean updateMinorFactions) {
		this.updateMinorFactions = updateMinorFactions;
	}

}
