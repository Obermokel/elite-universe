package borg.ed.universe.journal.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import borg.ed.universe.data.Coord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractSystemJournalEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = 8734273170824509602L;

    private String StarSystem = null;

    private Coord StarPos = null;

    private String SystemAllegiance = null;

    private String SystemEconomy = null;

    private String SystemGovernment = null;

    private String SystemSecurity = null;

    private BigDecimal Population = null;

    private List<String> Powers = null;

    private String PowerplayState = null;

    private List<AbstractSystemJournalEvent.Faction> Factions = null;

    private String SystemFaction = null;

    private String FactionState = null;

    @Getter
    @Setter
    public static class Faction implements Serializable {

        private static final long serialVersionUID = 9069177073281049819L;

        private String Name = null;

        private String FactionState = null;

        private String Government = null;

        private String Allegiance = null;

        private BigDecimal Influence = null;

        private List<AbstractSystemJournalEvent.Faction.State> RecoveringStates = null;

        private List<AbstractSystemJournalEvent.Faction.State> PendingStates = null;

        @Getter
        @Setter
        public static class State implements Serializable {

            private static final long serialVersionUID = -8262884270535718116L;

            private String State = null;

            private Integer Trend = null;

        }

    }

}
