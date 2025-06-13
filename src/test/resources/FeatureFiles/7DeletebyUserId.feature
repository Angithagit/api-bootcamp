@DeletebyUserIDscenario
Feature: Delete User by userId
Background: Admin sets the Valid Basic Auth for Authorization
  

  @TC59
  Scenario: Check if Admin is able to Delete User By invalid userId
    Given Admin sets DELETE request with invalid userId 
    When admin sends DELETE request
    Then admin receives status code 404  Not found

  @TC60
  Scenario: Check if Admin is able to Delete User ID which is already deleted
    Given Admin sets DELETE request with userId
    When admin sends DELETE request
    Then  admin receives status code 404  Not found

  @TC61
  Scenario: Check if Admin is able to Delete User By userId without authorizarion
    Given Admin sets DELETE request without auth
    When admin sends DELETE request
    Then admin receives status code 401 Unauthorized in response body

  @TC62
  Scenario: Check if Admin is able to Delete User By userId without endpoint
    Given Admin sets DELETE request without endpoint 
    When admin sends DELETE request
    Then  admin receives status code 404  Not found

  @TC63
  Scenario: Check if Admin is able to Delete User By userId with invalid endpoint
    Given Admin sets DELETE request with invalid end point
    When admin sends DELETE request
    Then admin receives status code 404  Not found
    
  @TC64
  Scenario: Check if Admin is able to Delete User By userId with invalid request type
    Given Admin sets POST request with userId and endpoint
    When admin sends HTTPS POST request
    Then  admin receives status code 405 Method Not Allowed Status Code in response body

  @TC65
  Scenario: Check if Admin is able to Delete User with userid in string format
    Given Admin sets DELETE request with userId in string format
    When admin sends DELETE request
    Then Admin receives status code 400  Bad Request in response body

  @TC66
  Scenario: Check if Admin is able to Delete User with userid in Alphanumeric values
    Given Admin sets DELETE request with userId in Alphanumeric values
    When admin sends DELETE request
    Then Admin receives status code 400  Bad Request in response body
    
     @TC67
  Scenario: Check if Admin is able to Delete User with userid in special characters format
    Given  Admin sets DELETE request with userId in special characters
    When admin sends DELETE request
    Then Admin receives status code 400  Bad Request in response body
    
    @TC68
  Scenario: Check if Admin is able to Delete User By userId with valid endpoint
    Given Admin sets DELETE request with userId and valid endpoint
    When admin sends DELETE request
    Then admin receives status code 200 OK with message in response body
