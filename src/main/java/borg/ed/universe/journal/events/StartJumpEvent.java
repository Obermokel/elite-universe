package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StartJumpEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class StartJumpEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -2312156798068166908L;
    
    static final Logger logger = LoggerFactory.getLogger(StartJumpEvent.class);
    
    private String JumpType = null;
    
    private String StarSystem = null;
    
    private String StarClass = null;

    @Override
    public String toString() {
        return super.toString() + " (To: "+this.StarSystem+" | Star Class: "+this.StarClass+")";
    }

}
