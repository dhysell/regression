package scratchpad.brett;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.NumberUtils;
import repository.gw.infobar.InfoBar;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

public class AnthonysPolicyLoader extends BaseTest {
    private static final Logger trainingPolicyBuilderLog = Logger.getLogger(AnthonysPolicyLoader.class.getName());
    private File csvOutputFile = null;
    private int policiesToCreate = 20;
    private WebDriver driver;

    @Test
    public void generatePolicy() throws Exception {
        int secondsToDelay = NumberUtils.generateRandomNumberInt(1, 10);
        Thread.sleep(secondsToDelay * 1000); //Used to force a random start to policy loading.
        new File("C://dev/Policy Loader").mkdirs();
        this.csvOutputFile = new File("C://dev/Policy Loader/Policy Loader Run - " + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".csv");
        trainingPolicyBuilderLog.setUseParentHandlers(false);
        Handler fileHandler = new FileHandler("C://dev/Policy Loader/Policy Loader Run - " + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + ".log", true);
        MyFormatter formatter = new MyFormatter();
        fileHandler.setFormatter(formatter);
        trainingPolicyBuilderLog.addHandler(fileHandler);
        trainingPolicyBuilderLog.setLevel(Level.INFO);
        trainingPolicyBuilderLog.info("Starting to Generate Policies...");
        trainingPolicyBuilderLog.info("Writing out Headers to CSV File...");
        csvFileOutputWriter("Account Number, Policy Number, Policy Name, Effective Date, Expiration Date");

        GeneratePolicy myPolicyObj = null;
        for (int i = 1; i < policiesToCreate; i++) {
            try {
                Config cf = new Config(ApplicationOrCenter.PolicyCenter);
                driver = buildDriver(cf);

                AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
                propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
                propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
                propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

                ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
                ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
                PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
                locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
                locOnePropertyList.add(locationOneProperty);
                locationsList.add(new PolicyLocation(locOnePropertyList));

                SquireLiability liabilitySection = new SquireLiability();

                SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
                myPropertyAndLiability.locationList = locationsList;
                myPropertyAndLiability.liabilitySection = liabilitySection;

                Squire mySquire = new Squire();
                mySquire.propertyAndLiability = myPropertyAndLiability;

                myPolicyObj = new GeneratePolicy.Builder(driver)
                        .withSquire(mySquire)
                        .withInsFirstLastName("Payer", "Assign")
                        .withProductType(ProductLineType.Squire)
                        .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                        .withPaymentPlanType(PaymentPlanType.Quarterly)
                        .withDownPaymentType(PaymentType.Cash)
                        .build(GeneratePolicyType.QuickQuote);

                trainingPolicyBuilderLog.info("     Account Number: " + myPolicyObj.accountNumber + ":");
                trainingPolicyBuilderLog.info("          Policy Number: " + myPolicyObj.squire.getPolicyNumber());
                trainingPolicyBuilderLog.info("          Insured First Name: " + myPolicyObj.pniContact.getFirstName());
                trainingPolicyBuilderLog.info("          Insured Last Name: " + myPolicyObj.pniContact.getLastName());
                trainingPolicyBuilderLog.info("          Policy Effective Date: " + myPolicyObj.squire.getEffectiveDate());
                trainingPolicyBuilderLog.info("          Policy Expiration Date: " + myPolicyObj.squire.getExpirationDate());

                trainingPolicyBuilderLog.info("");
                successfulPolicyGenerationWriter(myPolicyObj, myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.getEffectiveDate(), myPolicyObj.squire.getExpirationDate());
                driver.quit();
            } catch (NullPointerException npe) {
                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE POLICY FAILED TO COMPLETE AS A RESULT OF A NULL POINTER EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(npe));
                System.err.println(ExceptionUtils.getStackTrace(npe));
            } catch (Exception e) {
                if (e.getMessage().contains("//span[starts-with(@id,'TabBar:PolicyTab-btnWrap')]")) {
                    trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE POLICY FAILED, AS THE ACCOUNT WAS LOCKED. PLEASE VERIFY THAT THIS AGENT SHOULD IN FACT BE LOCKED. IF THEY SHOULD NOT BE, PLEASE MANUALLY CREATE POLICIES FOR THEM.");
                } else {
                    trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FAILED TO COMPLETE AS A RESULT OF AN EXCEPTION. WILL ATTEMPT TO RE-CREATE POLICY.");
                    trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(e));
                    System.err.println(ExceptionUtils.getStackTrace(e));
                }
            } catch (AssertionError ae) {
                trainingPolicyBuilderLog.severe("THE ATTEMPT TO GENERATE THE MONTHLY POLICY (" + ((myPolicyObj == null) ? ("ACCOUNT # " + getAccountNumberFromScreen()) : ("POLICY # " + myPolicyObj.squire.getPolicyNumber())) + ") FAILED TO COMPLETE AS A RESULT OF AN ASSERTION ERROR. WILL ATTEMPT TO RE-CREATE POLICY.");
                trainingPolicyBuilderLog.severe(ExceptionUtils.getStackTrace(ae));
                System.err.println(ExceptionUtils.getStackTrace(ae));
            }

            myPolicyObj = null;
            trainingPolicyBuilderLog.info("");
        }
        trainingPolicyBuilderLog.info("Policy Generation Complete. Thank you.");
    }

    private void successfulPolicyGenerationWriter(GeneratePolicy policyGenerated, String policyNumber, Date effectiveDate, Date expirationDate) {
        trainingPolicyBuilderLog.info("     Writing Policy # " + policyNumber + " Out to CSV File...");
        csvFileOutputWriter(policyGenerated.accountNumber + "," + policyNumber + "," + policyGenerated.pniContact.getFullName() + "," + effectiveDate + "," + expirationDate);
        trainingPolicyBuilderLog.info("     Write Complete.");

        trainingPolicyBuilderLog.info("     Policy Creation for " + policyNumber + "Completed Successfully.");
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
                builder.append("\n");
            } else {
                builder.append(df.format(new Date(record.getMillis()))).append(" - ");
                builder.append("[").append(record.getSourceClassName()).append(".");
                builder.append(record.getSourceMethodName()).append("] - ");
                builder.append("[").append(record.getLevel()).append("] - ");
                builder.append(formatMessage(record));
                builder.append("\n");
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
