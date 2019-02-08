package borg.ed.galaxy.eddn;

import java.time.ZonedDateTime;

import borg.ed.galaxy.journal.events.AbstractJournalEvent;

/**
 * EddnUpdateListener
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface EddnUpdateListener {

	void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event) throws InterruptedException;

}
