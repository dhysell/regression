package scratchpad.ian.repoItems;

import com.idfbins.driver.BaseTest;
public class GatherClaimsInfoToValidate extends BaseTest {


//    private String userName = ClaimsUsers.abatts.toString();
//    private String passWord = "gw";
//    private String claimNumber = "01242509012016020401";
//    private ClaimInfoValidation claim = new  ClaimInfoValidation();
//
//    private String testFirstNameToValidate = "Bruce";
//    private String testLastNameToValidate = "Wayne";
//
//    @AfterClass(alwaysRun=true)
//    public void tearDown() throws Exception {
//        //
//    }
//
//    @Test
//    public void gatherClaimsInfoToValidate() throws GuidewireException{
//
//        loginToProduct(ApplicationOrCenter.ClaimCenter, userName, passWord);
//
//        TopMenu topMenu = new TopMenu();
//        SideMenuCC sideMenu = new SideMenuCC();
//        PartiesInvolvedContacts partiesInvolved = new PartiesInvolvedContacts();
//        LossDetails lossDetails = new LossDetails();
//        topMenu.clickClaimTabArrow();
//        topMenu.setClaimNumberSearch(claimNumber);
//
//
//        //
//        sideMenu.clickPartiesInvolved();
//        partiesInvolved.clickReporterName();
//        partiesInvolved.clickEditContact();
//
//        System.out.println(partiesInvolved.getFirstNameValue());
//        claim.setContactFirstName(partiesInvolved.getFirstNameValue());
//
//        System.out.println(partiesInvolved.getLastNameValue());
//        claim.setContactLastName(partiesInvolved.getLastNameValue());
//
//        System.out.println(partiesInvolved.getAddressLineOneValue());
//        claim.setContactAddressLine1(partiesInvolved.getAddressLineOneValue());
//
//        System.out.println(partiesInvolved.getAddressLineTwoValue());
//        claim.setContactAddressLine2(partiesInvolved.getAddressLineTwoValue());
//
//        System.out.println(partiesInvolved.getCityValue());
//        claim.setContactCity(partiesInvolved.getCityValue());
//
//        System.out.println(partiesInvolved.getStateValue());
//        claim.setContactState(State.valueOf(partiesInvolved.getStateValue()));
//
//        System.out.println(partiesInvolved.getZipCodeValue());
//        claim.setContactZip(partiesInvolved.getZipCodeValue());
//
//        System.out.println(partiesInvolved.getHomePhoneValue());
//       claim.setContactHome(partiesInvolved.getHomePhoneValue());
//
//        System.out.println(partiesInvolved.getWorkPhoneValue());
//       claim.setContactWork(partiesInvolved.getWorkPhoneValue());
//
//        System.out.println(partiesInvolved.getCellPhoneValue());
//        claim.setContactCell(partiesInvolved.getCellPhoneValue());
//
//        System.out.println(partiesInvolved.getEmailValue());
//        claim.setContactEmail(partiesInvolved.getEmailValue());
//
//        sideMenu.clickLossDetailsLink();
//        lossDetails.clickEdit();
//
//
//        System.out.println(lossDetails.getLlAddressLineOne());
//        claim.setWhereDidItHappenAddressLine1(lossDetails.getLlAddressLineOne());
//        System.out.println(lossDetails.getLlAddressLineTwo());
//        claim.setWhereDidItHappenAddressLine2(lossDetails.getLlAddressLineTwo());
//        System.out.println(lossDetails.getLlCity());
//        claim.setWhereDidItHappenCity(lossDetails.getLlCity());
//        System.out.println(lossDetails.getLlState());
//        claim.setWhereDidItHappenState(State.valueOf(lossDetails.getLlState()));
//        System.out.println(lossDetails.getLlZipCode());
//        claim.setWhereDidItHappenZip(lossDetails.getLlZipCode());
//        System.out.println(lossDetails.getLlLossDescription());
//        claim.setDescribeWhatHappened(lossDetails.getLlLossDescription());
//
//
//
//
//
//
//    }
//
//    @Test(dependsOnMethods={"gatherClaimsInfoToValidate"})
//    public void validateReporterFirstName(){
//        Assert.assertTrue(claim.getContactFirstName().equals(testFirstNameToValidate), "Test");
//    }
//
//    @Test(dependsOnMethods={"gatherClaimsInfoToValidate"})
//    public void validateReporterLastName(){
//    	Assert.assertTrue(claim.getContactLastName().equals(testLastNameToValidate), "Test");
//    }
//
}
