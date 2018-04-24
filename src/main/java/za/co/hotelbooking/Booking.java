/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.hotelbooking;


import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import za.co.hotelbooking.util.HotelBookingUtilities;


public class Booking {
    
    WebDriver driver;

    public Booking(WebDriver driver) {

        this.driver = driver;
    }

    By firstname = By.id("firstname");
    By lastname = By.id("lastname");
    By totalprice = By.id("totalprice");
    By depositpaid = By.id("depositpaid");
    By checkin = By.id("checkin");
    By checkout = By.id("checkout");
    By saveButton = By.xpath("//input[@value=' Save ']");

    By allBookingsLocator = By.id("bookings");
    By allRowsLocator = By.cssSelector("div[class='row']");
    By firstNameInRowLocator = By.cssSelector("div[class='row'] div:nth-child(1)");
    By lastNameInRowLocator = By.cssSelector("div[class='row'] div:nth-child(2)");
    By totalPriceInRowLocator = By.cssSelector("div[class='row'] div:nth-child(3)");
    By depositPaidInRowLocator = By.cssSelector("div[class='row'] div:nth-child(4)");
    By checkInRowLocator = By.cssSelector("div[class='row'] div:nth-child(5)");
    By checkOutRowLocator = By.cssSelector("div[class='row'] div:nth-child(6)");

    public String populateBookings(Sheet bookingScenarioData, int rowno) throws InterruptedException, IOException {

        String inLastname;
        String inTotalPrice;
        String inDepositPaid;
        String inCheckIn;
        String inCheckOut;

        String firstNameRandomlyGenerated;

        Row row = bookingScenarioData.getRow(rowno);

        int waitTime = 60;
        int numDigitsInFirstName = 10;

        inLastname = row.getCell(1).getStringCellValue();
        inTotalPrice = row.getCell(2).getStringCellValue();
        inDepositPaid = row.getCell(3).getStringCellValue();
        inCheckIn = row.getCell(4).getStringCellValue();
        inCheckOut = row.getCell(5).getStringCellValue();

        firstNameRandomlyGenerated = HotelBookingUtilities.randomAlphaNumeric(numDigitsInFirstName);
        System.out.println("firstNameRandomlyGenerated=" + firstNameRandomlyGenerated);
        driver.findElement(firstname).sendKeys(firstNameRandomlyGenerated);
        driver.findElement(lastname).sendKeys(inLastname);
        driver.findElement(totalprice).sendKeys(inTotalPrice);

        WebElement depositDropdown = driver.findElement(depositpaid);
        Select depositDropdownSelect = new Select(depositDropdown);
        depositDropdownSelect.selectByVisibleText(inDepositPaid.toLowerCase());

        driver.findElement(checkin).sendKeys(inCheckIn);
        driver.findElement(checkout).sendKeys(inCheckOut);

        System.out.println("Before Value of name is : *" + driver.findElement(firstname).getAttribute("value"));

        driver.findElement(saveButton).click();

        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        wait.until(ExpectedConditions.attributeToBe(firstname, "value", ""));

        System.out.println("After1 Value of name is : *" + driver.findElement(firstname).getAttribute("value"));

        WebDriverWait wait2 = new WebDriverWait(driver, waitTime);

        WebElement bookings = driver.findElement(By.id("bookings"));
        wait2.until(ExpectedConditions.textToBePresentInElement(bookings, firstNameRandomlyGenerated));
        System.out.println("/nbookings = *" + bookings);

        System.out.println("After2 Value of name is : *" + driver.findElement(firstname).getAttribute("value"));

        WebElement allBookings = driver.findElement(allBookingsLocator);

        int rowcount = allBookings.findElements(allRowsLocator).size();
        System.out.println("rowcount bookings = " + rowcount);

        for (int j = 1; j < rowcount; j++) {
            String firstName = allBookings.findElements(firstNameInRowLocator).get(j).getText();

            System.out.println("firstName = " + firstName);
            if (firstName.equals(firstNameRandomlyGenerated)) {
                String lastName = allBookings.findElements(lastNameInRowLocator).get(j).getText();
                String price = allBookings.findElements(totalPriceInRowLocator).get(j).getText();
                String deposit = allBookings.findElements(depositPaidInRowLocator).get(j).getText();
                String checkin = allBookings.findElements(checkInRowLocator).get(j).getText();
                String checkout = allBookings.findElements(checkOutRowLocator).get(j).getText();
               // Assert.assertEquals(inLastname, lastName);
             //   Assert.assertEquals(inTotalPrice, price);
                //System.out.println("deposit=" + deposit);
              //  Assert.assertEquals(inDepositPaid.toLowerCase(), deposit.toLowerCase());
              //  Assert.assertEquals(inCheckIn, checkin);
              //  Assert.assertEquals(inCheckOut, checkout);
                break;
            }
        }

            return firstNameRandomlyGenerated;
        }

    public static void deleteBookingAndAssert(WebDriver driver, String firstNameRandom) throws InterruptedException {

        By allBookingsLocator  = By.id("bookings");
        By allRowsLocator = By.cssSelector("div[class='row']");
        By firstnameInRowLocator = By.cssSelector("div[class='row'] div:nth-child(1)");

        By deleteButton = By.cssSelector("div[class='row'] div:nth-child(7)");

        WebElement allBookings = driver.findElement(allBookingsLocator);

        int rowcount = allBookings.findElements(allRowsLocator).size();
        //System.out.println("rowcount bookings = " + rowcount);

        for (int j=1;j<rowcount;j++) {
            String firstName = allBookings.findElements(firstnameInRowLocator).get(j).getText();
            //System.out.println("firstName = " + firstName);
            if (firstName.equals(firstNameRandom)){
                allBookings.findElements(deleteButton).get(j).click();
                break;
            }
        }

        int rowcountAfterDelete = allBookings.findElements(allRowsLocator).size();

        // There is a delay between clicking the delete button and the screen refresh
        // This section will compare the n umber of rows at 500 ms interval for at m ost 1 minute
        // As soon as the number of rows has decreased, then the delete has been done and the screen refreshed
        int counter = 0;
        while ((rowcount == rowcountAfterDelete) && (counter < 120)) {
            //System.out.println("rowcountAfterDelete: " + rowcountAfterDelete);
            counter++;
            Thread.sleep(500);
            rowcountAfterDelete = allBookings.findElements(allRowsLocator).size();
        }

        // Now confirm that the deleted row is not still on the screen
        // by checking for tyhe unique first name
        for (int j=1;j<rowcountAfterDelete;j++) {
            String firstName = allBookings.findElements(firstnameInRowLocator).get(j).getText();
            //System.out.println("firstName = " + firstName);
          //  Assert.assertFalse(firstName.equals(firstNameRandom));
            }
    }
}