package repository.gw.generate;

import com.idfbins.enums.CountyIdaho;
import org.testng.Assert;
import persistence.globaldatarepo.helpers.AgentsHelper;
import repository.gw.enums.ContactRelationshipToMember;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.custom.*;

import java.util.ArrayList;
import java.util.Date;

import static repository.gw.enums.ProductLineType.*;

public class FieldIntegrityAndDefaults {

	public void checkClassFieldIntegrityAndDefaults(GeneratePolicy policy) throws Exception {
		
		switch (policy.productType) {
		case Businessowners:
			new FieldIntegrityAndDefaults_BOP().checkClassFieldIntegrityAndDefaults(policy);
            policy.busOwnLine.setEffectiveDate(policy.busOwnLine.getEffectiveDate());
            policy.busOwnLine.setExpirationDate(policy.busOwnLine.getExpirationDate());
			break;
		case CPP:
			new FieldIntegrityAndDefaults_CPP().checkClassFieldIntegrityAndDefaults_CPP(policy);
            policy.commercialPackage.setEffectiveDate(policy.commercialPackage.getEffectiveDate());
            policy.commercialPackage.setExpirationDate(policy.commercialPackage.getExpirationDate());
			break;
		case PersonalUmbrella:
			new FieldIntegrityAndDefaults_PersonalUmbrella().checkClassFieldIntegrityAndDefaults(policy);
            policy.squireUmbrellaInfo.setEffectiveDate(policy.squireUmbrellaInfo.getEffectiveDate());
            policy.squireUmbrellaInfo.setExpirationDate(policy.squireUmbrellaInfo.getExpirationDate());
			break;
		case Squire:
			if (!policy.getLineSelection().contains(repository.gw.enums.LineSelection.Membership)) {
				System.out.println("The Squire product type must have a membership line, but none was set. Setting it now.");
				policy.getLineSelection().add(LineSelection.Membership);
			}
			new FieldIntegrityAndDefaults_Squire().checkClassFieldIntegrityAndDefaults(policy);
            policy.squire.setEffectiveDate(policy.squire.getEffectiveDate());
            policy.squire.setExpirationDate(policy.squire.getExpirationDate());
			break;
		case StandardIM:
			new FieldIntegrityAndDefaults_StandardIM().checkClassFieldIntegrityAndDefaults(policy);
            policy.standardInlandMarine.setEffectiveDate(policy.standardInlandMarine.getEffectiveDate());
            policy.standardInlandMarine.setExpirationDate(policy.standardInlandMarine.getExpirationDate());
			break;
        case Membership:
            new FieldIntegrityAndDefaults_Membership().checkClassFieldIntegrityAndDefaults(policy);
            policy.membership.setEffectiveDate(policy.membership.getEffectiveDate());
            policy.membership.setExpirationDate(policy.membership.getExpirationDate());
            break;
        case StandardFire:
            new FieldIntegrityAndDefaults_StandardFire().checkClassFieldIntegrityAndDefaults(policy);
            policy.standardFire.setEffectiveDate(policy.standardFire.getEffectiveDate());
            policy.standardFire.setExpirationDate(policy.standardFire.getExpirationDate());
            break;
        case StandardLiability:
            new FieldIntegrityAndDefaults_StandardLiability().checkClassFieldIntegrityAndDefaults(policy);
            policy.standardLiability.setEffectiveDate(policy.standardLiability.getEffectiveDate());
            policy.standardLiability.setExpirationDate(policy.standardLiability.getExpirationDate());
		break;
		}

        policy.pniContact.setContactIsPNI(true);

        // if no agent is defined
		if (policy.agentInfo == null) {
			policy.agentInfo = AgentsHelper.getRandomAgent();
		}
//		else {
//			policy.agentInfo = AgentsHelper.getAgentByUserName(policy.agentInfo.getAgentUserName());
//		}
		
		if(policy.ratingCounty == null){
			policy.ratingCounty = CountyIdaho.valueOfName(policy.pniContact.getAddress().getCounty());
		}

		if (policy.aniList == null) {
			policy.aniList = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		}
		
		for(PolicyInfoAdditionalNamedInsured ani : policy.aniList) {
			if(ani.getRelatedToPrimaryNamedInsured()) {
				ani.setRelatedContact(policy.pniContact);
			}
		}
		
		for(PolicyBusinessownersLineAdditionalInsured ai : policy.busOwnLine.getAdditonalInsuredBOLineList()) {
			if(ai.getRelatedToPrimaryNamedInsured()) {
				ai.setRelatedContact(policy.pniContact);
			}
		}
		
		for(int i = 0; i<policy.busOwnLine.locationList.size(); i++) {
			for(PolicyLocationAdditionalInsured ai : policy.busOwnLine.locationList.get(i).getAdditionalInsuredLocationsList())
				if(ai.getRelatedToPrimaryNamedInsured()) {
					ai.setRelatedContact(policy.pniContact);
				}
		}
		
		if (policy.pniContact.isPerson() && !policy.polOrgType.isForPerson()) {
			Assert.fail("The contact type selected was Person, but the organization chosen was for a Company. Please correct options.");
		} else if (policy.pniContact.isCompany() && (policy.polOrgType == repository.gw.enums.OrganizationType.Individual || policy.polOrgType == OrganizationType.Sibling)) {
			Assert.fail("The contact type selected was Company, but the organization chosen was Individual. Please correct options.");
		}

//		// verify ins have proper names, SSN/TIN
//		if (policy.pniContact.isPerson()) {
//			if (policy.pniContact.getLastName() == null) {
//				policy.pniContact.setLastName(policy.generateDataFactory.getLastName());
//			}
//			if (policy.pniContact.getFirstName() == null) {
//				policy.pniContact.setFirstName(policy.generateDataFactory.getFirstName());
//			}
//		} else if (policy.pniContact.isCompany()) {
//			if (policy.pniContact.getCompanyName() == null) {
//				policy.pniContact.setCompanyName(policy.generateDataFactory.getBusinessName());
//			}
//		}//end else

		// verify ins has address
		if (policy.pniContact.getAddress() == null) {
			policy.pniContact.setAddress(new AddressInfo(true));
		}//end if
		
		if (policy.pniContact.getAddress().getCounty() == null || policy.pniContact.getAddress().getCounty().equals("")) {
			System.out.println("THE COUNTY FOR THE PRIMARY NAMED INSURED ADDRESS WAS SET TO NULL, SETTING IT TO THE AGENT'S COUNTY.");
			policy.pniContact.getAddress().setCounty(policy.agentInfo.getAgentCounty());
		}

		if(policy.aniList.size() > 0) {
			for (PolicyInfoAdditionalNamedInsured ani : policy.aniList) {
				if (ani.getAddress().getCounty() == null || ani.getAddress().getCounty().equals("")) {
					System.out.println("THE COUNTY FOR THE ADDITIONAL NAMED INSURED ADDRESS WAS SET TO NULL, SETTING IT TO THE AGENT'S COUNTY.");
					ani.getAddress().setCounty(policy.agentInfo.getAgentCounty());
				}
			}
		}

        //Membership Members and Membership Dues Checks:
        if (policy.membershipDuesOnAllInsureds) {
            policy.pniContact.setHasMembershipDues(true);
            if (!policy.aniList.isEmpty()) {
                for (PolicyInfoAdditionalNamedInsured ani : policy.aniList) {
                    ani.setHasMembershipDues(true);
                }
			}
		}

        boolean atLeastOneANIHasMembershipDues = false;
        ANILoop:
        if (!policy.aniList.isEmpty()) {
			for (PolicyInfoAdditionalNamedInsured ani : policy.aniList) {
				if (ani.hasMembershipDues()) {
                    atLeastOneANIHasMembershipDues = true;
                    break ANILoop;
				}
            }
        }
        boolean atLeastOneAdditionalMemberHasMembershipDues = false;
        AdditionalMembershipLoop:
        if (!policy.additionalMembersToAddToMembershipList.isEmpty()) {
            for (Contact additionalMembersToAdd : policy.additionalMembersToAddToMembershipList) {
                if (additionalMembersToAdd.getContactRelationshipToPNI().equals(ContactRelationshipToMember.None)) {
                    additionalMembersToAdd.setHasMembershipDues(true);
                }
                if (additionalMembersToAdd.hasMembershipDues()) {
                    atLeastOneAdditionalMemberHasMembershipDues = true;
                    break AdditionalMembershipLoop;
				}
            }
        }
        if ((!policy.pniContact.hasMembershipDues() && !atLeastOneANIHasMembershipDues && !atLeastOneAdditionalMemberHasMembershipDues) && ((policy.productType.equals(Squire)) || (policy.productType.equals(StandardIM)) || (policy.productType.equals(Membership)))) {
            System.out.println("NO ONE WAS MARKED AS HAVING MEMBERSHIP DUES, BUT THE POLICY TYPE IS SQUIRE, MEMBERSHIP, OR INLAND MARINE. THERE MUST BE AT LEAST ONE MEMBER WITH DUES FOR THESE POLICY TYPES. THEREFORE, WE ARE SETTING THE PNI TO HAVE DUES. IF THIS IS NOT INTENDED, PLEASE EXPLICITLY DECLARE A MEMBERSHIP MEMBERS CONTACT LIST.");
            policy.pniContact.setHasMembershipDues(true);
		}
        //END MEMBERSHIP CHECKS
		
		if((policy.pniContact.getSocialSecurityNumber() == null || policy.pniContact.getSocialSecurityNumber() == "") && (policy.pniContact.getTaxIDNumber() == null || policy.pniContact.getTaxIDNumber() == "") && (policy.pniContact.getAlternateID() == null || policy.pniContact.getAlternateID() == "")){
			policy.pniContact.setTaxIDNumber(String.valueOf(new Date().getTime()).substring(0, 9));
		}
		
	}// END checkClassFieldIntegrityAndDefaults
}
