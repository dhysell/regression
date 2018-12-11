package repository.gw.enums;

public enum RelationshipToInsured {
	Aunt("Aunt"),
	Brother("Brother"),
	Child("Child"),
	Cousin("Cousin"),
	Daughter("Daughter"),
	DaughterInLaw("Daughter In Law"),
	ExchangeStudent("Exchange Student"),
	Father("Father"),
	FatherInLaw("Father In Law"),
	FosterDaughter("Foster Daughter"),
	FosterSon("Foster Son"),
	Friend("Friend"),
	Granddaughter("Granddaughter"),
	Grandfather("Grandfather"),
	Grandmother("Grandmother"),
	Grandson("Grandson"),
	Insured("Insured"),
	Mother("Mother"),
	MotherInLaw("Mother In Law"),
	Nephew("Nephew"),
	Niece("Niece"),
	Partner("Partner"),
	SignificantOther("Significant Other/Partner"),
	Sister("Sister"),
	SisterInLaw("Sister In Law"),
	SonInLaw("Son In Law"),
	Stepdaughter("Stepdaughter"),
	Stepfather("Stepfather"),
	Stepmother("Stepmother"),
	Stepson("Stepson"),
	Uncle("Uncle");
	String value;
	
	RelationshipToInsured(String relationship) {
		value = relationship;
	}
	
	public String getValue(){
		return value;
	}

}
