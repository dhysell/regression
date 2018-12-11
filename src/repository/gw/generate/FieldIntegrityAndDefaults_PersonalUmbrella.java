package repository.gw.generate;


import org.testng.Assert;
import repository.gw.enums.CreateNew;
import repository.gw.enums.ProductLineType;

public class FieldIntegrityAndDefaults_PersonalUmbrella {
	
	public void checkClassFieldIntegrityAndDefaults(GeneratePolicy policy) {

		//checks Umbrella
		if(policy.productType.equals(ProductLineType.PersonalUmbrella)) {
            if (policy.squire == null) {
            	Assert.fail("squire can't be null because you must first have a Squire to have a PersonalUmbrella Policy");
			}

            policy.squireUmbrellaInfo.setCreateNew(CreateNew.Do_Not_Create_New);
		}//end if ProductLineType.PersonalUmbrella
	}//END OF METHOD
}//EOF
