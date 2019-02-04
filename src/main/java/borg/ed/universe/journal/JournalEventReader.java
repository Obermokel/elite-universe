package borg.ed.universe.journal;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import borg.ed.universe.data.Coord;
import borg.ed.universe.journal.events.AbstractJournalEvent;
import borg.ed.universe.journal.events.CargoEvent;
import borg.ed.universe.journal.events.CommitCrimeEvent;
import borg.ed.universe.journal.events.DiedEvent;
import borg.ed.universe.journal.events.DiscoveryScanEvent;
import borg.ed.universe.journal.events.DockFighterEvent;
import borg.ed.universe.journal.events.DockedEvent;
import borg.ed.universe.journal.events.DockedEvent_v1;
import borg.ed.universe.journal.events.FSDJumpEvent;
import borg.ed.universe.journal.events.FSDJumpEvent_v1;
import borg.ed.universe.journal.events.FSDTargetEvent;
import borg.ed.universe.journal.events.FSSSignalDiscoveredEvent;
import borg.ed.universe.journal.events.FighterDestroyedEvent;
import borg.ed.universe.journal.events.FighterRebuiltEvent;
import borg.ed.universe.journal.events.FuelScoopEvent;
import borg.ed.universe.journal.events.LaunchFighterEvent;
import borg.ed.universe.journal.events.LoadGameEvent;
import borg.ed.universe.journal.events.LoadoutEvent;
import borg.ed.universe.journal.events.LocationEvent;
import borg.ed.universe.journal.events.LocationEvent_v1;
import borg.ed.universe.journal.events.MaterialsEvent;
import borg.ed.universe.journal.events.MultiSellExplorationDataEvent;
import borg.ed.universe.journal.events.MusicEvent;
import borg.ed.universe.journal.events.ReceiveTextEvent;
import borg.ed.universe.journal.events.ScanEvent;
import borg.ed.universe.journal.events.ScanEvent_v1;
import borg.ed.universe.journal.events.SellExplorationDataEvent;
import borg.ed.universe.journal.events.SendTextEvent;
import borg.ed.universe.journal.events.ShieldStateEvent;
import borg.ed.universe.journal.events.ShipTargetedEvent;
import borg.ed.universe.journal.events.StartJumpEvent;
import borg.ed.universe.journal.events.SupercruiseEntryEvent;
import borg.ed.universe.journal.events.SupercruiseExitEvent;
import borg.ed.universe.journal.events.UnderAttackEvent;
import borg.ed.universe.journal.events.UndockedEvent;
import borg.ed.universe.util.GsonCoord;
import borg.ed.universe.util.GsonZonedDateTime;

/**
 * JournalEventReader
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@SuppressWarnings("deprecation")
public class JournalEventReader {

	static final Logger logger = LoggerFactory.getLogger(JournalEventReader.class);

	private final Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTime()).registerTypeAdapter(Coord.class, new GsonCoord()).create();

	@SuppressWarnings("unchecked")
	public AbstractJournalEvent readLine(String line) {
		if (StringUtils.isBlank(line)) {
			return null;
		} else {
			try {
				LinkedHashMap<String, Object> data = this.gson.fromJson(line, LinkedHashMap.class);

				return this.readLine(line, (String) data.get("event"));
			} catch (JsonSyntaxException e) {
				logger.error("Failed to read journal event\n\t" + line + "\n\t" + e);
				return null;
			}
		}
	}

	public AbstractJournalEvent readLine(String line, String event) {
		switch (event) {
		case "Location":
			try {
				return this.gson.fromJson(line, LocationEvent.class);
			} catch (JsonSyntaxException e) {
				return this.gson.fromJson(line, LocationEvent_v1.class).toNewLocationEvent();
			}
		case "FSDTarget":
			return this.gson.fromJson(line, FSDTargetEvent.class);
		case "StartJump":
			return this.gson.fromJson(line, StartJumpEvent.class);
		case "FSDJump":
			try {
				return this.gson.fromJson(line, FSDJumpEvent.class);
			} catch (JsonSyntaxException e) {
				return this.gson.fromJson(line, FSDJumpEvent_v1.class).toNewFSDJumpEvent();
			}
		case "DiscoveryScan":
			return this.gson.fromJson(line, DiscoveryScanEvent.class);
		case "Scan":
			try {
				return this.gson.fromJson(line, ScanEvent.class);
			} catch (JsonSyntaxException e) {
				return this.gson.fromJson(line, ScanEvent_v1.class).toNewScanEvent();
			}
		case "FuelScoop":
			return this.gson.fromJson(line, FuelScoopEvent.class);
		case "SupercruiseEntry":
			return this.gson.fromJson(line, SupercruiseEntryEvent.class);
		case "SupercruiseExit":
			return this.gson.fromJson(line, SupercruiseExitEvent.class);
		case "Docked":
			try {
				return this.gson.fromJson(line, DockedEvent.class);
			} catch (JsonSyntaxException e) {
				return this.gson.fromJson(line, DockedEvent_v1.class).toNewDockedEvent();
			}
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
		case "MultiSellExplorationData":
			return this.gson.fromJson(line, MultiSellExplorationDataEvent.class);
		case "Died":
			return this.gson.fromJson(line, DiedEvent.class);
		case "ReceiveText":
			return this.gson.fromJson(line, ReceiveTextEvent.class);
		case "SendText":
			return this.gson.fromJson(line, SendTextEvent.class);
		case "Music":
			return this.gson.fromJson(line, MusicEvent.class);
		case "CommitCrime":
			return this.gson.fromJson(line, CommitCrimeEvent.class);
		case "DockFighter":
			return this.gson.fromJson(line, DockFighterEvent.class);
		case "FighterDestroyed":
			return this.gson.fromJson(line, FighterDestroyedEvent.class);
		case "FighterRebuilt":
			return this.gson.fromJson(line, FighterRebuiltEvent.class);
		case "LaunchFighter":
			return this.gson.fromJson(line, LaunchFighterEvent.class);
		case "ShieldState":
			return this.gson.fromJson(line, ShieldStateEvent.class);
		case "ShipTargeted":
			return this.gson.fromJson(line, ShipTargetedEvent.class);
		case "UnderAttack":
			return this.gson.fromJson(line, UnderAttackEvent.class);
		case "FSSSignalDiscovered":
			return this.gson.fromJson(line, FSSSignalDiscoveredEvent.class);
		case "Scanned":
		case "Bounty":
		case "RedeemVoucher":
		case "Screenshot":
		case "Friends":
		case "Fileheader":
		case "Powerplay":
		case "Progress":
		case "Rank":
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
	}

}
