package borg.ed.universe.journal;

import borg.ed.universe.data.Coord;
import borg.ed.universe.journal.events.AbstractJournalEvent;
import borg.ed.universe.journal.events.CargoEvent;
import borg.ed.universe.journal.events.DiedEvent;
import borg.ed.universe.journal.events.DockedEvent;
import borg.ed.universe.journal.events.FSDJumpEvent;
import borg.ed.universe.journal.events.FuelScoopEvent;
import borg.ed.universe.journal.events.LoadGameEvent;
import borg.ed.universe.journal.events.LoadoutEvent;
import borg.ed.universe.journal.events.LocationEvent;
import borg.ed.universe.journal.events.MaterialsEvent;
import borg.ed.universe.journal.events.ScanEvent;
import borg.ed.universe.journal.events.SellExplorationDataEvent;
import borg.ed.universe.journal.events.StartJumpEvent;
import borg.ed.universe.journal.events.SupercruiseEntryEvent;
import borg.ed.universe.journal.events.SupercruiseExitEvent;
import borg.ed.universe.journal.events.UndockedEvent;
import borg.ed.universe.util.GsonCoord;
import borg.ed.universe.util.GsonZonedDateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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

	private final Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTime())
			.registerTypeAdapter(Coord.class, new GsonCoord()).create();

	public AbstractJournalEvent readLine(String line) {
		if (StringUtils.isBlank(line)) {
			return null;
		} else {
			try {
                LinkedHashMap<String, Object> data = this.gson.fromJson(line, LinkedHashMap.class);
                String event = (String) data.get("event");

                switch (event) {
                    case "Location":
                        return this.gson.fromJson(line, LocationEvent.class);
                    case "StartJump":
                        return this.gson.fromJson(line, StartJumpEvent.class);
                case "FSDJump":
                	return this.gson.fromJson(line, FSDJumpEvent.class);
                    case "Scan":
                        return this.gson.fromJson(line, ScanEvent.class);
                    case "FuelScoop":
                        return this.gson.fromJson(line, FuelScoopEvent.class);
                    case "SupercruiseEntry":
                        return this.gson.fromJson(line, SupercruiseEntryEvent.class);
                    case "SupercruiseExit":
                        return this.gson.fromJson(line, SupercruiseExitEvent.class);
                    case "Docked":
                        return this.gson.fromJson(line, DockedEvent.class);
                    case "Undocked":
                        return this.gson.fromJson(line, UndockedEvent.class);
                    case "LoadGame":
                        return this.gson.fromJson(line, LoadGameEvent.class);
                case "Loadout":
                	return this.gson.fromJson(line, LoadoutEvent.class);
                    case "Cargo":
                        return this.gson.fromJson(line, CargoEvent.class);
                    case "Materials":
                        return this.gson.fromJson(line, MaterialsEvent.class);
                    case "SellExplorationData":
                        return this.gson.fromJson(line, SellExplorationDataEvent.class);
                    case "Died":
                        return this.gson.fromJson(line, DiedEvent.class);
                    case "ReceiveText":
                    case "SendText":
                    case "Scanned":
                    case "CommitCrime":
                    case "Bounty":
                    case "RedeemVoucher":
                    case "Screenshot":
                    case "Music":
                    case "Friends":
                    case "Fileheader":
                    case "Progress":
                    case "Rank":
                    case "ShieldState":
                    case "HullDamage":
                    case "DockingRequested":
                    case "DockingGranted":
                    case "BuyAmmo":
                    case "Repair":
                    case "RepairAll":
                    case "RestockVehicle":
                        return null; // Irrelevant
                default:
                        logger.debug("Unknown event '" + event + "'");
                	return null;
                }
            } catch (JsonSyntaxException e) {
                logger.error("Failed to read journal event\n\t" + line + "\n\t" + e);
                return null;
            }
		}
	}

}
