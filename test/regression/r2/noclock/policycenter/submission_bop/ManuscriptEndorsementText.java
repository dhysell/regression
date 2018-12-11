/**
 * @author bmartin Aug 5, 2015
 * @notes ID: TA17317
 * Work Product: DE2877
 * This test puts 4630 characters into liability Manuscript Endorsement and property
 * Manuscript Endorsement in Businessowners Line under Exclusions & Conditions, and sees
 * if the text is actually there or has been cut short to the 1333 characters that the
 * previous version was.
 */
package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineExclusionsConditions;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLine;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineExclusionsConditions;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

@QuarantineClass
public class ManuscriptEndorsementText extends BaseTest {

    private String uwUserName;
    private String accountNumber;
    private String agentUserName;

    private WebDriver driver;

    @Test()
    public void testManuscriptEndorsementText() throws Exception {

        boolean testFailed = false;
        String failureString = "";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        int yearTest = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(
                false, true);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        myline.setAdditionalCoverageStuff(myLineAddCov);

        PolicyBusinessownersLineExclusionsConditions myExclCond = new PolicyBusinessownersLineExclusionsConditions();
        myline.setExclusionsConditionsStuff(myExclCond);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(yearTest);
        loc1Bldg1.setClassClassification("storage");

        AddressInfo addIntTest = new AddressInfo();

        AdditionalInterest loc1Bld1AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
        loc1Bld1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc1Bld1AddInterest.setAddress(addIntTest);

        locOneBuildingList.add(loc1Bldg1);

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));


        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Wayne Tech")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

        this.uwUserName = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter).getUnderwriterUserName();
        this.agentUserName = myPolicyObj.agentInfo.getAgentUserName();
        this.accountNumber = myPolicyObj.accountNumber;

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        guidewireHelpers.logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(uwUserName, "gw", accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuBusinessownersLine();

        GenericWorkorderBusinessownersLine myBOL = new GenericWorkorderBusinessownersLine(driver);
        myBOL.clickBusinessownersLine_ExclusionsConditions();

        GenericWorkorder myGenWork = new GenericWorkorder(driver);
        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderBusinessownersLineExclusionsConditions myExclCon = new GenericWorkorderBusinessownersLineExclusionsConditions(driver);
        myPolicyObj.busOwnLine.getExclusionsConditionsStuff().setLiabilityManuscriptEndorsement(true);
        myPolicyObj.busOwnLine.getExclusionsConditionsStuff().getLiabilityManuscriptEndorsementDescription().add(loremIpsim);
        myPolicyObj.busOwnLine.getExclusionsConditionsStuff().setPropertyManuscriptEndorsement(true);
        myPolicyObj.busOwnLine.getExclusionsConditionsStuff().getPropertyManuscriptEndorsementDescription().add(loremIpsim);
        myExclCon.fillOutOtherPolicyWideConditions(myPolicyObj.busOwnLine);

        myGenWork.clickGenericWorkorderQuote();

        mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuBusinessownersLine();

        myBOL = new GenericWorkorderBusinessownersLine(driver);
        myBOL.clickBusinessownersLine_ExclusionsConditions();

        if (!verifyTextInLiabilityManuscriptEndorsement()) {
            testFailed = true;
            failureString += "ERROR: Liability Manuscript Endorsement text is less that 5000 characters\n";
        }
        if (!verifyTextInPropertyManuscriptEndorsement()) {
            testFailed = true;
            failureString += "ERROR: Property Manuscript Endorsement text is less that 5000 characters";
        }

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("UnderWriter: " + uwUserName);
        System.out.println("Agent: " + agentUserName + "\n#############");

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + failureString);
        }
    }

    public boolean verifyTextInLiabilityManuscriptEndorsement() {

        String textLiabManEnd;

        GenericWorkorderBusinessownersLineExclusionsConditions myExclCon = new GenericWorkorderBusinessownersLineExclusionsConditions(driver);
        textLiabManEnd = myExclCon.getBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement();
        return textLiabManEnd.equals(loremIpsim);
    }

    public boolean verifyTextInPropertyManuscriptEndorsement() {

        String textPropManEnd;

        GenericWorkorderBusinessownersLineExclusionsConditions myExclCon = new GenericWorkorderBusinessownersLineExclusionsConditions(driver);
        textPropManEnd = myExclCon.getBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement();
        return textPropManEnd.equals(loremIpsim);
    }

    /**
     * This is 5000 characters which is the maximum number of characters that
     * can go in the updated box.
     */
    private String loremIpsim = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer non lorem quis magna volutpat ornare quis quis magna. Aliquam eget leo nunc. Morbi magna magna, rutrum fringilla mauris at, sollicitudin feugiat neque. Donec nibh leo, volutpat nec quam non, congue rhoncus dui. Aliquam a justo condimentum, malesuada purus tincidunt, tincidunt dolor. Duis mollis, arcu vitae pellentesque ullamcorper, velit est semper ipsum, ut ultrices libero mauris a ex. Nunc molestie tellus at mi dignissim, sed porttitor ante placerat. Nulla quis ornare sapien, at porttitor lorem. Fusce eu turpis rutrum, rhoncus dolor ac, pretium dui. Phasellus porttitor augue nec risus rutrum dictum. Maecenas ac ornare est, sed sollicitudin enim. Etiam arcu orci, varius in ultricies id, tincidunt et neque. Donec ut lacus aliquam, dictum nisl in, facilisis ante. Quisque suscipit tellus vel urna ultricies ultricies. Vivamus bibendum eros lorem, a viverra nisl condimentum vel. Etiam consequat iaculis lacus, ac efficitur elit viverra sed. Nulla commodo, eros quis vehicula maximus, risus nisi feugiat odio, et dictum purus elit a leo. Fusce vitae magna tellus. Pellentesque vel mi vel nulla convallis gravida. Morbi tincidunt augue in neque bibendum dictum. Morbi mollis id dui a mattis. Aenean suscipit quam in nunc interdum, at ultricies tortor egestas. Mauris ullamcorper tellus ut dolor congue varius. Donec fermentum ornare euismod. Donec vehicula suscipit felis, a dictum sem dignissim quis. Duis et arcu eu nisi faucibus varius. Pellentesque vel nunc ipsum. Maecenas ultrices venenatis quam, ac porta lacus aliquam at. Ut tincidunt enim a nisi iaculis dignissim. Vestibulum tempor posuere neque, sed suscipit mi. Sed iaculis odio sed ipsum lobortis dapibus. Fusce vestibulum molestie congue. Nunc consectetur, purus eget finibus fringilla, sapien dolor pretium turpis, ac lobortis est ligula vel augue. Ut tempor dapibus eros. Nulla quis erat ac est vulputate ullamcorper. Integer iaculis ante eget lobortis eleifend. Aenean commodo vestibulum dictum. Maecenas facilisis laoreet ex, nec molestie mi ullamcorper tempor. Suspendisse id diam in dolor malesuada semper et sed enim. Suspendisse sollicitudin sapien turpis, id tempus turpis ultrices vel. Pellentesque dictum quam vitae porttitor malesuada. Suspendisse vel varius mauris, sed fringilla ipsum. Aenean blandit, nisi vel consectetur varius, metus mi tristique leo, vel aliquam elit eros eu odio. Proin faucibus finibus dui in dictum. Proin quis arcu fermentum, pellentesque arcu ut, rutrum erat. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. In posuere viverra sapien nec tempor. Fusce nec purus sapien. Curabitur sed eros convallis, laoreet libero vestibulum, auctor odio. Pellentesque in placerat orci. Sed porttitor, est quis dictum lacinia, erat urna varius tellus, sed sagittis nulla elit non nisl. Sed sed nibh nec nisi hendrerit ullamcorper vel ut orci. Proin blandit convallis tortor, eget iaculis leo laoreet pulvinar. Mauris eget consequat velit. Nunc eros dui, venenatis vitae erat sit amet, elementum dignissim quam. Etiam elementum purus lacinia ornare consequat. Pellentesque et dui egestas, fringilla magna quis, faucibus velit. Duis at felis dolor. Fuscemi purus, lacinia non quam nec, pretium tempor lacus. Donec ipsum arcu, consectetur eget erat vel, placerat malesuada nunc. Donec at venenatis lacus, tristique tempor nulla. Pellentesque sagittis leo at lacinia pharetra. Nullam pulvinar non dolor ut scelerisque. Sed at turpis lorem. Vivamus dignissim est nisi, sed pellentesque justo pharetra sed. Sed mollis, neque at congue volutpat, nunc ex suscipit mauris, molestie efficitur odio erat sit amet nulla. Praesent non bibendum augue, sit amet efficitur mi. Donec tristique enim et tellus vestibulum tempus. Duis vel lorem nisi. Ut ac fringilla dolor, ut blandit dui. Vestibulum mi lorem, dictum vel consectetur feugiat, cursus a sem. Vivamus metus tortor, porttitor id dolor et, consequat rutrum ex. Fusce vel nisi et velit fringilla venenatis in vitae nibh. Suspendisse commodo magna vitae est hendrerit placerat. Fusce cursus dui eget sapien eleifend, et aliquet enim aliquam. Donec semper ut odio a lacinia. Morbi commodo, odio sed faucibus rhoncus, erat turpis consectetur ex, et auctor nisl neque id quam. Mauris tristique dolor nec augue dapibus, id viverra odio sagittis. Donec erat nunc, consequat in cursus vel, ultricies id mi. Cras elementum dignissim odio, ac tristique dui efficitur eu. Fusce at placerat arcu. Morbi eu pretium neque, pretium ultricies ex. Sed id sapien ut risus laoreet tincidunt vel vel mi.";

}
