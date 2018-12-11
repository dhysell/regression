package repository.gw.generate;


import org.testng.Assert;
import org.testng.SkipException;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;

public class FieldIntegrityAndDefaults_StandardFire {
    public void checkClassFieldIntegrityAndDefaults(GeneratePolicy policy) {


        for (PolicyLocation location : policy.standardFire.getLocationList()) {
            for (PLPolicyLocationProperty property : location.getPropertyList()) {
                property.setAddress(location.getAddress());
            }
        }

        String locationAddress = "";
        int lcv = 0;
        for (PolicyLocation location : policy.standardFire.getLocationList()) {
            if (location.getAddress().getLine1().equals(locationAddress)) {
                Assert.fail("You must have a different address for each location. Please specify a different address for each location.");
            }
            if (lcv == 0) {
                locationAddress = location.getAddress().getLine1();
                lcv++;
            }
            for (PLPolicyLocationProperty locationProperty : location.getPropertyList()) {
                for (AdditionalInterest additionalint : locationProperty.getBuildingAdditionalInterest()) {
                    additionalint.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
                }
            }
        }


        //checks Standard Liability
        if (policy.lineSelection.contains(repository.gw.enums.LineSelection.StandardLiabilityPL)) {
            if (!policy.lineSelection.contains(repository.gw.enums.LineSelection.StandardLiabilityPL)) {
            	Assert.fail("You must select at least one LineSelection for Standard Liability");
            }
        }

        //checks Standard Fire
        if (policy.lineSelection.contains(repository.gw.enums.LineSelection.StandardFirePL)) {
            if (!policy.lineSelection.contains(repository.gw.enums.LineSelection.StandardFirePL)) {
            	Assert.fail("You must select at least one LineSelection for Standard Fire");
            }
        }


        //StandardFire policy used for StdLiability
        if (policy.lineSelection.contains(repository.gw.enums.LineSelection.StandardLiabilityPL) && policy.stdFireLiability == true) {
            if (policy.standardFire == null) {
                throw new SkipException("standardFireAndLiability can't be null because you must first have a StandardFire to have a StandardLiability Policy");
            }
            policy.standardFire.setCreateNew(repository.gw.enums.CreateNew.Do_Not_Create_New);
        }// end if LineSelection.StandardLiabilityPL

        //Create Multiple StdFire policies
        if (policy.lineSelection.contains(LineSelection.StandardFirePL) && policy.multipleStdFire == true) {
            policy.standardFire.setCreateNew(repository.gw.enums.CreateNew.Do_Not_Create_New);
        }//end if LineSelection.StandardFirePL


        //Create Standard Fire by using Standard IM
        if (policy.productType.equals(ProductLineType.StandardFire) && policy.stdIMStdFire == true) {
            if (policy.standardInlandMarine == null) {
            	Assert.fail("standardInlandMarine can't be null");
            }
            policy.standardFire.setCreateNew(CreateNew.Do_Not_Create_New);
        }//end Create Standard Fire by using Standard IM


    }//END OF METHOD
}
