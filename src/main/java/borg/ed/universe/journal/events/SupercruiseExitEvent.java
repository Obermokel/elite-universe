package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SupercruiseExitEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class SupercruiseExitEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 3658582842465680120L;
    
    static final Logger logger = LoggerFactory.getLogger(SupercruiseExitEvent.class);
    
    private String StarSystem = null;
    
    private String Body = null;
    
    private String BodyType = null;

}
