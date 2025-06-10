    Feature: GET operation GET_userid API Validation

  Background: Authenticated user with Basic Authentication
    Given the system has one or more valid users
    
    
      Scenario Outline: Validate GET user with userid
    When the user sends a GET request for "<TestCaseName>"
    Then the response status should match the expected from excel for GET userid
    
        Examples:
      | TestCaseName                                |
      | ValidGetUserid                              |
      | NonExistUserid                              |
      | WrongFormatUserid                           |
      | MissingIdEndpoint                           |
      | StringUserid                                |
      | Negativeid                                  |
      | Zeroid                                      |
    