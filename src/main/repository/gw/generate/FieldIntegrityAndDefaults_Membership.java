package repository.gw.generate;

import org.testng.Assert;
import repository.gw.exception.GuidewireException;

public class FieldIntegrityAndDefaults_Membership {

    public void checkClassFieldIntegrityAndDefaults(GeneratePolicy policy) throws GuidewireException {
        if (policy.membership.getPolTermLengthDays() != 365) {
            Assert.fail("MEMBERSHIP POLICIES CAN ONLY BE ANNUAL, BUT YOU HAVE SELECTED A DIFFERENT NUMBER OF DAYS FOR YOUR POLICY.");
        }
    }
}


















