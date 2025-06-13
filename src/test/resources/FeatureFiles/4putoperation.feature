@putscenario
Feature: To update user details by using put operation

  Background: Admin sets the Valid Basic Auth for Authorization

  @TC20
  Scenario: Check if Admin is able to update all user details with valid endpoint and without authorizarion
      Given User updates PUT request with no auth
       When User sends HTTP PUT request 
       Then User receives 401 Unauthorized Status Code in response body
   @TC21
  Scenario: Check if Admin is able to update all user details with valid end point and invalid authorization
    Given User updates PUT request with invalid base auth
    When User sends HTTP PUT request
    Then User receives 401 Unauthorized Status Code in response body
    
     @TC22
  Scenario: Check if Admin is able to update all user details with valid end point and Basic authorization
    Given User updates PUT request with valid end point
    When User sends HTTP PUT request
    Then User receives 200 OK Status Code in response body
    
     @TC23
  Scenario: Check if Admin is able to update all user details with invalid end point
    Given User updates PUT request with invalid end point
    When User sends HTTP PUT request
    Then User receives 404  Not found Status Code in response body
     
  @TC24
  Scenario: Check if admin able to update contact number with  less than 10 digits
   Given User updates request body  with contact number less than 10 digits
    When User sends HTTP PUT request
    Then Admin receives Status code 400 BAD REQUEST with  message "Phone Number is required and should contains 10 numeric values only" in response body
    
    @TC25
    Scenario:Check if admin able to update contact number with  more than 10 digits
    Given User updates request body  with contact number more than 10 digits
    When User sends HTTP PUT request
    Then Admin receives Status code 400 BAD REQUEST with  message "Phone Number is required and should contains 10 numeric values only" in response body
    
  @TC26
Scenario: Check if admin able to update input with emailId has special characters
  Given User updates request body with emailid has special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "User email Id is required" in response body

@TC27
Scenario: Check if admin able to update FirstName with numeric values
  Given User updates request body  with FirstName has numeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "User First Name is mandatory and should contains alphabets only" in response body

@TC28
Scenario: Check if admin able to update FirstName with alphanumeric values
  Given User updates request body  with FirstName has alphanumeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "User First Name is mandatory and should contains alphabets only" in response body

@TC29
Scenario: Check if admin able to update FirstName with special characters
  Given User updates request body  with FirstName contains special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "User First Name is mandatory and should contains alphabets only" in response body

@TC30
Scenario: Check if admin able to update LastName with alphanumeric values
  Given  User updates request body  with LastName has alphanumeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "User Last Name is mandatory and should contains alphabets only" in response body
@TC31
Scenario: Check if admin able to update LastName with special characters
  Given User updates request body  with LastName has special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "User Last Name is mandatory and should contains alphabets only" in response body

@TC32
Scenario: Check if admin able to update LastName with numeric values
  Given User updates request body  with LastName has numeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "User Last Name is mandatory and should contains alphabets only" in response body

@TC33
Scenario: Check if admin able to update plotnumber with string format
  Given User updates request body with plotNumber contains string
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "Plot number should contain alphaNumeric values only" in response body

@TC34
Scenario: Check if admin able to update plotnumber with special characters
  Given User updates request body with plotNumber has special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "Plot number should contain alphaNumeric values only" in response body

@TC35
Scenario: Check if admin able to update plotnumber with numeric values
  Given User updates request body with plotNumber has numeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "Plot number should contain alphaNumeric values only" in response body

@TC36
Scenario: Check if admin able to update street with special characters
  Given User updates request body with street has special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "Street should contain alphabet characters only" in response body

@TC37
Scenario: Check if admin able to update street with numeric values
  Given User updates request body with street has numeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "Street should contain alphabet characters only" in response body

@TC38
Scenario: Check if admin able to update street with alphanumeric values
  Given User updates request body with street has alphanumeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "Street should contain alphabet characters only" in response body

@TC39
Scenario: Check if admin able to update state with special characters
  Given User updates request body with state has special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "State should contain alphabet characters only" in response body

@TC40
Scenario: Check if admin able to update state with numeric values
  Given User updates request body with state has numeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "State should contain alphabet characters only" in response body

@TC41
Scenario: Check if admin able to update state with alphanumeric values
  Given User updates request body with state has alphanumeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "State should contain alphabet characters only" in response body

@TC42
Scenario: Check if admin able to update country with numeric values
  Given User updates request body with country has numeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "country should contain alphabet characters only" in response body

@TC43
Scenario: Check if admin able to update country with special characters
  Given User updates request body with country has special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "country should contain alphabet characters only" in response body

@TC44
Scenario: Check if admin able to update country with alphanumeric values
  Given User updates request body with country  has alphanumeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "country should contain alphabet characters only" in response body

@TC45
Scenario: Check if admin able to update zipcode with special characters
  Given User updates request body with zipcode has special characters
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "zipcode should contain numbers only" in response body

@TC46
Scenario: Check if admin able to update zipcode with string format
  Given User updates request body with zipcode has string values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "zipcode should contain numbers only" in response body

@TC47
Scenario: Check if admin able to update zipcode with alphanumeric values
  Given User updates request body with zipcode has alphanumeric values
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "zipcode should contain numbers only" in response body

@TC48
Scenario: Check if admin able to update with existing contact number
  Given User updates request body with existing contactNumber 
  When User sends HTTP PUT request
  Then User receives Status code 400 BAD REQUEST with message "Contact number is already in use by another user" in response body


 
    
    