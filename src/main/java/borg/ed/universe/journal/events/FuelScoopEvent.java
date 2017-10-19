package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * FuelScoopEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class FuelScoopEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 980199958854035935L;
    
    static final Logger logger = LoggerFactory.getLogger(FuelScoopEvent.class);
    
    private BigDecimal Scooped = null;
    
    private BigDecimal Total = null;

}
