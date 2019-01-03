package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;

import java.util.ArrayList;

public class AccountInfo {
	
	private Contact pniContact = null;
	
	private String accountNumber = null;
	private String policyNumber = null;
	private String fullAccountNumber = null;
	
	private CountyIdaho ratingCounty= null;
	private ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<PolicyInfoAdditionalNamedInsured>();
	
	private Agents agentInfo = null;
	
	
	public Contact getPniContact() {
		return pniContact;
	}

	public void setPniContact(Contact pniContact) {
		this.pniContact = pniContact;
	}

	public Agents getAgentInfo() {
		return agentInfo;
	}

	public void setAgentInfo(Agents agentInfo) {
		this.agentInfo = agentInfo;
	}

	public boolean isRandomAgent() {
		return randomAgent;
	}

	public void setRandomAgent(boolean randomAgent) {
		this.randomAgent = randomAgent;
	}

	public Underwriters getUnderwriterInfo() {
		return underwriterInfo;
	}

	public void setUnderwriterInfo(Underwriters underwriterInfo) {
		this.underwriterInfo = underwriterInfo;
	}


	public boolean randomAgent = true;
	public Underwriters underwriterInfo = null;
	
	public AccountInfo() {
		if(pniContact != null) {
			ratingCounty = CountyIdaho.valueOf(pniContact.getAddress().getCounty());
		}
		
	}
	
	public AddressInfo getAddress() {
		return pniContact.getAddress();
	}


	public Contact getPNIContact() {
		return pniContact;
	}


	public void setPNIContact(Contact pniContact) {
		this.pniContact = pniContact;
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getPolicyNumber() {
		return policyNumber;
	}


	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}


	public String getFullAccountNumber() {
		return fullAccountNumber;
	}


	public void setFullAccountNumber(String fullAccountNumber) {
		this.fullAccountNumber = fullAccountNumber;
	}


	public CountyIdaho getRatingCounty() {
		return ratingCounty;
	}


	public void setRatingCounty(CountyIdaho ratingCounty) {
		this.ratingCounty = ratingCounty;
	}


	public ArrayList<PolicyInfoAdditionalNamedInsured> getAniList() {
		return aniList;
	}


	public void setAniList(ArrayList<PolicyInfoAdditionalNamedInsured> aniList) {
		this.aniList = aniList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
