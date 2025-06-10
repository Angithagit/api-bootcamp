Feature: Get all users

  Background: Authenticated user with Basic Authentication
  
  Scenario Outline: Validate all user-related API operations from Excel
    Given the system has one or more valid users
    When the user sends a GET request for  "<TestCaseName>"
    Then the response status should match the expected from excel

    Examples:
      | TestCaseName            |
      | ValidGetUsers           |
      | InvalidEndpoint         |
      | InvalidMethodPost       |
      | UnauthorizedAccess      |

