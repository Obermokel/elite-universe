package borg.ed.universe.repository;

import borg.ed.universe.model.StarSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * StarSystemRepository
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface StarSystemRepository extends ElasticsearchRepository<StarSystem, String> {

    @Query("{\"bool\": {\"must\": [{\"term\": {\"name\": \"?0\"}}]}}")
    Page<StarSystem> findByName(String name, Pageable pageable);

    Page<StarSystem> findByEddbId(Long eddbId, Pageable pageable);

    Page<StarSystem> findByEdsmId(Long edsmId, Pageable pageable);

}
