Feature: To get user details by using userFirstName

  Background: Admin sets the Valid Basic Auth for Authorization

  @TC01
  Scenario: Get users details with valid endpoint
    Given user has access to endpoint
    When user sends a GET request with valid endpoint
    Then Admin receives 200 OK Status Code and should display only user with that particular first name in response body

  @TC02
  Scenario: Get users details with invalid endpoint
    Given user has access to endpoint
    When user sends a GET request with invalid endpoint
    Then Admin receives 404 Not Found Status Code in response body

  @TC03
  Scenario: Get users details with invalid userFirstName
    Given user has access to endpoint 
    When user sends a GET request with invalid userFirstName
    Then Admin receives 404 Not Found Status Code in response body

  @TC04
  Scenario: Get user details by userFirstName in numeric format
    Given user has access to endpoint
    When user sends a GET request with userFirstName in numeric values
    Then Admin receives 404 Not Found Status Code in response body

  @TC05
  Scenario: Get user details by userFirstName in alphanumeric format
    Given user has access to endpoint
    When user sends a GET request with userFirstName in numeric values
    Then Admin receives 404 Not Found Status Code in response body

  @TC06
  Scenario: Get user details by userFirstName in special characters format
    Given user has access to endpoint 
    When user sends a GET request with userFirstName in special characters
    Then Admin receives 404 Not Found Status Code in response body

  @TC07
  Scenario: Check if Admin is able to get user with invalid request type
    Given user has access to endpoint 
    When user sends a POST request with valid endpoint
    Then Admin receives 405 Method Not Allowed Status Code in response body

  @TC08
  Scenario: Get users details with out endpoint
    Given user has access without endpoint
    When user sends a GET request without endpoint
    Then Admin receives 404 Not Found Status Code in response body

  @TC09
  Scenario: Check if Admin is able to Get users with No Auth
    Given user has access to endpoint
     When user sends a GET request with valid endpoint without auth
    Then Admin receives 401 Unauthorized Status Code in response body
