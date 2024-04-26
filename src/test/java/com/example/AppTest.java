package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.time.Duration;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.crypto.prng.drbg.DualECPoints;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.idealized.Javascript;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class AppTest 
{
    public static Logger log = LogManager.getLogger(AppTest.class);
    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait wait;
    ExtentReports reports;
    ExtentSparkReporter xReporter;
    ExtentTest test;

    @BeforeTest
    public void setting() throws Exception{
        reports = new ExtentReports();
        xReporter = new ExtentSparkReporter("C:\\Users\\Subramaniyam J\\Desktop\\demo\\src\\resources\\ExtentReport.html");
        reports.attachReporter(xReporter);
        PropertyConfigurator.configure("C:\\Users\\Subramaniyam J\\Desktop\\demo\\src\\resources\\log4j.properties");

    }
    @BeforeMethod
    public void setup() throws Exception{
        driver = new ChromeDriver();
        log.info("URL is opening!!");
        driver.get("https://www.barnesandnoble.com/");
        log.info("URL opened Successfully!!");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    public void TestCase1() throws Exception{
        FileInputStream fis = new FileInputStream("D:\\Software Testing\\credentails.xlsx");
        test = reports.createTest("Testcase 1 started...");
        String author = new XSSFWorkbook(fis).getSheet("cc2Sheet").getRow(1).getCell(0).getStringCellValue();
        driver.findElement(By.linkText("All")).click();
        driver.findElement(By.linkText("Books")).click();
        driver.findElement(By.xpath("//*[@id='rhf_header_element']/nav/div/div[3]/form/div/div[2]/div/input[1]")).sendKeys(author);
        driver.findElement(By.xpath("//*[@id='rhf_header_element']/nav/div/div[3]/form/div/span/button")).click();
        boolean msg = driver.getPageSource().contains("Chetan Bhagat");
        if(msg) test.log(Status.PASS, "testcase 1 verfied successfully");
        else test.log(Status.FAIL, "testcase 1 is unsuccessful");
        assertTrue(msg);
    }
    
    @Test
    public void TestCase2() throws Exception{
        test = reports.createTest("Testcase 2 started...");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.linkText("Audiobooks"))).perform();
        driver.findElement(By.linkText("Audiobooks Top 100")).click();
        js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 100)");
        driver.findElement(By.linkText("Funny Story")).click();
        js.executeScript("window.scrollBy(0, 300)");
        driver.findElement(By.xpath("//*[@id='commerce-zone']/div[2]/ul/li[2]/div/div/label/span")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"find-radio-checked\"]/div[1]/form/input[5]"))).click();
        String msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"dialog-title\"]/em/div"))).getText();
        if(msg.equals("Item Successfully Added To Your Cart")){
            test.log(Status.PASS, "testcase 2 verified successfully");
            log.info("testcase 2 is successful");
        }
        else{
            test.log(Status.FAIL, "testcase 2 is unsuccessful");
            log.error("testcase 2 is unsuccessful");
        }
        assertEquals(msg, "Item Successfully Added To Your Cart");
    }

    @Test
    public void TestCase3() throws Exception{
        test = reports.createTest("Testcase3 started...");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='onetrust-accept-btn-handler']"))).click();
        js.executeScript("window.scrollBy(0, 10000)");
        driver.findElement(By.linkText("B&N Membership")).click();
        js.executeScript("window.scrollBy(0, 2000)");
        driver.findElement(By.linkText("JOIN REWARDS")).click();
        String msg = driver.switchTo().frame(driver.findElement(By.xpath("/html/body/div[7]/div/iframe"))).findElement(By.xpath("//*[@id=\"dialog-title\"]")).getText();

        if(msg.equals("Sign in or Create an Account"))
            test.log(Status.PASS, "test case 3 is verified successfully");
        else
            test.log(Status.FAIL, "test case 3 is unsuccessful");

        log.info("testcase 3 execution finished successfully");

        assertEquals(msg, "Sign in or Create an Account");
    }

    @AfterMethod
    public void setdown() throws Exception{
        driver.close();
    }

    @AfterTest
    public void setted(){
        reports.flush();
    }

 } 
