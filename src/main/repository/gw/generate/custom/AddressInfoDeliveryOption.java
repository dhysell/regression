package repository.gw.generate.custom;

import repository.gw.enums.DeliveryOptionType;

public class AddressInfoDeliveryOption {

	private repository.gw.enums.DeliveryOptionType type;
	private String description;

	public AddressInfoDeliveryOption() {

	}

	public AddressInfoDeliveryOption(repository.gw.enums.DeliveryOptionType type) {
		setType(type);
		setDescription(type.getDescValue());
	}

	public AddressInfoDeliveryOption(AddressInfoDeliveryOption addressInfoDeliveryOptionToCopy) {
		this.type = addressInfoDeliveryOptionToCopy.getType();
		this.description = addressInfoDeliveryOptionToCopy.getDescription();
		if (!this.equals(addressInfoDeliveryOptionToCopy)) {
			System.out.println("The AddressInfoDeliveryOptions are not perfect copies.");
		}
	}

	public boolean equals(AddressInfoDeliveryOption addressInfoDeliveryOptionToCompareTo) {
		return (this.type.equals(addressInfoDeliveryOptionToCompareTo.getType())
				&& this.description.equals(addressInfoDeliveryOptionToCompareTo.getDescription()));
	}

	public AddressInfoDeliveryOption(repository.gw.enums.DeliveryOptionType type, String description) {
		setType(type);
		setDescription(description);
	}

	public repository.gw.enums.DeliveryOptionType getType() {
		return type;
	}

	public void setType(DeliveryOptionType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
