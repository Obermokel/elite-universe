package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DiedEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class DiedEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -7039522852460986026L;

    static final Logger logger = LoggerFactory.getLogger(DiedEvent.class);

    private String KillerName = null;

    private String KillerName_Localised = null;

    private String KillerShip = null;

    private String KillerRank = null;

}
