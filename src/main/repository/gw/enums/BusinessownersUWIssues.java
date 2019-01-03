package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum BusinessownersUWIssues {

	ResidentialBuildingMoreThan30YearsOldRequiresUnderwritingApproval("Residential Building More Than 30 Years Old, Requires Underwriting Approval.");
	String value;

	private BusinessownersUWIssues(String type){
		value = type;
	}

	public String getValue(){
		return value;
	}

	public static BusinessownersUWIssues valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "").replace(",", "").replace("/", "_").replace(".", "");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	private static final List<BusinessownersUWIssues> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static BusinessownersUWIssues random()  {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}



}
