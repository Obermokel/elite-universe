/*
 * Author:  Boris Guenther
 * Date:    27.12.2018
 * Time:    16:13:36
 */
package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;

/**
 * ShieldStateEvent
 *
 * @author <a href="mailto:boris.guenther@redteclab.com">Boris Guenther</a>
 */
@Getter
@Setter
public class ShieldStateEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 3485362736398792389L;
    
    private Boolean ShieldsUp = null;

}
