package borg.ed.galaxy.exceptions;

import java.util.List;

/**
 * NonUniqueResultException
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class NonUniqueResultException extends Exception {

	private static final long serialVersionUID = -8673589451769027124L;

	private final List<String> otherIds;
	private final List<String> others;

	public NonUniqueResultException(String message, List<String> otherIds, List<String> others) {
		super(message);

		this.otherIds = otherIds;
		this.others = others;
	}

	public List<String> getOtherIds() {
		return otherIds;
	}

	public List<String> getOthers() {
		return this.others;
	}

}
