package borg.ed.universe.eddn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;

import borg.ed.universe.converter.JournalConverter;
import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.exceptions.SuspiciousDataException;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;
import borg.ed.universe.repository.BodyRepository;
import borg.ed.universe.repository.MinorFactionRepository;
import borg.ed.universe.repository.StarSystemRepository;
import borg.ed.universe.service.UniverseService;

public class EddnReaderThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(EddnReaderThread.class);

	private static final String RELAY = "tcp://eddn.edcd.io:9500";

	private static final String SCHEMA_JOURNAL_v1 = "https://eddn.edcd.io/schemas/journal/1";
	private static final String SCHEMA_JOURNAL_v1_TEST = "https://eddn.edcd.io/schemas/journal/1/test";
	private static final String SCHEMA_COMMODITY_v3 = "https://eddn.edcd.io/schemas/commodity/3";
	private static final String SCHEMA_BLACKMARKET_v1 = "https://eddn.edcd.io/schemas/blackmarket/1";
	private static final String SCHEMA_SHIPYARD_v2 = "https://eddn.edcd.io/schemas/shipyard/2";
	private static final String SCHEMA_OUTFITTING_v2 = "https://eddn.edcd.io/schemas/outfitting/2";

	private static final String JOURNAL_EVENT_FSDJUMP = "FSDJump";
	private static final String JOURNAL_EVENT_SCAN = "Scan";
	private static final String JOURNAL_EVENT_DOCKED = "Docked";
	private static final String JOURNAL_EVENT_LOCATION = "Location";

	private final Gson gson = new Gson();

	private Context context = null;
	private Socket socket = null;

	@Autowired
	private UniverseService universeService = null;

	@Autowired
	private JournalConverter journalConverter = null;

	@Autowired
	private StarSystemRepository starSystemRepository = null;

	@Autowired
	private BodyRepository bodyRepository = null;

	@Autowired
	private MinorFactionRepository minorFactionRepository = null;

	public EddnReaderThread() {
		this.setName("EddnReaderThread");
		this.setDaemon(true);
	}

	@Override
	public void run() {
		logger.info(this.getName() + " started");

		this.connect();

		while (!Thread.currentThread().isInterrupted()) {
			try {
				byte[] compressed = socket.recv(0);
				byte[] decompressed = this.decompress(compressed);
				String jsonMessage = decompressed == null ? null : new String(decompressed, "UTF-8");

				if (jsonMessage == null) {
					logger.debug("Seems like we lost the ZMQ connection, will try to reconnect");
					this.disconnect();
					this.connect();
				} else {
					try {
						this.handleMessage(jsonMessage);
					} catch (NonUniqueResultException e) {
						logger.error("NonUniqueResultException: " + e.getMessage() + "\n\t" + e.getOthers() + "\n\t" + jsonMessage);
					} catch (SuspiciousDataException e) {
						//logger.warn("SuspiciousDataException: " + e.getMessage() + "\n\t" + jsonMessage);
					}
				}
			} catch (IOException | DataFormatException e) {
				logger.error("Exception in " + this.getName(), e);
			} catch (Exception e) {
				logger.error("Exception in " + this.getName(), e);
			}
		}

		this.disconnect();

		logger.info(this.getName() + " terminated");
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
			ZonedDateTime timestamp = ZonedDateTime.parse((String) message.get("timestamp"));
			String event = (String) message.get("event");

			if (JOURNAL_EVENT_FSDJUMP.equals(event)) {
				this.handleFsdJump(gatewayTimestamp, uploaderID, timestamp, message);
			} else if (JOURNAL_EVENT_SCAN.equals(event)) {
				this.handleScan(gatewayTimestamp, uploaderID, timestamp, message);
			} else if (JOURNAL_EVENT_DOCKED.equals(event)) {
				// TODO JOURNAL_EVENT_DOCKED
			} else if (JOURNAL_EVENT_LOCATION.equals(event)) {
				// TODO JOURNAL_EVENT_LOCATION
			} else {
				logger.warn("Unknown journal event: " + event);
			}
		} else if (SCHEMA_JOURNAL_v1_TEST.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_COMMODITY_v3.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_BLACKMARKET_v1.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_SHIPYARD_v2.equals(schemaRef)) {
			// NOOP
		} else if (SCHEMA_OUTFITTING_v2.equals(schemaRef)) {
			// NOOP
		} else {
			logger.warn("Unknown schemaRef: " + schemaRef);
		}
	}

	private void handleFsdJump(ZonedDateTime gatewayTimestamp, String uploaderID, ZonedDateTime timestamp, Map<String, Object> journalData)
			throws SuspiciousDataException, NonUniqueResultException {
		try {
			ZonedDateTime nowPlusTenMinutes = ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(10);

			if (timestamp.isAfter(nowPlusTenMinutes)) {
				throw new SuspiciousDataException("Received data from the future: " + timestamp + " > " + nowPlusTenMinutes);
			} else {
				this.readStarSystem(uploaderID, timestamp, journalData);
				this.readMinorFactions(uploaderID, timestamp, journalData);
			}
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void readStarSystem(String uploaderID, ZonedDateTime timestamp, Map<String, Object> journalData) throws NonUniqueResultException {
		StarSystem currentStarSystem = this.journalConverter.fsdJumpToStarSystem(journalData);
		StarSystem existingStarSystem = this.universeService.findStarSystemByName(currentStarSystem.getName());

		if (existingStarSystem == null) {
			currentStarSystem.setCreatedAt(Date.from(timestamp.toInstant()));
			currentStarSystem.setUpdatedAt(Date.from(timestamp.toInstant()));
			currentStarSystem.setFirstDiscoveredBy(uploaderID);

			this.starSystemRepository.index(currentStarSystem);

			logger.info("New star system discovered by " + uploaderID + ": " + currentStarSystem);

			// TODO See if there is another starsystem at the same coords. If so, it may have been renamed. Delete the old, including bodies, stations etc.
		} else if (!Date.from(timestamp.toInstant()).after(existingStarSystem.getUpdatedAt())) {
			logger.trace("Received outdated information for " + existingStarSystem);
		} else {
			this.updateStarSystem(existingStarSystem, currentStarSystem, timestamp);

			this.starSystemRepository.index(existingStarSystem);

			logger.trace("Updated " + existingStarSystem);
		}
	}

	private void readMinorFactions(String uploaderID, ZonedDateTime timestamp, Map<String, Object> journalData) throws NonUniqueResultException {
		List<MinorFaction> currentMinorFactions = this.journalConverter.fsdJumpToMinorFactions(journalData);

		if (currentMinorFactions != null) {
			for (MinorFaction currentMinorFaction : currentMinorFactions) {
				MinorFaction existingMinorFaction = this.universeService.findMinorFactionByName(currentMinorFaction.getName());

				if (existingMinorFaction == null) {
					currentMinorFaction.setCreatedAt(Date.from(timestamp.toInstant()));
					currentMinorFaction.setUpdatedAt(Date.from(timestamp.toInstant()));

					this.minorFactionRepository.index(currentMinorFaction);

					// TODO See if there is another starsystem at the same coords. If so, it may have been renamed. Delete the old, including bodies, stations etc.
				} else if (!Date.from(timestamp.toInstant()).after(existingMinorFaction.getUpdatedAt())) {
					logger.trace("Received outdated information for " + existingMinorFaction);
				} else {
					this.updateMinorFaction(existingMinorFaction, currentMinorFaction, timestamp);

					this.minorFactionRepository.index(existingMinorFaction);

					logger.trace("Updated " + existingMinorFaction);
				}
			}
		}
	}

	private void updateStarSystem(StarSystem oldData, StarSystem newData, ZonedDateTime timestamp) {
		oldData.setUpdatedAt(Date.from(timestamp.toInstant()));

		//oldData.setCoord(newData.getCoord());
		//oldData.setName(newData.getName());
		oldData.setPopulation(newData.getPopulation());
		oldData.setGovernment(newData.getGovernment());
		oldData.setAllegiance(newData.getAllegiance());
		oldData.setState(newData.getState());
		oldData.setSecurity(newData.getSecurity());
		oldData.setEconomy(newData.getEconomy());
		oldData.setPowers(newData.getPowers());
		oldData.setPowerState(newData.getPowerState());
		oldData.setPopulation(newData.getPopulation());
		//oldData.setNeedsPermit(newData.getNeedsPermit());
		oldData.setControllingMinorFactionName(newData.getControllingMinorFactionName());
		oldData.setMinorFactionPresences(newData.getMinorFactionPresences());
	}

	private void updateMinorFaction(MinorFaction oldData, MinorFaction newData, ZonedDateTime timestamp) {
		oldData.setUpdatedAt(Date.from(timestamp.toInstant()));

		//oldData.setHomeSystemId(newData.getHomeSystemId());
		//oldData.setCoord(newData.getCoord());
		//oldData.setName(newData.getName());
		oldData.setGovernment(newData.getGovernment());
		oldData.setAllegiance(newData.getAllegiance());
		if (newData.getState() != borg.ed.universe.constants.State.NONE) {
			oldData.setState(newData.getState());
		}
		//oldData.setIsPlayerFaction(newData.getIsPlayerFaction());
	}

	private void handleScan(ZonedDateTime gatewayTimestamp, String uploaderID, ZonedDateTime timestamp, Map<String, Object> journalData)
			throws SuspiciousDataException, NonUniqueResultException {
		try {
			ZonedDateTime nowPlusTenMinutes = ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(10);

			if (timestamp.isAfter(nowPlusTenMinutes)) {
				throw new SuspiciousDataException("Received data from the future: " + timestamp + " > " + nowPlusTenMinutes);
			} else {
				this.readBody(uploaderID, timestamp, journalData);
			}
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void readBody(String uploaderID, ZonedDateTime timestamp, Map<String, Object> journalData) throws NonUniqueResultException {
		Body currentBody = this.journalConverter.scanToBody(journalData);
		Body existingBody = this.universeService.findBodyByName(currentBody.getName());

		if (existingBody == null) {
			currentBody.setCreatedAt(Date.from(timestamp.toInstant()));
			currentBody.setUpdatedAt(Date.from(timestamp.toInstant()));
			currentBody.setFirstDiscoveredBy(uploaderID);

			this.bodyRepository.index(currentBody);

			logger.trace("New body discovered by " + uploaderID + ": " + currentBody);

			// TODO See if there is another starsystem at the same coords. If so, it may have been renamed. Delete the old, including bodies, stations etc.
		} else if (!Date.from(timestamp.toInstant()).after(existingBody.getUpdatedAt())) {
			logger.trace("Received outdated information for " + existingBody);
		} else {
			this.updateBody(existingBody, currentBody, timestamp);

			this.bodyRepository.index(existingBody);

			logger.trace("Updated " + existingBody);
		}
	}

	private void updateBody(Body oldData, Body newData, ZonedDateTime timestamp) {
		oldData.setUpdatedAt(Date.from(timestamp.toInstant()));

		//oldData.setStarSystemId(newData.getStarSystemId());
		//oldData.setStarSystemName(newData.getStarSystemName());
		//oldData.setCoord(newData.getCoord());
		//oldData.setName(newData.getName());
		oldData.setDistanceToArrival(newData.getDistanceToArrival());
		oldData.setStarClass(newData.getStarClass());
		oldData.setPlanetClass(newData.getPlanetClass());
		oldData.setSurfaceTemperature(newData.getSurfaceTemperature());
		oldData.setAge(newData.getAge());
		oldData.setSolarMasses(newData.getSolarMasses());
		oldData.setVolcanismType(newData.getVolcanismType());
		oldData.setAtmosphereType(newData.getAtmosphereType());
		oldData.setTerraformingState(newData.getTerraformingState());
		oldData.setEarthMasses(newData.getEarthMasses());
		oldData.setRadius(newData.getRadius());
		oldData.setGravity(newData.getGravity());
		oldData.setSurfacePressure(newData.getSurfacePressure());
		oldData.setOrbitalPeriod(newData.getOrbitalPeriod());
		oldData.setOrbitalInclination(newData.getOrbitalInclination());
		oldData.setArgOfPeriapsis(newData.getArgOfPeriapsis());
		oldData.setRotationalPeriod(newData.getRotationalPeriod());
		oldData.setTidallyLocked(newData.getTidallyLocked());
		oldData.setAxisTilt(newData.getAxisTilt());
		oldData.setIsLandable(newData.getIsLandable());
		oldData.setReserves(newData.getReserves());
		oldData.setRings(newData.getRings());
		oldData.setAtmosphereShares(newData.getAtmosphereShares());
		oldData.setMaterialShares(newData.getMaterialShares());
		oldData.setCompositionShares(newData.getCompositionShares());
	}

	private void connect() {
		logger.debug("Connecting to " + RELAY);
		context = ZMQ.context(1);
		socket = context.socket(ZMQ.SUB);
		socket.subscribe(new byte[0]);
		// socket.setReceiveTimeOut(600000);
		socket.connect(RELAY);
		logger.debug("Connected");
	}

	private void disconnect() {
		logger.debug("Disconnecting");
		socket.close();
		context.term();
		logger.debug("Disconnected");
	}

	private byte[] decompress(byte[] compressed) throws IOException, DataFormatException {
		if (compressed == null) {
			return null;
		} else {
			Inflater inflater = new Inflater();
			inflater.setInput(compressed);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressed.length);
			byte[] buffer = new byte[1024];
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
			return outputStream.toByteArray();
		}
	}

}
