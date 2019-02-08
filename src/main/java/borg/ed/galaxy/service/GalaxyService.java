package borg.ed.galaxy.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CloseableIterator;

import borg.ed.galaxy.constants.PlanetClass;
import borg.ed.galaxy.constants.StarClass;
import borg.ed.galaxy.data.Coord;
import borg.ed.galaxy.exceptions.NonUniqueResultException;
import borg.ed.galaxy.model.Body;
import borg.ed.galaxy.model.Body.MaterialShare;
import borg.ed.galaxy.model.MinorFaction;
import borg.ed.galaxy.model.StarSystem;

/**
 * GalaxyService
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface GalaxyService {

	StarSystem findStarSystemByName(String name) throws NonUniqueResultException;

	Map<String, StarSystem> findStarSystemsByName(Collection<String> names, boolean deleteDuplicates);

	StarSystem findNearestSystem(Coord coord);

	Page<StarSystem> findSystemsNear(Coord coord, float maxDistance, Pageable pageable);

	Page<StarSystem> findSystemsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Pageable pageable);

	MinorFaction findMinorFactionByName(String name) throws NonUniqueResultException;

	Body findBodyByName(String name) throws NonUniqueResultException;

	Map<String, Body> findBodiesByName(Collection<String> names, boolean deleteDuplicates);

	List<Body> findBodiesByStarSystemName(String starSystemName);

	CloseableIterator<StarSystem> streamAllSystemsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto);

	CloseableIterator<Body> streamStarsNear(Coord coord, float range, Boolean isMainStar, Collection<StarClass> starClasses);

	CloseableIterator<Body> streamStarsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isMainStar, Collection<StarClass> starClasses);

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

	Page<Body> findPlanetsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses,
			Pageable pageable);

	CloseableIterator<Body> streamPlanetsNear(Coord coord, float range, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses);

	CloseableIterator<Body> streamPlanetsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses);

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
