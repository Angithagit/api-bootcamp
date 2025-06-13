@DeletebyUserFirstNamescenario
Feature: Delete User by UserFirstName
Background: Admin sets the Valid Basic Auth for Authorization
  

  @TC69
  Scenario: Check if Admin is able to Delete User By invalid userFirstName
    Given user sets DELETE request with invalid userFirstName 
    When user sends DELETE request
    Then user receives status code 404  Not found in response body

  @TC70
  Scenario: Check if Admin is able to Delete by userFirstName which is already deleted
    Given user sets DELETE request with userFirstName which is already deleted
    When user sends DELETE request
    Then  user receives status code 404  Not found in response body

  @TC71
  Scenario: Check if Admin is able to Delete by userFirstName without authorizarion
    Given user sets DELETE request without authorizarion
    When user sends DELETE request
    Then user receives 401 Unauthorized Status Code in response body

  @TC72
  Scenario: Check if Admin is able to Delete User By userFirstName without endpoint
    Given user sets DELETE request By userFirstName and without endpoint 
    When user sends DELETE request
    Then  user receives status code 404  Not found in response body

  @TC73
  Scenario: Check if Admin is able to Delete User By userFirstName with invalid endpoint
    Given user sets DELETE request By userFirstName with invalid end point
    When user sends DELETE request
    Then user receives status code 404  Not found in response body

  @TC74
  Scenario: Check if Admin is able to Delete User By userFirstName with invalid request type
    Given user sets POST request with userFirstName and endpoint
    When user sends POST request
    Then  user receives 405 Method Not Allowed Status Code in response body

  @TC75
  Scenario: Check if Admin is able to Delete User with userFirstName in numeric values
    Given user sets DELETE request with userFirstName in numeric values
    When user sends DELETE request
    Then user receives status code 404  Not found in response body

  @TC76
  Scenario: Check if Admin is able to Delete User with userFirstName in Alphanumeric values
    Given user sets DELETE request with userFirstName in Alphanumeric values
    When user sends DELETE request
    Then user receives status code 404  Not found in response body
    
     @TC77
  Scenario: Check if Admin is able to Delete User with userFirstName in special characters format
    Given  user sets DELETE request with userFirstName in special characters
    When user sends DELETE request
    Then user receives status code 404  Not found in response body
    
    @TC78
  Scenario: Check if Admin is able to Delete User By userFirstName with valid endpoint
    Given user sets DELETE request with userFirstName and valid endpoint
    When user sends DELETE request
    Then user receives status code 200 OK with message in response body
