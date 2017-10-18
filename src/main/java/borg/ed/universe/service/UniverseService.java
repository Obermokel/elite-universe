package borg.ed.universe.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.data.Coord;
import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.Body.MaterialShare;
import borg.ed.universe.model.MinorFaction;
import borg.ed.universe.model.StarSystem;

/**
 * UniverseService
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface UniverseService {

	StarSystem findStarSystemByName(String name) throws NonUniqueResultException;

	StarSystem findStarSystemByEddbId(Long eddbId) throws NonUniqueResultException;

	MinorFaction findMinorFactionByName(String name) throws NonUniqueResultException;

	Body findBodyByName(String name) throws NonUniqueResultException;

	List<Body> findBodiesByStarSystemName(String starSystemName);

	Page<StarSystem> findSystemsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Pageable pageable);

	Page<Body> findStarsNear(Coord coord, float range, Boolean isMainStar, Collection<StarClass> starClasses, Pageable pageable);

	Page<Body> findStarsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isMainStar, Collection<StarClass> starClasses,
			Pageable pageable);

    /**
     * @param coord
     * @param range
     *         Ly
     * @param isTerraformingCandidate
     *         <code>true</code>, <code>false</code> or <code>null</code> for any
     * @param planetClasses
     *         <code>null</code> or empty for any
     * @param pageable
     * @return
     */
	Page<Body> findPlanetsNear(Coord coord, float range, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses, Pageable pageable);

	Page<Body> findPlanetsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isTerraformingCandidate,
			Collection<PlanetClass> planetClasses, Pageable pageable);

    /**
     * @param coord
     * @param range
     *         Ly
     * @param elements
     *         <code>null</code> or empty for any. All elements must occur on the planet, i.e. they are combined via logical AND.
     *         The percentage can be <code>null</code> for any, otherwise percentage of that element must be gte that value.
     * @param pageable
     * @return
     */
    Page<Body> findPlanetsHavingElementsNear(Coord coord, float range, Collection<MaterialShare> elements, Pageable pageable);

    Page<Body> findPlanetsHavingElementsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Collection<MaterialShare> elements, Pageable pageable);

}
