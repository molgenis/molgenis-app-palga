package org.molgenis.selenium;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class VrouwCytoTestCase
{
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeTest
	public void setUp() throws Exception
	{
		driver = new FirefoxDriver();
		baseUrl = "http://molgenis68.target.rug.nl/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testVrouwCyto() throws Exception
	{
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("Zoekvraag PALGA Openbare Databank")).click();
		for (int second = 0;; second++)
		{
			if (second >= 60) fail("timeout");
			try
			{
				if (driver.findElement(By.linkText("Zoeken")).isDisplayed()) break;
			}
			catch (Exception e)
			{
			}
			Thread.sleep(1000);
		}

		driver.findElement(By.linkText("Zoeken")).click();
		for (int second = 0;; second++)
		{
			if (second >= 60) fail("timeout");
			try
			{
				if (driver.findElement(By.xpath("//div[@id='aggregate-table-container']/table/tbody/tr[2]/td[2]/div"))
						.isDisplayed()) break;
			}
			catch (Exception e)
			{
			}
			Thread.sleep(1000);
		}

		assertEquals("3235348",
				driver.findElement(By.xpath("//div[@id='aggregate-table-container']/table/tbody/tr[2]/td[2]/div"))
						.getText());
	}

	@AfterTest
	public void tearDown() throws Exception
	{
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString))
		{
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by)
	{
		try
		{
			driver.findElement(by);
			return true;
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}

	private boolean isAlertPresent()
	{
		try
		{
			driver.switchTo().alert();
			return true;
		}
		catch (NoAlertPresentException e)
		{
			return false;
		}
	}

	private String closeAlertAndGetItsText()
	{
		try
		{
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert)
			{
				alert.accept();
			}
			else
			{
				alert.dismiss();
			}
			return alertText;
		}
		finally
		{
			acceptNextAlert = true;
		}
	}
}
