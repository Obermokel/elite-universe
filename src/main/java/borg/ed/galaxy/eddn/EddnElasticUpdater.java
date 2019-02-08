package borg.ed.galaxy.eddn;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import borg.ed.galaxy.converter.JournalConverter;
import borg.ed.galaxy.elastic.ElasticBufferThread;
import borg.ed.galaxy.journal.events.AbstractJournalEvent;
import borg.ed.galaxy.journal.events.AbstractSystemJournalEvent;
import borg.ed.galaxy.journal.events.DockedEvent;
import borg.ed.galaxy.journal.events.ScanEvent;
import borg.ed.galaxy.model.Body;
import borg.ed.galaxy.model.MinorFaction;
import borg.ed.galaxy.model.StarSystem;
import borg.ed.galaxy.model.Station;

/**
 * EddnElasticUpdater
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class EddnElasticUpdater implements EddnUpdateListener {

	static final Logger logger = LoggerFactory.getLogger(EddnElasticUpdater.class);

	@Autowired
	private JournalConverter journalConverter = null;

	@Autowired
	private ElasticBufferThread elasticBufferThread = null;

	private boolean updateMinorFactions = true;

	@Override
	public void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event) throws InterruptedException {
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

	void handleAbstractSystemJournalEvent(AbstractSystemJournalEvent event) throws InterruptedException {
		try {
			this.updateStarSystem(event);
			this.updateMinorFactions(event);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void updateStarSystem(AbstractSystemJournalEvent event) throws InterruptedException {
		StarSystem starSystem = this.journalConverter.abstractSystemJournalEventToStarSystem(event);
		this.elasticBufferThread.bufferStarSystem(starSystem);
	}

	private void updateMinorFactions(AbstractSystemJournalEvent event) throws InterruptedException {
		if (this.isUpdateMinorFactions()) {
			List<MinorFaction> minorFactions = this.journalConverter.abstractSystemJournalEventToMinorFactions(event);

			if (minorFactions != null) {
				for (MinorFaction minorFaction : minorFactions) {
					this.elasticBufferThread.bufferMinorFaction(minorFaction);
				}
			}
		}
	}

	void handleScan(ScanEvent event) throws InterruptedException {
		try {
			this.updateBody(event);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void updateBody(ScanEvent event) throws InterruptedException {
		// Only update with detailed scan events
		if (event.isDetailedScan()) {
			Body body = this.journalConverter.scanEventToBody(event);
			this.elasticBufferThread.bufferBody(body);
		}
	}

	void handleDocked(DockedEvent event) throws InterruptedException {
		try {
			this.updateStation(event);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void updateStation(DockedEvent event) throws InterruptedException {
		Station station = this.journalConverter.dockedEventToStation(event);
		this.elasticBufferThread.bufferStation(station);
	}

	public boolean isUpdateMinorFactions() {
		return updateMinorFactions;
	}

	public void setUpdateMinorFactions(boolean updateMinorFactions) {
		this.updateMinorFactions = updateMinorFactions;
	}

}
