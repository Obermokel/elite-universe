package borg.ed.universe.journal.events;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
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
@Deprecated
public class DockedEvent_v1 extends AbstractJournalEvent {

	private static final long serialVersionUID = -4588032241681280366L;

	static final Logger logger = LoggerFactory.getLogger(DockedEvent_v1.class);

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

	public DockedEvent toNewDockedEvent() {
		DockedEvent result = new DockedEvent();
		result.setTimestamp(this.getTimestamp());
		result.setEvent(this.getEvent());
		result.setStationName(this.getStationName());
		result.setStationType(this.getStationType());
		result.setStarSystem(this.getStarSystem());
		result.setStarPos(this.getStarPos());
		if (StringUtils.isNotEmpty(this.getStationFaction())) {
			result.setStationFaction(new Faction(this.getStationFaction(), this.getFactionState()));
		}
		result.setStationGovernment(this.getStationGovernment());
		result.setStationAllegiance(this.getStationAllegiance());
		result.setStationEconomy(this.getStationEconomy());
		result.setDistFromStarLS(this.getDistFromStarLS());
		return result;
	}

}
