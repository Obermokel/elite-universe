package borg.ed.galaxy.journal.events;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BountyEvent extends AbstractJournalEvent {

	private static final long serialVersionUID = -7159938681449418589L;

	private String Target = null;

	private BigDecimal TotalReward = null;

}
