package org.example.pages.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/** Элементы общие для всех страниц */
public abstract class AbstractPage {

    protected static WebDriver driver;
    @Step("Установка драйвера {webDriver} в классе AbstractPage")
    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }
}
