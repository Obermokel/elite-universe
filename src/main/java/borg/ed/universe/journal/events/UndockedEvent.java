package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UndockedEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class UndockedEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 8151227901001903985L;

    static final Logger logger = LoggerFactory.getLogger(UndockedEvent.class);

    private String StationName = null;

    private String StationType = null;

}
