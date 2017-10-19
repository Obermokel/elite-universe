package borg.ed.universe.repository;

import borg.ed.universe.model.Body;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * BodyRepository
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface BodyRepository extends ElasticsearchRepository<Body, String> {

    @Query("{\"bool\": {\"must\": [{\"term\": {\"name\": \"?0\"}}]}}")
    Page<Body> findByName(String name, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"term\": {\"starSystemName\": \"?0\"}}]}}")
    Page<Body> findByStarSystemName(String starSystemName, Pageable pageable);

    Page<Body> findByEddbId(Long eddbId, Pageable pageable);

    Page<Body> findByEdsmId(Long edsmId, Pageable pageable);

}
