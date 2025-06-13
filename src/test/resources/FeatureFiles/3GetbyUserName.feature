@getbyusername
Feature: To get user details by using userFirstName

  Background: Admin sets the Valid Basic Auth for Authorization

  @TC11
  Scenario: Get users details with valid endpoint
    Given user has access to endpoint
    When user sends a GET request with valid endpoint
    Then Admin receives 200 OK Status Code and should display only user with that particular first name in response body

  @TC12
  Scenario: Get users details with invalid endpoint
    Given user has access to endpoint
    When user sends a GET request with invalid endpoint
    Then Admin receives 404 Not Found Status Code in response body

  @TC13
  Scenario: Get users details with invalid userFirstName
    Given user has access to endpoint 
    When user sends a GET request with invalid userFirstName
    Then Admin receives 404 Not Found Status Code in response body

  @TC14
  Scenario: Get user details by userFirstName in numeric format
    Given user has access to endpoint
    When user sends a GET request with userFirstName in numeric values
    Then Admin receives 404 Not Found Status Code in response body

  @TC15
  Scenario: Get user details by userFirstName in alphanumeric format
    Given user has access to endpoint
    When user sends a GET request with userFirstName in numeric values
    Then Admin receives 404 Not Found Status Code in response body

  @TC16
  Scenario: Get user details by userFirstName in special characters format
    Given user has access to endpoint 
    When user sends a GET request with userFirstName in special characters
    Then Admin receives 404 Not Found Status Code in response body

  @TC17
  Scenario: Check if Admin is able to get user with invalid request type
    Given user has access to endpoint 
    When user sends a POST request with valid endpoint
    Then Admin receives 405 Method Not Allowed Status Code in response body

  @TC18
  Scenario: Get users details with out endpoint
    Given user has access without endpoint
    When user sends a GET request without endpoint
    Then Admin receives 404 Not Found Status Code in response body

  @TC19
  Scenario: Check if Admin is able to Get users with No Auth
    Given user has access to endpoint
     When user sends a GET request with valid endpoint without auth
    Then Admin receives 401 Unauthorized Status Code in response body
