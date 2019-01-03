package repository.gw.generate.custom;

import repository.gw.enums.FPP;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class SquireFPPTypeItem {
	
	private repository.gw.generate.custom.SquireFPP parent;
	private repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes type;
	private String description = "Test Description";
	private int value = 100;
	private String serialNumber = DateUtils.dateFormatAsString("yyMMddHHmmss", new Date());
	private repository.gw.generate.custom.AdditionalInterest additionalInterest;
	
	public SquireFPPTypeItem(repository.gw.generate.custom.SquireFPP parent) {
		setParent(parent);
	}

    public SquireFPPTypeItem(repository.gw.generate.custom.SquireFPP parent, repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes type) {
        setParent(parent);
        setType(type);
    }
	
	public repository.gw.generate.custom.SquireFPP getParent() {
		return parent;
	}

	public void setParent(SquireFPP parent) {
		this.parent = parent;
	}

	public repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes getType() {
		return type;
	}
	
	public void setType(FPP.FPPFarmPersonalPropertySubTypes type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public repository.gw.generate.custom.AdditionalInterest getAdditionalInterest() {
		return additionalInterest;
	}

	public void setAdditionalInterest(AdditionalInterest additionalInterest) {
		this.additionalInterest = additionalInterest;
	}
}
