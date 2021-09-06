import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class FrontedTests extends BaseUiTest {
    private static final String LOGIN_URL = "https://preview.keenthemes.com/metronic8/vue/demo1/#/sign-in";
    private static final String PROFILE_URL = "https://preview.keenthemes.com/metronic8/vue/demo1/#/crafted/pages/profile/overview";

    WebDriver driver;

    @BeforeMethod
    public void beforeMethod() {
        driver = initializeDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        driver.quit();
    }

    @Test
    public void testSuccessfulLogin() {
        driver.get(LOGIN_URL);
        LoginPageObject loginPageObject = new LoginPageObject(driver);
        login(loginPageObject, "admin@demo.com", "demo");

        confirmLogin(loginPageObject);
        assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    public void testUnsuccesfulLogin() {
        driver.get(LOGIN_URL);
        LoginPageObject loginPageObject = new LoginPageObject(driver);
        login(loginPageObject, "wrongUsername@email.com", "wrongPassword");
        getWebdriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LoginPageObject.LOGIN_ERROR_MODAL_CSS)));
        getWebdriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LoginPageObject.LOGIN_ERROR_ICON)));
        assertTrue(loginPageObject.getLoginErrorIcon().isDisplayed());
        assertTrue(loginPageObject.getLoginErrorContent().isDisplayed());
        assertEquals(loginPageObject.getLoginErrorContent().getText(), "The login detail is incorrect");
        assertTrue(loginPageObject.getModalConfirmButton().isDisplayed());
        loginPageObject.getModalConfirmButton().click();

        getWebdriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(LoginPageObject.LOGIN_ERROR_MODAL_CSS)));
        assertEquals(driver.getCurrentUrl(), LOGIN_URL);
    }

    @Test
    public void testProfile() {
        driver.get(PROFILE_URL);
        ProfilePageObject pageObject = new ProfilePageObject(driver);
        LoginPageObject loginPageObject = new LoginPageObject(driver);
        login(loginPageObject, "admin@demo.com", "demo");
        confirmLogin(loginPageObject);
        driver.get(PROFILE_URL);
        assertTrue(pageObject.getName().isDisplayed());
        assertEquals(pageObject.getName().getText(), "Max Smith");
        assertEquals(pageObject.getEarningsNumber().getText().trim(), "4,500$");

        List<String> timelineTexts = pageObject.getTimelineItems().stream()
                .filter(item -> CollectionUtils.hasElements(item.findElements(By.cssSelector(".timeline-label"))))
                .filter(item -> CollectionUtils.hasElements(item.findElements(By.cssSelector(".timeline-content"))))
                .map(item -> item.findElement(By.cssSelector(".timeline-label")).getText() + " " + item.findElement(By.cssSelector(".timeline-content")).getText())
                .collect(Collectors.toList());
        assertTrue(timelineTexts.contains("21:03 New order placed #XF-2356."));
        pageObject.getConnectionsTab().click();
        getWebdriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(ProfilePageObject.CONNECTION_CARDS_CSS)));
        Optional<WebElement> oliviaLarson = pageObject.getConnectionCards().stream()
                .filter(item -> CollectionUtils.hasElements(item.findElements(By.cssSelector(".text-hover-primary"))))
                .filter(item -> item.findElements(By.cssSelector(".text-hover-primary")).get(0).getText().contains("Olivia Larson"))
                .findFirst();
        assertTrue(oliviaLarson.isPresent());
        assertTrue(CollectionUtils.hasElements(oliviaLarson.get().findElements(By.cssSelector(".btn-light"))));
        assertFalse(CollectionUtils.hasElements(oliviaLarson.get().findElements(By.cssSelector(".btn-light-primary"))));
    }

    private void login(LoginPageObject loginPageObject, String username, String password) {
        assertTrue(loginPageObject.getLoginForm().isDisplayed(), "Login form is not displayed!");
        assertTrue(loginPageObject.getEmailInput().isDisplayed(), "Email input is not displayed!");
        assertTrue(loginPageObject.getPasswordInput().isDisplayed(), "Password input is not displayed!");
        loginPageObject.getEmailInput().sendKeys(username);
        loginPageObject.getPasswordInput().sendKeys(password);
        loginPageObject.getContinueButton().submit();
    }

    private void confirmLogin(LoginPageObject loginPageObject) {
        getWebdriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LoginPageObject.SUCCESS_OVERLAY_CSS)));
        assertTrue(loginPageObject.getModalConfirmButton().isDisplayed());
        assertTrue(loginPageObject.getSuccessContent().isDisplayed());
        assertTrue(loginPageObject.getSuccessRing().isDisplayed());
        assertEquals(loginPageObject.getSuccessContent().getText(), "All is cool! Now you submit this form");
        assertTrue(loginPageObject.getModalConfirmButton().isDisplayed());

        loginPageObject.getModalConfirmButton().click();
        getWebdriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(LoginPageObject.SUCCESS_OVERLAY_CSS)));
    }
}
