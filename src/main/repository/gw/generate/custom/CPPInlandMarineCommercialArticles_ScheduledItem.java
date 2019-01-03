package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

public class CPPInlandMarineCommercialArticles_ScheduledItem {

	private String make = "";
	private String description = "This is a description.";
	private String serialNumber = "";
	private String limit = "1000";
	private InlandMarineCPP.CommercialArticles_EquipmentType equipmentType = InlandMarineCPP.CommercialArticles_EquipmentType.Camera;
	
	private InlandMarineCPP.CamerasBlanket_MotionPicture motionPicture = InlandMarineCPP.CamerasBlanket_MotionPicture.ExcludingMotionPictureProducers;
	private InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments musicalInstruments = InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments.IndividualsProfessional;
	private InlandMarineCPP.CommercialArticles_BandType bandType = InlandMarineCPP.CommercialArticles_BandType.DanceBandsOrchestras;
	
	public CPPInlandMarineCommercialArticles_ScheduledItem() {
		//default constructor
	}
	
	public CPPInlandMarineCommercialArticles_ScheduledItem(InlandMarineCPP.CommercialArticles_EquipmentType _equipmentType) {
		this.setEquipmentType(_equipmentType);
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public InlandMarineCPP.CommercialArticles_EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(InlandMarineCPP.CommercialArticles_EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public InlandMarineCPP.CamerasBlanket_MotionPicture getMotionPicture() {
		return motionPicture;
	}

	public void setMotionPicture(InlandMarineCPP.CamerasBlanket_MotionPicture motionPicture) {
		this.motionPicture = motionPicture;
	}

	public InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments getMusicalInstruments() {
		return musicalInstruments;
	}

	public void setMusicalInstruments(InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments musicalInstruments) {
		this.musicalInstruments = musicalInstruments;
	}

	public InlandMarineCPP.CommercialArticles_BandType getBandType() {
		return bandType;
	}

	public void setBandType(InlandMarineCPP.CommercialArticles_BandType bandType) {
		this.bandType = bandType;
	}
	
	
	
}
