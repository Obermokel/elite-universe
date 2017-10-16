package borg.ed.universe.eddn;

import borg.ed.universe.journal.events.AbstractJournalEvent;

import java.time.ZonedDateTime;

/**
 * EddnUpdateListener
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface EddnUpdateListener {

    void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event);

}
