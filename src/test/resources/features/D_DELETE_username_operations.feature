    Feature: DELETE operation DELETE_username API Validation

  Background: Authenticated user with Basic Authentication
    Given the system has one or more valid users
    
    
      Scenario Outline: Validate DELETE user with username
    When the user sends a DELETE request for "<TestCaseName>"
    Then the response status should match the expected from excel for DELETE username
    
        Examples:
      | TestCaseName                                |
      | ExistingUserFirstname                       |
      | NonExistUserFirstname                       |
      | EmptyUserFirstname                          |
      | SpecialCharFirstname                        |
      | MultipleUsers                               |
      | SpaceExistFirstname                         |
    