    Feature: DELETE operation DELETE_userid API Validation

  Background: Authenticated user with Basic Authentication
    Given the system has one or more valid users
    
    
      Scenario Outline: Validate DELETE user with userid
    When the user sends a DELETE userid request for "<TestCaseName>"
    Then the response status should match the expected from excel for DELETE userid
    
        Examples:
      | TestCaseName                                |
      | ExistingUserId                              |
      | DeleteNonExistUserId                              |
      | EmptyUserId                                 |
      | SpecialCharUserId                           |
      | SpaceExistUserId                            |
    