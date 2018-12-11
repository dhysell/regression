package repository.gw.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

;

public enum RelationshipsAB {
		
			None("<none>", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			AffiliateTo("Affiliate To",Arrays.asList(repository.gw.enums.ContactSubType.values())),
			Affiliation("Affiliation", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			Agent("Agent", Arrays.asList(repository.gw.enums.ContactSubType.Company)),
			AgentOf("Agent of", Arrays.asList(repository.gw.enums.ContactSubType.Company)),
			AssignedCase("Assigned Case", Arrays.asList(repository.gw.enums.ContactSubType.Company)),
			ChildWard("Child / Ward", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			CollectionAgency("Collection Agency", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			CommonOwnership("Common Ownership", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			County("County", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			CountyFor("County for", Arrays.asList(repository.gw.enums.ContactSubType.Company)),
			CSR("CSR", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			CSRFor("CSR for", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Employee("Employee", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			Employer("Employer", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			Friend("Friend", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			FriendTo("Friend to", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Grandchild("Grandchild", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			GrandChildTo("GrandChild to", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Manager("Manager", Arrays.asList(repository.gw.enums.ContactSubType.Company)),
			ManagerOf("Manager Of", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			OtherFamily("Other Family", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			OtherFamilyTo("Other Family to", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			ParentGuardian("Parent / Guardian", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Partner("Partner", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			PartnerTo("Partner to", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			PrimaryContact("Primary Contact", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			PrimaryContactFor("Primary Contact For", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			ProductionAssistant("Production Assistant", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			ProductionAssistantFor("Production Assistant for", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Represents("Represents", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			RepresentsFor("Represents for", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			ServiceAssociate("Service Associate", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			ServiceAssociateFor("Service Associate for", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Sibling("Sibling", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			SiblingOf("Sibling of", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Spouse("Spouse", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			SpouseFor("Spouse for", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			Subsidiary("Subsidiary", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			SubsidiaryOf("Subsidiary of", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			ThirdPartyInsured("Third Party Insured", Arrays.asList(repository.gw.enums.ContactSubType.Company)),
			ThirdPartyInsurer("Third Party Insurer", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			Trust("Trust", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			Trustee("Trustee", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			DBA("DBA", Arrays.asList(repository.gw.enums.ContactSubType.values())),
			DbaFor("DBA for", Arrays.asList(repository.gw.enums.ContactSubType.Company)),
			PersonalAssistant("Personal Assistant", Arrays.asList(repository.gw.enums.ContactSubType.Person)),
			PersonalAssistantTo("Personal Assistant to", Arrays.asList(repository.gw.enums.ContactSubType.Person));
			
			String value;
			List<repository.gw.enums.ContactSubType> types;
			
			private RelationshipsAB(String value, List<repository.gw.enums.ContactSubType> types) {
				this.value = value;
				this.types = types;
			}
			
			public String getValue() {
				return this.value;
			}
			
			public List<repository.gw.enums.ContactSubType> getType() {
				return this.types;
			}
			
			public static RelationshipsAB getEnumFromStringValue(String text){
			    for(RelationshipsAB type: RelationshipsAB.values()){
			        if(text.equalsIgnoreCase(type.value)){
			            return type;
		            }
		        }
		        return null;
		    }
			
			public static List<RelationshipsAB> getListOfRelationshipsABByType(ContactSubType type) {
				List<RelationshipsAB> toReturn = new ArrayList<>();
				for(RelationshipsAB relationship : RelationshipsAB.values()) {
					toReturn.add(relationship);
				}
				return toReturn;
			}
			
	}	
	
	
		


