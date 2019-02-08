package borg.ed.galaxy.google;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.galaxy.journal.events.AbstractSystemJournalEvent;
import borg.ed.galaxy.journal.events.AbstractSystemJournalEvent.Faction;

public class GoogleBufferThread extends Thread {

	static final Logger logger = LoggerFactory.getLogger(GoogleBufferThread.class);

	private static final String spreadsheetId = "1z5USvjTp_htXdsd2o3qrm6DUgL7tlGmHoB8Xh51Fms0";

	public volatile boolean shutdown = false;

	private LinkedList<AbstractSystemJournalEvent> buffer = new LinkedList<>();

	public GoogleBufferThread() {
		this.setName("GoogleBufferThread");
		this.setDaemon(true);
	}

	@Override
	public void run() {
		logger.info(this.getName() + " started");

		this.flushBuffer();

		logger.info(this.getName() + " terminated");
	}

	void flushBuffer() {
		while (!Thread.currentThread().isInterrupted() && !this.shutdown) {
			try {
				if (this.buffer.isEmpty()) {
					Thread.sleep(1);
				} else {
					List<AbstractSystemJournalEvent> events = null;
					synchronized (this.buffer) {
						events = new ArrayList<>(this.buffer);
						this.buffer.clear();
						this.buffer.notifyAll();
					}
					for (AbstractSystemJournalEvent event : events) {
						try {
							this.updateFromSystemJournalEvent(event);
						} catch (IOException e) {
							logger.error("Google BGS update failed", e);
						} catch (NullPointerException e) {
							logger.error("Google BGS update failed", e);
						}
					}
				}
			} catch (InterruptedException e) {
				this.shutdown = true;
			}
		}
	}

	public void bufferEvent(AbstractSystemJournalEvent event) throws InterruptedException {
		synchronized (this.buffer) {
			if (this.buffer.size() >= 1000) {
				//logger.debug("StarSystem buffer full");
				this.buffer.wait();
				//logger.debug("StarSystem buffer ready");
			}
			this.buffer.addLast(event);
			this.buffer.notifyAll();
		}
	}

	private void updateFromSystemJournalEvent(AbstractSystemJournalEvent event) throws IOException {
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
