package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.time.Duration;

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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class AppTest 
{
    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() throws Exception{
        driver = new ChromeDriver();
        driver.get("https://www.barnesandnoble.com/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    public void TestCase1() throws Exception{
        FileInputStream fis = new FileInputStream("D:\\Software Testing\\credentails.xlsx");
        String author = new XSSFWorkbook(fis).getSheet("cc2Sheet").getRow(1).getCell(0).getStringCellValue();
        driver.findElement(By.linkText("All")).click();
        driver.findElement(By.linkText("Books")).click();
        driver.findElement(By.xpath("//*[@id='rhf_header_element']/nav/div/div[3]/form/div/div[2]/div/input[1]")).sendKeys(author);
        driver.findElement(By.xpath("//*[@id='rhf_header_element']/nav/div/div[3]/form/div/span/button")).click();
        assertTrue(driver.getPageSource().contains("Chetan Bhagat"));
    }

    @Test
    public void TestCase2() throws Exception{
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.linkText("Audiobooks"))).perform();
        driver.findElement(By.linkText("Audiobooks Top 100")).click();
        js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 100)");
        driver.findElement(By.xpath("//*[@id=\'addToBagForm_2940159543998\']/input[11]")).click();
        Thread.sleep(8000);
        String msg = driver.switchTo().activeElement().findElement(By.xpath("//*[@id=\"add-to-bag-main\"]/div[1]")).getText();

        assertEquals(msg, "Item Successfully Added To Your Cart");  

    }

    @Test
    public void TestCase3() throws Exception{
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='onetrust-accept-btn-handler']"))).click();
        js.executeScript("window.scrollBy(0, 10000)");
        driver.findElement(By.linkText("B&N Membership")).click();
        js.executeScript("window.scrollBy(0, 2000)");
        driver.findElement(By.linkText("JOIN REWARDS")).click();
        driver.switchTo().frame(By.xpath("/html"));
        assertEquals(msg, "Sign in or Create an Account");
    }

    @AfterMethod
    public void setdown() throws Exception{
        driver.close();
    }
 }
