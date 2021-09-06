import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProfilePageObject {
    public static final String CONNECTION_CARDS_CSS = ".col-md-6 .card";

    @FindBy(css = "#kt_content_container > .card a.me-1")
    private WebElement name;

    @FindBy(css = "#kt_content_container > .card .d-flex.flex-wrap.flex-stack .fw-bolder")
    private WebElement earningsNumber;

    @FindBy(css = ".timeline-item")
    private List<WebElement> timelineItems;

    @FindBy(css = ".nav-item a[href*='connections']")
    private WebElement connectionsTab;

    @FindBy(css = CONNECTION_CARDS_CSS)
    private List<WebElement> connectionCards;

    public ProfilePageObject(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getName() {
        return name;
    }

    public WebElement getEarningsNumber() {
        return earningsNumber;
    }

    public List<WebElement> getTimelineItems() {
        return timelineItems;
    }

    public WebElement getConnectionsTab() {
        return connectionsTab;
    }

    public List<WebElement> getConnectionCards() {
        return connectionCards;
    }
}
