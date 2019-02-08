package borg.ed.galaxy.journal.events;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * FSSSignalDiscoveredEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class FSSSignalDiscoveredEvent extends AbstractJournalEvent {

	private static final long serialVersionUID = -4655667768162501854L;

	private BigDecimal SystemAddress = null;

	private String SignalName = null;

	private String SignalName_Localised = null;

	private Boolean IsStation = null;

	private String USSType = null;

	private String USSType_Localised = null;

	private Integer ThreatLevel = null;

	private BigDecimal TimeRemaining = null; // Seconds

	private String SpawningFaction = null;

	private String SpawningFaction_Localised = null;

	private String SpawningState = null;

	private String SpawningState_Localised = null;

}
