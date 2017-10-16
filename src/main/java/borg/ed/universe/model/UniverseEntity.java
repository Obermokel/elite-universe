package borg.ed.universe.model;

import java.util.Date;

/**
 * UniverseEntity
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public interface UniverseEntity {

    String getId();

    Long getEddbId();

    Long getEdsmId();

    Date getCreatedAt();

    Date getUpdatedAt();

}
