package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.GeneralLiabilityCoverages;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PackageRiskType;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.pc.sidemenu.SideMenuPC;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderGeneralLiabilityCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderGeneralLiabilityCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setExposure(PolicyLocation location, String classCode) throws Exception {

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();
        CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(location, classCode);
        repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.clickExposureDetialsTab();
        
        exposuresPage.selectAll();
        
        exposuresPage.clickRemove();
        
        exposuresPage.clickAdd();
        
        exposuresPage.addExposure(myExposure);
        exposuresPage.clickLocationSpecificQuestionsTab();
        //FILL OUT UW QUESTIONS
        
        //		exposuresPage.fillOutBasicUWQuestionsQQ(myExposure);
        //fillOutBasicUWQuestionsFullApp(myExposure);

        //set coverages

        //quote the policy and check UW issues
        repository.pc.workorders.generic.GenericWorkorder genwo = new repository.pc.workorders.generic.GenericWorkorder(driver);
        genwo.clickGenericWorkorderQuote();
        
        sideMenu.clickSideMenuRiskAnalysis();
    }


    @SuppressWarnings("serial")

    public void setCoverage(PolicyLocation location, GeneralLiabilityCoverages coverage) throws Exception {
        List<String> tempList = new ArrayList<String>();

        switch (coverage) {
            case CG0001:
                //REQUIRED
                break;
            case CG2020:
                //This becomes required when the question Is the applicant/insured a not for profit organization whose major purposes is charitable causes?is answered Yes.
                break;
            case CG2022:
                //This becomes required when class code 41650 is selected
                setExposure(location, "41650");

                break;
            case CG2002:
                //This becomes required when one of the following class codes are selected: 41668, 41667, 41670, 41669, or 11138.
                tempList = new ArrayList<String>() {{
                    this.add("41668");
                    this.add("41667");
                    this.add("41670");
                    this.add("41669");
                    this.add("11138");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2003:
                //Available when Additional Insured - Concessionaires Trading Under Your Name CG 20 03 is selected on the Screen New Additional Insured.

                break;
            case CG2004:
                //			This becomes required when one of the following class codes are selected: 62000, 62001, 62002, or 62003.
                tempList = new ArrayList<String>() {{
                    this.add("62000");
                    this.add("62001");
                    this.add("62002");
                    this.add("62003");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));

                break;
            case CG2005:
                //			Available when Additional Insured - Controlling Interest CG 20 05 is selected on the Screen New Additional Insured.

                break;
            case CG2027:
                //			Available when Additional Insured - Co-Owner Of Insured Premises CG 20 27 is selected on the Screen New Additional Insured.

                break;
            case CG2026:
                //			Available when Additional Insured - Designated Person Or Organization CG 20 26 is selected on the Screen New Additional Insured.

                break;
            case CG2007:
                break;
            case CG2032:
                //			Available when Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32 is selected on the Screen New Additional Insured.

                break;
            case CG2023:
                //			Available when Organization Type is Trust. Located on the Policy Info.

                break;
            case CG2029:
                //			Available when Additional Insured - Grantor Of Franchise CG 20 29 is selected on the Screen New Additional Insured.

                break;
            case CG2036:
                //			Available when Additional Insured - Grantor Of Licenses CG 20 36 is selected on the Screen New Additional Insured.

                break;
            case CG2028:
                //			Available when Additional Insured - Lessor Of Leased Equipment CG 20 28 is selected on the Screen New Additional Insured.

                break;
            case CG2011:
                //			Available when Additional Insured - Managers Or Lessors Of Premises CG 20 11 is selected on the Screen New Additional Insured.

                break;
            case CG2018:
                //			Available when Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18 is selected on the Screen New Additional Insured.

                break;
            case CG2024:
                //			Available when Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24 is selected on the Screen New Additional Insured.

                break;
            case CG2037:
                //			Available when Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37 is selected on the Screen New Additional Insured.

                break;
            case CG2010:
                //			Available when Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10 is selected on the Screen New Additional Insured.

                break;
            case CG2012:
                //			Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12 is selected on the Screen New Additional Insured.

                break;
            case CG2013:
                //			Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13 is selected on the Screen New Additional Insured.

                break;
            case CG2017:
                //			This becomes required when class code 68500 is selected.
                setExposure(location, "68500");
                break;
            case CG2008:
                //			This becomes required when one of the following class codes are selected: 11138, 44070, 44072, 45190, 45191, 45192, or 45193.
                tempList = new ArrayList<String>() {{
                    this.add("11138");
                    this.add("44070");
                    this.add("44072");
                    this.add("45190");
                    this.add("45191");
                    this.add("45192");
                    this.add("45193");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2014:
                break;
            case CG2015:
                //			Available when Additional Insured - Vendors CG 20 15 is selected on the Screen New Additional Insured.

                break;
            case CG2151:
                break;
            case CG2417:
                //			This is only available for underwriters to select. The agent can see the endorsement only when the underwriter selects it and then they can also see what the underwriter has written but they are not able to edit it. The agent can unselect the endorsement but cannot reselect it, the endorsement will no longer be visible.

                break;
            case CG2503:
                //			Available when Designated Construction Project(s) General Aggregate Limit CG 25 03 located under the Additional Insured screen is selected Yes.

                break;
            case CG2504:
                //			Available when Designated Location(s) General Aggregate Limit CG 25 04 located under the Additional Insured screen is selected Yes.

                break;
            case IDCG312013:
                break;
            case IDCG040001:
                //			Available for Underwriters to select all the time. The agent can see the endorsement when the underwriter selects it and what the underwriter has entered in however they cannot edit it or select it. However if class code 97047 or 97050 is selected and the question "Is applicant/insured a licensed applicator?" is answered yes then the agent can select the endorsement. Also the agent can remove the endorsement whenever they want.

                break;
            case IDCG312006:
                //			This becomes available when class code 97047 and/or 97050 is selected.
                tempList = new ArrayList<String>() {{
                    this.add("97047");
                    this.add("97050");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2450:
                //			Not available when CG 21 09 is selected.

                break;
            case CG2406:
                //			"This becomes required when one of the following class codes are selected: 16905 or 16906.
                //			This also becomes required when the question ""Does applicant/insured allow patrons to bring their own alcoholic beverages?"" is answered Yes."
                tempList = new ArrayList<String>() {{
                    this.add("16905");
                    this.add("16906");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG0033:
                //			Available when one of the following class codes are selected: 70412, 58161, 50911, 59211, 58165, or 58166.
                tempList = new ArrayList<String>() {{
                    this.add("70412");
                    this.add("58161");
                    this.add("50911");
                    this.add("59211");
                    this.add("58165");
                    this.add("58166");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2001:
                break;
            case IDCG030002:
                //			"Available when Employment Practices Liability Insurance IDCG 31 2013 is selected and only in job type Policy Change.
                //			Available for Underwriters and Agents to see but the Underwriter will only be allowed to select the endorsement at a backdated policy change job if the policy is canceled."

                break;
            case IDCG312004:
                break;
            case CG2228:
                //			Available when class code 49333 is selected.
                setExposure(location, "49333");
                break;
            case CG2426:
                break;
            case CG2412:
                //			Required when class codes 10117, 40115, 40140, 40117, or 43760 is selected.
                tempList = new ArrayList<String>() {{
                    this.add("10117");
                    this.add("40115");
                    this.add("40140");
                    this.add("40117");
                    this.add("43760");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2416:
                //			Available when one of the following class codes are selected: 41668, 41667, 45190, 45192, 64074, 10110, 40111, 41422, 45191, 45193, or 64075.
                tempList = new ArrayList<String>() {{
                    this.add("41668");
                    this.add("41667");
                    this.add("45190");
                    this.add("45192");
                    this.add("64074");
                    this.add("10110");
                    this.add("40111");
                    this.add("41422");
                    this.add("45191");
                    this.add("45193");
                    this.add("64075");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2271:
                //			Available when one of the following class codes are selected: 67513, 67512,  47474, or 47477.
                tempList = new ArrayList<String>() {{
                    this.add("67513");
                    this.add("67512");
                    this.add("47474");
                    this.add("47477");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2410:
                break;
            case CG2144:
                break;
            case CG2806:
                break;
            case IDCG312003:
                break;
            case CG2268:
                //			Available when one of the following class codes are selected: 10072, 10073, 10367, 13453, 13455, or 18616.
                tempList = new ArrayList<String>() {{
                    this.add("10072");
                    this.add("10073");
                    this.add("10367");
                    this.add("13453");
                    this.add("13455");
                    this.add("18616");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case IDCG312005:
                break;
            case CG2407:
                //			Available when one of the following class codes are selected: 14401, 16820, 16819, 16900, 16901, 16902, 16905, 16906, 16910, 16911, 16915, 16916, 16930, 16931, and/or 16941.
                tempList = new ArrayList<String>() {{
                    this.add("14401");
                    this.add("16820");
                    this.add("16819");
                    this.add("16900");
                    this.add("16901");
                    this.add("16902");
                    this.add("16905");
                    this.add("16906");
                    this.add("16910");
                    this.add("16911");
                    this.add("16915");
                    this.add("16916");
                    this.add("16930");
                    this.add("16931");
                    this.add("16941");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2292:
                //			Available when class code 99310 is selected.
                setExposure(location, "99310");
                break;
            case CG2404:
                //			If Waiver of Subrogation is marked Yes under the Additional Insured screen then add this endorsement as required. This endorsement cannot be unselected. It is removed if Waiver of Subrogation is changed to a No.

                break;
            case CG2146:
                break;
            case CG2150:
                //			Available when one of the following class codes are selected: 16905 or 16906.
                tempList = new ArrayList<String>() {{
                    this.add("16905");
                    this.add("16906");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case IDCG312012:
                //			This is only available for underwriters to select. The agent can see the endorsement and what the underwriter has entered in however they cannot edit it or select it.

                break;
            case CG2132:
                break;
            case CG2147:
                //			Available when Employement Practices Liability Insurance IDCG 31 2013 is not selected.

                break;
            case IDCG312008:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.

                break;
            case CG2107:
                break;
            case CG2287:
                break;
            case CG2100:
                break;
            case CG2101:
                //			Available when one of the following class codes are selected: 63218, 63217, 63220, 63219, 43421, or 43424.
                tempList = new ArrayList<String>() {{
                    this.add("63218");
                    this.add("63217");
                    this.add("63220");
                    this.add("63219");
                    this.add("43421");
                    this.add("43424");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2239:
                //			Available when one of the following class codes are selected: 10332, 10331, or 41422.
                tempList = new ArrayList<String>() {{
                    this.add("10332");
                    this.add("10331");
                    this.add("41422");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2234:
                //			Available when class code 41620 is selected.
                setExposure(location, "41620");
                break;
            case CG2279:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.

                break;
            case CG2230:
                //			Available when one of the following class codes are selected: 67513, 67512, 47474, or 47477.
                tempList = new ArrayList<String>() {{
                    this.add("67513");
                    this.add("67512");
                    this.add("47474");
                    this.add("47477");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2157:
                //			Available when one of the following class codes are selected: 48600 or 41650.
                tempList = new ArrayList<String>() {{
                    this.add("48600");
                    this.add("41650");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2135:
                break;
            case CG2294:
                break;
            case CG2258:
                break;
            case CG2153:
                break;
            case IDCG312002:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 94225, 96816, 97047, 97050, 99471, or 99975.
                tempList = new ArrayList<String>() {{
                    this.add("94225");
                    this.add("96816");
                    this.add("97047");
                    this.add("97050");
                    this.add("99471");
                    this.add("99975");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2133:
                break;
            case CG2116:
                //			This becomes required when one of the following class codes are selected: 41650, 41677, 91805, 47052, 58408, 58409, 58456, 58457, 58458, or 58459. Other than that this remains electable.
                tempList = new ArrayList<String>() {{
                    this.add("41650");
                    this.add("41677");
                    this.add("91805");
                    this.add("47052");
                    this.add("58408");
                    this.add("58409");
                    this.add("58456");
                    this.add("58457");
                    this.add("58458");
                    this.add("58459");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2134:
                break;
            case CG2159:
                //			Available when class code 46112 is selected.
                setExposure(location, "46112");
                break;
            case CG2243:
                //			Available when one of the following class codes are selected: 92663 or 99471. However if CG 22 79 is on the policy then the endorsement (CG 22 43) is not availabile.
                tempList = new ArrayList<String>() {{
                    this.add("92663");
                    this.add("99471");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2281:
                //			Available when one of the following class codes are selected: 94225 or 16890.
                tempList = new ArrayList<String>() {{
                    this.add("94225");
                    this.add("16890");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case IDCG312001:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 91111, 91150, 91405, 91481, 91523, 94225, 96816, 97047, 97050, 98993, 99310, 99471, or 99975.

                break;
            case CG2186:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 96816, 97047, or 97050.

                break;
            case CG2250:
                //			Available when one of the following class codes are selected: 13410, 92445, 97501, 97502, and/or 99943.
                tempList = new ArrayList<String>() {{
                    this.add("13410");
                    this.add("92445");
                    this.add("97501");
                    this.add("97502");
                    this.add("99943");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2238:
                //			"Available when one of the following class codes are selected: 61223.
                //			If class code 61227 or 61226 is selected and the question ""Does applicant/insured act in a fiduciary capacity?"" is answered yes automatically attach this endorsement."
                tempList = new ArrayList<String>() {{
                    this.add("61227");
                    this.add("61226");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2152:
                //			"Available when class code 61223 is selected.
                //			If class code 61227 or 61226 is selected and the question ""Is applicant/insured involved in any of the following or related activities: Accounting, banking, credit card company, credit reporting, credit union, financial investment services, securities broker or dealer or tax preparation?"" is answered yes automatically attach this endorsement."
                tempList = new ArrayList<String>() {{
                    this.add("61227");
                    this.add("61226");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));

                break;
            case CG2156:
                //			Available when one of the following class codes is selected: 41604, 41603, 41697, 41696, 43889, 46005, and/or 46004.
                tempList = new ArrayList<String>() {{
                    this.add("41604");
                    this.add("41603");
                    this.add("41603");
                    this.add("41697");
                    this.add("41696");
                    this.add("43889");
                    this.add("46005");
                    this.add("46004");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2224:
                //			Available when one of the following class codes are selected: 61223, or 96317.
                tempList = new ArrayList<String>() {{
                    this.add("61223");
                    this.add("96317");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2248:
                //			This becomes required when one of the following class codes are selected: 61223 or 45334. Other than that this remains electable.
                tempList = new ArrayList<String>() {{
                    this.add("61223");
                    this.add("45334");
                }};
                break;
            case CG2141:
                break;
            case CG2298:
                //			Available when one of the following class codes are selected: 45334 and/or 47610.
                tempList = new ArrayList<String>() {{
                    this.add("45334");
                    this.add("47610");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2253:
                //			Available when one of the following class codes are selected: 14731, 19007, or 45678.
                tempList = new ArrayList<String>() {{
                    this.add("14731");
                    this.add("19007");
                    this.add("45678");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2240:
                break;
            case CG2117:
                //			Available when question "Does applicant/insured move buildings or structures?" is answered yes.

                break;
            case CG2136:
                break;
            case CG2273:
                break;
            case CG2138:
                //			Available CG 22 96 is selected unless the this become required because of the defined by script. Then CG 22 96 would be come unavailable.
                //			This becomes required when one of the following class codes are selected: 91130, 91636, 43200, 65007, 66123, 66122, 46822, 46882, 46881, 47052, or 98751. Other than that this remains electable.
                tempList = new ArrayList<String>() {{
                    this.add("91130");
                    this.add("91636");
                    this.add("43200");
                    this.add("65007");
                    this.add("66123");
                    this.add("66122");
                    this.add("46822");
                    this.add("46882");
                    this.add("46881");
                    this.add("47052");
                    this.add("98751");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2236:
                //			Available when one of the following class codes are selected: 12375, 12374, or 45900.
                tempList = new ArrayList<String>() {{
                    this.add("12375");
                    this.add("12374");
                    this.add("45900");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2237:
                //			Available when one of the following class codes are selected: 13759 or 15839.
                tempList = new ArrayList<String>() {{
                    this.add("13759");
                    this.add("15839");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2104:
                //			This becomes a default value with no other choices when one of the following class codes is selected: 12375, 12374, 45900, or 15839.
                //			These have to be the only class codes on the policy for this rule to work. If another class code is on the policy that is not in this list
                //			Otherwise Available when the class premium base does not include (+) and
                //			Exclude is chosen by an UW on CG 00 01 - Products / Completed Operations Aggregate Limit (no premium charge is made) attach this form as required.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("12375");
                        this.add("12374");
                        this.add("45900");
                        this.add("15839");
                    }
                };
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2158:
                //			Available when one of the following class codes are selected: 91200 or 99851.
                tempList = new ArrayList<String>() {{
                    this.add("91200");
                    this.add("99851");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2229:
                //			Available when class code 98751, 49763, and/or 18991 is on the policy.
                tempList = new ArrayList<String>() {{
                    this.add("98751");
                    this.add("49763");
                    this.add("18991");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2301:
                //			This becomes required when class code 47050 is selected.
                setExposure(location, "47050");
                break;
            case CG2246:
                break;
            case CG2244:
                //			This becomes required when one of the following class codes are selected: 40032, 40031, 43551, 66561.
                tempList = new ArrayList<String>() {{
                    this.add("40032");
                    this.add("40031");
                    this.add("43551");
                    this.add("66561");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2245:
                //			This becomes required when one of the following class codes are selected: 10113, 10115, 11128, 11127, 11234, 12356, 45190, 45192, 14655, 15600, 18912, 18911, 45191, or 45193.
                tempList = new ArrayList<String>() {{
                    this.add("10113");
                    this.add("10115");
                    this.add("11128");
                    this.add("11127");
                    this.add("11234");
                    this.add("12356");
                    this.add("45190");
                    this.add("45192");
                    this.add("14655");
                    this.add("15600");
                    this.add("18912");
                    this.add("18911");
                    this.add("45191");
                    this.add("45193");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2291:
                //			Available when class code 18575 and/or 99600 is selected.
                tempList = new ArrayList<String>() {{
                    this.add("18575");
                    this.add("99600");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2233:
                //			This becomes required when one of the following class codes are selected: 91135, 97003, or 97002.
                tempList = new ArrayList<String>() {{
                    this.add("91135");
                    this.add("97003");
                    this.add("97002");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2257:
                break;
            case CG2109:
                //			Not available when CG 24 50 is selected.

                break;
            case CG2166:
                break;
            case CG2160:
                break;
            case CG2184:
                break;
            case IDCG312007:
                //			Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy:  94225, 97047, 97050, and 96816.

                break;
            case IDCG312009:
                //			This becomes required when class code 94225 is selected.
                setExposure(location, "94225");

                break;
            case IDCG312010:
                //			This becomes required when class code 12683 is selected.
                setExposure(location, "12683");
                break;
            case CG2167:
                break;
            case CG2260:
                //			"This is not available when class code 47052 is on the policy
                //			This becomes required when class code 47050 is selected."
                tempList = new ArrayList<String>() {{
                    this.add("47052");
                    this.add("47050");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2296:
                //			"Not available when CG 21 38 is selected.
                //			This becomes required when one of the following class codes are selected: 66123 or 66122."
                tempList = new ArrayList<String>() {{
                    this.add("66123");
                    this.add("66122");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2266:
                //			This becomes required when one of the following class codes are selected: 10036, 12683, 13410 or 57001.
                tempList = new ArrayList<String>() {{
                    this.add("10036");
                    this.add("12683");
                    this.add("13410");
                    this.add("57001");
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case CG2277:
                //			This becomes required when class code 43151 is selected.
                setExposure(location, "43151");
                break;
            case CG2288:
                //			This becomes required when one of the following class codes are selected: 41675.
                setExposure(location, "41675");
                break;
            case CG2290:
                //			This becomes required when class code 18200 is selected.
                setExposure(location, "18200");
                break;
            case CG2299:
                //			This becomes required when class code 96930 is selected.
                setExposure(location, "96930");
                break;
            case CG2270:
                //			This becomes required when class code 47052 is selected.
                setExposure(location, "47052");
                break;
            case CG2196:
                break;
            case CG2149:
                //			Not available if CG 21 55 is selected.
                break;
            case CG2155:
                //			Not available if CG 21 49 is selected.
                break;
        }
    }

    Guidewire8Checkbox checkbox_Blank(GeneralLiabilityCoverages coverage) {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), '" + coverage.getValue() + "' )]/preceding-sibling::table");
    }


    public void setCoverages(PolicyLocation location, String classCode, GeneralLiabilityCoverages coverage) throws Exception {

        repository.pc.workorders.generic.GenericWorkorderLineSelection myLineSelect = new repository.pc.workorders.generic.GenericWorkorderLineSelection(driver);
        repository.pc.sidemenu.SideMenuPC mySideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        if (coverage != GeneralLiabilityCoverages.CG2020) {
            if (coverage != GeneralLiabilityCoverages.CG2023) {
                new GuidewireHelpers(getDriver()).editPolicyTransaction();
            }
        }

        //EXPOSURES
        if (classCode != null) {

            //LINE SELECTION PAGE
            if (classCode.substring(0, 1).equals("9")) {
                mySideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
                mySideMenu.clickSideMenuLineSelection();
                myLineSelect = new GenericWorkorderLineSelection(driver);
                myLineSelect.selectPackageRiskType(PackageRiskType.Contractor);
            }

            repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
            sideMenu.clickSideMenuGLExposures();
            CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(location, classCode);
            repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP(driver);
            exposuresPage.clickExposureDetialsTab();
            

            exposuresPage.selectAll();
            
            exposuresPage.clickRemove();
            


            exposuresPage.clickAdd();
            
            exposuresPage.addExposure(myExposure);


            exposuresPage.clickLocationSpecificQuestionsTab();
            
//			exposuresPage.fillOutL

            exposuresPage.clickUnderwritingQuestionsTab();

            //FILL OUT UW QUESTIONS
            
            //exposuresPage.fillOutBasicUWQuestionsQQ(myExposure);

            //CONTRACTOR UW QUESTIONS
            if (classCode.substring(0, 1).equals("9")) {
                exposuresPage.fillOutUnderwritingContractorQuestions(myExposure);
                switch (coverage) {
                    case CG2117:
                        exposuresPage.setDoesApplicantMoveBuildings(true);
                        break;
                    default:
                        break;
                }
            }
        }

        //COVERAGES
        
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();
        

        /**
         * @Author bmartin
         * @Requirement Defect # DE3242 in R2 PolicyCenter CPP
         * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx">WCIC General Liability-Product-Model</a>
         * @Description Check to make sure there is not any Validation Results messages for coverages that are added or removed for General Liability
         * @DATE Jun 9, 2016
         * @throws Exception
         */
        if (checkIfElementExists("//div[contains(text(), 'is required and has been added.' )]", 10)) {
        	Assert.fail("##########/n ERROR: Coverages Were Added And Validation Results Messages Are Present When They Shouldn't Be /n ##########");
        }
        if (checkIfElementExists("//div[contains(text(), 'is no longer available and has been removed.' )]", 10)) {
        	Assert.fail("##########/n ERROR: Coverages Were Removed And Validation Results Messages Are Present When They Shouldn't Be /n ##########");
        }

        //Look though the tabs for the Coverage to select. Tab order used: Additional Coverages - Exclusions & Conditions - Standard Coverages
        repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP myCoverages = new repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        sideMenu.clickSideMenuGLCoverages();
        myCoverages.clickAdditionalCoverages();
        if (!checkIfElementExists("//div[contains(text(), '" + coverage.getValue() + "' )]", 100)) {
            
            myCoverages.clickExclusionsAndConditions();
            if (!checkIfElementExists("//div[contains(text(), '" + coverage.getValue() + "' )]", 100)) {
                
                myCoverages.clickStandardCoverages();
                if (!checkIfElementExists("//div[contains(text(), '" + coverage.getValue() + "' )]", 100)) {
                    
                    Assert.fail("##########/n ERROR: Coverage " + coverage.getValue() + " Was NOT Available On Any Tab  /n ##########");
                }
            }
        }

        
        //		if (!isRequired(coverage.getValue()) && (isElectable(coverage.getValue()) || isSuggested(coverage.getValue()))) {
        //			
        //			checkbox_Blank(coverage).select(true);
        //TODO break this apart based on what tab is being used, and place it in the correct location that can use it above.
        myCoverages.setAdditionalCoverageQuestions(coverage, location);
        //		}

        //COVERAGES UNDERWRITING QUESTIONS
        myCoverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        myCoverages.clickUnderwritingQuestionsTab();
        repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions uwQuestions = new GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions(driver);
        uwQuestions.setGLCoveragesUWQuestion(coverage);

        //QUOTE THE POLICY
        repository.pc.workorders.generic.GenericWorkorder myGenwork = new repository.pc.workorders.generic.GenericWorkorder(driver);
        myGenwork.clickGenericWorkorderQuote();
        

        //CHECK UNDERWRITING ISSUES
        mySideMenu.clickSideMenuRiskAnalysis();
    }


    public void setCoveragesCheckbox(PolicyLocation location, GeneralLiabilityCoverages coverage) throws Exception {
        List<String> tempList = new ArrayList<String>();
        repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability myQualifications = new repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability(driver);
        repository.pc.sidemenu.SideMenuPC mySideMenu = new repository.pc.sidemenu.SideMenuPC(driver);

        repository.pc.workorders.generic.GenericWorkorderPolicyInfo myPolInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);


        switch (coverage) {
            case CG0001:
                //REQUIRED
                setCoverages(location, null, coverage);
                break;
            case CG2020:
                //This becomes required when the question Is the applicant/insured a not for profit organization whose major purposes is charitable causes?is answered Yes.
                new GuidewireHelpers(getDriver()).editPolicyTransaction();
                mySideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
                mySideMenu.clickSideMenuQualification();
                myQualifications = new GenericWorkorderQualification_GeneralLiability(driver);
                myQualifications.clickQualificationNotForProfitOrgCharitableCauses(true);
                setCoverages(location, null, coverage);
                break;
            case CG2022:
                //This becomes required when class code 41650 is selected
                setCoverages(location, "41650", coverage);
                break;
            case CG2002:
                //This becomes required when one of the following class codes are selected: 41668, 41667, 41670, 41669, or 11138.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("41668");
                        this.add("41667");
                        this.add("41670");
                        this.add("41669");
                        this.add("11138");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2003:
                //Available when Additional Insured - Concessionaires Trading Under Your Name CG 20 03 is selected on the Screen New Additional Insured.

                setCoverages(location, null, coverage);
                break;
            case CG2004:
                //			This becomes required when one of the following class codes are selected: 62000, 62001, 62002, or 62003.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("62000");
                        this.add("62001");
                        this.add("62002");
                        this.add("62003");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2005:
                //			Available when Additional Insured - Controlling Interest CG 20 05 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2027:
                //			Available when Additional Insured - Co-Owner Of Insured Premises CG 20 27 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2026:
                //			Available when Additional Insured - Designated Person Or Organization CG 20 26 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2007:
                //			UW Question @ QQ: Does the applicant/insured have a contract with the additional insured? Correct Answer - YES
                setCoverages(location, null, coverage);
                break;
            case CG2032:
                //			UW Question @ QQ: Is the applicant/insured required to provide insured status to the entity hired by someone else by contract? Correct Answer - YES
                //			Available when Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2023:
                //			Available when Organization Type is Trust. Located on the Policy Info. Has to be set up as a COMPANY.
            	new GuidewireHelpers(getDriver()).editPolicyTransaction();
                mySideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
                mySideMenu.clickSideMenuPolicyInfo();
                myPolInfo = new GenericWorkorderPolicyInfo(driver);
                myPolInfo.setPolicyInfoOrganizationType(OrganizationType.Trust);
                setCoverages(location, null, coverage);
                break;
            case CG2029:
                //			Available when Additional Insured - Grantor Of Franchise CG 20 29 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2036:
                //			Available when Additional Insured - Grantor Of Licenses CG 20 36 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2028:
                //			Available when Additional Insured - Lessor Of Leased Equipment CG 20 28 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2011:
                //			Available when Additional Insured - Managers Or Lessors Of Premises CG 20 11 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2018:
                //			Available when Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2024:
                //			Available when Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2037:
                //			Available when Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2010:
                //			Available when Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2012:
                //			Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2013:
                //			Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2017:
                //			This becomes required when class code 68500 is selected.
                setCoverages(location, "68500", coverage);
                break;
            case CG2008:
                //			This becomes required when one of the following class codes are selected: 11138, 44070, 44072, 45190, 45191, 45192, or 45193.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 3300502216568516719L;

                    {
                        this.add("11138");
                        this.add("44070");
                        this.add("44072");
                        this.add("45190");
                        this.add("45191");
                        this.add("45192");
                        this.add("45193");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2014:
                //			UW Question @ QQ: Please provide underwriting details of the equine exposure i.e. type of events, number of animals, number of events etc.  "String Text Box"
                setCoverages(location, null, coverage);
                break;
            case CG2015:
                //			UW Question @ QQ: Is applicant/insured the manufacturer of the product? Correct Answer - Yes
                //			Available when Additional Insured - Vendors CG 20 15 is selected on the Screen New Additional Insured.
                setCoverages(location, null, coverage);
                break;
            case CG2151:
                setCoverages(location, null, coverage);
                break;
            case CG2417:
                //			This is only available for underwriters to select. The agent can see the endorsement only when the underwriter selects it and then they can also see what the underwriter has written but they are not able to edit it. The agent can unselect the endorsement but cannot reselect it, the endorsement will no longer be visible.
                setCoverages(location, null, coverage);
                break;
            case CG2503:
                //			Available when Designated Construction Project(s) General Aggregate Limit CG 25 03 located under the Additional Insured screen is selected Yes.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("91130");
                        this.add("91636");
                        this.add("98751");
                        this.add("91629");
                        this.add("91636");
                        this.add("92101");
                        this.add("92445");
                        this.add("94444");
                        this.add("94590");
                        this.add("95357");
                        this.add("95625");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2504:
                //			Available when Designated Location(s) General Aggregate Limit CG 25 04 located under the Additional Insured screen is selected Yes.
                setCoverages(location, null, coverage);
                break;
            case IDCG312013:
                //			UW Questions @ QQ: Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont? Correct Answer - NO
                //			UW Questions @ QQ: Indicate if the applicant/insured is a: Correct Answer - NONE OF THE ABOVE
                setCoverages(location, null, coverage);
                break;
            case IDCG040001:
                //			Available for Underwriters to select all the time. The agent can see the endorsement when the underwriter selects it and what the underwriter has entered in however they cannot edit it or select it. However if class code 97047 or 97050 is selected and the question "Is applicant/insured a licensed applicator?" is answered yes then the agent can select the endorsement. Also the agent can remove the endorsement whenever they want.
                setCoverages(location, null, coverage);
                break;
            case IDCG312006:
                //			This becomes available when class code 97047 and/or 97050 is selected.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("97047");
                        this.add("97050");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2450:
                //			UW Questions @ QQ: Is insured licensed for unmanned aircraft by the FAA? Correct Answer - YES
                //			UW Questions @ QQ: Are all operators over the age of 21? Correct Answer - YES
                //			UW Questions @ QQ: Type of unmanned aircraft. "Select a dropdown choice"
                //			UW Questions @ QQ: Weight of unmanned aircraft. "Intiger Field"
                //			UW Questions @ QQ: Type of equipment attached to the unmanned aircraft. Correct Answer - PHOTOGRAPHIC
                //			UW Questions @ QQ: Will the unmanned aircraft be operated within 5 miles of an airport or private runway? Correct Answer - NO
                //			UW Questions @ QQ: Will the unmanned aircraft be operated within 5 miles of a wildfire? Correct Answer - NO
                //			UW Questions @ QQ: Will the unmanned aircraft be operated over people or crowds? Correct Answer - NO
                //			UW Questions @ QQ: Will the unmanned aircraft be operated out of the sight of the operator? Correct Answer - NO
                //			UW Questions @ QQ: What is the maximum altitude of the unmanned aircraft? "Intiger Field"
                //			UW Questions @ QQ: What is the maximum speed that the unmanned aircraft can operate at? "Intiger Field"
                //			UW Questions @ QQ: Does the unmanned aircraft have a return to home feature? Correct Answer - YES
                //			UW Questions @ FA: Please provide the qualifications of the drone operator. "String Text Box"
                //			UW Questions @ FA: Have any of the unmanned operators had an accident? Correct Answer - NO
                //			Not available when CG 21 09 is selected.
                setCoverages(location, null, coverage);
                break;
            case CG2406:
                //			This becomes required when one of the following class codes are selected: 16905 or 16906.
                //			This also becomes required when the question ""Does applicant/insured allow patrons to bring their own alcoholic beverages?"" is answered Yes."

                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("16905");
                        this.add("16906");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG0033:
                //			Available when one of the following class codes are selected: 70412, 58161, 50911, 59211, 58165, or 58166.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("70412");
                        this.add("58161");
                        this.add("50911");
                        this.add("59211");
                        this.add("58165");
                        this.add("58166");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2001:
                setCoverages(location, null, coverage);
                break;
            case IDCG030002:
                //			Available when Employment Practices Liability Insurance IDCG 31 2013 is selected and only in job type Policy Change.
                //			Available for Underwriters and Agents to see but the Underwriter will only be allowed to select the endorsement at a backdated policy change job if the policy is canceled."
                setCoverages(location, null, coverage);
                break;
            case IDCG312004:
                setCoverages(location, null, coverage);
                break;
            case CG2228:
                //			Available when class code 49333 is selected.
                setCoverages(location, "49333", coverage);
                break;
            case CG2426:
                setCoverages(location, null, coverage);
                break;
            case CG2412:
                //			Required when class codes 10117, 40115, 40140, 40117, or 43760 is selected.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("10117");
                        this.add("40115");
                        this.add("40140");
                        this.add("40117");
                        this.add("43760");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2416:
                //			Available when one of the following class codes are selected: 41668, 41667, 45190, 45192, 64074, 10110, 40111, 41422, 45191, 45193, or 64075.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("41668");
                        this.add("41667");
                        this.add("45190");
                        this.add("45192");
                        this.add("64074");
                        this.add("10110");
                        this.add("40111");
                        this.add("41422");
                        this.add("45191");
                        this.add("45193");
                        this.add("64075");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2271:
                //			Available when one of the following class codes are selected: 67513, 67512,  47474, or 47477.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("67513");
                        this.add("67512");
                        this.add("47474");
                        this.add("47477");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2410:
                //			UW Questions @ QQ: Is applicant/insured an additional insured on the manufacturerspolicy? Correct Answer - YES
                setCoverages(location, null, coverage);
                break;
            case CG2144:
                setCoverages(location, null, coverage);
                break;
            case CG2806:
                setCoverages(location, null, coverage);
                break;
            case IDCG312003:
                setCoverages(location, null, coverage);
                break;
            case CG2268:
                //			Available when one of the following class codes are selected: 10072, 10073, 10367, 13453, 13455, or 18616.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("10072");
                        this.add("10073");
                        this.add("10367");
                        this.add("13453");
                        this.add("13455");
                        this.add("18616");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case IDCG312005:
                setCoverages(location, null, coverage);
                break;
            case CG2407:
                //			Available when one of the following class codes are selected: 14401, 16820, 16819, 16900, 16901, 16902, 16905, 16906, 16910, 16911, 16915, 16916, 16930, 16931, and/or 16941.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("14401");
                        this.add("16820");
                        this.add("16819");
                        this.add("16900");
                        this.add("16901");
                        this.add("16902");
                        this.add("16905");
                        this.add("16906");
                        this.add("16910");
                        this.add("16911");
                        this.add("16915");
                        this.add("16916");
                        this.add("16930");
                        this.add("16931");
                        this.add("16941");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2292:
                //			Available when class code 99310 is selected.
                setCoverages(location, "99310", coverage);
                break;
            case CG2404:
                //			If Waiver of Subrogation is marked Yes under the Additional Insured screen then add this endorsement as required. This endorsement cannot be unselected. It is removed if Waiver of Subrogation is changed to a No.
                setCoverages(location, null, coverage);
                break;
            case CG2146:
                setCoverages(location, null, coverage);
                break;
            case CG2150:
                //			Available when one of the following class codes are selected: 16905 or 16906.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("16905");
                        this.add("16906");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case IDCG312012:
                //			This is only available for underwriters to select. The agent can see the endorsement and what the underwriter has entered in however they cannot edit it or select it.
                setCoverages(location, null, coverage);
                break;
            case CG2132:
                setCoverages(location, null, coverage);
                break;
            case CG2147:
                //			Available when Employement Practices Liability Insurance IDCG 31 2013 is not selected.
                setCoverages(location, null, coverage);
                break;
            case IDCG312008:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.
                String randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().getCode();
                boolean goodNumber = false;
                while (!goodNumber) {
                    randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().toString();
                    goodNumber = randExposure != ("99471");
                }
                setCoverages(location, randExposure, coverage);
                break;
            case CG2107:
                setCoverages(location, null, coverage);
                break;
            case CG2287:
                setCoverages(location, null, coverage);
                break;
            case CG2100:
                setCoverages(location, null, coverage);
                break;
            case CG2101:
                //			Available when one of the following class codes are selected: 63218, 63217, 63220, 63219, 43421, or 43424.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("63218");
                        this.add("63217");
                        this.add("63220");
                        this.add("63219");
                        this.add("43421");
                        this.add("43424");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2239:
                //			Available when one of the following class codes are selected: 10332, 10331, or 41422.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("10332");
                        this.add("10331");
                        this.add("41422");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2234:
                //			Available when class code 41620 is selected.
                setCoverages(location, "41620", coverage);
                break;
            case CG2279:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.
                randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().getCode();
                goodNumber = false;
                while (!goodNumber) {
                    randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().toString();
                    goodNumber = randExposure != ("99471");
                }
                setCoverages(location, randExposure, coverage);
                break;
            case CG2230:
                //			Available when one of the following class codes are selected: 67513, 67512, 47474, or 47477.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("67513");
                        this.add("67512");
                        this.add("47474");
                        this.add("47477");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2157:
                //			Available when one of the following class codes are selected: 48600 or 41650.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("48600");
                        this.add("41650");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2135:
                setCoverages(location, null, coverage);
                break;
            case CG2294:
                setCoverages(location, null, coverage);
                break;
            case CG2258:
                setCoverages(location, null, coverage);
                break;
            case CG2153:
                setCoverages(location, null, coverage);
                break;
            case IDCG312002:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 94225, 96816, 97047, 97050, 99471, or 99975.
                randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().getCode();
                goodNumber = false;
                while (!goodNumber) {
                    randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().toString();
                    goodNumber = randExposure != ("94225") && randExposure != ("96816") && randExposure != ("97047") && randExposure != ("97050") && randExposure != ("99471") && randExposure != ("99975");
                }
                setCoverages(location, randExposure, coverage);
                break;
            case CG2133:
                setCoverages(location, null, coverage);
                break;
            case CG2116:
                //			This becomes required when one of the following class codes are selected: 41650, 41677, 91805, 47052, 58408, 58409, 58456, 58457, 58458, or 58459. Other than that this remains electable.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("41650");
                        this.add("41677");
                        this.add("91805");
                        this.add("47052");
                        this.add("58408");
                        this.add("58409");
                        this.add("58456");
                        this.add("58457");
                        this.add("58458");
                        this.add("58459");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2134:
                setCoverages(location, null, coverage);
                break;
            case CG2159:
                //			Available when class code 46112 is selected.
                setCoverages(location, "46112", coverage);
                break;
            case CG2243:
                //			Available when one of the following class codes are selected: 92663 or 99471. However if CG 22 79 is on the policy then the endorsement (CG 22 43) is not availabile.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("92663");
                        this.add("99471");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2281:
                //			Available when one of the following class codes are selected: 94225 or 16890.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("94225");
                        this.add("16890");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case IDCG312001:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy:
                //			91111, 91150, 91405, 91481, 91523, 94225, 96816, 97047, 97050, 98993, 99310, 99471, or 99975.
                randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().getCode();
                goodNumber = false;
                while (!goodNumber) {
                    randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().getCode();
                    goodNumber = randExposure != ("91111") && randExposure != ("91150") && randExposure != ("91405") &&
                            randExposure != ("91481") && randExposure != ("91523") && randExposure != ("94225") &&
                            randExposure != ("96816") && randExposure != ("97047") && randExposure != ("97050") &&
                            randExposure != ("98993") && randExposure != ("99310") && randExposure != ("99471") &&
                            randExposure != ("99975");
                }
                setCoverages(location, randExposure, coverage);
                break;
            case CG2186:
                //			Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 96816, 97047, or 97050.
                randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().getCode();
                goodNumber = false;
                while (!goodNumber) {
                    randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().toString();
                    goodNumber = randExposure != ("96816") && randExposure != ("97047") && randExposure != ("97050");
                }
                setCoverages(location, randExposure, coverage);
                break;
            case CG2250:
                //			Available when one of the following class codes are selected: 13410, 92445, 97501, 97502, and/or 99943.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("13410");
                        //				this.add("92445");
                        //				this.add("97501");
                        //				this.add("97502");
                        //				this.add("99943");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2238:
                //			"Available when one of the following class codes are selected: 61223.
                //			If class code 61227 or 61226 is selected and the question ""Does applicant/insured act in a fiduciary capacity?"" is answered yes automatically attach this endorsement."
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("61223");
                        //				this.add("61227");
                        //				this.add("61226");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2152:
                //			"Available when class code 61223 is selected.
                //			If class code 61227 or 61226 is selected and the question ""Is applicant/insured involved in any of the following or related activities: Accounting, banking, credit card company, credit reporting, credit union, financial investment services, securities broker or dealer or tax preparation?"" is answered yes automatically attach this endorsement."
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("61227");
                        this.add("61226");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2156:
                //			Available when one of the following class codes is selected: 41604, 41603, 41697, 41696, 43889, 46005, and/or 46004.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("41604");
                        this.add("41603");
                        this.add("41603");
                        this.add("41697");
                        this.add("41696");
                        this.add("43889");
                        this.add("46005");
                        this.add("46004");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2224:
                //			Available when one of the following class codes are selected: 61223, or 96317.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("61223");
                        this.add("96317");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2248:
                //			This becomes required when one of the following class codes are selected: 61223 or 45334. Other than that this remains electable.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("61223");
                        this.add("45334");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2141:
                setCoverages(location, null, coverage);
                break;
            case CG2298:
                //			Available when one of the following class codes are selected: 45334 and/or 47610.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("45334");
                        this.add("47610");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2253:
                //			Available when one of the following class codes are selected: 14731, 19007, or 45678.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("14731");
                        this.add("19007");
                        this.add("45678");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2240:
                setCoverages(location, null, coverage);
                break;
            case CG2117:
                //			Available when question "Does applicant/insured move buildings or structures?" is answered yes.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("91130");
                        this.add("91636");
                        this.add("98751");
                        this.add("91629");
                        this.add("91636");
                        this.add("92101");
                        this.add("92445");
                        this.add("94444");
                        this.add("94590");
                        this.add("95357");
                        this.add("95625");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2136:
                setCoverages(location, null, coverage);
                break;
            case CG2273:
                setCoverages(location, null, coverage);
                break;
            case CG2138:
                //			Available CG 22 96 is selected unless the this become required because of the defined by script. Then CG 22 96 would be come unavailable.
                //			This becomes required when one of the following class codes are selected: 91130, 91636, 43200, 65007, 66123, 66122, 46822, 46882, 46881, 47052, or 98751. Other than that this remains electable.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("91130");
                        this.add("91636");
                        this.add("43200");
                        this.add("65007");
                        this.add("66123");
                        this.add("66122");
                        this.add("46822");
                        this.add("46882");
                        this.add("46881");
                        this.add("47052");
                        this.add("98751");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2236:
                //			Available when one of the following class codes are selected: 12375, 12374, or 45900.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("12375");
                        this.add("12374");
                        this.add("45900");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2237:
                //			Available when one of the following class codes are selected: 13759 or 15839.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("13759");
                        this.add("15839");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2104:
                //			This becomes a default value with no other choices when one of the following class codes is selected: 12375, 12374, 45900, or 15839.
                //			These have to be the only class codes on the policy for this rule to work. If another class code is on the policy that is not in this list
                //			Otherwise Available when the class premium base does not include (+) and
                //			Exclude is chosen by an UW on CG 00 01 - Products / Completed Operations Aggregate Limit (no premium charge is made) attach this form as required.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("12375");
                        this.add("12374");
                        this.add("45900");
                        this.add("15839");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2158:
                //			Available when one of the following class codes are selected: 91200 or 99851.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("91200");
                        this.add("99851");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2229:
                //			Available when class code 98751, 49763, and/or 18991 is on the policy.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("98751");
                        this.add("49763");
                        this.add("18991");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2301:
                //			This becomes required when class code 47050 is selected.
                setCoverages(location, "47050", coverage);
                break;
            case CG2246:
                setCoverages(location, null, coverage);
                break;
            case CG2244:
                //			This becomes required when one of the following class codes are selected: 40032, 40031, 43551, 66561.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("40032");
                        this.add("40031");
                        this.add("43551");
                        this.add("66561");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2245:
                //			This becomes required when one of the following class codes are selected: 10113, 10115, 11128, 11127, 11234, 12356, 45190, 45192, 14655, 15600, 18912, 18911, 45191, or 45193.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("10113");
                        this.add("10115");
                        this.add("11128");
                        this.add("11127");
                        this.add("11234");
                        this.add("12356");
                        this.add("45190");
                        this.add("45192");
                        this.add("14655");
                        this.add("15600");
                        this.add("18912");
                        this.add("18911");
                        this.add("45191");
                        this.add("45193");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2291:
                //			Available when class code 18575 and/or 99600 is selected.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("18575");
                        this.add("99600");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2233:
                //			This becomes required when one of the following class codes are selected: 91135, 97003, or 97002.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("91135");
                        this.add("97003");
                        this.add("97002");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2257:
                setCoverages(location, null, coverage);
                break;
            case CG2109:
                //			Not available when CG 24 50 is selected.
                setCoverages(location, null, coverage);
                break;
            case CG2166:
                setCoverages(location, null, coverage);
                break;
            case CG2160:
                setCoverages(location, null, coverage);
                break;
            case CG2184:
                setCoverages(location, null, coverage);
                break;
            case IDCG312007:
                //			Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy:  94225, 97047, 97050, and 96816.
                randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().getCode();
                goodNumber = false;
                while (!goodNumber) {
                    randExposure = GLClassCodeHelper.getRandomGLContractorClassCode().toString();
                    goodNumber = randExposure != ("94225") && randExposure != ("97047") && randExposure != ("97050") &&
                            randExposure != ("96816");
                }
                setCoverages(location, randExposure, coverage);
                break;
            case IDCG312009:
                //			This becomes required when class code 94225 is selected.
                setCoverages(location, "94225", coverage);
                break;
            case IDCG312010:
                //			This becomes required when class code 12683 is selected.
                setCoverages(location, "12683", coverage);
                break;
            case CG2167:
                setCoverages(location, null, coverage);
                break;
            case CG2260:
                //			"This is not available when class code 47052 is on the policy
                //			This becomes required when class code 47050 is selected."
                setCoverages(location, "47050", coverage);
                break;
            case CG2296:
                //			"Not available when CG 21 38 is selected.
                //			This becomes required when one of the following class codes are selected: 66123 or 66122."
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("66123");
                        this.add("66122");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2266:
                //			This becomes required when one of the following class codes are selected: 10036, 12683, 13410 or 57001.
                tempList = new ArrayList<String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add("10036");
                        this.add("12683");
                        this.add("13410");
                        this.add("57001");
                    }
                };
                setCoverages(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)), coverage);
                break;
            case CG2277:
                //			This becomes required when class code 43151 is selected.
                setCoverages(location, "43151", coverage);
                break;
            case CG2288:
                //			This becomes required when one of the following class codes are selected: 41675.
                setCoverages(location, "41675", coverage);
                break;
            case CG2290:
                //			This becomes required when class code 18200 is selected.
                setCoverages(location, "18200", coverage);
                break;
            case CG2299:
                //			This becomes required when class code 96930 is selected.
                setCoverages(location, "96930", coverage);
                break;
            case CG2270:
                //			This becomes required when class code 47052 is selected.
                setCoverages(location, "47052", coverage);
                break;
            case CG2196:
                setCoverages(location, null, coverage);
                break;
            case CG2149:
                //			Not available if CG 21 55 is selected.
                setCoverages(location, null, coverage);
                break;
            case CG2155:
                //			Not available if CG 21 49 is selected.
                setCoverages(location, null, coverage);
                break;
            default:
                break;
        }
    }

    /*
     * This makes sure a NullPointer for Contractors has been removed.
     */

    public void checkContractorNullPointer(PolicyLocation location, String classCode, List<CPPGeneralLiabilityExposures> policyExposureList) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();

        repository.pc.workorders.generic.GenericWorkorder myGenWork = new GenericWorkorder(driver);
        new GuidewireHelpers(getDriver()).editPolicyTransaction();

        CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(location, classCode);
        repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        List<String> returnCodes = new ArrayList<String>();
        returnCodes.add(classCode);
        exposuresPage.clickExposureDetialsTab();
        
        exposuresPage.clickAdd();
        
        exposuresPage.addExposure(myExposure);
        //FILL OUT UW QUESTIONS
        exposuresPage.clickLocationSpecificQuestionsTab();
        
        exposuresPage.fillOutBasicUWQuestionsQQ(myExposure, policyExposureList);
        exposuresPage.clickUnderwritingQuestionsTab();
        exposuresPage.clickLocationSpecificQuestionsTab();
        exposuresPage.clickUnderwritingQuestionsTab();
        //CONTRACTOR UW QUESTIONS
        
        exposuresPage.clickExposureDetialsTab();

        
        exposuresPage.selectClassCode(classCode);
        
        exposuresPage.clickRemove();
        
        exposuresPage.clickLocationSpecificQuestionsTab();
        if (checkIfElementExists("//div[contains(text(), 'NullPointerException: null')]", 100)) {
        	Assert.fail("##########/n ERROR: NullPointerException: null Message Is Present When It Shouldn't Be /n ##########");
        }
        myGenWork.clickPolicyChangeNext();
        if (checkIfElementExists("//div[contains(text(), 'NullPointerException: null')]", 100)) {
        	Assert.fail("##########/n ERROR: NullPointerException: null Message Is Present When It Shouldn't Be /n ##########");
        }
    }

    @FindBy(xpath = "//div[contains(@id,':RatingCumulDetailsPanelSet:0:GLExpCovCumulDetailLV-body')]")
    public WebElement table_ExposurePremium;

    public String getSublineColumnText(String classCode) {
        By classCodeColumn = By.xpath(table_ExposurePremium + "//div[contains(text()," + classCode + ")]");
        By sublineColumnText = By.xpath(classCodeColumn + "/parent::td/following-sibling::td[2]");
        return sublineColumnText.toString();
    }


    public void checkCheckSublineColumnForRemovedProducts(String classCode) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();
        

        if (getSublineColumnText(classCode) == "Products") {
            Assert.fail("##########/n ERROR: Products Displayed: Products Is Present On A Class Code In The Subline Column When It Shouldn't Be /n ##########");
        }

    }


    public void checkGLValidationMessages() throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();
        

		/*if(getSublineColumnText(classCode) == "Products"){
			throw new GuidewirePolicyCenterException(Configuration.getTargetServer(),
					"##########/n ERROR: Products Displayed: Products Is Present On A Class Code In The Subline Column When It Shouldn't Be /n ##########");
		}*/

    }

}
