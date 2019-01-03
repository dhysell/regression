package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;

import java.util.ArrayList;
import java.util.Date;

public class PolicyInfo {
	
	public String accountNumber = null;
	public String policyNumber = null;
	public String fullAccountNumber = null;
	public Date effectiveDate = null;
	public Date expirationDate = null;
	public Integer polTermLengthDays = 365;
	
	public CountyIdaho ratingCounty= null;
	public ArrayList<repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<PolicyInfoAdditionalNamedInsured>();

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
