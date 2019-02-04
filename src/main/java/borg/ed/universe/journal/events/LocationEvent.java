package borg.ed.universe.journal.events;

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
public class LocationEvent extends AbstractSystemJournalEvent {

	private static final long serialVersionUID = -3598986285224569310L;

	static final Logger logger = LoggerFactory.getLogger(LocationEvent.class);

	private Boolean Docked = null;

	private String StationName = null;

	private String StationType = null;

	private String Body = null;

	private String BodyType = null;

	private String SystemFaction = null;

}
