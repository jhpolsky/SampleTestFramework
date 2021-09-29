package com.example.demo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

// Ideally, these test running against an internal VM using network authentication, so there is no need
// for code to login and waste time during test creation/teardown

public class AddNewMoviePageTest {
    private WebDriver driver;
    private AddNewMoviePage AddNewMoviePage;

    // Ideally, the test data are parametrized, but I hard-coded here for readability and simplicity's sake
    private String title = "SuccessTest";
    private String releaseDate = "01/23/2021";
    private String rating = "7.5";
    private String baseUrl = "http://localhost/AddNewMoviePage.html";
    private String[] goodEntry = new String[]{title, releaseDate, rating};
    private String[] badEntry = new String[]{"", releaseDate, rating};

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(baseUrl);

        // Assumes that object models and appropriate methods exist for the list view and login pages
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
    // Test that a user can successfully enter a new movie
    public void addNewMovieSuccess() {
        AddNewMoviePage.movieTitleInput.sendKeys(title);
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        // Assuming that the application provides a success popup
        WebElement successPopup = driver.findElement(By.xpath("//*[@id=\"successMessage\"]"));
        assertTrue(successPopup.isDisplayed());
    }

    @Test
    public void addNewMovieUiEntryExists() {
        AddNewMoviePage.movieTitleInput.sendKeys(title);
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        // Assuming there is a class and method ListViewPage.RowExists() already created to verify existence of a row
        assertTrue(ListViewPage.RowExists(goodEntry));
    }

    @Test
    public void addNewMovieDbEntryExists() {
        AddNewMoviePage.movieTitleInput.sendKeys(title);
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        //  Assuming that there exists a class & method Db.getKey() to query the database
        assertNotNull(Db.getKey(goodEntry));
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
    public void addNewMovieUiFailedEntryDoesNotExist() {
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        assertFalse(ListViewPage.RowExists(badEntry));
    }

    @Test
    public void addNewMovieDbFailedEntryDoesNotExist() {
        AddNewMoviePage.releaseDateInput.sendKeys(releaseDate);
        AddNewMoviePage.ratingInput.sendKeys(rating);
        AddNewMoviePage.submitButton.click();

        assertNull(Db.getKey(badEntry));
    }
}