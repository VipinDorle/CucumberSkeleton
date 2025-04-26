Feature: Login Functionality

  Scenario: Successful Login with Valid Credentials
    Given User is on login page
    When User enters valid credentials from excel
    Then User should be logged in
