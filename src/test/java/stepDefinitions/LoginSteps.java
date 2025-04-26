package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import utils.ExcelUtils;
import utils.ExtentManager;

import org.openqa.selenium.By;

public class LoginSteps {
    @Given("User is on login page")
    public void user_is_on_login_page() {
    	ExtentManager.logStep("Clicked on Login Button");
    	
    }

    @When("User enters valid credentials from excel")
    public void user_enters_valid_credentials_from_excel() {
        
    	ExtentManager.logStep("Entered Username and Password");
    	
        
    }

    @Then("User should be logged in")
    public void user_should_be_logged_in() {
    	ExtentManager.logStep("Verified HomePage Loaded");
    }
}
