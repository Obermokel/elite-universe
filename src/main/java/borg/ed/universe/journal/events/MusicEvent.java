package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicEvent extends AbstractJournalEvent {

    public static final String TRACK_DESTINATION_FROM_HYPERSPACE = "DestinationFromHyperspace";

    private static final long serialVersionUID = 8819894126579044240L;

    private String MusicTrack = null;

}
