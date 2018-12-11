package repository.gw.generate.custom;

import com.idfbins.enums.Gender;
import org.apache.commons.lang3.time.DateUtils;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class LivestockScheduledItem {

	private repository.gw.enums.LivestockType parentLivestockType;
	private repository.gw.enums.LivestockScheduledItemType type;
	private String description;
	private String tagIdNum;
	private String brand;
	private String breed;
	private int limit;
	private int numOfHead;
	private Date purchaseDate;
	private String registrationIdNum;
	private Date birthdate;
	private String color;
	private boolean genderMale;
	private int weight;
	private BigDecimal purchasePrice;
	// ----------------------------------
	private ArrayList<String> additionalInsureds;
	
	public LivestockScheduledItem(repository.gw.enums.LivestockType parentLivestockType,
                                  repository.gw.enums.LivestockScheduledItemType type,
                                  Date purchaseDate,
                                  String tagIdNum,
                                  String breed,
                                  Date birthDate,
                                  String color,
                                  Gender gender,
                                  int weight,
                                  BigDecimal price,
                                  int limit) throws Exception {
		setParentLivestockType(parentLivestockType);
		setType(type);
		setPurchaseDate(purchaseDate);
		setTagIdNum(tagIdNum);
		setBreed(breed);
		setBirthdate(birthDate);
		setColor(color);
		setGenderMale(genderMale);
		setWeight(weight);
		setPurchasePrice(price);
		setLimit(limit);
	}
	
	public LivestockScheduledItem(repository.gw.enums.LivestockType parentLivestockType,
                                  repository.gw.enums.LivestockScheduledItemType type,
                                  String description,
                                  String tagIdNum,
                                  String brand,
                                  String breed,
                                  int limit) throws Exception {
		setParentLivestockType(parentLivestockType);
		setType(type);
		setDescription(description);
		setTagIdNum(tagIdNum);
		setBrand(brand);
		setBreed(breed);
		setLimit(limit);
	}
	
	public LivestockScheduledItem(repository.gw.enums.LivestockType parentLivestockType,
                                  repository.gw.enums.LivestockScheduledItemType type,
                                  String description,
                                  int limit) throws Exception {
		setParentLivestockType(parentLivestockType);
		setType(type);
		setDescription(description);
		setLimit(limit);
	}
	
	public LivestockScheduledItem(repository.gw.enums.LivestockType parentLivestockType,
                                  repository.gw.enums.LivestockScheduledItemType type,
                                  int numOfHead,
                                  String description) throws Exception {
		setParentLivestockType(parentLivestockType);
		setType(type);
		setDescription(description);
		setNumOfHead(numOfHead);
	}
	
	public repository.gw.enums.LivestockType getParentLivestockType() {
		return parentLivestockType;
	}
	
	public void setParentLivestockType(LivestockType parentLivestockType) {
		this.parentLivestockType = parentLivestockType;
	}
	
	private void checkIfParentLivestockTypeIsSet() throws Exception {
		if(getParentLivestockType() == null) {
			throw new Exception("ParentLivestockType must be set before you can set this field");
		}
	}
	
	public repository.gw.enums.LivestockScheduledItemType getType() {
		return type;
	}
	
	public void setType(repository.gw.enums.LivestockScheduledItemType type) throws Exception {
		checkIfParentLivestockTypeIsSet();
		ArrayList<repository.gw.enums.LivestockScheduledItemType> allForLivestock = LivestockScheduledItemType.getAllWithSpecificParent(getParentLivestockType());
		if(!allForLivestock.contains(type)) {
			throw new Exception("Type selected is not valid for Livestock Parent Type");
		}
		this.type = type;
	}
	
	private void checkIfTypeIsSet() throws Exception {
		if(getType() == null) {
			throw new Exception("Type must be set before you can set this field");
		}
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.description = description;
	}
	
	public String getTagIdNum() {
		return tagIdNum;
	}
	
	public void setTagIdNum(String tagIdNum) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.tagIdNum = tagIdNum;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.brand = brand;
	}
	
	public String getBreed() {
		return breed;
	}
	
	public void setBreed(String breed) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.breed = breed;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.limit = limit;
	}
	
	public int getNumOfHead() {
		return numOfHead;
	}
	
	public void setNumOfHead(int numOfHead) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.numOfHead = numOfHead;
	}
	
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	
	public void setPurchaseDate(Date purchaseDate) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.purchaseDate = purchaseDate;
	}
	
	public String getRegistrationIdNum() {
		return registrationIdNum;
	}
	
	public void setRegistrationIdNum(String registrationIdNum) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.registrationIdNum = registrationIdNum;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		if(birthdate.before(DateUtils.addDays(new Date(), -1821))||birthdate.after(DateUtils.addDays(new Date(), -90))){
			this.birthdate = birthdate;
		}else{
			this.birthdate = DateUtils.addDays(new Date(), -1000);
		}
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.color = color;
	}
	
	public boolean isGenderMale() {
		return genderMale;
	}
	
	public Gender getGender(){
		if(genderMale){
			return Gender.Male;
		}else{
			return Gender.Female;
		}	
	}
	
	public void setGenderMale(boolean genderMale) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.genderMale = genderMale;
	}
	
	public void setGenderMale(Gender genderMale) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		if(genderMale.equals(Gender.Female)){
			this.genderMale = false;
		}else{ this.genderMale = true;}		
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.weight = weight;
	}
	
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(BigDecimal purchasePrice) throws Exception {
		checkIfParentLivestockTypeIsSet();
		checkIfTypeIsSet();
		this.purchasePrice = purchasePrice;
	}
	
	public ArrayList<String> getAdditionalInsureds() {
		return additionalInsureds;
	}
	
	public void setAdditionalInsureds(ArrayList<String> additionalInsureds) throws Exception {
		this.additionalInsureds = additionalInsureds;
	}
	
	
}
