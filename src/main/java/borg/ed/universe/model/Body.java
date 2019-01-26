package borg.ed.universe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import borg.ed.universe.constants.AtmosphereType;
import borg.ed.universe.constants.BodyAtmosphere;
import borg.ed.universe.constants.Element;
import borg.ed.universe.constants.PlanetClass;
import borg.ed.universe.constants.ReserveLevel;
import borg.ed.universe.constants.RingClass;
import borg.ed.universe.constants.StarClass;
import borg.ed.universe.constants.TerraformingState;
import borg.ed.universe.constants.VolcanismType;
import borg.ed.universe.data.Coord;
import borg.ed.universe.util.PasswordUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Body
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
@Document(indexName = "body", type = "body", shards = 5, replicas = 0)
public class Body implements Serializable, UniverseEntity {

	private static final long serialVersionUID = 4859358199300000217L;

	static final Logger logger = LoggerFactory.getLogger(Body.class);

	@Id
	private String id = null;

	@Field(type = FieldType.Date, format = DateFormat.date_time_no_millis)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
	private Date createdAt = null;

	@Field(type = FieldType.Date, format = DateFormat.date_time_no_millis)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
	private Date updatedAt = null;

	@Field(type = FieldType.keyword)
	private String firstDiscoveredBy = null;

	@Field(type = FieldType.keyword)
	private String starSystemId = null;

	@Field(type = FieldType.keyword)
	private String starSystemName = null;

	private Coord coord = null;

	@Field(type = FieldType.keyword)
	private String name = null;

	@Field(type = FieldType.Double)
	private BigDecimal distanceToArrival = null;

	//@Field(type = FieldType.String)
	private StarClass starClass = null;

	//@Field(type = FieldType.String)
	private PlanetClass planetClass = null;

	@Field(type = FieldType.Double)
	private BigDecimal surfaceTemperature = null;

	@Field(type = FieldType.Double)
	private BigDecimal age = null;

	@Field(type = FieldType.Double)
	private BigDecimal solarMasses = null;

	//@Field(type = FieldType.String)
	private VolcanismType volcanismType = null;

	//@Field(type = FieldType.String)
	private AtmosphereType atmosphereType = null;

	//@Field(type = FieldType.String)
	private TerraformingState terraformingState = null;

	@Field(type = FieldType.Double)
	private BigDecimal earthMasses = null;

	@Field(type = FieldType.Double)
	private BigDecimal radius = null; // km

	@Field(type = FieldType.Double)
	private BigDecimal gravity = null; // G

	@Field(type = FieldType.Double)
	private BigDecimal surfacePressure = null;

	@Field(type = FieldType.Double)
	private BigDecimal orbitalPeriod = null;

	@Field(type = FieldType.Double)
	private BigDecimal semiMajorAxis = null;

	@Field(type = FieldType.Double)
	private BigDecimal orbitalEccentricity = null;

	@Field(type = FieldType.Double)
	private BigDecimal orbitalInclination = null;

	@Field(type = FieldType.Double)
	private BigDecimal argOfPeriapsis = null;

	@Field(type = FieldType.Double)
	private BigDecimal rotationalPeriod = null;

	private Boolean tidallyLocked = null;

	@Field(type = FieldType.Double)
	private BigDecimal axisTilt = null;

	private Boolean isLandable = null;

	//@Field(type = FieldType.String)
	private ReserveLevel reserves = null;

	@Field(type = FieldType.Nested)
	private List<Ring> rings = null;

	@Field(type = FieldType.Nested)
	private List<AtmosphereShare> atmosphereShares = null;

	@Field(type = FieldType.Nested)
	private List<MaterialShare> materialShares = null;

	@Getter
	@Setter
	public static class Ring implements Serializable {

		private static final long serialVersionUID = -2813238406926285560L;

		@Field(type = FieldType.keyword)
		private String name = null;

		//@Field(type = FieldType.String)
		private RingClass ringClass = null;

		@Field(type = FieldType.Double)
		private BigDecimal massMT = null;

		@Field(type = FieldType.Double)
		private BigDecimal innerRadius = null;

		@Field(type = FieldType.Double)
		private BigDecimal outerRadius = null;

	}

	@Getter
	@Setter
	public static class AtmosphereShare implements Serializable {

		private static final long serialVersionUID = -8496873742946186047L;

		//@Field(type = FieldType.String)
		private BodyAtmosphere name = null;

		@Field(type = FieldType.Double)
		private BigDecimal percent = null;

	}

	@Getter
	@Setter
	public static class MaterialShare implements Serializable {

		private static final long serialVersionUID = 6775268926323537568L;

		//@Field(type = FieldType.String)
		private Element name = null;

		@Field(type = FieldType.Double)
		private BigDecimal percent = null;

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
		Body other = (Body) obj;
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
		return PasswordUtil.md5(
				String.format(Locale.US, "%.4f:%.4f:%.4f|%s", this.getCoord().getX(), this.getCoord().getY(), this.getCoord().getZ(), this.getName().replace(this.getStarSystemName(), "")));
	}

}
