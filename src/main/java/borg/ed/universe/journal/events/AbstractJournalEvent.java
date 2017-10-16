package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AbstractJournalEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public abstract class AbstractJournalEvent implements Serializable, Comparable<AbstractJournalEvent> {

    private static final long serialVersionUID = 4420432616278189582L;

    static final Logger logger = LoggerFactory.getLogger(AbstractJournalEvent.class);

    private ZonedDateTime timestamp = null;

    private String event = null;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractJournalEvent other = (AbstractJournalEvent) obj;
        if (this.timestamp == null) {
            if (other.timestamp != null) {
                return false;
            }
        } else if (!this.timestamp.equals(other.timestamp)) {
            return false;
        }
        if (this.event == null) {
            if (other.event != null) {
                return false;
            }
        } else if (!this.event.equals(other.event)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.timestamp == null) ? 0 : this.timestamp.hashCode());
        result = prime * result + ((this.event == null) ? 0 : this.event.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return this.timestamp.format(DateTimeFormatter.ISO_INSTANT) + ": " + this.event;
    }

    @Override
    public int compareTo(AbstractJournalEvent other) {
        return this.timestamp.compareTo(other.timestamp);
    }

}
