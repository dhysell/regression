package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.IMUWQuestions;
import persistence.globaldatarepo.helpers.IMUWQuestionsHelper;
import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarine {
	
	private List<InlandMarineCPP.InlandMarineCoveragePart> coveragePart = new ArrayList<InlandMarineCPP.InlandMarineCoveragePart>();
	
	// Coverage Parts
	private CPPInlandMarineAccountsReceivable accountsReceivable = new CPPInlandMarineAccountsReceivable(new ArrayList<CPPInlandMarineAccountsReceivable_LocationBlanket>());
	private CPPInlandMarineBaileesCustomers baileesCustomers = new CPPInlandMarineBaileesCustomers(new ArrayList<BaileesCustomer>(), new ArrayList<BaileesCustomer>(), new ArrayList<BaileesCustomer>());
	private repository.gw.generate.custom.CPPInlandMarineCameraAndMusicalInstrumentDealers cameraAndMusicalInstrumentDealers = new repository.gw.generate.custom.CPPInlandMarineCameraAndMusicalInstrumentDealers(new ArrayList<CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket>());
	private CPPInlandMarineCommercialArticles commercialArticles = new CPPInlandMarineCommercialArticles();
	private CPPInlandMarineComputerSystems computerSystems = new CPPInlandMarineComputerSystems(new ArrayList<CPPInlandMarineComputerSystems_LocationBlanket>());
	private CPPInlandMarineContractorsEquipment contractorsEquipment = new CPPInlandMarineContractorsEquipment();
	private CPPInlaneMarineExhibition exhibition = new CPPInlaneMarineExhibition();
	private CPPInlandMarineInstallation installation = new CPPInlandMarineInstallation();
	private CPPInlandMarineMiscellaneousArticles miscellaneousArticles = new CPPInlandMarineMiscellaneousArticles(new ArrayList<CPPInlandMarineMiscellaneousArticles_ScheduledItem>());
	private CPPInlandMarineMotorTruckCargo motorTruckCargo = new CPPInlandMarineMotorTruckCargo(new ArrayList<CPPInlandMarineMotorTruckCargo_ScheduledCargo>());
	private CPPInlandMarineSignsCM signs = new CPPInlandMarineSignsCM(new ArrayList<CPPInlandMarineSignsCM_ScheduledSign>());
	private CPPInlandMarineTripTransit tripTransit = new CPPInlandMarineTripTransit(new ArrayList<InlandMarineCPP.InlandMarine_Cargo>());
	private CPPInlandMarineValuablePapers valuablePapers = new CPPInlandMarineValuablePapers(new CPPCommercialPropertyProperty());
	
	//Underwriting Questions
	private List<CPPInlandMarineUWQuestion> uwQuestions = new ArrayList<>();
	
	public CPPInlandMarine() {
		//default constructor
	}
	
	public CPPInlandMarine(List<InlandMarineCPP.InlandMarineCoveragePart> coverageParts) throws Exception {
		this.coveragePart = coverageParts;
		List<String> coveragePartNames = getCoveragePartNames();
		List<IMUWQuestions> imUWQuestions = IMUWQuestionsHelper.getUWQuestionsCoverageParts(coveragePartNames);
		for(IMUWQuestions imUWQuestion : imUWQuestions) {
			uwQuestions.add(new CPPInlandMarineUWQuestion(imUWQuestion));
		}
	}

	public List<InlandMarineCPP.InlandMarineCoveragePart> getCoveragePart() {
		return coveragePart;
	}

	public void setCoveragePart(List<InlandMarineCPP.InlandMarineCoveragePart> coveragePart) {
		this.coveragePart = coveragePart;
	}

	public CPPInlandMarineAccountsReceivable getAccountsReceivable() {
		return accountsReceivable;
	}

	public void setAccountsReceivable(CPPInlandMarineAccountsReceivable accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	public CPPInlandMarineBaileesCustomers getBaileesCustomers() {
		return baileesCustomers;
	}

	public void setBaileesCustomers(CPPInlandMarineBaileesCustomers baileesCustomers) {
		this.baileesCustomers = baileesCustomers;
	}

	public repository.gw.generate.custom.CPPInlandMarineCameraAndMusicalInstrumentDealers getCameraAndMusicalInstrumentDealers() {
		return cameraAndMusicalInstrumentDealers;
	}

	public void setCameraAndMusicalInstrumentDealers(CPPInlandMarineCameraAndMusicalInstrumentDealers cameraAndMusicalInstrumentDealers) {
		this.cameraAndMusicalInstrumentDealers = cameraAndMusicalInstrumentDealers;
	}

	public CPPInlandMarineCommercialArticles getCommercialArticles() {
		return commercialArticles;
	}

	public void setCommercialArticles(CPPInlandMarineCommercialArticles commercialArticles) {
		this.commercialArticles = commercialArticles;
	}

	public CPPInlandMarineComputerSystems getComputerSystems() {
		return computerSystems;
	}

	public void setComputerSystems(CPPInlandMarineComputerSystems computerSystems) {
		this.computerSystems = computerSystems;
	}

	public CPPInlandMarineContractorsEquipment getContractorsEquipment() {
		return contractorsEquipment;
	}

	public void setContractorsEquipment(CPPInlandMarineContractorsEquipment contractorsEquipment) {
		this.contractorsEquipment = contractorsEquipment;
	}

	public CPPInlaneMarineExhibition getExhibition() {
		return exhibition;
	}

	public void setExhibition(CPPInlaneMarineExhibition exhibition) {
		this.exhibition = exhibition;
	}

	public CPPInlandMarineInstallation getInstallation() {
		return installation;
	}

	public void setInstallation(CPPInlandMarineInstallation installation) {
		this.installation = installation;
	}

	public CPPInlandMarineMiscellaneousArticles getMiscellaneousArticles() {
		return miscellaneousArticles;
	}

	public void setMiscellaneousArticles(CPPInlandMarineMiscellaneousArticles miscellaneousArticles) {
		this.miscellaneousArticles = miscellaneousArticles;
	}

	public CPPInlandMarineMotorTruckCargo getMotorTruckCargo() {
		return motorTruckCargo;
	}

	public void setMotorTruckCargo(CPPInlandMarineMotorTruckCargo motorTruckCargo) {
		this.motorTruckCargo = motorTruckCargo;
	}

	public CPPInlandMarineSignsCM getSigns() {
		return signs;
	}

	public void setSigns(CPPInlandMarineSignsCM signs) {
		this.signs = signs;
	}

	public CPPInlandMarineTripTransit getTripTransit() {
		return tripTransit;
	}

	public void setTripTransit(CPPInlandMarineTripTransit tripTransit) {
		this.tripTransit = tripTransit;
	}

	public CPPInlandMarineValuablePapers getValuablePapers() {
		return valuablePapers;
	}

	public void setValuablePapers(CPPInlandMarineValuablePapers valuablePapers) {
		this.valuablePapers = valuablePapers;
	}
	
	public List<CPPInlandMarineUWQuestion> getUwQuestions() {
		return uwQuestions;
	}

	public void setUwQuestions(List<CPPInlandMarineUWQuestion> uwQuestions) {
		this.uwQuestions = uwQuestions;
	}
	
	public List<CPPInlandMarineUWQuestion> getUwQuestionsByCoveragePart(InlandMarineCPP.InlandMarineCoveragePart coveragePart) {
		List<CPPInlandMarineUWQuestion> questions = new ArrayList<CPPInlandMarineUWQuestion>();
		
		for (CPPInlandMarineUWQuestion q : this.getUwQuestions()) {
			if (q.getCoveragePart().equals(coveragePart)) {
				questions.add(q);
			}
		}
		
		return questions;
	}
	
	// Private Methods

	private List<String> getCoveragePartNames() {
		
		List<String> names = new ArrayList<>();
		
		for (InlandMarineCPP.InlandMarineCoveragePart part : getCoveragePart()) {
			names.add(part.getLocationOfQuestion());
		}
		
		return names;
		
	}
	
}














