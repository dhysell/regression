package repository.gw.generate.custom;

import com.idfbins.enums.State;
import repository.gw.enums.BankAccountType;

public class BankAccountInfo {

	private String paymentPayer = null;
	private boolean usePNIAsPaymentPayer = false;
	private String routingNumber = null;
	private String accountNumber = null;
	private String bankName = null;
	private String bankAddress = null;
	private String bankCity = null;
	private State bankState = null;
	private String bankZip = null;
	private repository.gw.enums.BankAccountType bankAccountType = null;

	public BankAccountInfo(String paymentPayer, String routingNumber, String accountNumber, String bankName, String bankAddress,
			String bankCity, State bankState, String bankZip, repository.gw.enums.BankAccountType bankAccountType) {
		setPaymentPayer(paymentPayer);
		setRoutingNumber(routingNumber);
		setAccountNumber(accountNumber);
		this.bankName = bankName;
		this.bankAddress = bankAddress;
		this.bankCity = bankCity;
		this.bankState = bankState;
		this.bankZip = bankZip;
		setBankAccountType(bankAccountType);
	}
	
	public BankAccountInfo(BankAccountInfo bankAccountInfo) {
		setPaymentPayer(bankAccountInfo.getPaymentPayer());
		setUsePNIAsPaymentPayer(bankAccountInfo.usePNIAsPaymentPayer);
		setRoutingNumber(bankAccountInfo.getRoutingNumber());
		setAccountNumber(bankAccountInfo.getAccountNumber());
		this.bankName = bankAccountInfo.getBankName();
		this.bankAddress = bankAccountInfo.getBankAddress();
		this.bankCity = bankAccountInfo.getBankCity();
		this.bankState = bankAccountInfo.getBankState();
		this.bankZip = bankAccountInfo.getBankZip();
		setBankAccountType(bankAccountInfo.getBankAccountType());
	}

	public BankAccountInfo() {
		setUsePNIAsPaymentPayer(true);
		setRoutingNumber("101089292"); // 324079555
		setAccountNumber("351659889");
		setBankAccountType(repository.gw.enums.BankAccountType.Business_Checking);
	}
	
	public String getPaymentPayer() {
		return paymentPayer;
	}

	public void setPaymentPayer(String paymentPayer) {
		this.paymentPayer = paymentPayer;
	}

	public boolean usePNIAsPaymentPayer() {
		return usePNIAsPaymentPayer;
	}

	public void setUsePNIAsPaymentPayer(boolean usePNIAsPaymentPayer) {
		this.usePNIAsPaymentPayer = usePNIAsPaymentPayer;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public State getBankState() {
		return bankState;
	}

	public void setBankState(State bankState) {
		this.bankState = bankState;
	}

	public String getBankZip() {
		return bankZip;
	}

	public void setBankZip(String bankZip) {
		this.bankZip = bankZip;
	}

	public repository.gw.enums.BankAccountType getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(BankAccountType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

}
