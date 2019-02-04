package borg.ed.universe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import borg.ed.universe.constants.Economy;
import borg.ed.universe.constants.State;
import borg.ed.universe.constants.StationType;
import borg.ed.universe.data.Coord;
import borg.ed.universe.util.PasswordUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Station
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Document(indexName = "station", type = "station", shards = 5, replicas = 0)
public class Station implements Serializable, UniverseEntity {

	private static final long serialVersionUID = -1509169585188272322L;

	static final Logger logger = LoggerFactory.getLogger(Station.class);

	@Id
	private String id = null;

	@Field(type = FieldType.Date, format = DateFormat.date_time_no_millis)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
	private Date updatedAt = null;

	@Field(type = FieldType.keyword)
	private String starSystemId = null;

	@Field(type = FieldType.keyword)
	private String starSystemName = null;

	private Coord coord = null;

	@Field(type = FieldType.keyword)
	private String name = null;

	//@Field(type = FieldType.String)
	private StationType stationType = null;

	private Boolean isPlanetary = null;

	@Field(type = FieldType.keyword)
	private String maxLandingPadSize = null;

	private BigDecimal marketId = null;

	@Field(type = FieldType.Double)
	private BigDecimal distanceToArrivalLs = null;

	@Field(type = FieldType.keyword)
	private String minorFactionId = null;

	@Field(type = FieldType.keyword)
	private String minorFactionName = null;

	//@Field(type = FieldType.String)
	private State minorFactionState = null;

	//@Field(type = FieldType.String)
	private Economy primaryEconomy = null;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Station other = (Station) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "[" + id + "] " + name + " (" + coord + ")";
	}

	public String generateId() {
		return generateId(this.getCoord(), this.getName());
	}

	public static String generateId(Coord coord, String name) {
		return PasswordUtil.md5(String.format(Locale.US, "%.2f:%.2f:%.2f|%s", coord.getX(), coord.getY(), coord.getZ(), name));
	}

}
