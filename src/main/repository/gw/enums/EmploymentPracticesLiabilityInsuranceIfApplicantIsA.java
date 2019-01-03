package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EmploymentPracticesLiabilityInsuranceIfApplicantIsA {

    EMPLOYEE_LEASING_FIRM("Employee leasing firm"),
    TEMPORARY_HELP_FIRM("Temporary help firm"),
    PRIVATE_GOLF_CLUB("Private golf club"),
    SCHOOL("School"),
    NONE_OF_THE_ABOVE("None of the above");

    String value;

    EmploymentPracticesLiabilityInsuranceIfApplicantIsA(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    private static final List<EmploymentPracticesLiabilityInsuranceIfApplicantIsA> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final Random RANDOM = new Random();

    public static EmploymentPracticesLiabilityInsuranceIfApplicantIsA random() {
        return VALUES.get(RANDOM.nextInt(VALUES.size()));
    }

}
