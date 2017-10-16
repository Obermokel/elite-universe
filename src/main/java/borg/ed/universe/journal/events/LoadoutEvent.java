package borg.ed.universe.journal.events;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * LoadoutEvent
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class LoadoutEvent extends AbstractJournalEvent {

    private static final long serialVersionUID = -724240268570555632L;

    static final Logger logger = LoggerFactory.getLogger(LoadoutEvent.class);

    private String Ship = null;

    private Long ShipID = null;

    private String ShipName = null;

    private String ShipIdent = null;

    private List<LoadoutEvent.Module> Modules = null;

    @Getter
    @Setter
    public static class Module implements Serializable {

        private static final long serialVersionUID = 3542264235454057646L;

        private String Slot = null;

        private String Item = null;

        private Boolean On = null;

        private Integer Priority = null;

        private BigDecimal Health = null;

        private BigDecimal Value = null;

        private String EngineerBlueprint = null;

        private Integer EngineerLevel = null;

    }

}
