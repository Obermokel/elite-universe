package borg.ed.universe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import borg.ed.universe.data.Coord;
import lombok.Getter;
import lombok.Setter;

/**
 * Station
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Document(indexName = "universe", type = "station", replicas = 0)
public class Station implements Serializable {

	private static final long serialVersionUID = -1509169585188272322L;

	static final Logger logger = LoggerFactory.getLogger(Station.class);

	@Id
	private String id = null;

	private Long eddbId = null;

	private Long edsmId = null;

	@Field(type = FieldType.Date, format = DateFormat.date_time_no_millis)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
	private Date createdAt = null;

	@Field(type = FieldType.Date, format = DateFormat.date_time_no_millis)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
	private Date updatedAt = null;

	private Long starSystemId = null;

	private Long bodyId = null;

	private Coord coord = null;

	@Field(type = FieldType.keyword)
	private String name = null;

	@Field(type = FieldType.Double)
	private BigDecimal distanceToArrival = null;

	@Field(type = FieldType.keyword)
	private String controllingMinorFactionName = null;

	// TODO isPlanetery, maxLandingPadSize etc

}
