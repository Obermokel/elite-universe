package borg.ed.universe.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.constants.TerraformingState;
import borg.ed.universe.data.Coord;
import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.Body.MaterialShare;
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
		Page<StarSystem> page = this.starSystemRepository.findByName(name, PageRequest.of(0, 10));

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
	public Map<String, StarSystem> findStarSystemsByName(Collection<String> names, boolean deleteDuplicates) {
		final int max = 10000;

		if (names.size() > max) {
			throw new IllegalArgumentException("Max " + max + " names allowed, but was " + names.size());
		} else {
			Map<String, StarSystem> result = new HashMap<>();
			List<StarSystem> duplicates = new ArrayList<>();

			BoolQueryBuilder qbRoot = QueryBuilders.boolQuery();
			BoolQueryBuilder qbNames = QueryBuilders.boolQuery();
			for (String name : names) {
				if (StringUtils.isNotEmpty(name)) {
					qbNames.should(QueryBuilders.termQuery("name", name));
				}
			}
			qbRoot.must(qbNames);
			SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qbRoot).withIndices("starsystem").withTypes("starsystem").withPageable(PageRequest.of(0, max)).build();
			AggregatedPage<StarSystem> page = this.elasticsearchTemplate.queryForPage(searchQuery, StarSystem.class);

			if (page.getTotalElements() > max) {
				throw new RuntimeException("Total elements found is greater than " + max);
			} else if (page.hasContent()) {
				for (StarSystem starSystem : page.getContent()) {
					if (!result.containsKey(starSystem.getName())) {
						result.put(starSystem.getName(), starSystem);
					} else {
						duplicates.add(starSystem);
					}
				}
			}

			if (deleteDuplicates && !duplicates.isEmpty()) {
				List<StarSystem> moreDuplicates = new ArrayList<>();
				for (StarSystem duplicate : duplicates) {
					StarSystem firstDuplicate = result.remove(duplicate.getName());
					if (firstDuplicate != null) {
						moreDuplicates.add(firstDuplicate);
					}
				}
				this.starSystemRepository.deleteAll(duplicates);
				this.starSystemRepository.deleteAll(moreDuplicates);
			}

			return result;
		}
	}

	@Override
	public StarSystem findNearestSystem(Coord coord) {
		logger.debug("Searching closest system to " + coord);

		StarSystem nearestSystem = null;
		Float nearestSystemDistance = null;
		for (float range = 2; range <= 16384 && nearestSystem == null; range *= 2) {
			Page<StarSystem> page = this.findSystemsNear(coord, range, PageRequest.of(0, 1000));
			while (page != null) {
				for (StarSystem system : page.getContent()) {
					float distance = system.getCoord().distanceTo(coord);
					if (nearestSystemDistance == null || distance < nearestSystemDistance) {
						nearestSystemDistance = distance;
						nearestSystem = system;
					}
				}
				if (page.hasNext()) {
					page = this.findSystemsNear(coord, range, page.nextPageable());
				} else {
					page = null;
				}
			}
		}

		return nearestSystem;
	}

	@Override
	public Page<StarSystem> findSystemsNear(Coord coord, float maxDistance, Pageable pageable) {
		return this.findSystemsWithin(coord.getX() - maxDistance, coord.getX() + maxDistance, coord.getY() - maxDistance, coord.getY() + maxDistance, coord.getZ() - maxDistance,
				coord.getZ() + maxDistance, pageable);
	}

	@Override
	public Page<StarSystem> findSystemsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Pageable pageable) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.rangeQuery("coord.x").gte(xfrom).lte(xto));
		qb.must(QueryBuilders.rangeQuery("coord.y").gte(yfrom).lte(yto));
		qb.must(QueryBuilders.rangeQuery("coord.z").gte(zfrom).lte(zto));
		logger.trace("findSystemsWithin.qb={}", qb);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("starsystem").withTypes("starsystem").withPageable(pageable).build();
		return this.elasticsearchTemplate.queryForPage(searchQuery, StarSystem.class);
	}

	@Override
	public MinorFaction findMinorFactionByName(String name) throws NonUniqueResultException {
		Page<MinorFaction> page = this.minorFactionRepository.findByName(name, PageRequest.of(0, 10));

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
		Page<Body> page = this.bodyRepository.findByName(name, PageRequest.of(0, 10));

		if (page.getTotalElements() < 1) {
			return null;
		} else if (page.getTotalElements() > 1) {
			throw new NonUniqueResultException("Found " + page.getTotalElements() + " bodies for '" + name + "'", page.getContent().stream().map(Body::toString).collect(Collectors.toList()));
		} else {
			return page.getContent().get(0);
		}
	}

	@Override
	public Map<String, Body> findBodiesByName(Collection<String> names, boolean deleteDuplicates) {
		final int max = 10000;

		if (names.size() > max) {
			throw new IllegalArgumentException("Max " + max + " names allowed, but was " + names.size());
		} else {
			Map<String, Body> result = new HashMap<>();
			List<Body> duplicates = new ArrayList<>();

			BoolQueryBuilder qbRoot = QueryBuilders.boolQuery();
			BoolQueryBuilder qbNames = QueryBuilders.boolQuery();
			for (String name : names) {
				if (StringUtils.isNotEmpty(name)) {
					qbNames.should(QueryBuilders.termQuery("name", name));
				}
			}
			qbRoot.must(qbNames);
			SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qbRoot).withIndices("body").withTypes("body").withPageable(PageRequest.of(0, max)).build();
			AggregatedPage<Body> page = this.elasticsearchTemplate.queryForPage(searchQuery, Body.class);

			if (page.getTotalElements() > max) {
				throw new RuntimeException("Total elements found is greater than " + max);
			} else if (page.hasContent()) {
				for (Body body : page.getContent()) {
					if (!result.containsKey(body.getName())) {
						result.put(body.getName(), body);
					} else {
						duplicates.add(body);
					}
				}
			}

			if (deleteDuplicates && !duplicates.isEmpty()) {
				List<Body> moreDuplicates = new ArrayList<>();
				for (Body duplicate : duplicates) {
					Body firstDuplicate = result.remove(duplicate.getName());
					if (firstDuplicate != null) {
						moreDuplicates.add(firstDuplicate);
					}
				}
				this.bodyRepository.deleteAll(duplicates);
				this.bodyRepository.deleteAll(moreDuplicates);
			}

			return result;
		}
	}

	@Override
	public List<Body> findBodiesByStarSystemName(String starSystemName) {
		return this.bodyRepository.findByStarSystemName(starSystemName, PageRequest.of(0, 1000)).getContent();
	}

	@Override
	public CloseableIterator<StarSystem> streamAllSystemsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.rangeQuery("coord.x").gte(xfrom).lte(xto));
		qb.must(QueryBuilders.rangeQuery("coord.y").gte(yfrom).lte(yto));
		qb.must(QueryBuilders.rangeQuery("coord.z").gte(zfrom).lte(zto));
		logger.trace("streamAllSystemsWithin.qb={}", qb);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("starsystem").withTypes("starsystem").withPageable(PageRequest.of(0, 10000)).build();
		return this.elasticsearchTemplate.stream(searchQuery, StarSystem.class);
	}

	@Override
	public CloseableIterator<Body> streamStarsNear(Coord coord, float range, Boolean isMainStar, Collection<StarClass> starClasses) {
		return this.streamStarsWithin(coord.getX() - range, coord.getX() + range, coord.getY() - range, coord.getY() + range, coord.getZ() - range, coord.getZ() + range, isMainStar,
				starClasses);
	}

	@Override
	public CloseableIterator<Body> streamStarsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isMainStar, Collection<StarClass> starClasses) {
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
		logger.trace("streamStarsWithin.qb={}", qb);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("body").withTypes("body").withPageable(PageRequest.of(0, 10000)).build();
		return this.elasticsearchTemplate.stream(searchQuery, Body.class);
	}

	@Override
	public Page<Body> findPlanetsNear(Coord coord, float range, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses, Pageable pageable) {
		return this.findPlanetsWithin(coord.getX() - range, coord.getX() + range, coord.getY() - range, coord.getY() + range, coord.getZ() - range, coord.getZ() + range,
				isTerraformingCandidate, planetClasses, pageable);
	}

	@Override
	public Page<Body> findPlanetsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses,
			Pageable pageable) {
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
		logger.trace("findPlanetsWithin.qb={}", qb);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("body").withTypes("body").withPageable(pageable).build();
		return this.elasticsearchTemplate.queryForPage(searchQuery, Body.class);
	}

	@Override
	public CloseableIterator<Body> streamPlanetsNear(Coord coord, float range, Boolean isTerraformingCandidate, Collection<PlanetClass> planetClasses) {
		return this.streamPlanetsWithin(coord.getX() - range, coord.getX() + range, coord.getY() - range, coord.getY() + range, coord.getZ() - range, coord.getZ() + range,
				isTerraformingCandidate, planetClasses);
	}

	@Override
	public CloseableIterator<Body> streamPlanetsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Boolean isTerraformingCandidate,
			Collection<PlanetClass> planetClasses) {
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
		logger.trace("streamPlanetsWithin.qb={}", qb);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("body").withTypes("body").withPageable(PageRequest.of(0, 10000)).build();
		return this.elasticsearchTemplate.stream(searchQuery, Body.class);
	}

	@Override
	public Page<Body> findPlanetsHavingElementsNear(Coord coord, float range, Collection<MaterialShare> elements, Pageable pageable) {
		return this.findPlanetsHavingElementsWithin(coord.getX() - range, coord.getX() + range, coord.getY() - range, coord.getY() + range, coord.getZ() - range, coord.getZ() + range,
				elements, pageable);
	}

	@Override
	public Page<Body> findPlanetsHavingElementsWithin(float xfrom, float xto, float yfrom, float yto, float zfrom, float zto, Collection<MaterialShare> elements, Pageable pageable) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.rangeQuery("coord.x").gte(xfrom).lte(xto));
		qb.must(QueryBuilders.rangeQuery("coord.y").gte(yfrom).lte(yto));
		qb.must(QueryBuilders.rangeQuery("coord.z").gte(zfrom).lte(zto));
		qb.must(QueryBuilders.termQuery("isLandable", Boolean.TRUE));
		for (MaterialShare element : elements) {
			BoolQueryBuilder elementAndPercentQuery = QueryBuilders.boolQuery();
			elementAndPercentQuery.must(QueryBuilders.termQuery("materialShares.name.keyword", element.getName().name()));
			if (element.getPercent() != null) {
				elementAndPercentQuery.must(QueryBuilders.rangeQuery("materialShares.percent").gte(element.getPercent().doubleValue()));
			}
			NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("materialShares", elementAndPercentQuery, ScoreMode.None);
			qb.must(nestedQuery);
		}
		logger.trace("findPlanetsHavingElementsWithin.qb={}", qb);
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb).withIndices("body").withTypes("body").withPageable(pageable).build();
		return this.elasticsearchTemplate.queryForPage(searchQuery, Body.class);
	}

}
