package borg.ed.universe.journal.events;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.universe.data.Coord;
import borg.ed.universe.journal.events.AbstractSystemJournalEvent.Faction;
import lombok.Getter;
import lombok.Setter;

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

	private BigDecimal MarketID = null;

	private Faction StationFaction = null;

	private String StationGovernment = null;

	private String StationAllegiance = null;

	private String StationEconomy = null;

	private BigDecimal DistFromStarLS = null;

}
