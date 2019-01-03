package repository.gw.enums;

public enum ValitaionRules_SectionI {
	PR012_Propertydeductibleover5000("Section I deductible of $5,000 or more requires at least 1 Coverage A Residence Premises or Condominium Residence Premises with limit greater or equal to $200,000 . (PR012)",ValidationAction.Quote),
	PR016_CoverageCMinValue("Coverage C limit must be greater or equal to $15,000 . (PR016)",ValidationAction.Screen),
	PR018_NoofResidenceandproperties("The total number of properties <count of properties> exceeds number of residence on Location # <no. of location>. Please correct the number of residence for this location. (PR018)",ValidationAction.Quote),
	PR019_BlanketIncludeandgeneralliabilitylivestock("Livestock added to Section II liability, and FPP coverage type is blanket include without Livestock. Add Livestock to FPP. (PR019)",ValidationAction.Quote),
	PR021_FPPLivestockLimit("FPP Livestock limit cannot exceed $2,500 per head of <<Type>>.",ValidationAction.Screen),
	PR025_ResidenceFrameNonFrameMinlimit("For <<Construction Type>> Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E. (PR025)",ValidationAction.Quote),
	PR026_ResidenceModularManufacturedMinlimit("Section I: For Modular/Manufactured Residence Premises to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR026)",ValidationAction.Quote),
	PR027_ResidenceModularManufacturedMinyear("Modular/Manufactured Residence Premises cannot be older than 1985. (PR027)",ValidationAction.Screen),
	PR028_ResidenceMobileWithfoundationMinlimit("Section I: For Mobile Home with foundation Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E. (PR028)",ValidationAction.Quote),
	PR029_ResidenceMobileWithOutfoundationMinlimit("For Mobile Home without foundation Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000 $4,000. If not then please move the property to Coverage E. (PR029)",ValidationAction.Quote),
	PR030_MobileorModularminsqft("For <<Construction Type>> <<Property Type>> to qualify for Coverage A, the square feet must be greater or equal to 400. If not then please move the property to Coverage E (PR030)",ValidationAction.Screen),
	PR031_CondoResidenceFrameNonFrameMinlimit("Section I: For <<Construction Type>> Condominium Residence Premises to qualify for Coverage A, the limit must be greater or equal to $40,000. (PR031)",ValidationAction.Quote),
	PR032_CondoResidenceFrameNonFrameMinyear("<<Construction Type>> Condominium Residence Premises cannot be older than 1954. (PR032)",ValidationAction.Screen),
	PR034_VacationhomeFrameNonFrameMinlimit("Section I: For <<Construction Type>> Vacation Home to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E. (PR034)",ValidationAction.Quote),
	PR035_ResidenceModularManufacturedMinlimit("Section I: For Modular/Manufactured Vacation Home to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR035)",ValidationAction.Quote),
	PR036_Vacationhomemodularminyear("Modular/Manufactured Vacation Home cannot be older than 1985. (PR036)",ValidationAction.Screen),
	PR037_VacationhomemobileWithfoundationminlimit("For Mobile Home with foundation Vacation Home to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E. (PR037)",ValidationAction.Quote),
	PR039_Condovacationframenonframeminlimit("Section I: For <<Construction Type>> Condominium Vacation Home to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR039)",ValidationAction.Quote),
	PR041_DwellingPremisesminlimit("Section I: For Dwelling Premises to qualify for Coverage A, the limit must be greater or equal to $20,000. If not then please move the property to Coverage E. (PR041)",ValidationAction.Quote),
	PR043_Dwellingpremisesmodularminyear("Modular/Manufactured Dwelling Premises cannot be older than 1985. (PR043)",ValidationAction.Screen),
	PR044_CondoDwellingFrameNonFrameminlimit("Section I: For <<Construction Type>> Condominium Dwelling Premises to qualify for Coverage A, the limit must be greater or equal to $40,000. (PR044)",ValidationAction.Quote),
	PR046_DwellingUnderConstructionminlimit("Section I: For Dwelling Under Construction to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR046)",ValidationAction.Quote),
	PR048_DwellingUnderConstructionyear("Dwelling Under Construction cannot be older than <<Current year>>. (PR048)",ValidationAction.Screen),
	PR049_ResidenceFrameNonFrameMinlimitonnewerhomes("For <<Construction Type>> Residence Premises to qualify for Coverage A built after 1968, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR049)",ValidationAction.Quote),
	PR050_Propertyandacres("Any location with property on it must have at least 1 acre. (PR050)",ValidationAction.Quote),
	PR075_CovAminonFire("Limit on Property �<<Property number>> on Location <<location number>> must be at least $40,000. (PR075)",ValidationAction.Quote),
	PR076_CovEhabitableminlimit("Limit on Property <<Property number>> on Location <<location Number>> must be at least $15,000. (PR076)",ValidationAction.Quote),
	PR079_VacantCovA("The buindling does not qualify for Coverage A. Please change the property to a Coverage Premises. (PR079)",ValidationAction.Screen),
	PR080_NoRiskonPropertyorFPP("Risk and Category must be entered on all properties and Farm and Personal Property (if added) to issue policy. (PR080)",ValidationAction.Issuance),
	PR086_BorrowedorNonOwnedEquip("Policy cannot have both Borrowed Equipment and Non-owned Equipment at same time. (PR086)",ValidationAction.Quote),
	PR088_Habitatforhumanity("All habitable buildings must have owners assigned. (PR088)",ValidationAction.Quote),
	PR089_CannotremovePNIorANI("<<policy members name>> is the PNI and cannot be removed.",ValidationAction.Screen),
	PR091_LessoronFPP("FPP lessor <<lessor name>> must be added to at least one item on FPP. (PR091)",ValidationAction.Quote),
	PR092_Propertyonlocation("A property exists on location # <<location number>> . Please remove it before removing the location. (PR092)",ValidationAction.Quote),
	PR093_Vehiclegaragedatlocation("Vehicle # <<vehicle number>>�is using location #<<location number>>�as the garaged location and must be changed before this location can be removed. (PR093)",ValidationAction.Quote),
	PR094_Farmequipmentonlocation("Farm equipment- Equip # <<vehicle number>> is using location #<<location number>> and must be changed before this location can be removed. (PR094)",ValidationAction.Quote);
	String validationMessage;
	ValidationAction validationAction;
	
	private ValitaionRules_SectionI(String validationMessage, ValidationAction action){
		this.validationMessage = validationMessage;
		this.validationAction = action;
	}
	
	public String getMessage(){
		return validationMessage;
	}
	
	public ValidationAction getValidationAction() {
		return validationAction;
	}
	
}
