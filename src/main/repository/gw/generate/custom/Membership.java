package repository.gw.generate.custom;

import repository.gw.enums.MembershipType;

import java.util.ArrayList;
import java.util.List;

public class Membership extends PolicyCommon {

	private repository.gw.enums.MembershipType membershipType = repository.gw.enums.MembershipType.Associate;

	private List<repository.gw.enums.Commodities.ProducerInformaion_Organization> producerInformaion_Organization_List = new ArrayList<repository.gw.enums.Commodities.ProducerInformaion_Organization>();
	private String organizationOther = "Other Organization";
	private List<repository.gw.enums.Commodities.ProducerInformaion_Agribusiness> producerInformaion_Agribusiness_List = new ArrayList<repository.gw.enums.Commodities.ProducerInformaion_Agribusiness>();
	private String agribusinessOther = "Other Agribusiness";
	private List<repository.gw.enums.Commodities.ProducerInformaion_WaterUser> producerInformaion_WaterUser_List = new ArrayList<repository.gw.enums.Commodities.ProducerInformaion_WaterUser>();
	private List<repository.gw.enums.Commodities.ProducerInformaion_DoYouMarketTheFollowing> ProducerInformaion_DoYouMarketTheFollowing = new ArrayList<repository.gw.enums.Commodities.ProducerInformaion_DoYouMarketTheFollowing>();
	private String doYouMarketTheFollowingOther = "Other Marketing Stuff";
	private List<AcresInOperation> acresInOperationList = new ArrayList<AcresInOperation>();
	private int numberOfEmployees = 1;
	private boolean doYouHaveH2AWorkers = false;

	private List<AcresPlanted> acresPlantedList = new ArrayList<AcresPlanted>();
	private List<LivestockInventory> livestockInventoryList = new ArrayList<LivestockInventory>();

	private List<Contact> membersList = new ArrayList<Contact>();

//	private boolean overrideEffExpDates = false;
//	public Date effectiveDate = null;
//	public Date expirationDate = null;
//	public Integer polTermLengthDays = 365;


    public Membership() {
		
	}
	
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}

	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	public boolean isDoYouHaveH2AWorkers() {
		return doYouHaveH2AWorkers;
	}

	public void setDoYouHaveH2AWorkers(boolean doYouHaveH2AWorkers) {
		this.doYouHaveH2AWorkers = doYouHaveH2AWorkers;
	}

	public repository.gw.enums.MembershipType getMembershipType() {
		return membershipType;
	}

	public void setMembershipType(MembershipType membershipType) {
		this.membershipType = membershipType;
	}

	public List<repository.gw.enums.Commodities.ProducerInformaion_Organization> getProducerInformaion_Organization_List() {
		return producerInformaion_Organization_List;
	}

	public void setProducerInformaion_Organization_List(
			List<repository.gw.enums.Commodities.ProducerInformaion_Organization> producerInformaion_Organization_List) {
		this.producerInformaion_Organization_List = producerInformaion_Organization_List;
	}

	public String getOrganizationOther() {
		return organizationOther;
	}

	public void setOrganizationOther(String organizationOther) {
		this.organizationOther = organizationOther;
	}

	public List<repository.gw.enums.Commodities.ProducerInformaion_Agribusiness> getProducerInformaion_Agribusiness_List() {
		return producerInformaion_Agribusiness_List;
	}

	public void setProducerInformaion_Agribusiness_List(
			List<repository.gw.enums.Commodities.ProducerInformaion_Agribusiness> producerInformaion_Agribusiness_List) {
		this.producerInformaion_Agribusiness_List = producerInformaion_Agribusiness_List;
	}

	public String getAgribusinessOther() {
		return agribusinessOther;
	}

	public void setAgribusinessOther(String agribusinessOther) {
		this.agribusinessOther = agribusinessOther;
	}

	public List<repository.gw.enums.Commodities.ProducerInformaion_WaterUser> getProducerInformaion_WaterUser_List() {
		return producerInformaion_WaterUser_List;
	}

	public void setProducerInformaion_WaterUser_List(List<repository.gw.enums.Commodities.ProducerInformaion_WaterUser> producerInformaion_WaterUser_List) {
		this.producerInformaion_WaterUser_List = producerInformaion_WaterUser_List;
	}

	public List<repository.gw.enums.Commodities.ProducerInformaion_DoYouMarketTheFollowing> getProducerInformaion_DoYouMarketTheFollowing() {
		return ProducerInformaion_DoYouMarketTheFollowing;
	}

	public void setProducerInformaion_DoYouMarketTheFollowing(
			List<repository.gw.enums.Commodities.ProducerInformaion_DoYouMarketTheFollowing> producerInformaion_DoYouMarketTheFollowing) {
		ProducerInformaion_DoYouMarketTheFollowing = producerInformaion_DoYouMarketTheFollowing;
	}

	public String getDoYouMarketTheFollowingOther() {
		return doYouMarketTheFollowingOther;
	}

	public void setDoYouMarketTheFollowingOther(String doYouMarketTheFollowingOther) {
		this.doYouMarketTheFollowingOther = doYouMarketTheFollowingOther;
	}

	public List<AcresInOperation> getAcresInOperationList() {
		return acresInOperationList;
	}

	public void setAcresInOperationList(List<AcresInOperation> acresInOperationList) {
		this.acresInOperationList = acresInOperationList;
	}
	
	public List<AcresPlanted> getAcresPlantedList() {
		return acresPlantedList;
	}

	public void setAcresPlantedList(List<AcresPlanted> acresPlantedList) {
		this.acresPlantedList = acresPlantedList;
	}

	public List<LivestockInventory> getLivestockInventoryList() {
		return livestockInventoryList;
	}

	public void setLivestockInventoryList(List<LivestockInventory> livestockInventoryList) {
		this.livestockInventoryList = livestockInventoryList;
	}

	public List<Contact> getMembersList() {
		return membersList;
	}

	public void setMembersList(List<Contact> members) {
		this.membersList = members;
	}

//	public boolean isOverrideEffExpDates() {
//		return overrideEffExpDates;
//	}
//
//	public void setOverrideEffExpDates(boolean overrideEffExpDates) {
//		this.overrideEffExpDates = overrideEffExpDates;
//	}

	public class AcresInOperation {

		private repository.gw.enums.Commodities.AcresInOperation acresInOperation = repository.gw.enums.Commodities.AcresInOperation.Irrigated;
		private int numberOfAcres = 1;

		public AcresInOperation(repository.gw.enums.Commodities.AcresInOperation acresInOperation, int numberOfAcres) {
			this.setAcresInOperation(acresInOperation);
			this.setNumberOfAcres(numberOfAcres);
		}

		public int getNumberOfAcres() {
			return numberOfAcres;
		}

		public void setNumberOfAcres(int numberOfAcres) {
			this.numberOfAcres = numberOfAcres;
		}

		public repository.gw.enums.Commodities.AcresInOperation getAcresInOperation() {
			return acresInOperation;
		}

		public void setAcresInOperation(repository.gw.enums.Commodities.AcresInOperation acresInOperation) {
			this.acresInOperation = acresInOperation;
		}

	}

	public class AcresPlanted {

		private repository.gw.enums.Commodities.AcresPlanted acresPlanted = repository.gw.enums.Commodities.AcresPlanted.Grass;
		private int numberOfAcres = 1;

		public AcresPlanted(repository.gw.enums.Commodities.AcresPlanted acresPlanted, int numberOfAcres) {
			this.setAcresPlanted(acresPlanted);
			this.setNumberOfAcres(numberOfAcres);
		}

		public int getNumberOfAcres() {
			return numberOfAcres;
		}

		public void setNumberOfAcres(int numberOfAcres) {
			this.numberOfAcres = numberOfAcres;
		}

		public repository.gw.enums.Commodities.AcresPlanted getAcesPlanted() {
			return acresPlanted;
		}

		public void setAcresPlanted(repository.gw.enums.Commodities.AcresPlanted acresPlanted) {
			this.acresPlanted = acresPlanted;
		}

	}
	
	public class LivestockInventory {
		
		private repository.gw.enums.Commodities.LivestockInventory livestockInventory = repository.gw.enums.Commodities.LivestockInventory.BeeHoney;
		private int numberOfAnimals = 1;
		
		public LivestockInventory(repository.gw.enums.Commodities.LivestockInventory livestockInventory, int numberOfAnimals) {
			
		}

		public repository.gw.enums.Commodities.LivestockInventory getLivestockInventory() {
			return livestockInventory;
		}

		public void setLivestockInventory(repository.gw.enums.Commodities.LivestockInventory livestockInventory) {
			this.livestockInventory = livestockInventory;
		}

		public int getNumberOfAnimals() {
			return numberOfAnimals;
		}

		public void setNumberOfAnimals(int numberOfAnimals) {
			this.numberOfAnimals = numberOfAnimals;
		}
		
	}


}













