package borg.ed.universe.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import borg.ed.universe.constants.Allegiance;
import borg.ed.universe.constants.State;
import borg.ed.universe.data.Coord;
import borg.ed.universe.util.PasswordUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(indexName = "location", type = "location", shards = 5, replicas = 0)
public class Location implements Serializable {

	private static final long serialVersionUID = 6001141103475118725L;

	@Id
	private String id = null;

	@Field(type = FieldType.Date, format = DateFormat.date_time_no_millis)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
	private Date timestamp = null;

	@Field(type = FieldType.keyword)
	private String uploaderId = null;

	private Boolean docked = null;

	private Coord coord = null;

	@Field(type = FieldType.keyword)
	private String starSystem = null;

	@Field(type = FieldType.keyword)
	private String body = null;

	@Field(type = FieldType.keyword)
	private String station = null;

	@Field(type = FieldType.keyword)
	private String faction = null;

	private Allegiance allegiance = null;

	private State state = null;

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
		Location other = (Location) obj;
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
		return "[" + id + "] " + starSystem + " (" + coord + ")";
	}

	public static String generateId(Location location) {
		String key = new SimpleDateFormat("yyyyMMddHHmmss").format(location.getTimestamp());
		key += "|";
		key += location.getStarSystem();
		key += "|";
		key += location.getUploaderId();
		return PasswordUtil.md5(key);
	}

}
