package repository.gw.generate;

import org.testng.Assert;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;

public class FieldIntegrityAndDefaults_BOP {


	public void checkClassFieldIntegrityAndDefaults(GeneratePolicy policy) throws Exception {
		
		if (policy.productType.equals(ProductLineType.Businessowners) && policy.busOwnLine == null) {
			Assert.fail("busOwnLine Can't Be Null");
		}

		//SETS BUILDING ADDRESSES AND WHAT TYPE OF PROPERTY THE ADDITIONAL INTERESTS ARE ATTACHED TO 
		for (PolicyLocation location : policy.busOwnLine.locationList) {
			for (PolicyLocationBuilding building : location.getBuildingList()) {
				for (AdditionalInterest additionalint : building.getAdditionalInterestList()) {
					additionalint.setAdditionalInterestSubType(AdditionalInterestSubType.BOPBuilding);
				}
				building.setLocationAddress(location.getAddress());
			}
		}
	}
}
