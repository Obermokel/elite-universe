package borg.ed.galaxy.journal.events;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscoveryScanEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 371996744181509420L;

    private BigDecimal SystemAddress = null;

    private Integer Bodies = null;

    @Override
    public String toString() {
        return super.toString() + " (Bodies: "+this.Bodies+")";
    }

}
