package borg.ed.galaxy.journal.events;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * LocationEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Deprecated
public class LocationEvent_v1 extends AbstractSystemJournalEvent {

	private static final long serialVersionUID = -3598986285224569310L;

	static final Logger logger = LoggerFactory.getLogger(LocationEvent_v1.class);

	private Boolean Docked = null;

	private String StationName = null;

	private String StationType = null;

	private String Body = null;

	private String BodyType = null;

	private String SystemFaction = null;

	private String FactionState = null;

	public LocationEvent toNewLocationEvent() {
		LocationEvent result = new LocationEvent();
		result.setTimestamp(this.getTimestamp());
		result.setEvent(this.getEvent());
		result.setStarSystem(this.getStarSystem());
		result.setStarPos(this.getStarPos());
		result.setSystemAllegiance(this.getSystemAllegiance());
		result.setSystemEconomy(this.getSystemEconomy());
		result.setSystemGovernment(this.getSystemGovernment());
		result.setSystemSecurity(this.getSystemSecurity());
		result.setPopulation(this.getPopulation());
		result.setPowers(this.getPowers());
		result.setPowerplayState(this.getPowerplayState());
		result.setFactions(this.getFactions());
		if (StringUtils.isNotEmpty(this.getSystemFaction())) {
			result.setSystemFaction(new Faction(this.getSystemFaction(), this.getFactionState()));
		}
		result.setDocked(this.getDocked());
		result.setStationName(this.getStationName());
		result.setStationType(this.getStationType());
		result.setBody(this.getBody());
		result.setBodyType(this.getBodyType());
		return result;
	}

}
