package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineTypes;

import java.util.ArrayList;

public class SquireInlandMarine {
	
	//COVERAGE SELECTION
	public ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarineTypes.InlandMarine>();
	public ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_Standard_IM = new ArrayList<InlandMarineTypes.InlandMarine>();
	
	//RECREATION EQUIPMENT
	public ArrayList<RecreationalEquipment> recEquipment_PL_IM = new ArrayList<RecreationalEquipment>();;
	
	//WATERCRAFT
	public ArrayList<SquireIMWatercraft> watercrafts_PL_IM = new ArrayList<SquireIMWatercraft>();
	
	//FARM EQUIPMENT
	public ArrayList<FarmEquipment> farmEquipment = new ArrayList<FarmEquipment>();
	
	//PERSONAL PROPERTY
	public ArrayList<PersonalProperty> personalProperty_PL_IM = new ArrayList<PersonalProperty>();
	
	//LIVESTOCK
	public ArrayList<Livestock> livestock_PL_IM = new ArrayList<Livestock>();
	
	public ArrayList<SquireIMCargo> cargo_PL_IM = new ArrayList<SquireIMCargo>();
	
	//LINE REVIEW
	
	
	

	public SquireInlandMarine() {
	}

}
