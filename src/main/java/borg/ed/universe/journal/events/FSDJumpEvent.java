package borg.ed.universe.journal.events;

import java.math.BigDecimal;

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
public class FSDJumpEvent extends AbstractSystemJournalEvent {

	private static final long serialVersionUID = -5264764860239254112L;

	static final Logger logger = LoggerFactory.getLogger(FSDJumpEvent.class);

	private BigDecimal JumpDist = null;

	private BigDecimal FuelUsed = null;

	private BigDecimal FuelLevel = null;

	private String SystemFaction = null;

}
