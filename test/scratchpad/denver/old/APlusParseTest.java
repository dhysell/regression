package scratchpad.denver.old;

import repository.cc.claim.SummaryOverview;
import repository.cc.claim.SummaryOverviewExposures;
import repository.cc.claim.VacationStatusCC;
import repository.cc.topmenu.TopMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class APlusParseTest extends BaseTest {

    private ClaimsUsers userName = ClaimsUsers.abatts;
    private String password = "gw";
    private WebDriver driver;

    @Test
    public void compareAPlusWithGW() throws Exception {

        List<APlusFile> aPlusRecords = parseAPlus();

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        VacationStatusCC vs = new VacationStatusCC(driver);

        login.login(userName.name(), password);
        vs.setVacationStatusAtWork();

        runCompare(aPlusRecords);

        System.out.print("");

    }

    private void runCompare(List<APlusFile> aPlusRecords) {
        SummaryOverview summary = new SummaryOverview(driver);
        TopMenu topMenu = new TopMenu(driver);

        String previousClaim = null;

        for (APlusFile aPlusFile : aPlusRecords) {
            if (!aPlusFile.claimNumber.equalsIgnoreCase(previousClaim)) {

                topMenu.goToClaimByClaimNumber(aPlusFile.claimNumber);
                int count = 0;

                // Get Exposures from Guidewire Claim Center.
                List<SummaryOverviewExposures> gwExposures = summary.getExposuresList();
                List<APlusFile> aPlusExposures = new ArrayList<APlusFile>();

                // Get exposures from APlus file
                for (APlusFile record : aPlusRecords) {
                    if (record.claimNumber.matches(aPlusFile.claimNumber)) {
                        aPlusExposures.add(record);
                    }
                }

                if (aPlusExposures.size() == gwExposures.size()) {
                    for (APlusFile aPlusExposure : aPlusExposures) {
                        if (aPlusExposure.amountPaid.doubleValue() == gwExposures.get(count).getRemainingReserves().doubleValue()) {
                            System.out.print(String.format("%-52s %13s", "\n" + aPlusExposure.claimNumber + " | Expected Remaining Reserves: ", NumberFormat.getCurrencyInstance().format(aPlusExposure.amountPaid.setScale(2, RoundingMode.HALF_UP))));
                            System.out.print(String.format("%-32s %13s", " | Actual Remaining Reserves: ", NumberFormat.getCurrencyInstance().format(gwExposures.get(count).getRemainingReserves().setScale(2, RoundingMode.HALF_UP))));
                        } else {
                            System.out.print(String.format("%-52s %13s", "\n" + aPlusExposure.claimNumber + " | Expected Remaining Reserves: ", NumberFormat.getCurrencyInstance().format(aPlusExposure.amountPaid.setScale(2, RoundingMode.HALF_UP))));
                            System.out.print(String.format("%-32s %13s", " | Actual Remaining Reserves: ", NumberFormat.getCurrencyInstance().format(gwExposures.get(count).getRemainingReserves().setScale(2, RoundingMode.HALF_UP))) + " -- ERROR");
                        }
                        count++;
                    }
                    System.out.println();
                } else {
                    System.out.println("----------------------------------------------------------------------------------------------------------");
                    System.out.println("\nMANUAL REVIEW - Number of reported exposures are different than the number of exposures in ClaimCenter.");
                    System.out.println();
                    System.out.println("APlus Report Exposures:");
                    for (APlusFile aPlusExposure : aPlusExposures) {
                        System.out.println(String.format("%-52s %13s", aPlusExposure.claimNumber + " | Expected Remaining Reserves: ", NumberFormat.getCurrencyInstance().format(aPlusExposure.amountPaid.setScale(2, RoundingMode.HALF_UP))));
                    }
                    System.out.println();
                    System.out.println("ClaimCenter Exposures:");
                    for (SummaryOverviewExposures gwExposure : gwExposures) {
                        if (gwExposures.isEmpty() || gwExposures.size() < 1) {
                            System.out.println("Claim cannot be found or is missing exposores.");
                        }
                        System.out.println(String.format("%-32s %15s", aPlusFile.claimNumber + " | Actual Remaining Reserves: ", NumberFormat.getCurrencyInstance().format(gwExposure.getRemainingReserves().setScale(2, RoundingMode.HALF_UP))));
                    }
                    System.out.println("----------------------------------------------------------------------------------------------------------");
                }
                previousClaim = aPlusFile.claimNumber;
            }
        }
    }

    private List<APlusFile> parseAPlus() {
        File file = null;
        Scanner input = null;

        try {
            file = new File("C:\\dev\\workspaces\\selenium\\guidewire\\GWTesting\\test\\scratchpad\\denver\\aplusfiles\\APPROP20170531.txt");
            input = new Scanner(file);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        List<String> claimNumbers = new ArrayList<String>();
        List<BigDecimal> amountsPaid = new ArrayList<BigDecimal>();

        while (input.hasNext()) {
            try {
                String tempString = input.nextLine();
                String claimNumber = tempString.substring(524, 544);
                String totalPaidString = tempString.substring(767, 778);
                BigDecimal totalPaid = formatTotalPaid(totalPaidString);

                claimNumbers.add(claimNumber);
                amountsPaid.add(totalPaid);

            } catch (Exception e) {
                System.out.println("Skip Parsing - Row does not contain parsable data.");
            }
        }

        input.close();

        return buildAPlusFileList(claimNumbers, amountsPaid);

    }

    private List<APlusFile> buildAPlusFileList(List<String> claimNumbers, List<BigDecimal> amountsPaid) {

        List<APlusFile> fileList = new ArrayList<>();

        for (int i = 0; i < claimNumbers.size(); i++) {
            fileList.add(new APlusFile(claimNumbers.get(i), amountsPaid.get(i)));
        }

        return fileList;
    }

    private BigDecimal formatTotalPaid(String amountString) {

        amountString = new StringBuilder(amountString).insert(amountString.length() - 2, ".").toString();
        boolean negateValue = false;
        BigDecimal value;

        if (amountString.contains("-")) {
            amountString = amountString.replace("-", "");
            negateValue = true;
        }

        if (negateValue) {
            value = new BigDecimal(amountString).negate();
        } else {
            value = new BigDecimal(amountString);
        }

        return value;
    }


}