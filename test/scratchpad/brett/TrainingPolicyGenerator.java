package scratchpad.brett;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

public class TrainingPolicyGenerator extends BaseTest {
    private boolean resetCountersToZeroBeforeRunning = false;
    private ARUsers arUser;
    private static final Logger trainingPolicyBuilderLog = Logger.getLogger(TrainingPolicyGenerator.class.getName());
    private File csvOutputFile = null;

    @BeforeTest
    public void BeforeClass() throws Exception {
        if (resetCountersToZeroBeforeRunning) {
            AgentsHelper.resetAgentUsedForPolicyCreationValueToZero();
            AgentsHelper.resetAgentPolicyTypeCreatedValueToZero();
            AgentsHelper.resetAgentClaimedByVMValueToZero();
        }
    }

    private WebDriver driver;

    @Test
    public void testPolicy() throws Exception {
        int secondsToDelay = NumberUtils.generateRandomNumberInt(1, 10);
        Thread.sleep(secondsToDelay * 1000); //Used to force a random start to policy loading.
        new File("C://dev/Training Policy Loader").mkdirs();
        this.csvOutputFile = new File("C://dev/Training Policy Loader/Policy Loader Run - " + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv");
        trainingPolicyBuilderLog.setUseParentHandlers(false);
        Handler fileHandler = new FileHandler("C://dev/Training Policy Loader/Policy Loader Run - " + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".log", true);
        MyFormatter formatter = new MyFormatter();
        fileHandler.setFormatter(formatter);
        trainingPolicyBuilderLog.addHandler(fileHandler);
        trainingPolicyBuilderLog.setLevel(Level.INFO);
        trainingPolicyBuilderLog.info("Starting to Generate Policies...");
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        trainingPolicyBuilderLog.info("Writing out Headers to CSV File...");
        csvFileOutputWriter("Agent Username, Agent First Name, Agent Last Name, Agent Number, Account Number, Policy Number, Policy Name, Effective Date, Expiration Date, Total Premium");
        GeneratePolicy myPolicyObj = null;
        List<Agents> agentList = AgentsHelper.getAllAgents();
        for (Agents agent : agentList) {
            int totalCreatedPoliciesCounter = 0;
            int policyAlreadyCreatedCounter = 0;
            boolean firstTimeThroughLoop = true;
            outerloop:
            while (totalCreatedPoliciesCounter < 3) {
                if (firstTimeThroughLoop) {
                    Agents refreshedAgent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                    if (refreshedAgent.agentClaimedByVM > 0) {
                        trainingPolicyBuilderLog.info("Agent: " + agent.getAgentFullName() + " Already Claimed by another VM. Skipping this Agent...");
                        break outerloop;
                    } else {
                        secondsToDelay = NumberUtils.generateRandomNumberInt(1, 10);
                        Thread.sleep(secondsToDelay * 1000); //Used to force a random start to policy loading.
                        refreshedAgent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                        if (refreshedAgent.agentClaimedByVM > 0) {
                            trainingPolicyBuilderLog.info("Agent: " + agent.getAgentFullName() + " Already Claimed by another VM. Skipping this Agent...");
                            break outerloop;
                        } else {
                            AgentsHelper.incrementAgentClaimedByVM(refreshedAgent.agentUserName);
                        }
                    }
                    totalCreatedPoliciesCounter = refreshedAgent.agentUsedForPolicyCreation;
                    policyAlreadyCreatedCounter = refreshedAgent.agentPolicyTypeCreated;
                    firstTimeThroughLoop = false;
                }
                switch (totalCreatedPoliciesCounter) {
                    case 0:
                        trainingPolicyBuilderLog.info("Generating Monthly policy for Agent: " + agent.getAgentFullName());
                        try {
                            myPolicyObj = createPolicy(PaymentPlanType.Monthly, agent);

                            trainingPolicyBuilderLog.info("     Account Number: " + myPolicyObj.accountNumber + ":");
                            trainingPolicyBuilderLog.info("          Policy Number: " + myPolicyObj.squire.getPolicyNumber());
                            trainingPolicyBuilderLog.info("          Insured First Name: " + myPolicyObj.pniContact.getFirstName());
                            trainingPolicyBuilderLog.info("          Insured Last Name: " + myPolicyObj.pniContact.getLastName());
                            trainingPolicyBuilderLog.info("          Policy Effective Date: " + myPolicyObj.squire.getEffectiveDate());
                            trainingPolicyBuilderLog.info("          Policy Expiration Date: " + myPolicyObj.squire.getExpirationDate());
                            trainingPolicyBuilderLog.info("          Total Premium Amount: " + myPolicyObj.squire.getPremium().getTotalNetPremium());
                            trainingPolicyBuilderLog.info("          Down Payment Amount: " + myPolicyObj.squire.getPremium().getDownPaymentAmount());
                            trainingPolicyBuilderLog.info("          Down Payment Type: " + myPolicyObj.downPaymentType.getValue());
                            trainingPolicyBuilderLog.info("          Payment Plan Type: " + myPolicyObj.paymentPlanType.getValue());

                            trainingPolicyBuilderLog.info("");
                            trainingPolicyBuilderLog.info("        Paying Full Annual Premium on Policy Number " + myPolicyObj.squire.getPolicyNumber() + "...");

                            payPolicyPremiumForEntirePolicy(myPolicyObj, myPolicyObj.squire.getPolicyNumber());
                            new GuidewireHelpers(driver).logout();
                            successfulPolicyGenerationWriter(agent, myPolicyObj, myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.getEffectiveDate(), myPolicyObj.squire.getExpirationDate());
                            totalCreatedPoliciesCounter++;
                            policyAlreadyCreatedCounter = 1;
                        } catch (NullPointerException npe) {
                            trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF A NULL POINTER EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                            trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(npe));
                            System.err.println(ExceptionUtils.getStackTrace(npe));
                            agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                        } catch (Exception e) {
                            if (e.getMessage().contains("//span[starts-with(@id,'TabBar:PolicyTab-btnWrap')]")) {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED, AS THE ACCOUNT WAS LOCKED. PLEASE VERIFY THAT THIS AGENT SHOULD IN FACT BE LOCKED. IF THEY SHOULD NOT BE, PLEASE MANUALLY CREATE POLICIES FOR THEM.");
                                AgentsHelper.setAgentUsedForPolicyCreation(agent.agentUserName, 150);
                                totalCreatedPoliciesCounter = 150;
                            } else {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(e));
                                System.err.println(ExceptionUtils.getStackTrace(e));
                                agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                            }
                        } catch (AssertionError ae) {
                            trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN ASSERTION ERROR. WILL ATTEMPT TO RE-CREATE POLICY.");
                            trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(ae));
                            System.err.println(ExceptionUtils.getStackTrace(ae));
                            agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                        }

                        myPolicyObj = null;
                        trainingPolicyBuilderLog.info("");

                        trainingPolicyBuilderLog.info("Generating Quarterly policy for Agent: " + agent.getAgentFullName());
                        try {
                            myPolicyObj = createPolicy(PaymentPlanType.Quarterly, agent);

                            trainingPolicyBuilderLog.info("     Account Number: " + myPolicyObj.accountNumber + ":");
                            trainingPolicyBuilderLog.info("          Policy Number: " + myPolicyObj.squire.getPolicyNumber());
                            trainingPolicyBuilderLog.info("          Insured First Name: " + myPolicyObj.pniContact.getFirstName());
                            trainingPolicyBuilderLog.info("          Insured Last Name: " + myPolicyObj.pniContact.getLastName());
                            trainingPolicyBuilderLog.info("          Policy Effective Date: " + myPolicyObj.squire.getEffectiveDate());
                            trainingPolicyBuilderLog.info("          Policy Expiration Date: " + myPolicyObj.squire.getExpirationDate());
                            trainingPolicyBuilderLog.info("          Total Premium Amount: " + myPolicyObj.squire.getPremium().getTotalNetPremium());
                            trainingPolicyBuilderLog.info("          Down Payment Amount: " + myPolicyObj.squire.getPremium().getDownPaymentAmount());
                            trainingPolicyBuilderLog.info("          Down Payment Type: " + myPolicyObj.downPaymentType.getValue());
                            trainingPolicyBuilderLog.info("          Payment Plan Type: " + myPolicyObj.paymentPlanType.getValue());

                            trainingPolicyBuilderLog.info("");
                            trainingPolicyBuilderLog.info("        Paying Full Annual Premium on Policy Number " + myPolicyObj.squire.getPolicyNumber() + "...");

                            payPolicyPremiumForEntirePolicy(myPolicyObj, myPolicyObj.squire.getPolicyNumber());
                            new GuidewireHelpers(driver).logout();
                            successfulPolicyGenerationWriter(agent, myPolicyObj, myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.getEffectiveDate(), myPolicyObj.squire.getExpirationDate());
                            totalCreatedPoliciesCounter++;
                            policyAlreadyCreatedCounter = 2;
                        } catch (NullPointerException npe) {
                            trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF A NULL POINTER EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                            trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(npe));
                            System.err.println(ExceptionUtils.getStackTrace(npe));
                            agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                        } catch (Exception e) {
                            if (e.getMessage().contains("//span[starts-with(@id,'TabBar:PolicyTab-btnWrap')]")) {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE QUARTERLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED, AS THE ACCOUNT WAS LOCKED. PLEASE VERIFY THAT THIS AGENT SHOULD IN FACT BE LOCKED. IF THEY SHOULD NOT BE, PLEASE MANUALLY CREATE POLICIES FOR THEM.");
                                AgentsHelper.setAgentUsedForPolicyCreation(agent.agentUserName, 150);
                                totalCreatedPoliciesCounter = 150;
                            } else {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE QUARTERLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(e));
                                System.err.println(ExceptionUtils.getStackTrace(e));
                                agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                            }
                        } catch (AssertionError ae) {
                            trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE QUARTERLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN ASSERTION ERROR. WILL ATTEMPT TO RE-CREATE POLICY.");
                            trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(ae));
                            System.err.println(ExceptionUtils.getStackTrace(ae));
                            agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                        }

                        myPolicyObj = null;
                        trainingPolicyBuilderLog.info("");
                        break;

                    case 1:
                        if (policyAlreadyCreatedCounter == 2) {
                            trainingPolicyBuilderLog.info("Generating Monthly policy for Agent: " + agent.getAgentFullName());
                            try {
                                myPolicyObj = createPolicy(PaymentPlanType.Monthly, agent);

                                trainingPolicyBuilderLog.info("     Account Number: " + myPolicyObj.accountNumber + ":");
                                trainingPolicyBuilderLog.info("          Policy Number: " + myPolicyObj.squire.getPolicyNumber());
                                trainingPolicyBuilderLog.info("          Insured First Name: " + myPolicyObj.pniContact.getFirstName());
                                trainingPolicyBuilderLog.info("          Insured Last Name: " + myPolicyObj.pniContact.getLastName());
                                trainingPolicyBuilderLog.info("          Policy Effective Date: " + myPolicyObj.squire.getEffectiveDate());
                                trainingPolicyBuilderLog.info("          Policy Expiration Date: " + myPolicyObj.squire.getExpirationDate());
                                trainingPolicyBuilderLog.info("          Total Premium Amount: " + myPolicyObj.squire.getPremium().getTotalNetPremium());
                                trainingPolicyBuilderLog.info("          Down Payment Amount: " + myPolicyObj.squire.getPremium().getDownPaymentAmount());
                                trainingPolicyBuilderLog.info("          Down Payment Type: " + myPolicyObj.downPaymentType.getValue());
                                trainingPolicyBuilderLog.info("          Payment Plan Type: " + myPolicyObj.paymentPlanType.getValue());

                                trainingPolicyBuilderLog.info("");
                                trainingPolicyBuilderLog.info("        Paying Full Annual Premium on Policy Number " + myPolicyObj.squire.getPolicyNumber() + "...");

                                payPolicyPremiumForEntirePolicy(myPolicyObj, myPolicyObj.squire.getPolicyNumber());
                                new GuidewireHelpers(driver).logout();
                                successfulPolicyGenerationWriter(agent, myPolicyObj, myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.getEffectiveDate(), myPolicyObj.squire.getExpirationDate());
                                totalCreatedPoliciesCounter++;
                                policyAlreadyCreatedCounter = 1;
                            } catch (NullPointerException npe) {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF A NULL POINTER EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(npe));
                                System.err.println(ExceptionUtils.getStackTrace(npe));
                                agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                            } catch (Exception e) {
                                if (e.getMessage().contains("//span[starts-with(@id,'TabBar:PolicyTab-btnWrap')]")) {
                                    trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED, AS THE ACCOUNT WAS LOCKED. PLEASE VERIFY THAT THIS AGENT SHOULD IN FACT BE LOCKED. IF THEY SHOULD NOT BE, PLEASE MANUALLY CREATE POLICIES FOR THEM.");
                                    AgentsHelper.setAgentUsedForPolicyCreation(agent.agentUserName, 150);
                                    totalCreatedPoliciesCounter = 150;
                                } else {
                                    trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                                    trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(e));
                                    System.err.println(ExceptionUtils.getStackTrace(e));
                                    agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                                }
                            } catch (AssertionError ae) {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN ASSERTION ERROR. WILL ATTEMPT TO RE-CREATE POLICY.");
                                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(ae));
                                System.err.println(ExceptionUtils.getStackTrace(ae));
                                agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                            }

                            myPolicyObj = null;
                            trainingPolicyBuilderLog.info("");
                        } else if (policyAlreadyCreatedCounter == 1) {
                            trainingPolicyBuilderLog.info("Generating Quarterly policy for Agent: " + agent.getAgentFullName());
                            try {
                                myPolicyObj = createPolicy(PaymentPlanType.Quarterly, agent);

                                trainingPolicyBuilderLog.info("     Account Number: " + myPolicyObj.accountNumber + ":");
                                trainingPolicyBuilderLog.info("          Policy Number: " + myPolicyObj.squire.getPolicyNumber());
                                trainingPolicyBuilderLog.info("          Insured First Name: " + myPolicyObj.pniContact.getFirstName());
                                trainingPolicyBuilderLog.info("          Insured Last Name: " + myPolicyObj.pniContact.getLastName());
                                trainingPolicyBuilderLog.info("          Policy Effective Date: " + myPolicyObj.squire.getEffectiveDate());
                                trainingPolicyBuilderLog.info("          Policy Expiration Date: " + myPolicyObj.squire.getExpirationDate());
                                trainingPolicyBuilderLog.info("          Total Premium Amount: " + myPolicyObj.squire.getPremium().getTotalNetPremium());
                                trainingPolicyBuilderLog.info("          Down Payment Amount: " + myPolicyObj.squire.getPremium().getDownPaymentAmount());
                                trainingPolicyBuilderLog.info("          Down Payment Type: " + myPolicyObj.downPaymentType.getValue());
                                trainingPolicyBuilderLog.info("          Payment Plan Type: " + myPolicyObj.paymentPlanType.getValue());

                                trainingPolicyBuilderLog.info("");
                                trainingPolicyBuilderLog.info("        Paying Full Annual Premium on Policy Number " + myPolicyObj.squire.getPolicyNumber() + "...");

                                payPolicyPremiumForEntirePolicy(myPolicyObj, myPolicyObj.squire.getPolicyNumber());
                                new GuidewireHelpers(driver).logout();
                                successfulPolicyGenerationWriter(agent, myPolicyObj, myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.getEffectiveDate(), myPolicyObj.squire.getExpirationDate());
                                totalCreatedPoliciesCounter++;
                                policyAlreadyCreatedCounter = 2;
                            } catch (NullPointerException npe) {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF A NULL POINTER EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(npe));
                                System.err.println(ExceptionUtils.getStackTrace(npe));
                                agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                            } catch (Exception e) {
                                if (e.getMessage().contains("//span[starts-with(@id,'TabBar:PolicyTab-btnWrap')]")) {
                                    trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE QUARTERLY POLICY FOR AGENT: " + agent.getAgentFullName() + " FAILED, AS THE ACCOUNT WAS LOCKED. PLEASE VERIFY THAT THIS AGENT SHOULD IN FACT BE LOCKED. IF THEY SHOULD NOT BE, PLEASE MANUALLY CREATE POLICIES FOR THEM.");
                                    AgentsHelper.setAgentUsedForPolicyCreation(agent.agentUserName, 150);
                                    totalCreatedPoliciesCounter = 150;
                                } else {
                                    trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE QUARTERLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                                    trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(e));
                                    System.err.println(ExceptionUtils.getStackTrace(e));
                                    agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                                }
                            } catch (AssertionError ae) {
                                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE QUARTERLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FOR AGENT: " + agent.getAgentFullName() + " FAILED TO COMPLETE AS A RESULT OF AN ASSERTION ERROR. WILL ATTEMPT TO RE-CREATE POLICY.");
                                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(ae));
                                System.err.println(ExceptionUtils.getStackTrace(ae));
                                agent = AgentsHelper.getAgentByUserName(agent.agentUserName);
                            }

                            myPolicyObj = null;
                            trainingPolicyBuilderLog.info("");
                        }
                        break;

                    case 2:
                        trainingPolicyBuilderLog.info("BOTH REQUIRED POLICIES ARE ALREADY CREATED FOR " + agent.getAgentFullName() + ". SKIPPING POLICY CREATION...");
                        totalCreatedPoliciesCounter++;
                        break;

                    default:
                        trainingPolicyBuilderLog.severe("THE CHECK FOR POLICIES THAT WERE ALREADY CREATED FOR AGENT: " + agent.getAgentFullName() + " HAD A NUMBER THAT WAS NOT IN THE CASES EXPECTED. PLEASE INVESTIGATE! ");
                }
            }
        }
        trainingPolicyBuilderLog.info("Policy Generation Complete. Thank you.");
    }

    private GeneratePolicy createPolicy(PaymentPlanType paymentPlanType, Agents agentToUse) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOnePropertyOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        PLPolicyLocationProperty locationOnePropertyTwo = new PLPolicyLocationProperty(PropertyTypePL.Shed);
        locOnePropertyList.add(locationOnePropertyOne);
        locOnePropertyList.add(locationOnePropertyTwo);
        PolicyLocation locationOne = new PolicyLocation(locOnePropertyList);
        locationOne.setPlNumAcres(10);
        locationsList.add(locationOne);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicleOne = new Vehicle();
        Vehicle vehicleTwo = new Vehicle();
        vehicleList.add(vehicleOne);
        vehicleList.add(vehicleTwo);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setVehicleList(vehicleList);

        ArrayList<RecreationalEquipment> recreationalVehicleATV = new ArrayList<RecreationalEquipment>();
        recreationalVehicleATV.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Monthly "));

        ArrayList<InlandMarine> squireInlandMarine = new ArrayList<InlandMarine>();
        squireInlandMarine.add(InlandMarine.RecreationalEquipment);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = squireInlandMarine;
        myInlandMarine.recEquipment_PL_IM = recreationalVehicleATV;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agentToUse)
                .withInsFirstLastName(agentToUse.getAgentUserName(), paymentPlanType.getValue())
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withPaymentPlanType(paymentPlanType)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        return myPolicyObj;
    }

    private void payPolicyPremiumForEntirePolicy(GeneratePolicy policyObjectToUse, String policyNumber) throws Exception {
        try {
            new Login(policyObjectToUse.getWebDriver()).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), policyObjectToUse.accountNumber);

            NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
            newPayment.makeInsuredDownpaymentForPolicyLoaderCustom(policyObjectToUse, policyObjectToUse.squire.getPremium().getTotalNetPremium(), policyNumber);
        } catch (Exception e) {
            trainingPolicyBuilderLog.severe("THE ATTEMPT TO PAY THE ENTIRE PREMIUM AMOUNT FOR POLICY # " + policyNumber + " FAILED AS A RESULT OF AN EXCEPTION. PLEASE GO BACK AND COMPLETE TRANSACTION.");
            throw new Exception(e);
        } catch (AssertionError ae) {
            trainingPolicyBuilderLog.severe("THE ATTEMPT TO PAY THE ENTIRE PREMIUM AMOUNT FOR POLICY # " + policyNumber + " FAILED AS A RESULT OF AN ASSERTION ERROR. PLEASE GO BACK AND COMPLETE TRANSACTION.");
            throw new AssertionError(ae);
        }
    }

    private void successfulPolicyGenerationWriter(Agents agent, GeneratePolicy policyGenerated, String policyNumber, Date effectiveDate, Date expirationDate) throws Exception {
        AgentsHelper.incrementAgentUsedForPolicyCreation(agent.agentUserName);
        AgentsHelper.setAgentPolicyTypeCreated(agent.agentUserName, 2);
        trainingPolicyBuilderLog.info("     Writing Policy # " + policyNumber + " Out to CSV File...");
        csvFileOutputWriter(agent.getAgentUserName() + "," + agent.getAgentFirstName() + "," + agent.getAgentLastName() + "," + agent.getAgentNum() + "," + policyGenerated.accountNumber + "," + policyNumber + "," + policyGenerated.pniContact.getFullName() + "," + effectiveDate + "," + expirationDate + "," + "\"" + StringsUtils.currencyRepresentationOfNumber(policyGenerated.squire.getPremium().getTotalNetPremium()) + "\"");
        trainingPolicyBuilderLog.info("     Write Complete.");

        trainingPolicyBuilderLog.info("     Policy Creation for " + policyNumber + " By Agent: " + agent.getAgentFullName() + " Completed Successfully.");
    }

    private String getAccountNumberFromScreen() {
        String accountNumberToReturn = "";
        try {
            InfoBar informationBar = new InfoBar(driver);
            accountNumberToReturn = informationBar.getInfoBarAccountNumber();
        } catch (Exception e) {
            accountNumberToReturn = "(UNABLE TO GET ACCOUNT NUMBER)";
        }
        return accountNumberToReturn;
    }

    private void csvFileOutputWriter(String lineToWrite) {
        BufferedWriter output = null;
        try {
            FileWriter fileOutput = new FileWriter(this.csvOutputFile, true);
            output = new BufferedWriter(fileOutput);
            output.append(lineToWrite);
            output.append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    class MyFormatter extends Formatter {
        // Create a DateFormat to format the logger timestamp.
        private DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(100000);
            if (record.getMessage() == "") {
                builder.append(System.lineSeparator());
            } else {
                builder.append(df.format(new Date(record.getMillis()))).append(" - ");
                builder.append("[").append(record.getSourceClassName()).append(".");
                builder.append(record.getSourceMethodName()).append("] - ");
                builder.append("[").append(record.getLevel()).append("] - ");
                builder.append(formatMessage(record));
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }

        public String getHead(Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }
}
