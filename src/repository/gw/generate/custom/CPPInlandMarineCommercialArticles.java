package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineCommercialArticles {
	
	// Coverages
	private InlandMarineCPP.CommercialArticlesCoverageForm_CM_00_20_Deductible deductible = InlandMarineCPP.CommercialArticlesCoverageForm_CM_00_20_Deductible.TwoHundredFifty;
	private boolean camerasBlanket = true;
	private InlandMarineCPP.CamerasBlanket_MotionPicture motionPicture = InlandMarineCPP.CamerasBlanket_MotionPicture.ExcludingMotionPictureProducers;
	private String camerasLimitPerAnyOneOccurance = "1000";
	
	private boolean musicalInstrumentsBlanket = true;
	private InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments musicalInstruments = InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments.IndividualsProfessional;
	private String musicalInstrumentsLimitPerAnyOneOccurance = "1000";
	
	//Scheduled Items
	private List<repository.gw.generate.custom.CPPInlandMarineCommercialArticles_ScheduledItem> scheduledItems = new ArrayList<>();
	
	public CPPInlandMarineCommercialArticles() {
		scheduledItems.add(new repository.gw.generate.custom.CPPInlandMarineCommercialArticles_ScheduledItem(InlandMarineCPP.CommercialArticles_EquipmentType.Camera));
		scheduledItems.add(new repository.gw.generate.custom.CPPInlandMarineCommercialArticles_ScheduledItem(InlandMarineCPP.CommercialArticles_EquipmentType.MusicalInstruments));
	}
	
	public InlandMarineCPP.CommercialArticlesCoverageForm_CM_00_20_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.CommercialArticlesCoverageForm_CM_00_20_Deductible deductible) {
		this.deductible = deductible;
	}

	public boolean isCamerasBlanket() {
		return camerasBlanket;
	}

	public void setCamerasBlanket(boolean camerasBlanket) {
		this.camerasBlanket = camerasBlanket;
	}

	public InlandMarineCPP.CamerasBlanket_MotionPicture getMotionPicture() {
		return motionPicture;
	}

	public void setMotionPicture(InlandMarineCPP.CamerasBlanket_MotionPicture motionPicture) {
		this.motionPicture = motionPicture;
	}

	public String getCamerasLimitPerAnyOneOccurance() {
		return camerasLimitPerAnyOneOccurance;
	}

	public void setCamerasLimitPerAnyOneOccurance(String camerasLimitPerAnyOneOccurance) {
		this.camerasLimitPerAnyOneOccurance = camerasLimitPerAnyOneOccurance;
	}

	public boolean isMusicalInstrumentsBlanket() {
		return musicalInstrumentsBlanket;
	}

	public void setMusicalInstrumentsBlanket(boolean musicalInstrumentsBlanket) {
		this.musicalInstrumentsBlanket = musicalInstrumentsBlanket;
	}

	public InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments getMusicalInstruments() {
		return musicalInstruments;
	}

	public void setMusicalInstruments(InlandMarineCPP.MusicalInstrumentsBlanket_MusicalInstruments musicalInstruments) {
		this.musicalInstruments = musicalInstruments;
	}

	public String getMusicalInstrumentsLimitPerAnyOneOccurance() {
		return musicalInstrumentsLimitPerAnyOneOccurance;
	}

	public void setMusicalInstrumentsLimitPerAnyOneOccurance(String musicalInstrumentsLimitPerAnyOneOccurance) {
		this.musicalInstrumentsLimitPerAnyOneOccurance = musicalInstrumentsLimitPerAnyOneOccurance;
	}

	public List<repository.gw.generate.custom.CPPInlandMarineCommercialArticles_ScheduledItem> getScheduledItems() {
		return scheduledItems;
	}

	public void setScheduledItems(List<CPPInlandMarineCommercialArticles_ScheduledItem> scheduledItems) {
		this.scheduledItems = scheduledItems;
	}

}
