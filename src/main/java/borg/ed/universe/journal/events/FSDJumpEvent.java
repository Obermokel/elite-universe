package borg.ed.universe.journal.events;

import borg.ed.universe.data.Coord;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * FSDJumpEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class FSDJumpEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -5264764860239254112L;

    static final Logger logger = LoggerFactory.getLogger(FSDJumpEvent.class);

    private String StarSystem = null;

    private Coord StarPos = null;

    private String SystemAllegiance = null;

    private String SystemEconomy = null;

    private String SystemGovernment = null;

    private String SystemSecurity = null;

    private BigDecimal Population = null;

    private BigDecimal JumpDist = null;

    private BigDecimal FuelUsed = null;

    private BigDecimal FuelLevel = null;

    private List<String> Powers = null;

    private String PowerplayState = null;

    private List<FSDJumpEvent.Faction> Factions = null;

    private String SystemFaction = null;

    @Getter
    @Setter
    public static class Faction implements Serializable {

        private static final long serialVersionUID = 9069177073281049819L;

        private String Name = null;

        private String FactionState = null;

        private String Government = null;

        private String Allegiance = null;

        private BigDecimal Influence = null;

        private List<FSDJumpEvent.Faction.State> RecoveringStates = null;

        private List<FSDJumpEvent.Faction.State> PendingStates = null;

        @Getter
        @Setter
        public static class State implements Serializable {

            private static final long serialVersionUID = -8262884270535718116L;

            private String State = null;

            private Integer Trend = null;

        }

    }

}
