
@postoperation
Feature: POST Operation - Create User Scenarios

  @TC06
  Scenario: Check if admin is able to create user with valid inputs
    Given Admin creates request body with valid inputs
    When Admin sends HTTP POST request
    Then Admin receives Status code 200 OK with response body
    
  @TC07
  Scenario: Check if admin is able to create user with existing emailid
    Given Admin creates request body with existing emailid
    When Admin sends HTTP POST request
    Then Admin receives Status code 409 CONFLICT with message in response body

  @TC08
   Scenario: Check if Admin is able to create user with invalid end point
    Given Admin creates request with invalid end point
    When Admin sends HTTP POST request
    Then Admin receives 404  Not found Status Code in response body
    
   @TC09
Scenario: Check if admin able to create input with emailId has special characters
  Given User creates request body with emailid has special characters
  When Admin sends HTTP POST request
  Then User receives Status code 400 BAD REQUEST with message in response body

@TC10
Scenario: Check if admin able to create FirstName with numeric values
  Given User creates request body  with FirstName has numeric values
  When Admin sends HTTP POST request
  Then User receives Status code 400 BAD REQUEST with message in response body
  

 