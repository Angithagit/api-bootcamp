Feature: To update user details by using put operation

  Background: Admin sets the Valid Basic Auth for Authorization

  @TC01
  Scenario: Check if Admin is able to update all user details with valid endpoint and without authorizarion
      Given User updates PUT request with no auth
       When User sends HTTP PUT request 
       Then User receives 401 Unauthorized Status Code in response body
   @TC02
  Scenario: Check if Admin is able to update all user details with valid end point and invalid authorization
    Given User updates PUT request with invalid base auth
    When User sends HTTP PUT request
    Then User receives 401 Unauthorized Status Code in response body
    
     @TC03
  Scenario: Check if Admin is able to update all user details with valid end point and Basic authorization
    Given User updates PUT request with valid end point
    When User sends HTTP PUT request
    Then User receives 200 OK Status Code in response body
    
  
  
