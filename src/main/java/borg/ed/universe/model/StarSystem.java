package borg.ed.universe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import borg.ed.universe.constants.Allegiance;
import borg.ed.universe.constants.Economy;
import borg.ed.universe.constants.Government;
import borg.ed.universe.constants.Power;
import borg.ed.universe.constants.PowerState;
import borg.ed.universe.constants.ReserveLevel;
import borg.ed.universe.constants.State;
import borg.ed.universe.constants.SystemSecurity;
import borg.ed.universe.data.Coord;
import lombok.Getter;
import lombok.Setter;

/**
 * StarSystem
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Document(indexName = "universe", type = "starsystem", replicas = 0)
public class StarSystem implements Serializable, UniverseEntity {

	private static final long serialVersionUID = 4133521407269215822L;

	static final Logger logger = LoggerFactory.getLogger(StarSystem.class);

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

	@Field(type = FieldType.keyword)
	private String firstDiscoveredBy = null;

	private Coord coord = null;

	@Field(type = FieldType.keyword)
	private String name = null;

	@Field(type = FieldType.Long)
	private BigDecimal population = null;

	//@Field(type = FieldType.String)
	private Government government = null;

	//@Field(type = FieldType.String)
	private Allegiance allegiance = null;

	//@Field(type = FieldType.String)
	private State state = null;

	//@Field(type = FieldType.String)
	private SystemSecurity security = null;

	//@Field(type = FieldType.String)
	private Economy economy = null;

	private List<Power> powers = null;

	//@Field(type = FieldType.String)
	private PowerState powerState = null;

	//@Field(type = FieldType.String)
	private ReserveLevel reserves = null;

	private Boolean needsPermit = null;

	@Field(type = FieldType.keyword)
	private String controllingMinorFactionName = null;

	@Field(type = FieldType.Nested)
	private List<FactionPresence> minorFactionPresences = null;

	@Getter
	@Setter
	public static class FactionPresence implements Serializable {

		private static final long serialVersionUID = 7199415515520928969L;

		@Field(type = FieldType.keyword)
		private String name = null;

		private List<State> recoveringStates = null;

		//@Field(type = FieldType.String)
		private State state = null;

		private List<State> pendingStates = null;

		@Field(type = FieldType.Double)
		private BigDecimal influence = null;

	}

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
		StarSystem other = (StarSystem) obj;
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

	public float distanceTo(StarSystem other) {
		return this.getCoord().distanceTo(other.getCoord());
	}

}
