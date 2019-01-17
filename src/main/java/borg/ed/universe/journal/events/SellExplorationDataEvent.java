package borg.ed.universe.journal.events;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * SellExplorationDataEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class SellExplorationDataEvent extends AbstractJournalEvent {

	private static final long serialVersionUID = -4382614998662972752L;

	static final Logger logger = LoggerFactory.getLogger(SellExplorationDataEvent.class);

	private List<String> Systems = null;

	private List<String> Discovered = null;

	private BigDecimal BaseValue = null;

	private BigDecimal Bonus = null;

	private BigDecimal TotalEarnings = null;

}
