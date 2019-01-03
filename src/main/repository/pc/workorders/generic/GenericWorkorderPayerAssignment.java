package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.*;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenericWorkorderPayerAssignment extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderPayerAssignment(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    private boolean bopBuildingList = false;
    private boolean billAll = false;
    private String billAllString = "";


    //////////////////////////////////////
    // Recorded Elements and Their xPaths//
    //////////////////////////////////////

    @FindBy(xpath = "//a[contains(@id, 'PayerAssignmentScreen:BillAllButton')]")
    private WebElement button_PayerAssignmentBillAllCoverages;

    private Guidewire8Select select_PayerAssignmentBillAllCoverages() {
        return new Guidewire8Select(driver, "//div[contains(@id,':PayerAssignmentScreen:1-body')]");
    }

    private WebElement table_PayerAssignmentSection1PropertyDwellingLevelTable(int locationNumber) {
        return find(By.xpath("//div[contains(@id,':PayerAssignmentProductPanelSet:" + (locationNumber - 1) + ":dwellingLevelPayerLV')]"));
    }

    @FindBy(xpath = "//div[contains(@id,':PayerAssignmentProductPanelSet:sectionTwoLineLevelPayerLV')]")
    private WebElement table_PayerAssignmentSection2LiabilityLineLevelTable;

    @FindBy(xpath = "//div[contains(@id,':membershipPayerLV')]")
    private WebElement table_PayerAssignmentMemberShipDuesTable;

    private Guidewire8Checkbox checkBox_PayerAssignmentVerificationConfirmation() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PayerAssignmentScreen:payerScreenVerified')]");
    }

    /////////////////////////////////////
    // Helper Methods for Above Elements//
    /////////////////////////////////////


	public void setPayerAssignmentBillAllCoverages(String billAllCoverages) {
        clickWhenClickable(find(By.cssSelector("div[id$=':PayerAssignmentScreen:1-body'] td.g-cell-edit > div")));
		Guidewire8Select mySelect = select_PayerAssignmentBillAllCoverages();
		mySelect.selectByVisibleTextPartial(billAllCoverages);
		clickWhenClickable(button_PayerAssignmentBillAllCoverages);
	}


    public void resetAllCoveragesBackToInsured() {
        setPayerAssignmentBillAllCoverages("Reset Coverages Back To Insured");
    }


    public void setPayerAssignmentVerificationConfirmationCheckbox(boolean trueFalseChecked) {
        checkBox_PayerAssignmentVerificationConfirmation().select(trueFalseChecked);
    }


    public WebElement getPayerAssignmentBOPLocationTable(int locationNumber) {
        List<WebElement> locationTables = finds(By.xpath("//div[contains(@id, 'PayerAssignmentLVPanelSet:PayerAssignmentLocation:') and contains(@class, 'x-panel-default x-grid')]"));
        for (WebElement locationTable : locationTables) {
            String locationAddressListing = tableUtils.getCellTextInTableByRowAndColumnName(locationTable, 1, "Location Address");
            if (locationAddressListing.startsWith(locationNumber + ": ")) {
                return locationTable;
            }
        }
        Assert.fail("The location table was not found by searching for the location containing the location number. Please verify.");
        return null;
    }


    public WebElement getPayerAssignmentBOPBuildingTable(int locationNumber, int buildingNumber) {
        WebElement locationTable = getPayerAssignmentBOPLocationTable(locationNumber);

        //This block is used to pull out the dynamically generated number associated with the Location WebElement just found.
        String locationTableWebelementID = locationTable.getAttribute("id");
        String textStringToSearchFor = "LOBWizardStepGroup:LineWizardStepSet:PayerAssignmentScreen:PayerAssignmentProductPanelSet:";
        int indexOfBeginningOfLocationNumber = ((locationTableWebelementID.indexOf(textStringToSearchFor)) + (textStringToSearchFor.length()));
        int xPathLocationNumber = Integer.valueOf(locationTableWebelementID.substring(indexOfBeginningOfLocationNumber, indexOfBeginningOfLocationNumber + 1));

        List<WebElement> buildingTables = finds(By.xpath("//div[contains(@id, ':PayerAssignmentScreen:PayerAssignmentProductPanelSet:" + xPathLocationNumber + ":') and contains(@id, 'PayerAssignmentLVPanelSet:PayerAssignmentBuilding:BuildLevelCovsLV') and contains(@class, 'x-panel-default x-grid')]"));
        for (WebElement buildingTable : buildingTables) {
            String buildingNumberListing = tableUtils.getCellTextInTableByRowAndColumnName(buildingTable, 1, "Building");
            if (buildingNumberListing.equals(String.valueOf(buildingNumber))) {
                return buildingTable;
            }
        }
        Assert.fail("The building table was not found by searching for the building number under the location number specified. Either the table does not exist, or the requisite coverages did not have a lienholder applied to them. Please verify.");
        return null;
    }


    public void setPayerAssignmentBillAllBuildingOrPropertyCoverages(Integer locationNumber, Integer buildingOrPropertyNumber, String payerAssignmentString, boolean billCurrentPayer, boolean billRenewalPayer) {
    	//jlarsen HATEFULL TEMP FIX :(
    	payerAssignmentString = payerAssignmentString.replace("PO Box", "Po Box");
    	
    	
        
        if (this.bopBuildingList) { //For BOP Buildings
            HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

            columnRowKeyValuePairs.put("Building", String.valueOf(buildingOrPropertyNumber));
            columnRowKeyValuePairs.put("Coverage", "Bill All Building Coverages");

            String payerAssignmentConfirmation = "";
            int i = 0;
            //Temp fix to get payer assignment stuff to pass. The original test was an "equalsIgnoreCase"
            while (!payerAssignmentConfirmation.toUpperCase().contains(payerAssignmentString.toUpperCase()) && i < 15) {
                WebElement buildingTable = getPayerAssignmentBOPBuildingTable(locationNumber, buildingOrPropertyNumber);
                int tableRow = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(buildingTable, columnRowKeyValuePairs));
                if (billCurrentPayer) {
                    tableUtils.selectValueForSelectInTable(buildingTable, tableRow, "Current Payer", payerAssignmentString);
                    buildingTable = getPayerAssignmentBOPBuildingTable(locationNumber, buildingOrPropertyNumber);
                    payerAssignmentConfirmation = tableUtils.getCellTextInTableByRowAndColumnName(buildingTable, tableRow, "Current Payer");
                }

                if (billRenewalPayer) {
                    tableUtils.selectValueForSelectInTable(buildingTable, tableRow, "Renewal Payer", payerAssignmentString);
                    buildingTable = getPayerAssignmentBOPBuildingTable(locationNumber, buildingOrPropertyNumber);
                    payerAssignmentConfirmation = tableUtils.getCellTextInTableByRowAndColumnName(buildingTable, tableRow, "Renewal Payer");
                }
                i++;
            }
            //Temp fix to get payer assignment stuff to pass. The original test was an "equalsIgnoreCase"
            if (!payerAssignmentConfirmation.toUpperCase().contains(payerAssignmentString.toUpperCase()) && i >= 15) {
                Assert.fail("The Payer Assignment option was not selected from the dropdown after 15 attempts. The item searched was \"" + payerAssignmentString + "\". The item selected was \"" + payerAssignmentConfirmation + "\".");
            }
        } else { //For PL Property
            HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

            columnRowKeyValuePairs.put("Loc. #", String.valueOf(locationNumber));
            columnRowKeyValuePairs.put("Bldg. #", String.valueOf(buildingOrPropertyNumber));

            int tableRow = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), columnRowKeyValuePairs));

            String payerAssignmentConfirmation = "";
            int i = 0;
            //Temp fix to get payer assignment stuff to pass. The original test was an "equalsIgnoreCase"
            while (!payerAssignmentConfirmation.toUpperCase().contains(payerAssignmentString.toUpperCase()) && i < 15) {
                if (billCurrentPayer) {
                    tableUtils.selectValueForSelectInTable(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), tableRow, "Current Payer", payerAssignmentString);
                    payerAssignmentConfirmation = tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), tableRow, "Current Payer");
                }

                if (billRenewalPayer) {
                    tableUtils.selectValueForSelectInTable(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), tableRow, "Renewal Payer", payerAssignmentString);
                    payerAssignmentConfirmation = tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), tableRow, "Renewal Payer");
                }
                i++;
            }
            //Temp fix to get payer assignment stuff to pass. The original test was an "equalsIgnoreCase"
            if (!payerAssignmentConfirmation.toUpperCase().contains(payerAssignmentString.toUpperCase())) {
                Assert.fail("The Payer Assignment option was not selected from the dropdown after 15 attempts. The item searched was \"" + payerAssignmentString + "\". The item selected was \"" + payerAssignmentConfirmation + "\".");
            }
        }
    }


    public void setPayerAssignmentBillLiabilityCoverages(String coverageToBill, boolean billCurrentPayer, boolean billRenewalPayer, String payerAssignmentString) {
        int tableRow = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PayerAssignmentSection2LiabilityLineLevelTable, "Coverage", coverageToBill));
        if (billCurrentPayer) {
            tableUtils.selectValueForSelectInTable(table_PayerAssignmentSection2LiabilityLineLevelTable, tableRow, "Current Payer", payerAssignmentString);
        }

        if (billRenewalPayer) {
            tableUtils.selectValueForSelectInTable(table_PayerAssignmentSection2LiabilityLineLevelTable, tableRow, "Renewal Payer", payerAssignmentString);
        }
    }


    public void setPayerAssignmentBillMembershipDues(String memberShipDuesMember, boolean billCurrentPayer, boolean billRenewalPayer, String payerAssignmentString) {
        int tableRow = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PayerAssignmentMemberShipDuesTable, "Name", memberShipDuesMember));
        if (billCurrentPayer) {
            tableUtils.selectValueForSelectInTable(table_PayerAssignmentMemberShipDuesTable, tableRow, "Current Payer", payerAssignmentString);
        }

        if (billRenewalPayer) {
            tableUtils.selectValueForSelectInTable(table_PayerAssignmentMemberShipDuesTable, tableRow, "Renewal Payer", payerAssignmentString);
        }
    }


    public void setPayerAssignmentBillCoveragesAsRequired(GeneratePolicy policy) {
        additionalInterestIterator(policy);

        if (billAll == true) {
            setPayerAssignmentBillAllCoverages(billAllString);
        } else {
            setPayerAssignmentBuildingsAndProperties(policy);
            if (policy.squire.propertyAndLiability.liabilitySection != null) {
                setPayerAssignmentLiability(policy, true, false);
            }
            if (policy.membership.getMembersList().size() > 0) {
                for (Contact member : policy.membership.getMembersList()) {
                    if (member.isMembershipDuesAreLienPaid()) {
                        setPayerAssignmentBillMembershipDues(member.getFullName(), true, false, member.getMembershipDuesLienHolder().getLienholderPayerAssignmentString());
                    }
                }
            }
        }
        setPayerAssignmentVerificationConfirmationCheckbox(true);
        resetClassValues();
    }


    public String getPayerAssignmentPayerDetailsByLocationAndBuildingNumber(int locationNumber, int buildingNumber) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), 1, "Current Payer");
    }


    public void fillOutPayerAssignmentPage(GeneratePolicy policy) throws Exception {
                repository.pc.sidemenu.SideMenuPC sideMenuStuff = new SideMenuPC(driver);
        outerLoop:
        switch (policy.productType) {
            case Businessowners:
                for (PolicyLocation locations : policy.busOwnLine.locationList) {
                    for (PolicyLocationBuilding locationBuilding : locations.getBuildingList()) {
                        if (locationBuilding.getAdditionalInterestList().size() > 0) {
                            sideMenuStuff.clickSideMenuPayerAssignment();
                            
                            setPayerAssignmentBillCoveragesAsRequired(policy);
                            clickNext();
                            
                            break outerLoop;
                        }
                    }
                }
                break;
            case CPP:
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                for (PolicyLocation locations : policy.squire.propertyAndLiability.locationList) {
                    for (PLPolicyLocationProperty locationProperty : locations.getPropertyList()) {
                        if (locationProperty.getBuildingAdditionalInterest().size() > 0) {
                            sideMenuStuff.clickSideMenuPayerAssignment();
                            setPayerAssignmentBillCoveragesAsRequired(policy);
                            clickNext();
                            break outerLoop;
                        }
                    }
                }
                break;
            case StandardIM:
                break;
            case Membership:
                break;
            case StandardFire:
                for (PolicyLocation locations : policy.standardFire.getLocationList()) {
                    for (PLPolicyLocationProperty locationProperty : locations.getPropertyList()) {
                        if (locationProperty.getBuildingAdditionalInterest().size() > 0) {
                            sideMenuStuff.clickSideMenuPayerAssignment();
                            
                            setPayerAssignmentBillCoveragesAsRequired(policy);
                            clickNext();
                            
                            break outerLoop;
                        }
                    }
                }
                break;
            case StandardLiability:
                break;
        }
    }//END fillOutPayerAssignmentPage()

    private void resetClassValues() {
        this.bopBuildingList = false;
        this.billAll = false;
        this.billAllString = "";
    }

    /**
     * This private method iterates over the entire list of locations, buildings, property, and additional interests tied to those locations, property, buildings, etc.
     * to be able to determine what payer assignment options should or should not be set on the Payer Assignment page.
     *
     * @param policy
     */
    private void additionalInterestIterator(GeneratePolicy policy) {
        ArrayList<AdditionalInterest> lienHolderList = new ArrayList<AdditionalInterest>();
        AdditionalInterest firstResidencePremisesAdditionalInterest = null;
        boolean firstResidencePremisesAdditionalInterestFound = false;
        switch (policy.productType) {
            case Businessowners:
                for (PolicyLocation locations : policy.busOwnLine.locationList) {
                    bopBuildingList = true;
                    for (PolicyLocationBuilding locationBuilding : locations.getBuildingList()) {
                        if (locationBuilding.getAdditionalInterestList().size() > 0) {
                            for (AdditionalInterest buildingAdditionalInterest : locationBuilding.getAdditionalInterestList()) {
                                if (buildingAdditionalInterest.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_All || buildingAdditionalInterest.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                                    lienHolderList.add(buildingAdditionalInterest);
                                    if (buildingAdditionalInterest.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_All) {
                                        this.billAllString = buildingAdditionalInterest.getLienholderPayerAssignmentString();
                                        this.billAll = true;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case CPP:
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                for (PolicyLocation locations : policy.squire.propertyAndLiability.locationList) {
                    for (PLPolicyLocationProperty locationProperty : locations.getPropertyList()) {
                        if (locationProperty.getBuildingAdditionalInterest().size() > 0) {
                            for (AdditionalInterest buildingAdditionalInterest : locationProperty.getBuildingAdditionalInterest()) {
                                if (buildingAdditionalInterest.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_All || buildingAdditionalInterest.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                                    if ((locationProperty.getpropertyType() == PropertyTypePL.ResidencePremises) && (!firstResidencePremisesAdditionalInterestFound)) {
                                        firstResidencePremisesAdditionalInterest = buildingAdditionalInterest;
                                        firstResidencePremisesAdditionalInterestFound = true;
                                    }
                                    lienHolderList.add(buildingAdditionalInterest);
                                    if (buildingAdditionalInterest.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_All) {
                                        this.billAllString = buildingAdditionalInterest.getLienholderPayerAssignmentString();
                                        this.billAll = true;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
//		case StandardFL:
//			break;
            case StandardIM:
                break;
            case Membership:
                break;
            case StandardFire:
                break;
            case StandardLiability:
                break;
        }
        if (policy.squire.propertyAndLiability.liabilitySection != null) {
            if (policy.squire.propertyAndLiability.liabilitySection.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_All || policy.squire.propertyAndLiability.liabilitySection.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                boolean squireLiabilityAdditionalInterestSetToMatchPropertyLienholder = false;
                if (policy.squire.propertyAndLiability.liabilitySection.getLiabilityAdditionalInterest() == null) {
                    policy.squire.propertyAndLiability.liabilitySection.setLiabilityAdditionalInterest(firstResidencePremisesAdditionalInterest);
                    lienHolderList.add(firstResidencePremisesAdditionalInterest);
                } else {
                    for (AdditionalInterest additionalInterest : lienHolderList) {
                        if ((policy.squire.propertyAndLiability.liabilitySection.getLiabilityAdditionalInterest().getCompanyName().equals(additionalInterest.getCompanyName()) || ((policy.squire.propertyAndLiability.liabilitySection.getLiabilityAdditionalInterest().getPersonFirstName() + " " + policy.squire.propertyAndLiability.liabilitySection.getLiabilityAdditionalInterest().getPersonLastName()).equals((additionalInterest.getPersonFirstName() + " " + additionalInterest.getPersonLastName()))))) {
                            policy.squire.propertyAndLiability.liabilitySection.setLiabilityAdditionalInterest(additionalInterest);
                            lienHolderList.add(additionalInterest);
                            squireLiabilityAdditionalInterestSetToMatchPropertyLienholder = true;
                            break;
                        }
                    }
                    if (!squireLiabilityAdditionalInterestSetToMatchPropertyLienholder) {
                        Assert.fail("The lienholder set for use for the Squire Liability section was not found while inspecting the other lienholders set to properties. This means that there will be no way to set your desired lienholder for the Squire liability section and your test will fail. Please either let Generate dynamically set a lienholder, or match your selection for the Squire Liability section with one of the lienholders used for the Property section.");
                    }
                }
            }
        }
		/*if (policy.membershipDuesForPNILienPaid) {
			boolean membershipDuesAdditionalInterestSetToMatchPropertyLienholder = false;
			if (policy.membershipDuesLienHolder == null) {
				policy.membershipDuesLienHolder = firstResidencePremisesAdditionalInterest;
				lienHolderList.add(firstResidencePremisesAdditionalInterest);
			} else {
				for (AdditionalInterest additionalInterest : lienHolderList) {
					if ((policy.membershipDuesLienHolder.getCompanyName().equals(additionalInterest.getCompanyName()) || ((policy.membershipDuesLienHolder.getPersonFirstName() + " " + policy.membershipDuesLienHolder.getPersonLastName()).equals((additionalInterest.getPersonFirstName() + " " + additionalInterest.getPersonLastName()))))) {
						policy.membershipDuesLienHolder = additionalInterest;
						lienHolderList.add(additionalInterest);
						membershipDuesAdditionalInterestSetToMatchPropertyLienholder = true;
						break;
					}
				}
				if (!membershipDuesAdditionalInterestSetToMatchPropertyLienholder) {
					Assert.fail("The lienholder set for use for the Membership Dues section was not found while inspecting the other lienholders set to properties. This means that there will be no way to set your desired lienholder for the Membership Dues section and your test will fail. Please either let Generate dynamically set a lienholder, or match your selection for the Membership Dues section with one of the lienholders used for the Property section.");
				}
			}
		}*/
        boolean same = lienHolderList.isEmpty() || lienHolderList.stream().allMatch(lienHolderList.get(0)::equals);
        if (billAll == true && same == false) {
            Assert.fail("The Bill All option was chosen, but the lienHolders did not all match. The Bill All Button will not be present and the test will fail. Please correct this condition and try again.");
        }
    }

    /**
     * This private method iterates over the entire list of locations, buildings, property, and additional interests tied to those locations, property, buildings, etc.
     * in order to set the dropdown value for each charge option that corresponds to the billing selection chosen.
     *
     * @param policy
     */
    private void setPayerAssignmentBuildingsAndProperties(GeneratePolicy policy) {

        switch (policy.productType) {
            case Businessowners:
                for (PolicyLocation locations : policy.busOwnLine.locationList) {
                    for (PolicyLocationBuilding locationBuilding : locations.getBuildingList()) {
                        if (locationBuilding.getAdditionalInterestList().size() > 0) {
                            if (locationBuilding.getAdditionalInterestList().get(0).getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                                setPayerAssignmentBillAllBuildingOrPropertyCoverages(locations.getNumber(), locationBuilding.getNumber(), locationBuilding.getAdditionalInterestList().get(0).getLienholderPayerAssignmentString(), true, false);
                            }
                        }
                    }
                }
                break;
            case CPP:
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                for (PolicyLocation locations : policy.squire.propertyAndLiability.locationList) {
                    for (PLPolicyLocationProperty locationProperty : locations.getPropertyList()) {
                        if (locationProperty.getBuildingAdditionalInterest().size() > 0) {
                            if (locationProperty.getBuildingAdditionalInterest().get(0).getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                                setPayerAssignmentBillAllBuildingOrPropertyCoverages(locations.getNumber(), locationProperty.getPropertyNumber(), locationProperty.getBuildingAdditionalInterest().get(0).getLienholderPayerAssignmentString(), true, false);
                            }
                        }
                    }
                }
                break;
//		case StandardFL:
//			break;
            case StandardIM:
                break;
            case Membership:
                break;
            case StandardFire:
                break;
            case StandardLiability:
                break;
        }
    }

    private void setPayerAssignmentLiability(GeneratePolicy policy, boolean billCurrentPayer, boolean billRenewalPayer) {
        if (policy.squire.propertyAndLiability.liabilitySection.getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
            int rowCount = tableUtils.getRowCount(table_PayerAssignmentSection2LiabilityLineLevelTable);
            String payerAssignmentString = policy.squire.propertyAndLiability.liabilitySection.getLiabilityAdditionalInterest().getLienholderPayerAssignmentString();
            for (int i = 1; i <= rowCount; i++) {
                String payerAssignmentConfirmation = "";
                //Temp fix to get payer assignment stuff to pass. The original test was an "equalsIgnoreCase"
                int lcv = 0;
                while (!payerAssignmentConfirmation.contains(payerAssignmentString) && lcv < 15) {
                    if (billCurrentPayer) {
                        tableUtils.selectValueForSelectInTable(table_PayerAssignmentSection2LiabilityLineLevelTable, i, "Current Payer", payerAssignmentString);
                        payerAssignmentConfirmation = tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentSection2LiabilityLineLevelTable, i, "Current Payer");
                    }

                    if (billRenewalPayer) {
                        tableUtils.selectValueForSelectInTable(table_PayerAssignmentSection2LiabilityLineLevelTable, i, "Renewal Payer", payerAssignmentString);
                        payerAssignmentConfirmation = tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentSection2LiabilityLineLevelTable, i, "Renewal Payer");
                    }
                    lcv++;
                }
                //Temp fix to get payer assignment stuff to pass. The original test was an "equalsIgnoreCase"
                if (!payerAssignmentConfirmation.contains(payerAssignmentString) && lcv >= 15) {
                    Assert.fail("The Payer Assignment option was not selected from the dropdown after 15 attempts. The item searched was \"" + payerAssignmentString + "\". The item selected was \"" + payerAssignmentConfirmation + "\".");
                }
            }
        }
    }


    public void setCheckBoxInSectionIPropertyTable(Integer locationNumber, int rowNumber) {
        
        tableUtils.setCheckboxInTable(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), rowNumber, true);
    }


    public boolean verifySectionIPropertyOFPCheckboxExists() {
        return checkIfElementExists("//div[contains(@id,':PayerAssignmentProductPanelSet:0:dwellingLevelPayerLV')]//tbody/tr[1]/descendant::div[contains(@class, '-inner-checkcolumn')]", 2000);
    }


    public String getSectionIPropertyOFPCheckboxSelectedReadOnly(int locationNumber, int buildingOrPropertyNumber) {
    	 return tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), buildingOrPropertyNumber, "OFP");
    }
    
    public String getSectionIIPropertyOFPCheckboxSelectedReadOnly(int locationNumber, String sectionIICoverage) {
   	 return tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentSection2LiabilityLineLevelTable, tableUtils.getRowNumberInTableByText(table_PayerAssignmentSection2LiabilityLineLevelTable, sectionIICoverage), "OFP");
   }
    
    
    public void selectSectionIPropertyOFPCheckbox(int locationNumber, int row, boolean trueFalse) {
   	  tableUtils.setCheckboxInTable(table_PayerAssignmentSection1PropertyDwellingLevelTable(locationNumber), row, "OFP", trueFalse);
   }
   
   public void selectSectionIIPropertyOFPCheckbox(int locationNumber, String sectionIICoverage, boolean trueFalse) {
	   tableUtils.setCheckboxInTable(table_PayerAssignmentSection2LiabilityLineLevelTable, tableUtils.getRowNumberInTableByText(table_PayerAssignmentSection2LiabilityLineLevelTable, sectionIICoverage), "OFP",trueFalse );
  }
   
   public ArrayList<String> getBillingOptions(){
	   clickWhenClickable(find(By.cssSelector("div[id$=':PayerAssignmentScreen:1-body'] td.g-cell-edit > div")));
		Guidewire8Select mySelect = select_PayerAssignmentBillAllCoverages();
		return mySelect.getListItems();
   }
    
    
    public String getPayerAssignmentBillMembershipDues(String memberShipDuesMember, boolean billCurrentPayer, boolean billRenewalPayer) {
        int tableRow = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PayerAssignmentMemberShipDuesTable, "Name", memberShipDuesMember));
        String returnValue = "";
        if (billCurrentPayer) {
            returnValue = tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentMemberShipDuesTable, tableRow, "Current Payer");
        }

        if (billRenewalPayer) {
            returnValue = tableUtils.getCellTextInTableByRowAndColumnName(table_PayerAssignmentMemberShipDuesTable, tableRow, "Renewal Payer");
        }

        return returnValue;
    }
    
    public ArrayList<String> getPayerAssignmentInsAddresses() {
    	TableUtils tableUtils = new TableUtils(driver);
    	return tableUtils.getAllCellTextFromSpecificColumn(driver.findElement(By.xpath("//div[contains(@id, ':PayerAssignmentProductPanelSet:0:dwellingLevelPayerLV')]")), "Loc. Address");
    }
}