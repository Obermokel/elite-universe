package borg.ed.galaxy.eddn;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import borg.ed.galaxy.google.GoogleBufferThread;
import borg.ed.galaxy.journal.events.AbstractJournalEvent;
import borg.ed.galaxy.journal.events.AbstractSystemJournalEvent;
import borg.ed.galaxy.journal.events.AbstractSystemJournalEvent.Faction;

/**
 * EddnGoogleBgsUpdater
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class EddnGoogleBgsUpdater implements EddnUpdateListener {

	static final Logger logger = LoggerFactory.getLogger(EddnGoogleBgsUpdater.class);

	@Autowired
	private GoogleBufferThread googleBufferThread = null;

	@Override
	public void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event) throws InterruptedException {
		ZonedDateTime nowPlusTenMinutes = ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(10);

		if (event == null || event.getTimestamp() == null) {
			// NOOP
		} else if (event.getTimestamp().isAfter(nowPlusTenMinutes)) {
			logger.warn("Received data from the future: " + event.getTimestamp() + " > " + nowPlusTenMinutes + ", uploaderID=" + uploaderID);
		} else if (event instanceof AbstractSystemJournalEvent) {
			this.handleSystemEvent(gatewayTimestamp, uploaderID, (AbstractSystemJournalEvent) event);
		} else {
			//logger.warn("Unknown journal event: " + event);
		}
	}

	void handleSystemEvent(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractSystemJournalEvent event) throws InterruptedException {
		try {
			// GPL present?
			if (isGplPresent(event.getFactions())) {
				this.googleBufferThread.bufferEvent(event);
			}
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private boolean isGplPresent(List<Faction> factions) {
		if (factions == null || factions.isEmpty()) {
			return false;
		} else {
			return factions.stream().filter(f -> "German Pilot Lounge".equals(f.getName())).findFirst().isPresent();
		}
	}

}
