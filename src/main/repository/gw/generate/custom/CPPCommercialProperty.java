package repository.gw.generate.custom;

import repository.gw.enums.CommercialPropertyForms;
import repository.gw.enums.ContactSubType;
import repository.gw.generate.CommercialPackagePolicy;

import java.util.ArrayList;
import java.util.List;

public class CPPCommercialProperty extends repository.gw.generate.CommercialPackagePolicy {
	
	List<repository.gw.enums.CommercialPropertyForms> cpForms = new ArrayList<repository.gw.enums.CommercialPropertyForms>();
	
	//COMMERCIAL PROPERTY LINE
	CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine();
	//PROPERTY
	List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>();
	//MODIFIERS
	
	public CPPCommercialProperty() {

	}
	
	public CPPCommercialProperty(CPPCommercialPropertyProperty property) {
		commercialPropertyList.add(property);
	}

	public CPPCommercialPropertyLine getCommercialPropertyLine() {
		return commercialPropertyLine;
	}

	public void setCommercialPropertyLine(CPPCommercialPropertyLine commercialPropertyLine) {
		this.commercialPropertyLine = commercialPropertyLine;
	}

	public List<CPPCommercialPropertyProperty> getCommercialPropertyList() {
		return commercialPropertyList;
	}

	public void setCommercialPropertyList(List<CPPCommercialPropertyProperty> commercialPropertyList) {
		this.commercialPropertyList = commercialPropertyList;
	}
	
	public List<repository.gw.enums.CommercialPropertyForms> getCpForms() {
		return cpForms;
	}

	public void setCpForms(List<CommercialPropertyForms> cpForms) {
		this.cpForms = cpForms;
	}
	
	
	
	
	
	
	
	
	
	public static class CommercialProperty_Builder {
		
		CPPCommercialPropertyLine commercialPropertyLine_builder = new CPPCommercialPropertyLine();
		List<CPPCommercialPropertyProperty> commercialPropertyList_builder = new ArrayList<CPPCommercialPropertyProperty>();
		private repository.gw.generate.custom.AccountInfo accountInfo_Builder = new AccountInfo();
		
		
		public CommercialProperty_Builder() {
			
		}
		
		public CommercialProperty_Builder withcommercialPropertyLine(CPPCommercialPropertyLine commercialPropertyLine) {
			this.commercialPropertyLine_builder = commercialPropertyLine;
			return this;
		}
		
		public CommercialProperty_Builder withCommercialPropertyList(List<CPPCommercialPropertyProperty> commercialPropertyList) {
			this.commercialPropertyList_builder = commercialPropertyList;
			new CommercialPackagePolicy();
			return this;
		}
		
		public CommercialProperty_Builder withPNI(Contact pniContact) {
			this.accountInfo_Builder.setPNIContact(pniContact);
			return this;
		}
		
		public CommercialProperty_Builder withCompanyName(String companyName) {
			this.accountInfo_Builder.getPNIContact().setCompanyName(companyName);
			this.accountInfo_Builder.getPNIContact().setPersonOrCompany(repository.gw.enums.ContactSubType.Company);
			return this;
		}
		
		public CommercialProperty_Builder withPersonName(String firstName, String lastName) {
			this.accountInfo_Builder.getPNIContact().setFirstName(firstName);
			this.accountInfo_Builder.getPNIContact().setLastName(lastName);
			this.accountInfo_Builder.getPNIContact().setPersonOrCompany(ContactSubType.Person);
			return this;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
