package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SupercruiseEntryEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class SupercruiseEntryEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 562253949717159240L;
    
    static final Logger logger = LoggerFactory.getLogger(SupercruiseEntryEvent.class);
    
    private String StarSystem = null;

}
