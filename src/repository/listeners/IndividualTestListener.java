package repository.listeners;

import com.idfbins.driver.BaseTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;

import java.io.*;
import java.util.Date;

public class IndividualTestListener implements ITestListener, ISuiteListener, IInvokedMethodListener {

	private String filePath = "C:\\tmp\\";
	private File tmpFolder, dateFolder, suitePackageFolder, classFolder, methodFolder;
	private File logSourceFile = new File("C:/tmp/temporary_log_file.txt");
	private String shortDate = "";
	private PrintStream consoleOut = System.out;
	private String suitePackageName = "";

	private void getPCFInformation(WebDriver driver) throws IOException {
		if (driver.getCurrentUrl().toUpperCase().contains("UAT")) {
			// Do Nothing
		} else {
			String winHandleBefore = driver.getWindowHandle();
			new GuidewireHelpers(driver).pressAltShiftI();

			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}

			createPCFPageHTMLFile(driver);

			driver.close();
			driver.switchTo().window(winHandleBefore);
		}
	}

	private void checkTMPFolder() {
		tmpFolder = new File(filePath);
		tmpFolder.mkdir();
	}

	private void checkFolderStructure(ITestResult arg0) {
		String testMethodNameForFolder = getTestClassName(arg0.getName()).trim();
		String testClassNameForFolder = arg0.getInstance().getClass().getName();

		String dateForFolder = DateUtils.dateFormatAsString("yyyy-MM-dd", new Date());

		dateFolder = new File(filePath + "\\" + dateForFolder);
		suitePackageFolder = new File(dateFolder + "\\" + suitePackageName);
		classFolder = new File(suitePackageFolder + "\\" + testClassNameForFolder);
		methodFolder = new File(classFolder + "\\" + testMethodNameForFolder);

		dateFolder.mkdir();
		classFolder.mkdir();
		methodFolder.mkdir();
	}

	private void copyLogFromTemp() throws IOException {
		String DestinationFile = (methodFolder + "\\" + shortDate + "_" + "console-output.txt");
		File logDestinationFile = new File(DestinationFile);
		FileUtils.copyFile(logSourceFile, logDestinationFile);
	}

	private void createPCFPageHTMLFile(WebDriver driver) throws IOException {
		try {
			FileWriter fstream = new FileWriter(methodFolder + "\\" + shortDate + "_" + "pcf-page.html");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(driver.getPageSource());
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void takeScreenShot(WebDriver driver) throws IOException {
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File DestinationFile = new File(methodFolder + "\\" + shortDate + "_screenshot.jpg");
		FileUtils.copyFile(screenshotFile, DestinationFile);
	}

	private String getTestClassName(String testName) {
		String[] reqTestClassname = testName.split("\\.");
		int i = reqTestClassname.length - 1;
		return reqTestClassname[i];
	}

	private String returnMethodName(ITestNGMethod method) {
		return method.getRealClass().getSimpleName() + "." + method.getMethodName();
	}

	private String getStacktraceFromThrowable(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

	
	public void onStart(ISuite arg0) {
		checkTMPFolder();
		if (arg0.getName().equals("Default suite")) {
			suitePackageName = "local-eclipse";
		} else {
			suitePackageName = arg0.getName();
		}

	}

	
	public void onTestStart(ITestResult arg0) {
		try {
			if (logSourceFile.exists()) {
				if (logSourceFile.delete()) {
				}
			}
			logSourceFile.createNewFile();
			TeeOutputStream teeOut = new TeeOutputStream(consoleOut, new PrintStream(logSourceFile));
			System.setOut(new PrintStream(teeOut));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("@@@@@@@@@@@@@@@@@@ ||||| TEST METHOD " + returnMethodName(arg0.getMethod())
				+ " STARTED @@@@@@@@@@@@@@@@@@");
		System.out.println("@@@@@@@@@@@@@@@@@@ vvvvv");
	}

	
	public void onTestSuccess(ITestResult arg0) {
		System.out.println("@@@@@@@@@@@@@@@@@@ ^^^^^");
		System.out.println("@@@@@@@@@@@@@@@@@@ ||||| TEST METHOD " + returnMethodName(arg0.getMethod())
				+ " PASSED @@@@@@@@@@@@@@@@@@");
		System.out.println("------------------------");
		System.out.println("------------------------");
	}

	
	public void onTestFailure(ITestResult arg0) {
		Object currentClass = arg0.getInstance();
        WebDriver driver = ((BaseTest) currentClass).getBaseTestDriverForListener();
		System.out.println(arg0.getThrowable());
		System.out.println(getStacktraceFromThrowable(arg0.getThrowable()));
		System.out.println("@@@@@@@@@@@@@@@@@@ ^^^^^");
		System.out.println("@@@@@@@@@@@@@@@@@@ ||||| TEST METHOD " + returnMethodName(arg0.getMethod())
				+ " FAILED @@@@@@@@@@@@@@@@@@");
		System.out.println("------------------------");
		System.out.println("------------------------");

		checkFolderStructure(arg0);

		try {
			shortDate = DateUtils.dateFormatAsString("HHmm-ssSS", new Date());
			getPCFInformation(driver);
			takeScreenShot(driver);
			copyLogFromTemp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void onTestSkipped(ITestResult arg0) {
		System.out.println("@@@@@@@@@@@@@@@@@@ ^^^^^");
		System.out.println("@@@@@@@@@@@@@@@@@@ ||||| TEST METHOD " + returnMethodName(arg0.getMethod())
				+ " SKIPPED @@@@@@@@@@@@@@@@@@");
		System.out.println("------------------------");
		System.out.println("------------------------");
	}

	
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub

	}

	
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub

	}

	
	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub

	}

	
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}

}