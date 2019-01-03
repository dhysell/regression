package repository.gw.generate.custom;

public class BaileesCustomer {
	
	private CPPCommercialPropertyProperty property;
	private int limit = 1000;
	
	public BaileesCustomer(CPPCommercialPropertyProperty property, int limit) {
		this.property = property;
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public CPPCommercialPropertyProperty getProperty() {
		return property;
	}

	public void setProperty(CPPCommercialPropertyProperty property) {
		this.property = property;
	}

}
