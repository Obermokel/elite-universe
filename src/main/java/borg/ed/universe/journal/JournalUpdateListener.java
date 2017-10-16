package borg.ed.universe.journal;

import borg.ed.universe.journal.events.AbstractJournalEvent;

/**
 * JournalUpdateListener
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface JournalUpdateListener {

    void onNewJournalEntry(AbstractJournalEvent event);

}
