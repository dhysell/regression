package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.pc.sidemenu.SideMenuPC;


public class GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_UWQuestions extends repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP {

    private WebDriver driver;

    public GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_UWQuestions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void isOnUnderwritingQuestinsTab() {
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP commercialPropertyLine = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(driver);
        if (finds(By.xpath("//label[text()='General Questions']")).isEmpty()) {
            if (finds(By.xpath("//span[contains(@id, ':CPWizardStepGroup:CPLineScreen:ttlBar') and (text()='Commercial Property Line')]")).isEmpty()) {
                repository.pc.sidemenu.SideMenuPC sidemenu = new SideMenuPC(driver);
                sidemenu.clickSideMenuCPCommercialPropertyLine();
            }
            commercialPropertyLine.clickUnderwritingQuestionsTab();
        }
    }


    /**
     * @param GeneratePolicy policy
     *                       FILL OUT COMMERCIAL PROPERTY LINE UNDERWRITING QUESTIONS TAB
     */
    public void fillOutUnderwritingQuestions(GeneratePolicy policy) {
        isOnUnderwritingQuestinsTab();
        CPPCommercialPropertyLine_Coverages myCoverages = policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages();
        CPPCommercialPropertyLine myProperty = policy.commercialPropertyCPP.getCommercialPropertyLine();

        if (myCoverages.isEmployeeTheft()) {
//			setAreAllReferencesChecked(myProperty.isAreAllReferencesChecked());
            setHowOftenAreAuditsconducted(myProperty.getHowOftenAreAuditsConducted());
            if (setWhoPerformsAudits(myProperty.getWhoPerformsTheseAudits()).equals("Other")) {
                setPleaseDescribeOther(myProperty.getWhoPerformsTheseAudits_PleaseDescribeOther());
            }
            setDoesApplicantHaveDifferentEmployeesToReconcileChecks(myProperty.isDifferentEmployeesWriteChecks());

            setProceduresForLargeChecks(myProperty.isProceduresInPlaceForLargeChecks());
            setIsApplicantAFraternalOrgOrLaborUnion(myProperty.isApplicantAFraternalOrgonizationOrLaborUnion());
        }

        if (myCoverages.isInsideThePremises_TheftOfMoneyAndSecurities() || myCoverages.isOutsideThePremises()) {
            if (!setDoYouDepositDaily(myProperty.isDoYouDepositDaily())) {
                setExplainHowOftenDepositsAreMade(myProperty.getExplainHowOftenDepositsAreDone());
            }
        }
    }


    private void setHowOftenAreAuditsconducted(String desc) {
        clickWhenClickable(find(By.xpath("//div[text()='How often are audits conducted?']/parent::td/following-sibling::td/div")));
        find(By.xpath("//textarea[@name='c2']")).sendKeys(Keys.CONTROL + "a");
        find(By.xpath("//textarea[@name='c2']")).sendKeys(desc);
        clickProductLogo();
    }

    private String setWhoPerformsAudits(String who) {
        clickWhenClickable(find(By.xpath("//div[text()='Who performs these audits?']/parent::td/following-sibling::td/div")));
        find(By.xpath("//input[@name='c2']")).sendKeys(Keys.CONTROL + "a");
        find(By.xpath("//input[@name='c2']")).sendKeys(who);
        clickProductLogo();
        return who;
    }

    private void setPleaseDescribeOther(String other) {
        clickWhenClickable(find(By.xpath("//div[text()='     Please describe other.']/parent::td/following-sibling::td/div")));
        find(By.xpath("//textarea[@name='c2']")).sendKeys(Keys.CONTROL + "a");
        find(By.xpath("//textarea[@name='c2']")).sendKeys(other);
        clickProductLogo();
    }

    private void setDoesApplicantHaveDifferentEmployeesToReconcileChecks(boolean yesno) {
        if (yesno) {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Does applicant have different employees write checks from those who reconcile bank statements?']/parent::td/parent::tr/descendant::input[@inputvalue='true']")));
        } else {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Does applicant have different employees write checks from those who reconcile bank statements?']/parent::td/parent::tr/descendant::input[@inputvalue='false']")));
        }
    }

    private boolean setDoYouDepositDaily(boolean yesno) {
        if (yesno) {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Do you deposit daily?']/parent::td/parent::tr/descendant::input[@inputvalue='true']")));
        } else {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Do you deposit daily?']/parent::td/parent::tr/descendant::input[@inputvalue='false']")));
        }
        return yesno;
    }

    private void setExplainHowOftenDepositsAreMade(String explination) {
        clickWhenClickable(find(By.xpath("//div[text()='     Please explain how often deposit are done and why they are not done daily.']/parent::td/following-sibling::td/div")));
        find(By.xpath("//textarea[@name='c2']")).sendKeys(Keys.CONTROL + "a");
        find(By.xpath("//textarea[@name='c2']")).sendKeys(explination);
        clickProductLogo();
    }

    private void setProceduresForLargeChecks(boolean yesno) {
        if (yesno) {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Are there procedures in place for large checks to have multiple signatures?']/parent::td/parent::tr/descendant::input[@inputvalue='true']")));
        } else {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Are there procedures in place for large checks to have multiple signatures?']/parent::td/parent::tr/descendant::input[@inputvalue='false']")));
        }
    }

    public void setIsApplicantAFraternalOrgOrLaborUnion(boolean yesno) {
        if (yesno) {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Is the applicant/insured a fraternal organization or labor union?']/parent::td/parent::tr/descendant::input[@inputvalue='true']")));
        } else {
            clickAndHoldAndRelease(find(By.xpath("//div[text()='Is the applicant/insured a fraternal organization or labor union?']/parent::td/parent::tr/descendant::input[@inputvalue='false']")));
        }
    }


}//END OF FILE

























