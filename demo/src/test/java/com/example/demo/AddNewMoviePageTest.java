package com.example.demo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

// Ideally, these tests are running against an internal VM using network authentication, so there is no need
// for code to login and waste time during test creation/teardown

public class AddNewMoviePageTest {
    private WebDriver driver;
    private AddNewMoviePage AddNewMoviePage;

    // Ideally, the test data are parametrized, but I hard-coded here for readability and simplicity's sake
    private String title1 = "SuccessTest1";
    private String title2 = "SuccessTest2";
    private String title3 = "SuccessTest3";
    private String releaseDate = "01/23/2021";
    private String rating = "7.5";
    private String baseUrl = "http://localhost/AddNewMoviePage.html";
    private String[] goodEntry2 = new String[]{title2, releaseDate, rating};
    private String[] goodEntry3 = new String[]{title3, releaseDate, rating};
    private String[] badEntry = new String[]{"", releaseDate, rating};

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(baseUrl);

        // Assumes that object models and appropriate methods exist for the login and listview pages
        // Also assumes an Authorization class with login/logout methods
        AddNewLoginPage = new LoginPage(driver);
        Authorization.login();

        AddNewMoviePage = new AddNewMoviePage(driver);
        ListViewPage = new ListViewPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        LoginPage.logout();
    }

    @Test
    // Test that a user can successfully enter a new movie and see success popup
    public void addNewMovieSuccess() {
        AddNewMoviePage.movieTitleInput.sendKeys(title1);
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        // Assuming that the application provides a success popup
        WebElement successPopup = driver.findElement(By.xpath("//*[@id=\"successMessage\"]"));
        assertTrue(successPopup.isDisplayed());
    }

    @Test
    // Test that a user can successfully enter a new movie and see it on the list page
    public void addNewMovieUiEntryExists() {
        AddNewMoviePage.movieTitleInput.sendKeys(title2);
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        // Assuming there is a class and method ListViewPage.RowExists() already created to verify existence of a row
        assertTrue(ListViewPage.RowExists(goodEntry2));
    }

    @Test
    // Test that a user can successfully enter a new movie
    public void addNewMovieDbEntryExists() {
        AddNewMoviePage.movieTitleInput.sendKeys(title3);
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        //  Assuming that there exists a class & method Db.getKey() to query the database
        assertNotNull(Db.getKey(goodEntry3));
    }

    @Test
    // Test that an entry is not saved (assumes that the page is designed such that the UI requires the title field)
    public void addNewMovieFailure() {
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        // Assuming that the application provides a failure popup
        WebElement failurePopup = driver.findElement(By.xpath("//*[@message=\"failureMessage\"]"));
        assertTrue(failurePopup.isDisplayed());
    }

    @Test
    // Test that a failed entry is not present on the list page
    public void addNewMovieUiFailedEntryDoesNotExist() {
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        assertFalse(ListViewPage.RowExists(badEntry));
    }

    @Test
    // Test that a failed entry is not persisted to the db
    public void addNewMovieDbFailedEntryDoesNotExist() {
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        assertNull(Db.getKey(badEntry));
    }
}
