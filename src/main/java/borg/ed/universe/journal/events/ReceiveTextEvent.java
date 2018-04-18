package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveTextEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -5757862731433752281L;
    
    private String From = null;
    
    private String Message = null;
    
    private String Channel = null;

    @Override
    public String toString() {
        return super.toString() + " (FROM "+this.From+": "+this.Message+")";
    }

}
