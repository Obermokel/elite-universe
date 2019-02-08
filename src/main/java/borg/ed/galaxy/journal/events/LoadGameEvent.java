package borg.ed.galaxy.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * LoadGameEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class LoadGameEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -84737319125295504L;
    
    static final Logger logger = LoggerFactory.getLogger(LoadGameEvent.class);
    
    private String Commander = null;

    private String Ship = null;

    private Long ShipID = null;

    private String ShipName = null;

    private String ShipIdent = null;
    
    private BigDecimal FuelLevel = null;
    
    private BigDecimal FuelCapacity = null;

    private String GameMode = null;

    private String Group = null;
    
    private BigDecimal Credits = null;
    
    private BigDecimal Loan = null;

}
