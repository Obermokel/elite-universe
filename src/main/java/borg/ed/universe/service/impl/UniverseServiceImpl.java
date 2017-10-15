package borg.ed.universe.service.impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;
import borg.ed.universe.repository.BodyRepository;
import borg.ed.universe.repository.MinorFactionRepository;
import borg.ed.universe.repository.StarSystemRepository;
import borg.ed.universe.service.UniverseService;

@Service
public class UniverseServiceImpl implements UniverseService {

	@Autowired
	private StarSystemRepository starSystemRepository = null;

	@Autowired
	private BodyRepository bodyRepository = null;

	@Autowired
	private MinorFactionRepository minorFactionRepository = null;

	@Override
	public StarSystem findStarSystemByName(String name) throws NonUniqueResultException {
		Page<StarSystem> page = this.starSystemRepository.findByName(name, new PageRequest(0, 10));

		if (page.getTotalElements() < 1) {
			return null;
		} else if (page.getTotalElements() > 1) {
			throw new NonUniqueResultException("Found " + page.getTotalElements() + " systems for '" + name + "'",
					page.getContent().stream().map(StarSystem::toString).collect(Collectors.toList()));
		} else {
			return page.getContent().get(0);
		}
	}

	@Override
	public MinorFaction findMinorFactionByName(String name) throws NonUniqueResultException {
		Page<MinorFaction> page = this.minorFactionRepository.findByName(name, new PageRequest(0, 10));

		if (page.getTotalElements() < 1) {
			return null;
		} else if (page.getTotalElements() > 1) {
			throw new NonUniqueResultException("Found " + page.getTotalElements() + " minor factions for '" + name + "'",
					page.getContent().stream().map(MinorFaction::toString).collect(Collectors.toList()));
		} else {
			return page.getContent().get(0);
		}
	}

	@Override
	public Body findBodyByName(String name) throws NonUniqueResultException {
		Page<Body> page = this.bodyRepository.findByName(name, new PageRequest(0, 10));

		if (page.getTotalElements() < 1) {
			return null;
		} else if (page.getTotalElements() > 1) {
			throw new NonUniqueResultException("Found " + page.getTotalElements() + " bodies for '" + name + "'",
					page.getContent().stream().map(Body::toString).collect(Collectors.toList()));
		} else {
			return page.getContent().get(0);
		}
	}

}
