package borg.ed.galaxy.eddn;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import borg.ed.galaxy.exceptions.NonUniqueResultException;
import borg.ed.galaxy.exceptions.SuspiciousDataException;
import borg.ed.galaxy.journal.JournalEventReader;
import borg.ed.galaxy.journal.events.AbstractJournalEvent;

public class EddnBufferThread extends Thread {

	static final Logger logger = LoggerFactory.getLogger(EddnBufferThread.class);

	private static final String SCHEMA_JOURNAL_v1 = "https://eddn.edcd.io/schemas/journal/1";
	private static final String SCHEMA_JOURNAL_v1_TEST = "https://eddn.edcd.io/schemas/journal/1/test";
	private static final String SCHEMA_COMMODITY_v3 = "https://eddn.edcd.io/schemas/commodity/3";
	private static final String SCHEMA_COMMODITY_v3_TEST = "https://eddn.edcd.io/schemas/commodity/3/test";
	private static final String SCHEMA_BLACKMARKET_v1 = "https://eddn.edcd.io/schemas/blackmarket/1";
	private static final String SCHEMA_SHIPYARD_v2 = "https://eddn.edcd.io/schemas/shipyard/2";
	private static final String SCHEMA_SHIPYARD_v2_TEST = "https://eddn.edcd.io/schemas/shipyard/2/test";
	private static final String SCHEMA_OUTFITTING_v2 = "https://eddn.edcd.io/schemas/outfitting/2";
	private static final String SCHEMA_OUTFITTING_v2_TEST = "https://eddn.edcd.io/schemas/outfitting/2/test";

	public volatile boolean shutdown = false;

	private final Gson gson = new Gson();

	private LinkedList<String> jsonStringBuffer = new LinkedList<>();

	private final List<EddnUpdateListener> listeners = new ArrayList<>();

	@Autowired
	private JournalEventReader journalEventReader = null;

	public EddnBufferThread() {
		this.setName("EddnBufferThread");
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
				if (this.jsonStringBuffer.isEmpty()) {
					Thread.sleep(1);
				} else {
					List<String> jsonStrings = null;
					synchronized (this.jsonStringBuffer) {
						jsonStrings = new ArrayList<>(this.jsonStringBuffer);
						this.jsonStringBuffer.clear();
						this.jsonStringBuffer.notifyAll();
					}
					for (String jsonString : jsonStrings) {
						try {
							this.handleMessage(jsonString);
						} catch (NonUniqueResultException e) {
							logger.error("NonUniqueResultException: " + e.getMessage() + "\n\t" + e.getOthers() + "\n\t" + jsonString);
						} catch (SuspiciousDataException e) {
							//logger.warn("SuspiciousDataException: " + e.getMessage() + "\n\t" + jsonString);
						}
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void handleMessage(String jsonMessage) throws NonUniqueResultException, SuspiciousDataException {
		//System.out.println(jsonMessage);

		LinkedHashMap<String, Object> data = this.gson.fromJson(jsonMessage, LinkedHashMap.class);
		String schemaRef = (String) data.get("$schemaRef");

		if (SCHEMA_JOURNAL_v1.equals(schemaRef)) {
			Map<String, Object> header = (Map<String, Object>) data.get("header");
			ZonedDateTime gatewayTimestamp = ZonedDateTime.parse((String) header.get("gatewayTimestamp"));
			String uploaderID = (String) header.get("uploaderID");
			Map<String, Object> message = (Map<String, Object>) data.get("message");

			AbstractJournalEvent journalEvent = this.journalEventReader.readLine(this.gson.toJson(message));

			for (EddnUpdateListener listener : this.listeners) {
				try {
					listener.onNewJournalMessage(gatewayTimestamp, uploaderID, journalEvent);
				} catch (NullPointerException e) {
					logger.warn(listener + " failed for message:\n" + jsonMessage, e);
				} catch (Exception e) {
					logger.warn(listener + " failed: " + e);
				}
			}
		} else if (SCHEMA_JOURNAL_v1_TEST.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_COMMODITY_v3.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_COMMODITY_v3_TEST.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_BLACKMARKET_v1.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_SHIPYARD_v2.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_SHIPYARD_v2_TEST.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_OUTFITTING_v2.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_OUTFITTING_v2_TEST.equals(schemaRef)) {
			// NOOP
		} else {
			logger.warn("Unknown schemaRef: " + schemaRef);
		}
	}

	public void bufferJsonString(String jsonString) throws InterruptedException {
		synchronized (this.jsonStringBuffer) {
			if (this.jsonStringBuffer.size() >= 10_000) {
				logger.warn("JSON string buffer full");
				this.jsonStringBuffer.wait();
				logger.info("JSON string buffer ready");
			}
			this.jsonStringBuffer.addLast(jsonString);
			this.jsonStringBuffer.notifyAll();
		}
	}

	public boolean addListener(EddnUpdateListener listener) {
		if (listener == null || this.listeners.contains(listener)) {
			return false;
		} else {
			return this.listeners.add(listener);
		}
	}

	public boolean removeListener(EddnUpdateListener listener) {
		if (listener == null) {
			return false;
		} else {
			return this.listeners.remove(listener);
		}
	}

}
