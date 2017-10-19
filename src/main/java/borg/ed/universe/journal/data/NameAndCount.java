package borg.ed.universe.journal.data;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * NameAndCount
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
@Getter
@Setter
public class NameAndCount implements Serializable {

    private static final long serialVersionUID = 7936384583457307312L;

    static final Logger logger = LoggerFactory.getLogger(NameAndCount.class);

    private String Name = null;

    private Integer Count = null;

}
