package scratchpad.bill;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class GenerateSimple extends BaseTest {

    //Instance Data
    GeneratePolicy myPolicy;
    String userName;
    String userPass;
    String uwUserName;
    String uwPassword;
    String accountNumber;
    String policyNumber;
    ARUsers arUser;
    String arUsername;
    String arPassword;


    private WebDriver driver;


    @Test(enabled = true,
            description = "Create Policy")
    public void createPolicy() throws Exception {

//		Configuration.overrideServerToRun("IT");
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


        // ArrayList<PolicyLocationBuilding> locOneBuildingList = new
        // ArrayList<PolicyLocationBuilding>();
        ArrayList<PolicyLocation> locationsLists = null;

        // locOneBuildingList.add(new PolicyLocationBuilding());
        locationsLists = new ArrayList<PolicyLocation>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new PolicyLocation() {{
                    this.setAddress(new AddressInfo(true));
					/*this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
					}}); // END ADDITIONAL COVERAGES STUFF
*/
                    this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
                        private static final long serialVersionUID = 1L;

                        {
                            this.add(new PolicyLocationBuilding() {{
                                this.setNumStories(3);
                            }});
                        }
                    });
                }});

				/*this.add(new PolicyLocation(new AddressInfo(true), false) {{
					this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
					}}); // END ADDITIONAL COVERAGES STUFF
*/
                //Set AI
/*					this.setAdditionalInsuredLocationsList(new ArrayList<PolicyLocationAdditionalInsured>() {{
						this.add(new PolicyLocationAdditionalInsured(ContactSubType.Company) {{
							this.setAiRole(AdditionalInsuredRole.MortgageesAssigneesOrReceivers);
						}});
					}});
*/
					/*this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
						private static final long serialVersionUID = 1L;
					{
						this.add(new PolicyLocationBuilding() {{
							this.setPhotoYear(2034);
							this.setCostEstimatorYear(2034);
						}});
					}});*/
//				}});
            }
        };


        this.myPolicy = new GeneratePolicy.Builder(driver)
//			.withPolTermLengthDays(21)
                //Creates Business or Person policy depending on date.
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Wayne Tech")
//				.withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                //If using the Business or Person switch this has to be Joint since it is the only one on both.
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsLists)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                //Change based on how far the test needs to go
                .build(GeneratePolicyType.PolicyIssued);

        userName = (myPolicy.agentInfo.getAgentUserName());
        userPass = (myPolicy.agentInfo.getAgentPassword());
        accountNumber = (myPolicy.accountNumber);


        /**
         * Uncomment this next section if creating anything past Full Application
         */
//		uwUserName = (myPolicy.underwriterInfo.getUnderwriterUserName());
//		uwPassword = (myPolicy.underwriterInfo.getUnderwriterPassword());
//		policyNumber = (myPolicy.policyNumber);


        System.out.println("#############\nAccount Number: " + accountNumber);
//		System.out.println("Policy Number: " + policyNumber);
//		System.out.println("Under Writer: " + uwUserName);
        System.out.println("Agent: " + userName + "\n#############");


    }

    /**
     * @Description This will pay Insured's part of the policy off, and is only needed if going past Full Application
     * Set true is being used
     */
    @Test(enabled = false,
            description = "Makes Insured Payment on account",
            dependsOnMethods = {"createPolicy"})
    public void makeInsuredDownPayment() throws Exception {

        arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

        arUsername = arUser.getUserName();
        arPassword = arUser.getPassword();
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(arUsername, arPassword);

        DesktopActionsMultiplePayment makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeInsuredMultiplePaymentDownpayment(myPolicy, myPolicy.busOwnLine.getPolicyNumber());

        new GuidewireHelpers(driver).logout();
    }

    @Test(enabled = false,
            description = "Second Part of Test",
            dependsOnMethods = {"createPolicy"})
    public void testName() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(userName, userPass, accountNumber);

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuToolsDocuments();
    }

}
