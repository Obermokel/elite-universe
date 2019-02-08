package borg.ed.galaxy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import borg.ed.galaxy.model.StarSystem;

/**
 * StarSystemRepository
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface StarSystemRepository extends ElasticsearchRepository<StarSystem, String> {

	@Query("{\"bool\": {\"must\": [{\"term\": {\"name\": \"?0\"}}]}}")
	Page<StarSystem> findByName(String name, Pageable pageable);

}
