/*
 * Author:  Boris Guenther
 * Date:    28.12.2018
 * Time:    13:05:37
 */
package borg.ed.galaxy.journal.events;

import lombok.Getter;
import lombok.Setter;

/**
 * UnderAttackEvent
 *
 * @author <a href="mailto:boris.guenther@redteclab.com">Boris Guenther</a>
 */
@Getter
@Setter
public class UnderAttackEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -2620838023092109343L;

    private String Target = null;

}
