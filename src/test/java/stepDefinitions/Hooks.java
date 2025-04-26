package stepDefinitions;




import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.*;
import utils.DriverFactory;
import utils.ExtentManager;
import utils.ScreenshotUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class Hooks {

    private static ExtentReports extent = ExtentManager.getExtentReports();
    private static ExtentTest test;
    private WindowsDriver driver;

    @Before
    public void setup(Scenario scenario) {
        ExtentManager.createTest(scenario.getName());
    }


    @AfterStep
    public void addScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ScreenshotUtils.captureScreenAsBytes();
            if (screenshot != null) {
                scenario.attach(screenshot, "image/png", "Failed Step Screenshot");

                // For Extent Report
                test.fail("Step failed").addScreenCaptureFromBase64String(
                        ScreenshotUtils.captureScreenAsBase64()
                );
            }
        }
    }

    @After
    public void tearDown() {
        ExtentManager.getExtentReports().flush();
    }

}
