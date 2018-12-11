package repository.gw.generate.custom;

public class CPPInlandMarineTripTransit_Trip {

	private repository.gw.generate.custom.AddressInfo fromAddress = new repository.gw.generate.custom.AddressInfo(true);
	private repository.gw.generate.custom.AddressInfo toAddress = new repository.gw.generate.custom.AddressInfo(true);
	
	public CPPInlandMarineTripTransit_Trip() {
		
	}

	public repository.gw.generate.custom.AddressInfo getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(repository.gw.generate.custom.AddressInfo fromAddress) {
		this.fromAddress = fromAddress;
	}

	public repository.gw.generate.custom.AddressInfo getToAddress() {
		return toAddress;
	}

	public void setToAddress(AddressInfo toAddress) {
		this.toAddress = toAddress;
	}
	
}
