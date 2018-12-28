/*
 * Author:  Boris Guenther
 * Date:    27.12.2018
 * Time:    17:23:43
 */
package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;

/**
 * LaunchFighterEvent
 *
 * @author <a href="mailto:boris.guenther@redteclab.com">Boris Guenther</a>
 */
@Getter
@Setter
public class LaunchFighterEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -5793047551121210597L;
    
    private String Loadout = null;
    
    private Boolean PlayerControlled = null;

}
