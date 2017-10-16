package borg.ed.universe.journal;

import borg.ed.universe.data.Coord;
import borg.ed.universe.journal.events.AbstractJournalEvent;
import borg.ed.universe.journal.events.FSDJumpEvent;
import borg.ed.universe.journal.events.LoadoutEvent;
import borg.ed.universe.journal.events.ScanEvent;
import borg.ed.universe.util.GsonCoord;
import borg.ed.universe.util.GsonZonedDateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

/**
 * JournalEventReader
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class JournalEventReader {

    static final Logger logger = LoggerFactory.getLogger(JournalEventReader.class);

    private final Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTime()).registerTypeAdapter(Coord.class, new GsonCoord()).create();

    public AbstractJournalEvent readLine(String line) {
        if (StringUtils.isBlank(line)) {
            return null;
        } else {
            LinkedHashMap<String, Object> data = this.gson.fromJson(line, LinkedHashMap.class);
            String event = (String) data.get("event");

            switch (event) {
                case "FSDJump":
                    return this.gson.fromJson(line, FSDJumpEvent.class);
                case "Loadout":
                    return this.gson.fromJson(line, LoadoutEvent.class);
                case "Scan":
                    return this.gson.fromJson(line, ScanEvent.class);
                default:
                    logger.warn("Unknown event '" + event + "'");
                    return null;
            }
        }
    }

}
