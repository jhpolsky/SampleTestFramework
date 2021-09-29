package com.example.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddNewMoviePage {
    @FindBy(xpath = "//*[@id=\"movieTitle\"]")
    public WebElement movieTitleInput;

    @FindBy(xpath = "//*[@id=\"releaseDate\"]")
    public WebElement releaseDateInput;

    @FindBy(xpath = "//*[@id=\"rating\"]")
    public WebElement ratingInput;

    @FindBy(xpath = "/html/body/form/input[4]")
    public WebElement submitButton;

    public AddNewMoviePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
