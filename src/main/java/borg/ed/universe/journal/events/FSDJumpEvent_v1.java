package borg.ed.universe.journal.events;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * FSDJumpEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Deprecated
public class FSDJumpEvent_v1 extends AbstractSystemJournalEvent {

	private static final long serialVersionUID = -5264764860239254112L;

	static final Logger logger = LoggerFactory.getLogger(FSDJumpEvent_v1.class);

	private BigDecimal JumpDist = null;

	private BigDecimal FuelUsed = null;

	private BigDecimal FuelLevel = null;

	private String SystemFaction = null;

	private String FactionState = null;

	public FSDJumpEvent toNewFSDJumpEvent() {
		FSDJumpEvent result = new FSDJumpEvent();
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
		result.setJumpDist(this.getJumpDist());
		result.setFuelUsed(this.getFuelUsed());
		result.setFuelLevel(this.getFuelLevel());
		return result;
	}

}
