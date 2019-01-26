package borg.ed.universe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import borg.ed.universe.model.Station;

/**
 * StationRepository
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface StationRepository extends ElasticsearchRepository<Station, String> {

	@Query("{\"bool\": {\"must\": [{\"term\": {\"name\": \"?0\"}}]}}")
	Page<Station> findByName(String name, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"range\": {\"coord.x\": {\"gte\": ?0, \"lte\": ?1}}}, {\"range\": {\"coord.y\": {\"gte\": ?2, \"lte\": ?3}}}, {\"range\": {\"coord.z\": {\"gte\": ?4, \"lte\": ?5}}}]}}]}}")
	Page<Station> findByCoordWithin(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax, Pageable pageable);

}
