@GetbyUserID
Feature: Get User details by userId
Background: Admin sets the Valid Basic Auth for Authorization
  

  @TC49
  Scenario: Check if Admin is able to Get User By invalid userId
    Given Admin sets a GET request with invalid userId
    When Admin sends GET request
    Then admin receives status code 404  Not found in response body

  
  @TC50
  Scenario: Check if Admin is able to Get User By userId without authorizarion
    Given Admin sets GET request without auth
    When Admin sends GET request
    Then admin receives 401 Unauthorized Status Code message in response body

  @TC51
  Scenario: Check if Admin is able to Get User By userId without endpoint
    Given Admin sets GET request without endpoint 
    When Admin sends GET request
    Then  admin receives status code 404  Not found in response body

  @TC52
  Scenario: Check if Admin is able to Get User By userId with invalid endpoint
    Given Admin sets GET request with invalid end point
    When Admin sends GET request
    Then admin receives status code 404  Not found in response body

  @TC53
  Scenario: Check if Admin is able to Get User By userId with invalid request type
    Given admin sets POST request with userId and endpoint
    When admin sends POST request
    Then  Admin receives 405 Method Not Allowed Status Code message in response body

  @TC54
  Scenario: Check if Admin is able to Get User with userid in string format
    Given Admin sets GET request with userId in string format
    When Admin sends GET request
    Then admin receives status code 400  Bad Request in response body

  @TC55
  Scenario: Check if Admin is able to Get User with userid in Alphanumeric values
    Given Admin sets GET request with userId in Alphanumeric values
    When Admin sends GET request
    Then admin receives status code 400  Bad Request in response body
    
     @TC56
  Scenario: Check if Admin is able to Get User with userid in special characters format
    Given  Admin sets GET request with userId in special characters
    When Admin sends GET request
    Then admin receives status code 404  Not found in response body
    
    @TC57
  Scenario: Check if Admin is able to Get User By userId with valid endpoint
    Given Admin sets GET request with userId and valid endpoint
    When Admin sends GET request
    Then Admin receives status code 200 OK in response body
