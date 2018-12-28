/*
 * Author:  Boris Guenther
 * Date:    27.12.2018
 * Time:    16:16:45
 */
package borg.ed.universe.journal.events;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * ShipTargetedEvent
 *
 * @author <a href="mailto:boris.guenther@redteclab.com">Boris Guenther</a>
 */
@Getter
@Setter
public class ShipTargetedEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -1801590629387498515L;
    
    private Boolean TargetLocked = null; // false = lost target
    
    private Integer ScanStage = null; // 0..3, see below
    
    // ScanStage = 0
    
    private String Ship = null; // orca
    
    // ScanStage = 1
    
    private String PilotName = null; // $npc_name_decorate:#name=Lelu;
    
    private String PilotName_Localised = null; // Lelu
    
    private String PilotRank = null; // Competent
    
    // ScanStage = 2
    
    private BigDecimal ShieldHealth = null; // 100.000000
    
    private BigDecimal HullHealth = null; // 100.000000
    
    // ScanStage = 3
    
    private String Faction = null; // Independent Ehlauneti Future
    
    private String LegalStatus = null; // Clean OR Wanted
    
    private BigDecimal Bounty = null; // 159375

}
