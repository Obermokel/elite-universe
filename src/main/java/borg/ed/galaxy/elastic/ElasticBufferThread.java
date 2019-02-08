package borg.ed.galaxy.elastic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import borg.ed.galaxy.exceptions.NonUniqueResultException;
import borg.ed.galaxy.model.Body;
import borg.ed.galaxy.model.MinorFaction;
import borg.ed.galaxy.model.StarSystem;
import borg.ed.galaxy.model.Station;
import borg.ed.galaxy.repository.BodyRepository;
import borg.ed.galaxy.repository.MinorFactionRepository;
import borg.ed.galaxy.repository.StarSystemRepository;
import borg.ed.galaxy.repository.StationRepository;
import borg.ed.galaxy.service.GalaxyService;

public class ElasticBufferThread extends Thread {

	static final Logger logger = LoggerFactory.getLogger(ElasticBufferThread.class);

	public volatile boolean shutdown = false;

	@Autowired
	private GalaxyService galaxyService = null;

	@Autowired
	private StarSystemRepository starSystemRepository = null;

	@Autowired
	private BodyRepository bodyRepository = null;

	@Autowired
	private MinorFactionRepository minorFactionRepository = null;

	@Autowired
	private StationRepository stationRepository = null;

	private LinkedList<StarSystem> starSystemBuffer = new LinkedList<>();

	private LinkedList<Body> bodyBuffer = new LinkedList<>();

	private LinkedList<MinorFaction> minorFactionBuffer = new LinkedList<>();

	private LinkedList<Station> stationBuffer = new LinkedList<>();

	public ElasticBufferThread() {
		this.setName("ElasticBufferThread");
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
				if (this.starSystemBuffer.isEmpty()) {
					Thread.sleep(1);
				} else {
					List<StarSystem> starSystems = null;
					synchronized (this.starSystemBuffer) {
						starSystems = new ArrayList<>(this.starSystemBuffer);
						this.starSystemBuffer.clear();
						this.starSystemBuffer.notifyAll();
					}
					this.deleteDuplicateStarSystems(starSystems.stream().map(StarSystem::getName).collect(Collectors.toList()));
					this.starSystemRepository.saveAll(starSystems);
				}

				if (this.minorFactionBuffer.isEmpty()) {
					Thread.sleep(1);
				} else {
					List<MinorFaction> minorFactions = null;
					synchronized (this.minorFactionBuffer) {
						minorFactions = new ArrayList<>(this.minorFactionBuffer);
						this.minorFactionBuffer.clear();
						this.minorFactionBuffer.notifyAll();
					}
					this.minorFactionRepository.saveAll(minorFactions);
				}

				if (this.stationBuffer.isEmpty()) {
					Thread.sleep(1);
				} else {
					List<Station> stations = null;
					synchronized (this.stationBuffer) {
						stations = new ArrayList<>(this.stationBuffer);
						this.stationBuffer.clear();
						this.stationBuffer.notifyAll();
					}
					this.stationRepository.saveAll(stations);
				}

				if (this.bodyBuffer.isEmpty()) {
					Thread.sleep(1);
				} else {
					List<Body> bodies = null;
					synchronized (this.bodyBuffer) {
						bodies = new ArrayList<>(this.bodyBuffer);
						this.bodyBuffer.clear();
						this.bodyBuffer.notifyAll();
					}
					this.deleteDuplicateStarSystems(bodies.stream().map(Body::getStarSystemName).collect(Collectors.toList()));
					for (Body body : bodies) {
						this.ensureStarSystemForBody(body);
					}
					this.bodyRepository.saveAll(bodies);
				}
			} catch (InterruptedException e) {
				this.shutdown = true;
			}
		}
	}

	public void bufferStarSystem(StarSystem starSystem) throws InterruptedException {
		synchronized (this.starSystemBuffer) {
			if (this.starSystemBuffer.size() >= 1000) {
				//logger.debug("StarSystem buffer full");
				this.starSystemBuffer.wait();
				//logger.debug("StarSystem buffer ready");
			}
			this.starSystemBuffer.addLast(starSystem);
			this.starSystemBuffer.notifyAll();
		}
	}

	public void bufferBody(Body body) throws InterruptedException {
		synchronized (this.bodyBuffer) {
			if (this.bodyBuffer.size() >= 1000) {
				//logger.debug("Body buffer full");
				this.bodyBuffer.wait();
				//logger.debug("Body buffer ready");
			}
			this.bodyBuffer.addLast(body);
			this.bodyBuffer.notifyAll();
		}
	}

	public void bufferMinorFaction(MinorFaction minorFaction) throws InterruptedException {
		synchronized (this.minorFactionBuffer) {
			if (this.minorFactionBuffer.size() >= 1000) {
				//logger.debug("Body buffer full");
				this.minorFactionBuffer.wait();
				//logger.debug("Body buffer ready");
			}
			this.minorFactionBuffer.addLast(minorFaction);
			this.minorFactionBuffer.notifyAll();
		}
	}

	public void bufferStation(Station station) throws InterruptedException {
		synchronized (this.stationBuffer) {
			if (this.stationBuffer.size() >= 1000) {
				//logger.debug("Body buffer full");
				this.stationBuffer.wait();
				//logger.debug("Body buffer ready");
			}
			this.stationBuffer.addLast(station);
			this.stationBuffer.notifyAll();
		}
	}

	private void deleteDuplicateStarSystems(List<String> starSystemNames) {
		for (String starSystemName : starSystemNames) {
			try {
				this.galaxyService.findStarSystemByName(starSystemName);
			} catch (NonUniqueResultException e) {
				logger.warn("Duplicate star system. Will delete all of them: " + e.getOthers());
				for (String id : e.getOtherIds()) {
					this.starSystemRepository.deleteById(id);
				}
			}
		}
	}

	private void ensureStarSystemForBody(Body body) {
		Page<StarSystem> page = this.starSystemRepository.findByName(body.getStarSystemName(), PageRequest.of(0, 1));

		if (!page.hasContent()) {
			// Create a dummy star system
			StarSystem starSystem = new StarSystem();
			starSystem.setId(StarSystem.generateId(body.getCoord()));
			starSystem.setUpdatedAt(body.getUpdatedAt());
			starSystem.setCoord(body.getCoord());
			starSystem.setName(body.getStarSystemName());
			starSystem.setPopulation(BigDecimal.ZERO);
			this.starSystemRepository.index(starSystem);
		}
	}

}
