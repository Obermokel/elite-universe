package borg.ed.galaxy.journal.events;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * FSDTargetEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class FSDTargetEvent extends AbstractJournalEvent {

	private static final long serialVersionUID = 3587020586603508361L;

	private BigDecimal SystemAddress = null;

	private String Name = null;

}
