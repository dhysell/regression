package repository.gw.generate.custom;

import repository.gw.enums.PersonalPropertyType;

import java.util.ArrayList;

public class PersonalPropertyList {
	
	private ArrayList<PersonalProperty> medicalSuppliesAndEquipment = new ArrayList<PersonalProperty>();
	private PersonalProperty refrigeratedMilk;
	private PersonalProperty milkContaminationAndRefrigeration;
	private PersonalProperty sportingEquipment;
	private PersonalProperty golfEquipment;
	private PersonalProperty photographicEquipment;
	private PersonalProperty musicalInstruments;
	private PersonalProperty exteriorOrnaments;
	private PersonalProperty collectibles;
	private PersonalProperty fineArts;
	private PersonalProperty furs;
	private PersonalProperty jewelry;
	private PersonalProperty saddlesAndTack;
	private PersonalProperty medicalDevices;
	private PersonalProperty officeEquipment;
	private PersonalProperty radioAntennae;
	private PersonalProperty radioReceiversAndTransmitters;
	private PersonalProperty stereoEquipment;
	private PersonalProperty tailoringEquipment;
	private PersonalProperty telephoneEquipment;
	private PersonalProperty videoEquipment;
	private PersonalProperty tools;
	private PersonalProperty beeContainers;
	private PersonalProperty blanketRadios;
	
	public ArrayList<PersonalProperty> getAllPersonalPropertyAsList() {
		ArrayList<PersonalProperty> toReturn = new ArrayList<PersonalProperty>();
		for(PersonalProperty pp : getMedicalSuppliesAndEquipment()) {
			toReturn.add(pp);
		}
		toReturn.add(getRefrigeratedMilk());
		toReturn.add(getMilkContaminationAndRefrigeration());
		toReturn.add(getSportingEquipment());
		toReturn.add(getGolfEquipment());
		toReturn.add(getPhotographicEquipment());
		toReturn.add(getMusicalInstruments());
		toReturn.add(getExteriorOrnaments());
		toReturn.add(getAntiqueCollectibles());
		toReturn.add(getFineArts());
		toReturn.add(getFurs());
		toReturn.add(getJewelry());
		toReturn.add(getSaddlesAndTack());
		toReturn.add(getMedicalDevices());
		toReturn.add(getOfficeEquipment());
		toReturn.add(getRadioAntennae());
		toReturn.add(getRadioReceiversAndTransmitters());
		toReturn.add(getStereoEquipment());
		toReturn.add(getTailoringEquipment());
		toReturn.add(getTelephoneEquipment());
		toReturn.add(getVideoEquipment());
		toReturn.add(getTools());
		toReturn.add(getBeeContainers());
		toReturn.add(getBlanketRadios());
		
		return toReturn;
	}

	public ArrayList<PersonalProperty> getMedicalSuppliesAndEquipment() {
		return medicalSuppliesAndEquipment;
	}

	public void setMedicalSuppliesAndEquipment(ArrayList<PersonalProperty> medicalSuppliesAndEquipment) throws Exception {
		if(medicalSuppliesAndEquipment != null) {
			for(PersonalProperty msae : medicalSuppliesAndEquipment) {
				if(msae.getType() != repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment) {
					throw new Exception("ERROR: This must be of type MedicalSuppliesAndEquipment");
				}
			}
			
		}
		this.medicalSuppliesAndEquipment = medicalSuppliesAndEquipment;
	}

	public PersonalProperty getRefrigeratedMilk() {
		return refrigeratedMilk;
	}

	public void setRefrigeratedMilk(PersonalProperty refrigeratedMilk) throws Exception {
		if(refrigeratedMilk != null) {
			if(refrigeratedMilk.getType() != repository.gw.enums.PersonalPropertyType.RefrigeratedMilk) {
				throw new Exception("ERROR: This must be of type RefrigeratedMilk");
			}
		}
		this.refrigeratedMilk = refrigeratedMilk;
	}

	public PersonalProperty getMilkContaminationAndRefrigeration() {
		return milkContaminationAndRefrigeration;
	}

	public void setMilkContaminationAndRefrigeration(PersonalProperty milkContaminationAndRefrigeration) throws Exception {
		if(milkContaminationAndRefrigeration != null) {
			if(milkContaminationAndRefrigeration.getType() != repository.gw.enums.PersonalPropertyType.MilkContaminationAndRefrigeration) {
				throw new Exception("ERROR: This must be of type MilkContaminationAndRefrigeration");
			}
		}
		this.milkContaminationAndRefrigeration = milkContaminationAndRefrigeration;
	}

	public PersonalProperty getSportingEquipment() {
		return sportingEquipment;
	}

	public void setSportingEquipment(PersonalProperty sportingEquipment) throws Exception {
		if(sportingEquipment != null) {
			if(sportingEquipment.getType() != repository.gw.enums.PersonalPropertyType.SportingEquipment) {
				throw new Exception("ERROR: This must be of type SportingEquipment");
			}
		}
		this.sportingEquipment = sportingEquipment;
	}

	public PersonalProperty getGolfEquipment() {
		return golfEquipment;
	}

	public void setGolfEquipment(PersonalProperty golfEquipment) throws Exception {
		if(golfEquipment != null) {
			if(golfEquipment.getType() != repository.gw.enums.PersonalPropertyType.GolfEquipment) {
				throw new Exception("ERROR: This must be of type GolfEquipment");
			}
		}
		this.golfEquipment = golfEquipment;
	}

	public PersonalProperty getPhotographicEquipment() {
		return photographicEquipment;
	}

	public void setPhotographicEquipment(PersonalProperty photographicEquipment) throws Exception {
		if(photographicEquipment != null) {
			if(photographicEquipment.getType() != repository.gw.enums.PersonalPropertyType.PhotographicEquipment){
				throw new Exception("ERROR: This must be of type photographicEquipment");
			}
		}
		this.photographicEquipment = photographicEquipment;
	}

	public PersonalProperty getMusicalInstruments() {
		return musicalInstruments;
	}

	public void setMusicalInstruments(PersonalProperty musicalInstruments) throws Exception {
		if(musicalInstruments != null) {
			if(musicalInstruments.getType() != repository.gw.enums.PersonalPropertyType.MusicalInstruments) {
				throw new Exception("ERROR: This must be of type musicalInstruments");
			}
		}
		this.musicalInstruments = musicalInstruments;
	}

	public PersonalProperty getExteriorOrnaments() {
		return exteriorOrnaments;
	}

	public void setExteriorOrnaments(PersonalProperty exteriorOrnaments) throws Exception {
		if(exteriorOrnaments != null) {
			if(exteriorOrnaments.getType() != repository.gw.enums.PersonalPropertyType.ExteriorOrnaments) {
				throw new Exception("ERROR: This must be of type exteriorOrnaments");
			}
		}
		this.exteriorOrnaments = exteriorOrnaments;
	}

	public PersonalProperty getAntiqueCollectibles() {
		return collectibles;
	}

	public void setCollectibles(PersonalProperty collectibles) throws Exception {
		if(collectibles != null) {
			if(collectibles.getType() != repository.gw.enums.PersonalPropertyType.Collectibles) {
				throw new Exception("ERROR: This must be of type antiqueCollectibles");
			}
		}
		this.collectibles = collectibles;
	}

	public PersonalProperty getFineArts() {
		return fineArts;
	}

	public void setFineArts(PersonalProperty fineArts) throws Exception {
		if(fineArts != null) {
			if(fineArts.getType() != repository.gw.enums.PersonalPropertyType.FineArts) {
				throw new Exception("ERROR: This must be of type fineArts");
			}
		}
		this.fineArts = fineArts;
	}

	public PersonalProperty getFurs() {
		return furs;
	}

	public void setFurs(PersonalProperty furs) throws Exception {
		if(furs != null) {
			if(furs.getType() != repository.gw.enums.PersonalPropertyType.Furs) {
				throw new Exception("ERROR: This must be of type furs");
			}
		}
		this.furs = furs;
	}

	public PersonalProperty getJewelry() {
		return jewelry;
	}

	public void setJewelry(PersonalProperty jewelry) throws Exception {
		if(jewelry != null) {
			if(jewelry.getType() != repository.gw.enums.PersonalPropertyType.Jewelry) {
				throw new Exception("ERROR: This must be of type jewelry");
			}
		}
		this.jewelry = jewelry;
	}

	public PersonalProperty getSaddlesAndTack() {
		return saddlesAndTack;
	}

	public void setSaddlesAndTack(PersonalProperty saddlesAndTack) throws Exception {
		if(saddlesAndTack != null) {
			if(saddlesAndTack.getType() != repository.gw.enums.PersonalPropertyType.SaddlesAndTack) {
				throw new Exception("ERROR: This must be of type saddlesAndTack");
			}
		}
		this.saddlesAndTack = saddlesAndTack;
	}

	public PersonalProperty getMedicalDevices() {
		return medicalDevices;
	}

	public void setMedicalDevices(PersonalProperty medicalDevices) throws Exception {
		if(medicalDevices != null) {
			if(medicalDevices.getType() != repository.gw.enums.PersonalPropertyType.MedicalDevices) {
				throw new Exception("ERROR: This must be of type medicalDevices");
			}
		}
		this.medicalDevices = medicalDevices;
	}

	public PersonalProperty getOfficeEquipment() {
		return officeEquipment;
	}

	public void setOfficeEquipment(PersonalProperty officeEquipment) throws Exception {
		if(officeEquipment != null) {
			if(officeEquipment.getType() != repository.gw.enums.PersonalPropertyType.OfficeEquipment) {
				throw new Exception("ERROR: This must be of type officeEquipment");
			}
		}
		this.officeEquipment = officeEquipment;
	}

	public PersonalProperty getRadioAntennae() {
		return radioAntennae;
	}

	public void setRadioAntennae(PersonalProperty radioAntennae) throws Exception {
		if(radioAntennae != null) {
			if(radioAntennae.getType() != repository.gw.enums.PersonalPropertyType.RadioAntennae) {
				throw new Exception("ERROR: This must be of type radioAntennae");
			}
		}
		this.radioAntennae = radioAntennae;
	}

	public PersonalProperty getRadioReceiversAndTransmitters() {
		return radioReceiversAndTransmitters;
	}

	public void setRadioReceiversAndTransmitters(PersonalProperty radioReceiversAndTransmitters) throws Exception {
		if(radioReceiversAndTransmitters != null) {
			if(radioReceiversAndTransmitters.getType() != repository.gw.enums.PersonalPropertyType.RadioReceiversAndTransmitters) {
				throw new Exception("ERROR: This must be of type radioReceiversAndTransmitters");
			}
		}
		this.radioReceiversAndTransmitters = radioReceiversAndTransmitters;
	}

	public PersonalProperty getStereoEquipment() {
		return stereoEquipment;
	}

	public void setStereoEquipment(PersonalProperty stereoEquipment) throws Exception {
		if(stereoEquipment != null) {
			if(stereoEquipment.getType() != repository.gw.enums.PersonalPropertyType.StereoEquipment) {
				throw new Exception("ERROR: This must be of type stereoEquipment");
			}
		}
		this.stereoEquipment = stereoEquipment;
	}

	public PersonalProperty getTailoringEquipment() {
		return tailoringEquipment;
	}

	public void setTailoringEquipment(PersonalProperty tailoringEquipment) throws Exception {
		if(tailoringEquipment != null) {
			if(tailoringEquipment.getType() != repository.gw.enums.PersonalPropertyType.TailoringEquipment) {
				throw new Exception("ERROR: This must be of type tailoringEquipment");
			}
		}
		this.tailoringEquipment = tailoringEquipment;
	}

	public PersonalProperty getTelephoneEquipment() {
		return telephoneEquipment;
	}

	public void setTelephoneEquipment(PersonalProperty telephoneEquipment) throws Exception {
		if(telephoneEquipment != null) {
			if(telephoneEquipment.getType() != repository.gw.enums.PersonalPropertyType.TelephoneEquipment) {
				throw new Exception("ERROR: This must be of type telephoneEquipment");
			}
		}
		this.telephoneEquipment = telephoneEquipment;
	}

	public PersonalProperty getVideoEquipment() {
		return videoEquipment;
	}

	public void setVideoEquipment(PersonalProperty videoEquipment) throws Exception {
		if(videoEquipment != null) {
			if(videoEquipment.getType() != repository.gw.enums.PersonalPropertyType.VideoEquipment) {
				throw new Exception("ERROR: This must be of type videoEquipment");
			}
		}
		this.videoEquipment = videoEquipment;
	}

	public PersonalProperty getTools() {
		return tools;
	}

	public void setTools(PersonalProperty tools) throws Exception {
		if(tools != null) {
			if(tools.getType() != repository.gw.enums.PersonalPropertyType.Tools) {
				throw new Exception("ERROR: This must be of type tools");
			}
		}
		this.tools = tools;
	}

	public PersonalProperty getBeeContainers() {
		return beeContainers;
	}

	public void setBeeContainers(PersonalProperty beeContainers) throws Exception {
		if(beeContainers != null) {
			if(beeContainers.getType() != repository.gw.enums.PersonalPropertyType.BeeContainers) {
				throw new Exception("ERROR: This must be of type beeContainers");
			}
		}
		this.beeContainers = beeContainers;
	}

	public PersonalProperty getBlanketRadios() {
		return blanketRadios;
	}

	public void setBlanketRadios(PersonalProperty blanketRadios) throws Exception {
		if(blanketRadios != null) {
			if(blanketRadios.getType() != PersonalPropertyType.BlanketRadios) {
				throw new Exception("ERROR: This must be of type blanketRadios");
			}
		}
		this.blanketRadios = blanketRadios;
	}

}
