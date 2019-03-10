package borg.ed.galaxy.journal.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FSSDiscoveryScanEvent extends AbstractJournalEvent {

	private static final long serialVersionUID = -1895024755686491324L;

	private Float Progress = null;

	private Integer BodyCount = null;

	private Integer NonBodyCount = null;

	@Override
	public String toString() {
		return super.toString() + " (BodyCount: " + this.BodyCount + ")";
	}

}
