package borg.ed.universe.journal.events;

import borg.ed.universe.data.Coord;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * DockedEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class DockedEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -4588032241681280366L;

    static final Logger logger = LoggerFactory.getLogger(DockedEvent.class);

    private String StationName = null;

    private String StationType = null;

    private String StarSystem = null;

    private Coord StarPos = null;

    private String StationFaction = null;

    private String FactionState = null;

    private String StationGovernment = null;

    private String StationAllegiance = null;

    private String StationEconomy = null;

    private BigDecimal DistFromStarLS = null;

}
