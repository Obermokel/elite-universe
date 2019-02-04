package borg.ed.universe.eddn;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import borg.ed.universe.converter.JournalConverter;
import borg.ed.universe.journal.events.AbstractJournalEvent;
import borg.ed.universe.journal.events.AbstractSystemJournalEvent;
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

	private boolean updateMinorFactions = true;

	@Override
	public void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event) {
		ZonedDateTime nowPlusTenMinutes = ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(10);

		if (event == null || event.getTimestamp() == null) {
			// NOOP
		} else if (event.getTimestamp().isAfter(nowPlusTenMinutes)) {
			logger.warn("Received data from the future: " + event.getTimestamp() + " > " + nowPlusTenMinutes + ", uploaderID=" + uploaderID);
		} else if (event instanceof AbstractSystemJournalEvent) {
			this.handleAbstractSystemJournalEvent(gatewayTimestamp, uploaderID, (AbstractSystemJournalEvent) event);
		} else if (event instanceof ScanEvent) {
			this.handleScan(gatewayTimestamp, uploaderID, (ScanEvent) event);
		} else {
			//logger.warn("Unknown journal event: " + event);
		}
	}

	void handleAbstractSystemJournalEvent(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractSystemJournalEvent event) {
		try {
			this.readStarSystem(uploaderID, event);

			if (this.isUpdateMinorFactions()) {
				this.readMinorFactions(uploaderID, event);
			}
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void readStarSystem(String uploaderID, AbstractSystemJournalEvent event) {
		StarSystem currentStarSystem = this.journalConverter.abstractSystemJournalEventToStarSystem(event);
		this.starSystemRepository.index(currentStarSystem);
	}

	private void readMinorFactions(String uploaderID, AbstractSystemJournalEvent event) {
		List<MinorFaction> currentMinorFactions = this.journalConverter.abstractSystemJournalEventToMinorFactions(event);

		if (currentMinorFactions != null) {
			for (MinorFaction currentMinorFaction : currentMinorFactions) {
				this.minorFactionRepository.index(currentMinorFaction);
			}
		}
	}

	void handleScan(ZonedDateTime gatewayTimestamp, String uploaderID, ScanEvent event) {
		try {
			this.readBody(uploaderID, event);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void readBody(String uploaderID, ScanEvent event) {
		Body currentBody = this.journalConverter.scanEventToBody(event);
		this.bodyRepository.index(currentBody);
	}

	public boolean isUpdateMinorFactions() {
		return updateMinorFactions;
	}

	public void setUpdateMinorFactions(boolean updateMinorFactions) {
		this.updateMinorFactions = updateMinorFactions;
	}

}
