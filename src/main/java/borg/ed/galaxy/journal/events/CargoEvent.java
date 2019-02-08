package borg.ed.galaxy.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.galaxy.journal.data.NameAndCount;

import java.util.List;

/**
 * CargoEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class CargoEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -116571123620083439L;
    
    static final Logger logger = LoggerFactory.getLogger(CargoEvent.class);
    
    private List<NameAndCount> Inventory = null;

}
