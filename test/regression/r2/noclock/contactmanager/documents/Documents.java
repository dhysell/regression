package regression.r2.noclock.contactmanager.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.NewDocumentPC;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

@QuarantineClass
public class Documents extends BaseTest {
	private WebDriver driver;
    private AbUsers abUser;
    private GenerateContact myContactObj;
    private String documentDirectoryPath = "\\\\fbmsqa11\\testing_data\\test-documents";
    private ArrayList<String> documentsToUpload = new ArrayList<String>();

    @BeforeMethod(alwaysRun = true)
    public void letsTest() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
    }


    /**
     * @Author sbroderick
     * @Requirement ContactManager now has the functionality for Document Uploads.
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description ContactManager now has the functionality for Document Uploads.
     * @DATE Sep 6, 2016
     */


    public void makeContact() throws Exception {

        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");


        this.myContactObj = new GenerateContact.Builder(driver)
                .withCreator(abUser)
                .withCompanyName("Documents")
                .withGenerateAccountNumber(true)
                .build(GenerateContactType.Company);

        System.out.println(this.myContactObj.accountNumber);
        new GuidewireHelpers(driver).logout();
        new Login(driver).login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchByAccountNumber(myContactObj.accountNumber);
    }

    @Test
    public void uploadDocument() throws Exception {
        
        makeContact();
        
        this.documentsToUpload.add("\\txt_test.txt");
/*		this.documentsToUpload.add("\\BitmapDoc.bmp");
        this.documentsToUpload.add("\\CSVDoc.csv");
        this.documentsToUpload.add("\\doc_test.doc");
        this.documentsToUpload.add("\\Excel Document.xlsx");
        this.documentsToUpload.add("\\GIFDoc.gif");
        this.documentsToUpload.add("\\HTMLDoc.html");
        this.documentsToUpload.add("\\JPG Document.jpg");
        this.documentsToUpload.add("\\MPEGVideo.mpg");
        this.documentsToUpload.add("\\PDF Document.pdf");
        this.documentsToUpload.add("\\PNG Document.png");
//		this.documentsToUpload.add("\\PostScript.ps");
        this.documentsToUpload.add("\\PPT.pptx");
        this.documentsToUpload.add("\\Rich text format.rtf");
        this.documentsToUpload.add("\\TIFFDoc.tif");
        this.documentsToUpload.add("\\Wave Audio.wav");
        this.documentsToUpload.add("\\Word Document.docx");
		this.documentsToUpload.add("\\xml_test.xml");

        int dayOfWeek = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.ContactManager, "u");
        ContactRole todayRole;

        switch (dayOfWeek) {
            case 1:
/*            case 5:
                todayRole = ContactRole.Agent;
                getRandomAgent();
                break;
            case 4:
            case 2:
                todayRole = ContactRole.Lienholder;
                makeContact();
                break;
            case 3:
            case 6:
                todayRole = ContactRole.Vendor;
                getVendor();
                break;
*//*       case 4:  todayRole = ContactRole.ClaimParty;
        		  getClaimParty();
                  break;

            default:
                todayRole = ContactRole.Client;
                getLienholder();
                break;
        }

*/
        Date today = DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager);
        SidebarAB sideMenu = new SidebarAB(driver);
        NewDocumentPC doc = new NewDocumentPC(driver);
        //if Account Number or Lienholder Number does not exist then find another


        for (String document : documentsToUpload) {
            sideMenu = new SidebarAB(driver);
            sideMenu.initiateAttachedDocument();
            doc = new NewDocumentPC(driver);
            doc.setAttachment(documentDirectoryPath, document);
            doc.selectRelatedTo(ContactRole.Client.getValue());
            doc.selectDocumentType(DocumentType.Other);
            doc.clickUpdate();
        }

        sideMenu = new SidebarAB(driver);
        sideMenu.clickSideBarDocuments();

        repository.ab.documents.Documents docsPage = new repository.ab.documents.Documents(driver);
        docsPage.clickSearch();
        for (String document : documentsToUpload) {
            System.out.println(documentDisplay(document));
            System.out.println(DateUtils.dateFormatAsString("MM/dd/yyyy", today));
            if (!docsPage.verifyDocumentByNameTypeAuthorDate(documentDisplay(document), DocumentType.Other, this.abUser.getUserFirstName() + " " + this.abUser.getUserLastName(), today)) {
                Assert.fail(documentDisplay(document) + " was added to the contact. Please ensure that this can be seen on the documents page.");
            }
            if (document.contains(".txt") || document.contains(".gif") || document.contains("\\HTMLDoc.html") || document.contains("\\JPG Document.jpg") || document.contains("\\PDF Document.pdf") || document.contains("\\PNG Document.png")) {
                String mainWindowHandle = docsPage.clickViewDocument(documentDisplay(document), DocumentType.Other, this.abUser.getUserFirstName() + " " + this.abUser.getUserLastName(), today, 0);
                boolean error = docsPage.isErrorTextPresent();
                String url = driver.getCurrentUrl();
                docsPage.closeDocumentsWebpage(mainWindowHandle);
                System.out.println("The Document is stored at: " + url + ".");
                if (error) {
                    Assert.fail(driver.getCurrentUrl() + "The document failed to make the round trip. The URL that Chris needs is" + url + ".");
                }
            }
        }
    }
	
	/* To view documents in UAT, make sure the Message Broker script parameter is set for UAT.
	 * DEV, Prod and UAT are the only brokers that are defaulted.
	 *  Script Parameters Name		Type				Value
	 * 		BrokerURLTest   		java.lang.String    mbuat.idfbins.com:7080

		After Resets this will need to be changed.
	 * */


    public void getRandomAgent() throws Exception {
        Agents agent = AgentsHelper.getRandomAgent();
        new Login(driver).login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchAgentContact(agent);

    }

    public void getLienholder() throws Exception {
        new Login(driver).login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.getRandomLienholderByName(ContactSubType.Contact, "Credit Union");
    }

    public void getClaimParty() throws Exception {
        new Login(driver).login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.getRandomClaimParty("Smi");
    }

    public void getVendor() throws Exception {
        new Login(driver).login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.getRandomVendor("Auto");
    }

    public String documentDisplay(String doc) {
        int indexOfPeriod = doc.lastIndexOf(".");
        return doc.substring(1, indexOfPeriod);
    }
}
