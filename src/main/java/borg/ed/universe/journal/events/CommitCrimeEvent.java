/*
 * Author:  Boris Guenther
 * Date:    27.12.2018
 * Time:    17:19:45
 */
package borg.ed.universe.journal.events;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * CommitCrimeEvent
 *
 * @author <a href="mailto:boris.guenther@redteclab.com">Boris Guenther</a>
 */
@Getter
@Setter
public class CommitCrimeEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 8090184353212782437L;
    
    private String CrimeType = null;
    
    private String Faction = null;
    
    private String Victim = null;
    
    private BigDecimal Fine = null;
    
    private BigDecimal Bounty = null;

}
