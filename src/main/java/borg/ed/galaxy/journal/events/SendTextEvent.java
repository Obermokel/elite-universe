package borg.ed.galaxy.journal.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendTextEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 2940499397246933525L;
    
    private String To = null;
    
    private String Message = null;

    @Override
    public String toString() {
        return super.toString() + " (TO "+this.To+": "+this.Message+")";
    }

}
