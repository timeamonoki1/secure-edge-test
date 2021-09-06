import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPageObject {
    public static final String SUCCESS_OVERLAY_CSS = ".swal2-popup.swal2-modal.swal2-icon-success.swal2-show";
    public static final String LOGIN_ERROR_MODAL_CSS = ".swal2-container .swal2-icon-error";
    public static final String LOGIN_ERROR_ICON = ".swal2-container .swal2-icon-error .swal2-x-mark";

    @FindBy(css = "#kt_login_signin_form")
    private WebElement loginForm;

    @FindBy(css = ".form-control.form-control-lg[name='email']")
    private WebElement emailInput;

    @FindBy(css = ".form-control.form-control-lg[name='password']")
    private WebElement passwordInput;

    @FindBy(css = "#kt_sign_in_submit")
    private WebElement continueButton;

    @FindBy(css = SUCCESS_OVERLAY_CSS)
    private WebElement successLoginOverlay;

    @FindBy(css = ".swal2-actions button.swal2-confirm")
    private WebElement modalConfirmButton;

    @FindBy(css = ".swal2-content")
    private WebElement successContent;

    @FindBy(css = ".swal2-success-ring")
    private WebElement successRing;

    @FindBy(css = ".fv-help-block span[role='alert']")
    private WebElement invalidUsernameAlert;

    @FindBy(css = LOGIN_ERROR_MODAL_CSS)
    private WebElement loginErrorModal;

    @FindBy(css = ".swal2-container .swal2-icon-error .swal2-x-mark")
    private WebElement loginErrorIcon;

    @FindBy(css = ".swal2-container .swal2-icon-error #swal2-content")
    private WebElement loginErrorContent;

    public LoginPageObject(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getLoginForm() {
        return loginForm;
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public WebElement getContinueButton() {
        return continueButton;
    }

    public WebElement getSuccessLoginOverlay() {
        return successLoginOverlay;
    }

    public WebElement getModalConfirmButton() {
        return modalConfirmButton;
    }

    public WebElement getSuccessContent() {
        return successContent;
    }

    public WebElement getSuccessRing() {
        return successRing;
    }

    public WebElement getInvalidUsernameAlert() {
        return invalidUsernameAlert;
    }

    public WebElement getLoginErrorModal() {
        return loginErrorModal;
    }

    public WebElement getLoginErrorIcon() {
        return loginErrorIcon;
    }

    public WebElement getLoginErrorContent() {
        return loginErrorContent;
    }
}
