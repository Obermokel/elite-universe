package borg.ed.universe.exceptions;

import java.util.List;

/**
 * NonUniqueResultException
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class NonUniqueResultException extends Exception {

    private static final long serialVersionUID = -8673589451769027124L;

    private final List<String> others;

    public NonUniqueResultException(String message, List<String> others) {
        super(message);

        this.others = others;
    }

    public List<String> getOthers() {
        return this.others;
    }

}
