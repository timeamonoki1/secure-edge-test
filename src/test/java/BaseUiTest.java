import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

public class BaseUiTest {

    protected WebDriver initializeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        chromeOptions.setAcceptInsecureCerts(true);
        return new ChromeDriver(chromeOptions);
    }

    protected Wait<WebDriver> getWebdriverWait(WebDriver driver, long waitTimeoutSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(waitTimeoutSeconds))
                .pollingEvery(Duration.ofMillis(50))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
    }
}
