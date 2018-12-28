/*
 * Author:  Boris Guenther
 * Date:    27.12.2018
 * Time:    17:22:22
 */
package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;

/**
 * FighterRebuiltEvent
 *
 * @author <a href="mailto:boris.guenther@redteclab.com">Boris Guenther</a>
 */
@Getter
@Setter
public class FighterRebuiltEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -6484230841598579778L;
    
    private String Loadout = null;

}
