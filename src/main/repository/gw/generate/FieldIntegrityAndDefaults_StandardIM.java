package repository.gw.generate;


import org.testng.Assert;
import org.testng.SkipException;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FarmEquipment;

public class FieldIntegrityAndDefaults_StandardIM {

	public void checkClassFieldIntegrityAndDefaults(GeneratePolicy policy) {

		//StandardFire policy used for StdLiability 
		if(policy.lineSelection.contains(LineSelection.StandardLiabilityPL) && policy.stdFireLiability == true){

			if(policy.standardLiability == null) {
				throw new SkipException("standardFireAndLiability can't be null because you must first have a StandardFire to have a StandardLiability Policy");
			}

			if(!policy.standardInlandMarine.getCreateNew().equals(repository.gw.enums.CreateNew.Do_Not_Create_New)) {
				policy.standardInlandMarine.setCreateNew(repository.gw.enums.CreateNew.Do_Not_Create_New);
			}
		}// end if LineSelection.StandardLiabilityPL

		//squire policy used to create standard IM
		if(policy.productType.equals(repository.gw.enums.ProductLineType.StandardIM) && policy.stdIMSquire == true){
			if(policy.squire == null) {
            	Assert.fail("squire can't be null");
			}
			policy.standardInlandMarine.setCreateNew(repository.gw.enums.CreateNew.Do_Not_Create_New);
		}//end if productType.equals(ProductLineType.StandardIM

		//Create Standard IM by using Standard Fire
		if(policy.productType.equals(ProductLineType.StandardIM) && policy.stdFireLiabilityIM == true){

			if(policy.standardFire == null) {
            	Assert.fail("standardFireAndLiability can't be null");
			}
			policy.standardInlandMarine.setCreateNew(CreateNew.Do_Not_Create_New);
		}//end if LineSelection.StandardFireStandardIM
		
		for(FarmEquipment farmEquipment : policy.standardInlandMarine.farmEquipment) {
			for(AdditionalInterest additionalInterest : farmEquipment.getAdditionalInterests()) {
				additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.StandardInlandMarine);
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}


















