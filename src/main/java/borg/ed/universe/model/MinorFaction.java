package borg.ed.universe.model;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import borg.ed.universe.constants.Allegiance;
import borg.ed.universe.constants.Government;
import borg.ed.universe.constants.State;
import borg.ed.universe.data.Coord;
import lombok.Getter;
import lombok.Setter;

/**
 * MinorFaction
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Document(indexName = "faction", type = "faction", shards = 5, replicas = 0)
public class MinorFaction implements Serializable, UniverseEntity {

	private static final long serialVersionUID = -171165054433075808L;

	static final Logger logger = LoggerFactory.getLogger(MinorFaction.class);

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

	private String homeSystemId = null;

	private Coord coord = null;

	@Field(type = FieldType.keyword)
	private String name = null;

	//@Field(type = FieldType.String)
	private Government government = null;

	//@Field(type = FieldType.String)
	private Allegiance allegiance = null;

	//@Field(type = FieldType.String)
	private State state = null;

	private Boolean isPlayerFaction = null;

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
		MinorFaction other = (MinorFaction) obj;
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

}
