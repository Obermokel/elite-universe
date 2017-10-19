package borg.ed.universe.journal.events;

import borg.ed.universe.journal.data.NameAndCount;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MaterialsEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class MaterialsEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -2685957238216861641L;
    
    static final Logger logger = LoggerFactory.getLogger(MaterialsEvent.class);
    
    private List<NameAndCount> Raw = null;
    
    private List<NameAndCount> Manufactured = null;
    
    private List<NameAndCount> Encoded = null;

}
