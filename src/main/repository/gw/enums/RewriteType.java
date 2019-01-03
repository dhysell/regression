package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum RewriteType {
	NewTerm,
	FullTerm,
//	NewAccount,
	RemainderOfTerm;
	
	private static final List<RewriteType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final Random RANDOM = new Random();

	public static RewriteType random()  {
		return VALUES.get(RANDOM.nextInt(VALUES.size()));
	}
}
