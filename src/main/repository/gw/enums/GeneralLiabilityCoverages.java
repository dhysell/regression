package repository.gw.enums;

public enum GeneralLiabilityCoverages {
	/**
	 * Coverage <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	CG0001("Commercial General Liability Coverage Form CG 00 01"),
	/**
	 * Coverage <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when one of the following class codes are selected: 70412, 58161, 50911, 59211, 58165, or 58166.
	 */
	CG0033("Liquor Liability Coverage Form CG 00 33"),
	/**
	 * TODO Continue working on Coverages. I was searching the spreadsheet and filling this out in order. 
	 * Coverage <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2001("Primary And Noncontributory - Other Insurance Condition CG 20 01"),
	/**
	 * <p>
	 * This becomes required when one of the following class codes are selected: 41668, 41667, 41670, 41669, or 11138. 
	 */
	CG2002("Additional Insured - Club Members CG 20 02"),
	/**
	 * <p>
	 * Available when Additional Insured - Concessionaires Trading Under Your Name CG 20 03 is selected on the Screen New Additional Insured.
	 */
	CG2003("Additional Insured - Concessionaires Trading Under Your Name CG 20 03"),
	/**
	 * This becomes required when one of the following class codes are selected: 62000, 62001, 62002, or 62003.
	 */
	CG2004("Additional Insured - Condominium Unit Owners CG 20 04"),
	/**
	 * Available when Additional Insured - Controlling Interest CG 20 05 is selected on the Screen New Additional Insured.
	 */
	CG2005("Additional Insured - Controlling Interest CG 20 05"),
	/**
	 * UW Question @ QQ: Does the applicant/insured have a contract with the additional insured? Correct Answer - YES
	 */
	CG2007("Additional Insured - Engineers, Architects Or Surveyors CG 20 07"),
	/**
	 * This becomes required when one of the following class codes are selected: 11138, 44070, 44072, 45190, 45191, 45192, or 45193. 
	 */
	CG2008("Additional Insured - Users Of Golfmobiles CG 20 08"),
	/**
	 * Available when Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10 is selected on the Screen New Additional Insured.
	 */
	CG2010("Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10"),
	/**
	 * Available when Additional Insured - Managers Or Lessors Of Premises CG 20 11 is selected on the Screen New Additional Insured.
	 */
	CG2011("Additional Insured - Managers Or Lessors Of Premises CG 20 11"),
	/**
	 * Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12 is selected on the Screen New Additional Insured.
	 */
	CG2012("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12"),
	/**
	 * Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13 is selected on the Screen New Additional Insured.
	 */
	CG2013("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13"),
	/**
	 * UW Question @ QQ: Please provide underwriting details of the equine exposure i.e. type of events, number of animals, number of events etc.  "String Text Box"
	 */
	CG2014("Additional Insured - Users Of Teams, Draft Or Saddle Animals CG 20 14"),
	/**
	 * UW Question @ QQ: Is applicant/insured the manufacturer of the product? Correct Answer - Yes <br>
	 * Available when Additional Insured - Vendors CG 20 15 is selected on the Screen New Additional Insured.
	 */
	CG2015("Additional Insured - Vendors CG 20 15"),
	/**
	 * This becomes required when class code 68500 is selected.
	 */
	CG2017("Additional Insured - Townhouse Associations CG 20 17"),
	/**
	 * Available when Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18 is selected on the Screen New Additional Insured.
	 */
	CG2018("Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18"),
	/**
	 * <p>
	 * Only available in Full Application because it needs the Qualification question <br>
	 * �Is the applicant/insured a not for profit organization whose major purposes is charitable causes?� to be answered Yes <br>
	 * Which is only available in Full Application.
	 */
	CG2020("Additional Insured - Charitable Institutions CG 20 20"),
	/**
	 * This becomes required when class code 41650 is selected
	 */
	CG2022("Additional Insured - Church Members And Officers CG 20 22"),
	/**
	 * <p>
	 * Only available when Organization Type is Trust. Located on the Policy Info.  <br>
	 * Has to be set up as a COMPANY.
	 */
	CG2023("Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23"),
	/**
	 * Available when Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24 is selected on the Screen New Additional Insured.
	 */
	CG2024("Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24"),
	/**
	 * Available when Additional Insured - Designated Person Or Organization CG 20 26 is selected on the Screen New Additional Insured.
	 */
	CG2026("Additional Insured - Designated Person Or Organization CG 20 26"),
	/**
	 * Available when Additional Insured - Co-Owner Of Insured Premises CG 20 27 is selected on the Screen New Additional Insured.
	 */
	CG2027("Additional Insured - Co-Owner Of Insured Premises CG 20 27"),
	/**
	 * Available when Additional Insured - Lessor Of Leased Equipment CG 20 28 is selected on the Screen New Additional Insured.
	 */
	CG2028("Additional Insured - Lessor Of Leased Equipment CG 20 28"),
	/**
	 *  Available when Additional Insured - Grantor Of Franchise CG 20 29 is selected on the Screen New Additional Insured.
	 */
	CG2029("Additional Insured - Grantor Of Franchise CG 20 29"),
	/**
	 * UW Question @ QQ: Is the applicant/insured required to provide insured status to the entity hired by someone else by contract? Correct Answer - YES <br>
	 * Available when Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32 is selected on the Screen New Additional Insured.
	 */
	CG2032("Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32"),
	/**
	 * Available when Additional Insured - Grantor Of Licenses CG 20 36 is selected on the Screen New Additional Insured.
	 */
	CG2036("Additional Insured - Grantor Of Licenses CG 20 36"),
	/**
	 * Available when Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37 is selected on the Screen New Additional Insured.
	 */
	CG2037("Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2100("Exclusion - All Hazards In Connection With Designated Premises CG 21 00"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 63218, 63217, 63220, 63219, 43421, or 43424.
	 */
	CG2101("Exclusion - Athletic Or Sports Participants CG 21 01"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes a default value with no other choices when one of the following class codes is selected: 12375, 12374, 45900, or 15839. <br>
	 * These have to be the only class codes on the policy for this rule to work. If another class code is on the policy that is not in this list 
	 * Otherwise Available when the class premium base does not include (+) and 
	 * Exclude is chosen by an UW on CG 00 01 - Products / Completed Operations Aggregate Limit (no premium charge is made) attach this form as required.
	 */
	CG2104("Exclusion - Products-Completed Operations Hazard CG 21 04"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	CG2107("Exclusion - Access Or Disclosure Of Confidential Or Personal Information And Data-Related Liability - Limited Bodily Injury Exception Not Included CG 21 07"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 * Not available when CG 24 50 is selected.
	 */
	CG2109("Exclusion - Unmanned Aircraft CG 21 09"),
	/**
	 * Exclusion <br>
	 * Defined By Script <br>
	 * Nothing to fill out <br>
	 * This becomes required when one of the following class codes are selected: 41650, 41677, 91805, 47052, 58408, 58409, 58456, 58457, 58458, or 58459. Other than that this remains electable.
	 */
	CG2116("Exclusion - Designated Professional Services CG 21 16"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when question "Does applicant/insured move buildings or structures?" is answered yes. 
	 */
	CG2117("Exclusion - Movement Of Buildings Or Structures CG 21 17"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2132("Communicable Disease Exclusion CG 21 32"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2133("Exclusion - Designated Products CG 21 33"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2134("Exclusion - Designated Work CG 21 34"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2135("Exclusion - Coverage C - Medical Payments CG 21 35"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2136("Exclusion - New Entities CG 21 36"),
	/**
	 * Available CG 22 96 is selected unless the this become required because of the defined by script. Then CG 22 96 would be come unavailable. <br>
	 * This becomes required when one of the following class codes are selected: 91130, 91636, 43200, 65007, 66123, 66122, 46822, 46882, 46881, 47052, or 98751. Other than that this remains electable.
	 */
	CG2138("Exclusion - Personal And Advertising Injury CG 21 38"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2141("Exclusion - Intercompany Products Suits CG 21 41"),
	/**
	 * Condition <br>
	 * Electable <br>
	 * Nothing to fill out
	 */
	CG2144("Limitation Of Coverage To Designated Premises, Project Or Operation CG 21 44"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	CG2146("Abuse Or Molestation Exclusion CG 21 46"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when Employment Practices Liability Insurance IDCG 31 2013 is not selected.
	 */
	CG2147("Employment-Related Practices Exclusion CG 21 47"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 * Not available if CG 21 55 is selected.
	 */
	CG2149("Total Pollution Exclusion Endorsement CG 21 49"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 16905 or 16906.
	 */
	CG2150("Amendment Of Liquor Liability Exclusion CG 21 50"),
	/**
	 * Coverage <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2151("Amendment Of Liquor Liability Exclusion - Exception For Scheduled Activities CG 21 51"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when class code 61223 is selected. <br>
	 * If class code 61227 or 61226 is selected and the question ""Is applicant/insured involved in any of the following or related activities: Accounting, banking, credit card company, credit reporting, credit union, financial investment services, securities broker or dealer or tax preparation?"" is answered yes automatically attach this endorsement."
	 */
	CG2152("Exclusion - Financial Services CG 21 52"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2153("Exclusion - Designated Ongoing Operations CG 21 53"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 * Not available if CG 21 49 is selected.
	 */
	CG2155("Total Pollution Exclusion with A Hostile Fire Exception CG 21 55"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes is selected: 41604, 41603, 41697, 41696, 43889, 46005, and/or 46004.
	 */
	CG2156("Exclusion - Funeral Services CG 21 56"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when one of the following class codes are selected: 48600 or 41650.
	 */
	CG2157("Exclusion - Counseling Services CG 21 57"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when one of the following class codes are selected: 91200 or 99851.
	 */
	CG2158("Exclusion - Professional Veterinarian Services CG 21 58"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when class code 46112 is selected.
	 */
	CG2159("Exclusion - Diagnostic Testing Laboratories CG 21 59"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	CG2160("Exclusion - Year 2000 Computer-Related And Other Electronic Problems CG 21 60"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2166("Exclusion - Volunteer Workers CG 21 66"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	CG2167("Fungi Or Bacteria Exclusion CG 21 67"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	CG2184("Exclusion Of Certified Nuclear, Biological, Chemical Or Radiological Acts Of Terrorism; Cap On Covered Certified Acts Losses From Certified Acts Of Terrorism CG 21 84"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 96816, 97047, or 97050.
	 */
	CG2186("Exclusion - Exterior Insulation And Finish Systems CG 21 86"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	CG2196("Silica or Silica-Related Dust Exclusion CG 21 96"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 61223, or 96317.
	 */
	CG2224("Exclusion - Inspection, Appraisal And Survey Companies CG 22 24"),
	/**
	 * Condition  <br>
	 * Required <br>
	 * Nothing to fill out  <br>
	 * Required when class code 49333 is selected.
	 */
	CG2228("Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when class code 98751, 49763, and/or 18991 is on the policy.
	 */
	CG2229("Exclusion - Property Entrusted CG 22 29"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 67513, 67512, 47474, or 47477.
	 */
	CG2230("Exclusion - Corporal Punishment CG 22 30"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when one of the following class codes are selected: 91135, 97003, or 97002.
	 */
	CG2233("Exclusion - Testing Or Consulting Errors And Omissions CG 22 33"),
	/**
	 * Available when class code 41620 is selected.
	 */
	CG2234("Exclusion - Construction Management Errors And Omissions CG 22 34"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 12375, 12374, or 45900.
	 */
	CG2236("Exclusion - Products And Professional Services (Druggists) CG 22 36"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 13759 or 15839.
	 */
	CG2237("Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 61223. <br>
	 * If class code 61227 or 61226 is selected and the question "Does applicant/insured act in a fiduciary capacity?" is answered yes automatically attach this endorsement."
	 */
	CG2238("Exclusion - Fiduciary Or Representative Liability Of Financial Institutions CG 22 38"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 10332, 10331, or 41422.
	 */
	CG2239("Exclusion - Camps Or Campgrounds CG 22 39"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2240("Exclusion - Medical Payments To Children Day Care Centers CG 22 40"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 92663 or 99471. <br>
	 * However if CG 22 79 is on the policy then the endorsement (CG 22 43) is not available.
	 */
	CG2243("Exclusion - Engineers, Architects Or Surveyors Professional Liability CG 22 43"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when one of the following class codes are selected: 40032, 40031, 43551, 66561.
	 */
	CG2244("Exclusion - Services Furnished By Health Care Providers CG 22 44"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when one of the following class codes are selected: 10113, 10115, 11128, 11127, 11234, 12356, 45190, 45192, 14655, 15600, 18912, 18911, 45191, or 45193.
	 */
	CG2245("Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2246("Exclusion - Rolling Stock - Railroad Construction CG 22 46"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when one of the following class codes are selected: 61223 or 45334. Other than that this remains electable.
	 */
	CG2248("Exclusion - Insurance And Related Operations CG 22 48"),
	/**
	 * Exclusion <br>
	 * Defined By Script <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 13410, 92445, 97501, 97502, and/or 99943.
	 */
	CG2250("Exclusion - Failure To Supply CG 22 50"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 14731, 19007, or 45678.
	 */
	CG2253("Exclusion - Laundry And Dry Cleaning Damage CG 22 53"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2257("Exclusion - Underground Resources And Equipment CG 22 57"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2258("Exclusion - Described Hazards (Carnivals, Circuses And Fairs) CG 22 58"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This is not available when class code 47052 is on the policy <br>
	 * This becomes required when class code 47050 is selected.
	 */
	CG2260("Limitation Of Coverage - Real Estate Operations CG 22 60"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when one of the following class codes are selected: 10036, 12683, 13410 or 57001.
	 */
	CG2266("Misdelivery Of Liquid Products Coverage CG 22 66"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 10072, 10073, 10367, 13453, 13455, or 18616.
	 */
	CG2268("Operation Of Customers Autos On Particular Premises CG 22 68"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when class code 47052 is selected.
	 */
	CG2270("Real Estate Property Managed CG 22 70"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out  <br>
	 * Required when one of the following class codes are selected: 67513, 67512,  47474, or 47477.
	 */
	CG2271("Colleges Or Schools (Limited Form) CG 22 71"),
	/**
	 * Exclusion <br>
	 * Defined By Script <br>
	 * Nothing to fill out <br>
	 */
	CG2273("Exclusion - Oil Or Gas Producing Operations CG 22 73"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when class code 43151 is selected.
	 */
	CG2277("Professional Liability Exclusion - Computer Data Processing CG 22 77"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.
	 */
	CG2279("Exclusion - Contractors - Professional Liability CG 22 79"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when one of the following class codes are selected: 94225 or 16890.
	 */
	CG2281("Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81"),
	/**
	 * Exclusion <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 */
	CG2287("Exclusion - Adult Day Care Centers CG 22 87"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when one of the following class codes are selected: 41675.
	 */
	CG2288("Professional Liability Exclusion - Electronic Data Processing Services And Computer Consulting Or Programming Services CG 22 88"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when class code 18200 is selected.
	 */
	CG2290("Professional Liability Exclusion - Spas Or Personal Enhancement Facilities CG 22 90"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when class code 18575 and/or 99600 is selected.
	 */
	CG2291("Exclusion - Telecommunication Equipment Or Service Providers Errors And Omissions CG 22 91"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when class code 99310 is selected.
	 */
	CG2292("Snow Plow Operations Coverage CG 22 92"),
	/**
	 * Exclusion <br>
	 * Defined By Script <br>
	 * Nothing to fill out <br>
	 */
	CG2294("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Not available when CG 21 38 is selected. <br>
	 * This becomes required when one of the following class codes are selected: 66123 or 66122.
	 */
	CG2296("Limited Exclusion - Personal And Advertising Injury - Lawyers CG 22 96"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 45334 and/or 47610.
	 */
	CG2298("Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when class code 96930 is selected.
	 */
	CG2299("Professional Liability Exclusion - Web Site Designers CG 22 99"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when class code 47050 is selected.
	 */
	CG2301("Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * If Waiver of Subrogation is marked Yes under the Additional Insured screen then add this endorsement as required. <br>
	 * This endorsement cannot be unselected.  <br>
	 * It is removed if Waiver of Subrogation is changed to a No.
	 */
	CG2404("Waiver Of Transfer Of Rights Of Recovery Against Others To Us CG 24 04"),
	/**
	 * This becomes required when one of the following class codes are selected: 16905 or 16906. <br>
	 * This also becomes required when the question ""Does applicant/insured allow patrons to bring their own alcoholic beverages?"" is answered Yes."
	 */
	CG2406("Liquor Liability - Bring Your Own Alcohol Establishments CG 24 06"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Fill Out Description of Premises and Operations <br>
	 * Required when one of the following class codes are selected: 14401, 16820, 16819, 16900, 16901, 16902, 16905, 16906, 16910, 16911, 16915, 16916, 16930, 16931, and/or 16941.
	 */
	CG2407("Products/Completed Operations Hazard Redefined CG 24 07"),
	/**
	 * Condition <br>
	 * Electable <br>
	 * Nothing to fill out <br>
	 * UW Questions @ QQ: Is applicant/insured an additional insured on the manufacturers� policy? Correct Answer - YES
	 */
	CG2410("Excess Provision - Vendors CG 24 10"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Required when class codes 10117, 40115, 40140, 40117, or 43760 is selected. <br>
	 * When endorsement CG 24 12 is selected the user is required to add a Description and Location. Display the message if the items are not selected.
	 */
	CG2412("Boats CG 24 12"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when one of the following class codes are selected: 41668, 41667, 45190, 45192, 64074, 10110, 40111, 41422, 45191, 45193, or 64075.
	 */
	CG2416("Canoes Or Rowboats CG 24 16"),
	/**
	 * <p>
	 * This is only available for underwriters to select. <br>
	 * The agent can see the endorsement only when the underwriter selects it and then they can also see what the underwriter has written but they are not able to edit it. <br>
	 * The agent can unselect the endorsement but cannot reselect it, the endorsement will no longer be visible.
	 */
	CG2417("Contractual Liability - Railroads CG 24 17"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out
	 */
	CG2426("Amendment Of Insured Contract Definition CG 24 26"),
	/**
	 * UW Questions @ QQ: Is insured licensed for unmanned aircraft by the FAA? Correct Answer - YES <br>
	 * UW Questions @ QQ: Are all operators over the age of 21? Correct Answer - YES <br>
	 * UW Questions @ QQ: Type of unmanned aircraft. "Select a dropdown choice" <br>
	 * UW Questions @ QQ: Weight of unmanned aircraft. "Intiger Field" <br>
	 * UW Questions @ QQ: Type of equipment attached to the unmanned aircraft. Correct Answer - PHOTOGRAPHIC <br>
	 * UW Questions @ QQ: Will the unmanned aircraft be operated within 5 miles of an airport or private runway? Correct Answer - NO <br>
	 * UW Questions @ QQ: Will the unmanned aircraft be operated within 5 miles of a wildfire? Correct Answer - NO <br>
	 * UW Questions @ QQ: Will the unmanned aircraft be operated over people or crowds? Correct Answer - NO <br>
	 * UW Questions @ QQ: Will the unmanned aircraft be operated out of the sight of the operator? Correct Answer - NO <br>
	 * UW Questions @ QQ: What is the maximum altitude of the unmanned aircraft? "Intiger Field" <br>
	 * UW Questions @ QQ: What is the maximum speed that the unmanned aircraft can operate at? "Intiger Field" <br>
	 * UW Questions @ QQ: Does the unmanned aircraft have a return to home feature? Correct Answer - YES <br>
	 * UW Questions @ FA: Please provide the qualifications of the drone operator. "String Text Box" <br>
	 * UW Questions @ FA: Have any of the unmanned operators had an accident? Correct Answer - NO <br>
	 * Not available when CG 21 09 is selected.
	 */
	CG2450("Limited Coverage For Designated Unmanned Aircraft CG 24 50"),
	/**
	 * Available when Designated Construction Project(s) General Aggregate Limit CG 25 03 located under the Additional Insured screen is selected Yes.
	 */
	CG2503("Designated Construction Project(s) General Aggregate Limit CG 25 03"),
	/**
	 * Available when Designated Location(s) General Aggregate Limit CG 25 04 located under the Additional Insured screen is selected Yes.
	 */
	CG2504("Designated Location(s) General Aggregate Limit CG 25 04"),
	/**
	 * Condition <br>
	 * Electable <br>
	 * TODO Items to fill out
	 */
	CG2806("Limitation Of Coverage To Insured Premises CG 28 06"),
	/**
	 * Available when Employment Practices Liability Insurance IDCG 31 2013 is selected and only in job type Policy Change.  <br>
	 * Available for Underwriters and Agents to see but the Underwriter will only be allowed to select the endorsement at a backdated policy change job if the policy is canceled."
	 */
	IDCG030002("Liquor Liability Declarations IDCG 03 0002"),
	/**
	 * Available for Underwriters to select all the time. The agent can see the endorsement when the underwriter selects it and what the underwriter has entered in however they cannot edit it or select it. However if class code 97047 or 97050 is selected and the question "Is applicant/insured a licensed applicator?" is answered yes then the agent can select the endorsement. Also the agent can remove the endorsement whenever they want.
	 */
	IDCG040001("Idaho Professional Applicator Certificate Of Insurance IDCG 04 0001"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 
	 * 91111, 91150, 91405, 91481, 91523, 94225, 96816, 97047, 97050, 98993, 99310, 99471, or 99975.
	 */
	IDCG312001("Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 
	 * 94225, 96816, 97047, 97050, 99471, or 99975.
	 */
	IDCG312002("Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out
	 */
	IDCG312003("Mobile Equipment Modification Endorsement IDCG 31 2003"),
	/**
	 *  Condition <br>
	 *  Required <br>
	 *  Nothing to fill out
	 */
	IDCG312004("Affiliate And Subsidiary Definition Endorsement IDCG 31 2004"),
	/**
	 * Condition <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 */
	IDCG312005("Pollutants Definition Endorsement IDCG 31 2005"),
	/**
	 * This becomes available when class code 97047 and/or 97050 is selected.
	 */
	IDCG312006("Lawn Care Services Coverage IDCG 31 2006"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * TODO add this to GenericWorkorderGeneralLiabilityCoveragesCPP - fillOutExclutionsAndConditionsTab 
	 * Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy:  94225, 97047, 97050, and 96816.
	 */
	IDCG312007("Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * TODO add this to GenericWorkorderGeneralLiabilityCoveragesCPP - fillOutExclutionsAndConditionsTab 
	 * Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.
	 */
	IDCG312008("Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when class code 94225 is selected.
	 */
	IDCG312009("Farm Machinery Operations By Contractors Exclusion Endorsement IDCG 31 2009"),
	/**
	 * Exclusion <br>
	 * Required <br>
	 * Nothing to fill out <br>
	 * This becomes required when class code 12683 is selected.
	 */
	IDCG312010("Fertilizer Distributors And Dealers Exclusion Endorsement IDCG 31 2010"),
	/**
	 * Exclusion & Condition  <br>
	 * Electable <br>
	 * There are two of these. One under Exclusions and one under Conditions <br>
	 * This is only available for underwriters to select. The agent can see the endorsement and what the underwriter has entered in however they cannot edit it or select it. <br>
	 * Add then fill out Description
	 */
	IDCG312012("Commercial General Liability Manuscript Endorsement IDCG 31 2012"),
	/**
	 * UW Questions @ QQ: Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont? Correct Answer - NO <br>
	 * UW Questions @ QQ: Indicate if the applicant/insured is a: Correct Answer - NONE OF THE ABOVE
	 */
	IDCG312013("Employment Practice Liability Insurance IDCG 31 2013");

	String value;
	
	private GeneralLiabilityCoverages(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static GeneralLiabilityCoverages getRandomCoverage() {
        return values()[(int) (Math.random() * values().length)];
    }
	
}
