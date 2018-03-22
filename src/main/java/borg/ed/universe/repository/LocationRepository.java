package borg.ed.universe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import borg.ed.universe.model.Location;

public interface LocationRepository extends ElasticsearchRepository<Location, String> {

    @Query("{\"bool\": {\"must\": [{\"term\": {\"name\": \"?0\"}}]}}")
    Page<Location> findByName(String name, Pageable pageable);

}
