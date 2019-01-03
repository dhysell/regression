package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum CommercialAutoUWIssues {
	//INFORMATIONAL
	  Pleaseprovideunderwritingwithcopiesofleaseagreementsifnotafinancialinstitution("Please provide underwriting with copies of lease agreements if not a financial institution."),
	  Mobilehomecontentscoveragedoesnotincludeofficeequipmentsalessamplestheftmoneyetc("Mobile home contents coverage does not include office equipment, sales samples, theft, money, etc."),
	  Notifytheapplicant_insuredthatprofessionalsareexcludedContactbrokerageforassistance("Notify the applicant/insured that professionals are excluded. Contact brokerage for assistance."),
	  TruckersInsuranceforNon_truckinguseislimitedtosituationswheretheinsuredisnothaulingpropertyofothershaulingotherstrailersoroperatingunderaleaseagreementwithothersExplaintherestrictionsofcoveragetotheapplicant_insured("Truckers Insurance for Non-trucking use is limited to situations where the insured is not hauling property of others, hauling other�s trailers or operating under a lease agreement with others. Explain the restrictions of coverage to the applicant/insured."),
	  Provideunderwritingwithaphotodescriptionanduseoftheamphibiousvehicletobeinsured("Provide underwriting with a photo, description and use of the amphibious vehicle to be insured."),
	  PleaseensurethatalldriversthatneedtobeaddedtotheDriveOtherCarareadded("Please ensure that all drivers that need to be added to the Drive Other Car are added."),
	  VehiclehasbeendeletedifthereiscargocoveragepleasereviewInlandMarineforanychanges("Vehicle has been deleted if there is cargo coverage please review Inland Marine for any changes."),
	  Pleasenotifytheapplicant_insuredthatnocoverageisprovidedforcontentsofthetrailerequippedwithlivingquartersorthemobilehomeIfcoverageisdesiredchooseCA2016MobileHomesContentsCoverage("Please notify the applicant/insured that no coverage is provided for contents of the trailer equipped with living quarters or the mobile home.  If coverage is desired choose CA 20 16 Mobile Homes Contents Coverage"),
	  Informapplicant_insuredthatweareexcludingtheexistingdamage("Inform applicant/insured that we are excluding the existing damage."),
	  InordertoprocessanexcludeddrivertheinsuredmustsignarequestshowingthattheyacknowledgethatspecifieddriveristobeexcludedPleasescanthedocumenttofileandsubmittounderwritingforconsideration("In order to process an excluded driver the insured must sign a request showing that they acknowledge that specified driver is to be excluded. Please scan the document to file and submit to underwriting for consideration."),
	  OutofStatevehiclesnotlistedonthepolicyareexcludedNotifyApplicant_insuredofthislimitation("Out of State vehicles not listed on the policy are excluded. Notify Applicant/insured of this limitation."),
	 
	  //BLOCK BIND
	  Applicant_insuredhasrejectedIdahoUninsuredandUnderinsuredMotoristcoveragecompleterejectionformIDCA100002andsubmittounderwriting("Applicant/insured has rejected Idaho Uninsured and Underinsured Motorist coverage complete rejection form IDCA 10 0002 and submit to underwriting."),
	  ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940hasbeenselectedProvideunderwritingdetailsastotheothercoverageinplaceandreasonthatWCisnotinsuringtheautos("Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40 has been selected.  Provide underwriting details as to the other coverage in place and reason that WC is not insuring the autos."),
	  Therearevehiclesover45001lbsanddriversunder25Pleasesubmittounderwritingforconsideration("There are vehicles over 45,000 lbs. and drivers under 25. Please submit to underwriting for consideration."),
	  Pleaseadddriverstothepolicy("Please add drivers to the policy."),
	  LongdistanceoptionissettoDenverProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed("Long distance option is set to Denver. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed."),
	  LongdistanceoptionissettoPortlandProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed("Long distance option is set to Portland. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed."),
	  LongdistanceoptionsettoSaltLakeCityProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed("Long distance option is set to Salt Lake City. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed."),
	  LongdistanceoptionissettoPacificProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed("Long distance option is set to Pacific. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed."),
	  LongdistanceoptionissettoMountainProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed("Long distance option is set to Mountain. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed."),

	  //REJECT
	  CannothaveMedicalPaymentsCA9903ontrailersonlyorwithoutanyvehiclesPleaseremoveMedicalPaymentsCA9903("Cannot have Medical Payments CA 99 03 on trailers only or without any vehicles. Please remove Medical Payments CA 99 03."),
	  DriveOtherCarCoverageisonlyavailableifpolicyhasaprivatepassengervehicleinsuredContactunderwritingasitmaybenecessarytochangetheratingonalightvehicle("Drive Other Car Coverage is only available if policy has a private passenger vehicle insured. Contact underwriting as it may be necessary to change the rating on a light vehicle."),
	  CannotsetLiabilitylimitsbelowUnderinsuredMotorist_UninsuredMotorist_DOCUnderinsuredMotorist_DOCUninsuredmotoristPleaseadjustlimitsbelowLiabilityOptionalCoveragesBasedonexistence("Cannot set Liability limits below {Underinsured Motorist/Uninsured Motorist/DOC Underinsured Motorist/ DOC Uninsured motorist}. Please adjust limits below Liability. (OptionalCoverages Based on existence)"),
	  Inordertoqualifyforgaragekeeperscoveragetheapplicant_insuredmusthaveliabilitycoverageonownedornon_ownedvehiclesPleaseaddliabilitycoverage("In order to qualify for garagekeepers coverage the applicant/insured must have liability coverage on owned or non-owned vehicles.  Please add liability coverage."),
	  TheremustbeoneyouthfuldriverpereachprivatepassengervehicleIftherearemoreyouthfuldriversthentherearevehiclesputtheexcessdriverasNotRated("There must be one youthful driver per each private passenger vehicle. If there are more youthful drivers then there are vehicles put the excess driver as Not Rated."),
	  ThereismorethanoneyouthfuldriverratedonthesamevehiclePleaseassignoneyouthfuloperatorpervehicle("There is more than one youthful driver rated on the same vehicle. Please assign one youthful operator per vehicle."),
	  Therearedriversundertheageof21pleaseaddthedriverstothedriversscreen("There are drivers under the age of 21 please add the driver(s) to the drivers screen."),
	  ThequestionIstrailerbeingtowedbyalighttruckismarkedyesHowevertherearenolighttrucksonthepolicyPleaseaddalighttruckorchangestheanswertono("The question Is trailer being towed by a light truck? is marked yes. However there are no light trucks on the policy. Please add a light truck or changes the answer to no."),
	  LiabilityneedstoberemovedfromthepolicybecausetherearenovehicleswithLiabilityCoverage("Liability needs to be removed from the policy because there are no vehicles with Liability Coverage."),
	  OneormoreofthevehicleshavetheLiabilityCoveragequestionmarkedYesPleaseaddLiabilityontothepolicyorchangetheanswertoNo("One or more of the vehicles have the Liability Coverage question marked Yes. Please add Liability on to the policy or change the answer to No."),
	
	  //Blocks Quote Release
	  ForCA2070pleaserefertoUnderwritingsupervisorforreviewtreatyexceptionandpricing("For CA 20 70 please refer to Underwriting supervisor for review, treaty exception, and pricing."),
	  AudioVisualandDataElectronicEquipmentlimitsover10000requirepriorapprovalProvideUnderwritingalistofequipmentapproximatevaluesandthepurposeoftheequipmentforconsideration("Audio, Visual and Data Electronic Equipment limits over $10,000 require prior approval. Provide Underwriting a list of equipment, approximate values and the purpose of the equipment for consideration."),
	  ProvideproofofrepairsandphotostounderwritinginordertoshowrepairshavebeenmadeforIDCA313006("Provide proof of repairs and photos to underwriting in order to show repairs have been made for IDCA 31 3006."),
	  Applicant_insuredhasrequestedaMCS_90ThismustbereferredtounderwritingExplainthebondfeaturesofthisendorsementandtheirobligationtoreimburseusforotherwiseuncoveredlosses("Applicant/insured has requested a MCS-90. This must be referred to underwriting. Explain the bond features of this endorsement and their obligation to reimburse us for otherwise uncovered losses."),
	  Applicant_insuredtransportsperishablerefrigeratedfoodproductsDescribecleaningandsanitationproceduresofthetrailerfromoneloadtotheother("Applicant/insured transports perishable refrigerated food products. Describe cleaning and sanitation procedures of the trailer from one load to the other."),
	  Applicant_insuredisajunkdealerPleasedescribenatureofitemstransportedtounderwritingtodetermineeligibility("Applicant/insured is a junk dealer. Please describe nature of items transported to underwriting to determine eligibility."),
	  Applicant_insuredsautooperationisclassifiedasallotherPleasecontactunderwritingwithdetailregardingtheoperationforassistanceinrating("Applicant/insured�s auto operation is classified as all other. Please contact underwriting with detail regarding the operation for assistance in rating."),
	  PleaseverifythatZipcodeisthecorrectzipcodeIfitiscorrectpleasecontactunderwritingforterritorycode("Please verify that Zipcode is the correct zip code. If it is correct please contact underwriting for territory code."),
	  PleaseexplainwhytheFactoryCostNewvaluewaschangedfromitsoriginalvalue("Please explain why the Factory Cost New value was changed from its original value."),
	  Pleaserequestapprovalfromtheunderwritertoaddsymbol123or4tothepolicyorremoveExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940("Please request approval from the underwriter to add symbol 1, 2, 3, or 4 to the policy or remove Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40."),
	  
	  
	  //WARN USER
	  IdahoUninsuredandUnderinsuredMotoristhavebeenchangedtomatchtheDriveOtherCarUninsuredandUnderinsuredMotorist("Idaho Uninsured and Underinsured Motorist have been changed to match the Drive Other Car Uninsured and Underinsured Motorist.");
	
	String value;
	
	private CommercialAutoUWIssues(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static CommercialAutoUWIssues valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "").replace(",", "").replace("/", "_").replace(".", "");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
	
	private static final List<CommercialAutoUWIssues> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static CommercialAutoUWIssues random()  {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
