    Feature: GET operation GET_username API Validation

  Background: Authenticated user with Basic Authentication
    Given the system has one or more valid users
    
    
      Scenario Outline: Validate GET user with username
    When the user sends a GET_username request for "<TestCaseName>"
    Then the response status should match the expected from excel for GET username
    
        Examples:
      | TestCaseName                                |
      | ExistingUserFirstname                       |
      | NonExistUserFirstname                       |
      | ExistingUserLastname                        |
      | SpecialCharFirstname                        |
      | SpaceExistFirstname                         |
      | Numeric                                     |
      | NoEndpoint                                  |
      