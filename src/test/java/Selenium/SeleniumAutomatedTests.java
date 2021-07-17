package Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SeleniumAutomatedTests
{
    private WebDriver driver;
    public TakesScreenshot takesScreenshot;

    @Before
    public void setUp()
    {
        WebDriverManager.chromedriver().setup();
        ChromeOptions capabilities = new ChromeOptions();
        capabilities.setCapability("takesScreenshot", true);
//        System.setProperty("webdriver.chrome.driver", "/usr/bin/google-chrome"); // EC2 Instance
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver/chromedriver.exe"); // Locally

        driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();
//        driver.get("http://localhost:8080"); // From IntelliJ
//        driver.get("http://localhost:8080/poc-automation/"); // From Local Tomcat Server
        driver.get("http://18.118.252.234:8080/poc-automation/"); //From AWS Tomcat Server
        takesScreenshot = (TakesScreenshot) driver;
    }

    @Test
    public void SaveStudy() throws AWTException, InterruptedException
    {
        // ARRANGE & WAIT
        WebElement addStudy = driver.findElement(By.name("addStudy"));
        addStudy.click();
        Thread.sleep(1000);

        WebElement studyName = driver.findElement(By.name("studyName"));
        studyName.sendKeys("Optimal");
        Thread.sleep(1000);

        WebElement ID = driver.findElement(By.name("ID"));
        ID.sendKeys("IVX-12");
        Thread.sleep(1000);

        WebElement cdiscStandard = driver.findElement(By.name("cdisc"));
        cdiscStandard.sendKeys("SDTM");
        Thread.sleep(1000);

        WebElement save = driver.findElement(By.name("save"));
        Actions actions = new Actions(driver);
        Robot robot = new Robot();
        robot.mouseMove(50,50);
        actions.click().build().perform();
        robot.mouseMove(200,70);
        actions.click().build().perform();
        save.click();
        Thread.sleep(2000);

        // ASSERT
        Assert.assertTrue(driver.getPageSource().contains("Optimal"));
        Assert.assertTrue(driver.getPageSource().contains("IVX-12"));
        Assert.assertTrue(driver.getPageSource().contains("SDTM"));
    }

    @Test
    public void FilterField() throws IOException, InterruptedException
    {
        WebElement filter = driver.findElement(By.name("filter"));
        filter.sendKeys("Optimal");
        Thread.sleep(5000);

        File screenShot = takesScreenshot.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenShot, new File("./Screenshots/FilterTestSave.png"));
    }

    @Test
    public void DeleteStudy() throws InterruptedException
    {
        WebElement selectStudy = driver.findElement(By.xpath("//vaadin-grid-cell-content[1]"));
        selectStudy.click();
        Thread.sleep(2000);

        WebElement delete = driver.findElement(By.name("delete"));
        delete.click();
        Thread.sleep(2000);

        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }
}
