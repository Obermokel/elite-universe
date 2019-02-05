package borg.ed.universe.eddn;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.google.GoogleSpreadsheet;
import borg.ed.universe.google.GoogleTable;
import borg.ed.universe.journal.events.AbstractJournalEvent;
import borg.ed.universe.journal.events.AbstractSystemJournalEvent;
import borg.ed.universe.journal.events.AbstractSystemJournalEvent.Faction;

/**
 * EddnGoogleBgsUpdater
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class EddnGoogleBgsUpdater implements EddnUpdateListener {

	static final Logger logger = LoggerFactory.getLogger(EddnGoogleBgsUpdater.class);

	private static final String spreadsheetId = "1z5USvjTp_htXdsd2o3qrm6DUgL7tlGmHoB8Xh51Fms0";

	@Override
	public void onNewJournalMessage(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractJournalEvent event) {
		try {
			ZonedDateTime nowPlusTenMinutes = ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(10);

			if (event == null || event.getTimestamp() == null) {
				// NOOP
			} else if (event.getTimestamp().isAfter(nowPlusTenMinutes)) {
				logger.warn("Received data from the future: " + event.getTimestamp() + " > " + nowPlusTenMinutes + ", uploaderID=" + uploaderID);
			} else if (event instanceof AbstractSystemJournalEvent) {
				this.handleSystemEvent(gatewayTimestamp, uploaderID, (AbstractSystemJournalEvent) event);
			} else {
				//logger.warn("Unknown journal event: " + event);
			}
		} catch (NonUniqueResultException | IOException e) {
			logger.error("Google BGS update failed", e);
		} catch (NullPointerException e) {
			logger.error("Google BGS update failed", e);
		}
	}

	void handleSystemEvent(ZonedDateTime gatewayTimestamp, String uploaderID, AbstractSystemJournalEvent event) throws NonUniqueResultException, IOException {
		try {
			// GPL present?
			if (isGplPresent(event.getFactions())) {
				final String starSystemTableName = event.getStarSystem().toUpperCase().replaceAll("\\W", "_");
				final String date = getBgsDate(event.getTimestamp());
				final String tableNameInfluence = "INFLUENCE_" + starSystemTableName;
				GoogleSpreadsheet gplInfluence = new GoogleSpreadsheet(spreadsheetId, tableNameInfluence);
				GoogleTable tblInfluence = gplInfluence.getTable(tableNameInfluence);
				String tableNameState = "STATE_" + starSystemTableName;
				GoogleSpreadsheet gplState = new GoogleSpreadsheet(spreadsheetId, tableNameState);
				GoogleTable tblState = gplState.getTable(tableNameState);

				for (Faction faction : event.getFactions()) {
					logger.info(event.getStarSystem() + ": " + faction);

					String factionName = faction.getName().toUpperCase();

					if (faction.getInfluence() != null && faction.getInfluence().floatValue() > 0) {
						String influence = String.format(Locale.GERMANY, "%.4f%%", faction.getInfluence().multiply(new BigDecimal(100)));
						int colIdx = tblInfluence.getColumnIndex(factionName);
						if (colIdx < 0) {
							logger.debug("Adding column '" + factionName + "' to table '" + tableNameInfluence + "'");
							colIdx = tblInfluence.addColumn(factionName);
						}
						int rowIdx = tblInfluence.getRowIndex(date);
						if (rowIdx < 0) {
							logger.debug("Adding row '" + date + "' to table '" + tableNameInfluence + "'");
							rowIdx = tblInfluence.addRow(date);
						}
						String existingValue = tblInfluence.getCellValue(rowIdx, colIdx);
						if (StringUtils.isNotEmpty(existingValue) && !existingValue.equals(influence)) {
							logger.debug("Overwriting " + existingValue + " with " + influence + " for " + factionName + " (" + date + ")");
						}
						tblInfluence.setCellValue(rowIdx, colIdx, influence);
					}

					if (faction.getFactionState() != null && faction.getInfluence() != null && faction.getInfluence().floatValue() > 0) {
						String state = faction.getFactionState().toUpperCase();
						int colIdx = tblState.getColumnIndex(factionName);
						if (colIdx < 0) {
							logger.debug("Adding column '" + factionName + "' to table '" + tableNameState + "'");
							colIdx = tblState.addColumn(factionName);
						}
						int rowIdx = tblState.getRowIndex(date);
						if (rowIdx < 0) {
							logger.debug("Adding row '" + date + "' to table '" + tableNameState + "'");
							rowIdx = tblState.addRow(date);
						}
						String existingValue = tblState.getCellValue(rowIdx, colIdx);
						if (StringUtils.isNotEmpty(existingValue) && !existingValue.equals(state)) {
							logger.debug("Overwriting " + existingValue + " with " + state + " for " + factionName + " (" + date + ")");
						}
						tblState.setCellValue(rowIdx, colIdx, state);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private boolean isGplPresent(List<Faction> factions) {
		if (factions == null || factions.isEmpty()) {
			return false;
		} else {
			return factions.stream().filter(f -> "German Pilot Lounge".equals(f.getName())).findFirst().isPresent();
		}
	}

	private String getBgsDate(ZonedDateTime eventTimestamp) {
		// TODO Before/after tick?
		//        int eventHour = eventTimestamp.getHour();
		//        int bgsTickAtHour = getBgsTickAtHour();
		//
		//        if(eventHour >= bgsTickAtHour) {
		//            // Generally treat as today, except if the tick hour is very late in the evening
		//            if(bgsTickAtHour >= 23) {
		//
		//            }
		//        }
		//
		//        return new SimpleDateFormat("dd.MM.yyyy").format(eventTimestamp);
		return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(eventTimestamp);
	}

	//    private int getBgsTickAtHour() {
	//        return 13; // TODO Estimate from top-visited systems like Shinrarta and/or Deciat
	//    }

}
