package borg.ed.universe.service;

import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;

/**
 * UniverseService
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface UniverseService {

	StarSystem findStarSystemByName(String name) throws NonUniqueResultException;

	MinorFaction findMinorFactionByName(String name) throws NonUniqueResultException;

	Body findBodyByName(String name) throws NonUniqueResultException;

}
