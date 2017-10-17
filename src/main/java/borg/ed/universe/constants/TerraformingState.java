package borg.ed.universe.constants;

import org.apache.commons.lang.StringUtils;

/**
 * TerraformingState
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public enum TerraformingState {

	TERRAFORMABLE, TERRAFORMING, TERRAFORMED;

	public static TerraformingState fromJournalValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			switch (value) {
			case "Not terraformable":
				return null;
			case "Terraformable":
			case "Candidate for terraforming":
				return TerraformingState.TERRAFORMABLE;
			case "Terraforming":
			case "Being terraformed":
				return TerraformingState.TERRAFORMING;
			case "Terraformed":
			case "Terraforming completed":
				return TerraformingState.TERRAFORMED;
			default:
				throw new IllegalArgumentException("Unknown value '" + value + "' for TerraformingState");
			}
		}
	}

}
