package repository.gw.generate.ab;

import repository.gw.enums.AbAccountType;
import repository.gw.helpers.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class AbAccounts {
	
	private boolean primaryDesignation = true;
	private String accountNumber = null;
	private String policyNum;
	private repository.gw.enums.AbAccountType policyType;
	private String policyStatus;
	private Date cancelDate = null;
	
	public AbAccounts(boolean _primaryDesignation, String _accountNumber, String _policyNum, String _policyType, String _policyStatus, String _cancelDate) throws ParseException{
		setPrimaryDesignation(_primaryDesignation);
		setAccountNumber(_accountNumber);
		setPolicyNumber(_policyNum);
		setPolicyType(_policyType);
		setPolicyStatus(_policyStatus);
		setCancelDate(_cancelDate);
	}
	
	public AbAccounts(String _accountNumber, String _policyNum, String _policyType, String _policyStatus, String _cancelDate) throws ParseException{
		setAccountNumber(_accountNumber);
		setPolicyNumber(_policyNum);
		setPolicyType(_policyType);
		setPolicyStatus(_policyStatus);
		setCancelDate(_cancelDate);
	}
	
	public boolean getPrimaryDesignation(){
		return this.primaryDesignation;
	}
	
	public void setPrimaryDesignation(boolean isPrimary){
		this.primaryDesignation = isPrimary;
	}
	
	public String getAccountNumber(){
		return this.accountNumber;
	}
	
	public void setAccountNumber(String _acctNum){
		if(_acctNum.matches("^[0-9]{6}$")){
			this.accountNumber = _acctNum;
		} else {
			System.out.println("Put in a real account number. " + _acctNum + "is not six digits." );
		}
	}
	
	public String getPolicyNumber(){
		return this.policyNum;
	}
	
	public void setPolicyNumber(String _policyNumber){
		
			this.policyNum = _policyNumber;
		
//			System.out.println("Put in a real policy number. " + _policyNumber + "is not 10 digits." );
	
	}
	
	public repository.gw.enums.AbAccountType getPolicyType(){
		return this.policyType;
	}
	
	public void setPolicyType(String _policyType){
		if(_policyType.contains("(") && _policyType.contains(")")){
			_policyType = _policyType.replace("(", "");
			_policyType = _policyType.replace(")", "");
		}
		try{
			this.policyType = repository.gw.enums.AbAccountType.getEnumFromStringValue(_policyType);
		}catch(Exception e){
			System.out.println("Unable to set policyType.");
		}
	}
	
	public void setPolicyType(AbAccountType _policyType){
		this.policyType = _policyType;
	}
	
	public String getPolicyStatus(){
		return this.policyStatus;
	}
	
	public void setPolicyStatus(String _policyStatus){
		this.policyStatus = _policyStatus;
	}
	
	public Date getCancelDate(){
		return this.cancelDate;
	}
	
	public void setCancelDate(Date _cancelDate){
		this.cancelDate = _cancelDate;
	}
	
	public void setCancelDate(String _cancelDate) throws ParseException{
		try{
			this.cancelDate = DateUtils.convertStringtoDate(_cancelDate, "MM/dd/yyyy");
		}catch(ParseException e){
			this.cancelDate = null;
		}
		
	}
	
	

}
