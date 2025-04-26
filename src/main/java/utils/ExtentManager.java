package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final String SCREENSHOT_FOLDER_PATH = System.getProperty("user.dir") + "/test-output/screenshots/";

    public static ExtentReports getExtentReports() {
        if (extent == null) {
            createScreenshotFolder(); // Create folder automatically

            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
            htmlReporter.config().setDocumentTitle("Automation Report");
            htmlReporter.config().setReportName("Desktop App Automation");
            htmlReporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Tester", "Vipin Dorle");
        }
        return extent;
    }

    public static void createTest(String testName) {
        ExtentTest extentTest = getExtentReports().createTest(testName);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static String captureScreenshot() {
        try {
            Robot robot = new Robot();
            String filePath = SCREENSHOT_FOLDER_PATH + UUID.randomUUID() + ".png";
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            File screenshotFile = new File(filePath);
            ImageIO.write(screenFullImage, "png", screenshotFile);
            return filePath;
        } catch (AWTException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void logStep(String stepDescription) {
        String screenshotPath = captureScreenshot();
        getTest().info(stepDescription, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }

    private static void createScreenshotFolder() {
        File screenshotFolder = new File(SCREENSHOT_FOLDER_PATH);
        if (!screenshotFolder.exists()) {
            screenshotFolder.mkdirs(); // Automatically create folder if not exists
        }
    }
}
