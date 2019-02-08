package borg.ed.galaxy.journal.events;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * MultiSellExplorationDataEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class MultiSellExplorationDataEvent extends AbstractJournalEvent {

	private static final long serialVersionUID = 662669423494793067L;

	private BigDecimal BaseValue = null;

	private BigDecimal Bonus = null;

	private BigDecimal TotalEarnings = null;

}
