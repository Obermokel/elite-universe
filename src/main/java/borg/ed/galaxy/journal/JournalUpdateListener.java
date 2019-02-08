package borg.ed.galaxy.journal;

import borg.ed.galaxy.journal.events.AbstractJournalEvent;

/**
 * JournalUpdateListener
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface JournalUpdateListener {

    void onNewJournalEntry(AbstractJournalEvent event);

}
