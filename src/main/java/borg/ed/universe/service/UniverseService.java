package borg.ed.universe.service;

import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * UniverseService
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface UniverseService {

    StarSystem findStarSystemByName(String name) throws NonUniqueResultException;

    MinorFaction findMinorFactionByName(String name) throws NonUniqueResultException;

    Body findBodyByName(String name) throws NonUniqueResultException;

    Page<StarSystem> findSystemsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Pageable pageable);

}
