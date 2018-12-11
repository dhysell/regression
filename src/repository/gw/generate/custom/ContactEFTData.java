package repository.gw.generate.custom;

import repository.gw.enums.ContactEFTBankAccountType;

public class ContactEFTData {

	private String address = null;
	private repository.gw.enums.ContactEFTBankAccountType accountType = null;
	private String accountNumber = null;
	private String routingNumber = null;
	private boolean primary;

	public ContactEFTData() {

	}

	public ContactEFTData(String address, repository.gw.enums.ContactEFTBankAccountType accountType, String accountNumber,
                          String routingNumber, boolean primary) {
		setAddress(address);
		setAccountType(accountType);
		setAccountNumber(accountNumber);
		setRoutingNumber(routingNumber);
		setPrimary(primary);
	}

	public ContactEFTData(AddressInfo address, repository.gw.enums.ContactEFTBankAccountType accountType, String accountNumber,
                          String routingNumber, boolean primary) {
		setAddress(address);
		setAccountType(accountType);
		setAccountNumber(accountNumber);
		setRoutingNumber(routingNumber);
		setPrimary(primary);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAddress(AddressInfo address) {
		if (address.getLine2() != null) {
			this.address = address.getLine1() + ", " + address.getLine2() + ", " + address.getCity() + ", "
					+ address.getState().getAbbreviation() + address.getZip();
		} else {
			this.address = address.getLine1() + ", " + address.getCity() + ", " + address.getState().getAbbreviation()
					+ address.getZip();
		}
	}

	public repository.gw.enums.ContactEFTBankAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(ContactEFTBankAccountType accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

}
