@Getallusers
Feature: Get all User details
Background: Admin sets the Valid Basic Auth for Authorization


  @TC01
  Scenario: Check if Admin is able to Get All User List with valid credentials
    Given Admin sets the GET request with valid endpoint
    When Admin sends GET Request with endpoint
    Then Admin receives 200 OK Status Code and should display all the users in response body

  @TC02
  Scenario: Check if Admin is able to Get All User List without endpoint
    Given Admin sets the GET request without endpoint
    When Admin sends GET Request without endpoint
    Then Admin receives 404 Not Found Status Code

  @TC03
  Scenario: Check if Admin is unable to Get All User List with invalid endpoint
    Given Admin sets the GET request with invalid endpoint
    When Admin sends GET Request with invalid endpoint
    Then Admin receives 404 Not Found in response body

  @TC04
  Scenario: Check if Admin is able to get all users with invalid request type
    Given Admin sets the POST request with valid endpoint 
    When Admin sends POST Request with endpoint
    Then Admin receives 405 Method Not Allowed

  @TC05
  Scenario: Check if Admin is able to Get All Users with No Auth
    Given Admin sets the GET request with valid endpoint and no authorization
    When Admin sends HTTPS Request with endpoint
    Then Admin receives 401 Unauthorized Status Code
