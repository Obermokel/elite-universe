package borg.ed.universe.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.constants.TerraformingState;
import borg.ed.universe.data.Coord;
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

	static final Logger logger = LoggerFactory.getLogger(UniverseServiceImpl.class);

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate = null;

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
	public StarSystem findStarSystemByEddbId(Long eddbId) throws NonUniqueResultException {
		Page<StarSystem> page = this.starSystemRepository.findByEddbId(eddbId, new PageRequest(0, 10));

		if (page.getTotalElements() < 1) {
			return null;
		} else if (page.getTotalElements() > 1) {
			throw new NonUniqueResultException("Found " + page.getTotalElements() + " systems for eddbId " + eddbId,
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

	@Override
	public List<Body> findBodiesByStarSystemName(String starSystemName) {
		return this.bodyRepository.findByStarSystemName(starSystemName, PageRequest.of(0, 1000)).getContent();
	}

	@Override
	public Page<StarSystem> findSystemsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Pageable pageable) {
		return this.starSystemRepository.findByCoordWithin(xfrom, xto, yfrom, yto, zfrom, zto, pageable);
	}

	@Override
	public Page<Body> findStarsNear(Coord coord, float range, Boolean isMainStar, Collection<StarClass> starClasses, Pageable pageable) {
		return this.findStarsWithin(coord.getX() - range, coord.getX() + range, coord.getY() - range, coord.getY() + range, coord.getZ() - range,
				coord.getZ() + range, isMainStar, starClasses, pageable);
	}

	@Override
	public Page<Body> findStarsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isMainStar,
			Collection<StarClass> starClasses, Pageable pageable) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.rangeQuery("coord.x").gte(xfrom).lte(xto));
		qb.must(QueryBuilders.rangeQuery("coord.y").gte(yfrom).lte(yto));
		qb.must(QueryBuilders.rangeQuery("coord.z").gte(zfrom).lte(zto));
		if (Boolean.TRUE.equals(isMainStar)) {
			qb.must(QueryBuilders.rangeQuery("distanceToArrival").lte(0));
		} else if (Boolean.FALSE.equals(isMainStar)) {
			qb.must(QueryBuilders.rangeQuery("distanceToArrival").gt(0));
		}
		if (starClasses == null || starClasses.isEmpty()) {
			qb.must(QueryBuilders.existsQuery("starClass.keyword"));
		} else {
			BoolQueryBuilder starClassIn = QueryBuilders.boolQuery();
			for (StarClass starClass : starClasses) {
				starClassIn.should(QueryBuilders.termQuery("starClass.keyword", starClass.name()));
			}
			qb.must(starClassIn);
		}
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("universe").withTypes("body").withPageable(pageable).build();
		return this.elasticsearchTemplate.queryForPage(searchQuery, Body.class);
	}

	@Override
	public Page<Body> findPlanetsNear(Coord coord, float range, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses, Pageable pageable) {
		return this.findPlanetsWithin(coord.getX() - range, coord.getX() + range, coord.getY() - range, coord.getY() + range, coord.getZ() - range,
				coord.getZ() + range, isTerraformingCandidate, planetClasses, pageable);
	}

	@Override
	public Page<Body> findPlanetsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isTerraformingCandidate,
			Collection<PlanetClass> planetClasses, Pageable pageable) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.rangeQuery("coord.x").gte(xfrom).lte(xto));
		qb.must(QueryBuilders.rangeQuery("coord.y").gte(yfrom).lte(yto));
		qb.must(QueryBuilders.rangeQuery("coord.z").gte(zfrom).lte(zto));
		if (Boolean.TRUE.equals(isTerraformingCandidate)) {
			qb.must(QueryBuilders.termQuery("terraformingState.keyword", TerraformingState.TERRAFORMABLE.name()));
		} else if (Boolean.FALSE.equals(isTerraformingCandidate)) {
			qb.mustNot(QueryBuilders.termQuery("terraformingState.keyword", TerraformingState.TERRAFORMABLE.name()));
		}
		if (planetClasses == null || planetClasses.isEmpty()) {
			qb.must(QueryBuilders.existsQuery("planetClass.keyword"));
		} else {
			BoolQueryBuilder starClassIn = QueryBuilders.boolQuery();
			for (PlanetClass planetClass : planetClasses) {
				starClassIn.should(QueryBuilders.termQuery("planetClass.keyword", planetClass.name()));
			}
			qb.must(starClassIn);
		}
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("universe").withTypes("body").withPageable(pageable).build();
		return this.elasticsearchTemplate.queryForPage(searchQuery, Body.class);
	}

}
