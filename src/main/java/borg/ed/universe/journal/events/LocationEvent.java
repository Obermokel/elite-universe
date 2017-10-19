package borg.ed.universe.journal.events;

import borg.ed.universe.data.Coord;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LocationEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class LocationEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -3598986285224569310L;

    static final Logger logger = LoggerFactory.getLogger(LocationEvent.class);

    private Boolean Docked = null;

    private String StationName = null;

    private String StationType = null;

    private String StarSystem = null;

    private Coord StarPos = null;

    private String Allegiance = null;

    private String Economy = null;

    private String Government = null;

    private String Security = null;

    private String Body = null;

    private String BodyType = null;

    private String Faction = null;

    private String FactionState = null;

}
