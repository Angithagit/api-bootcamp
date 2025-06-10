Feature: POST operation Create User API Validation

  Background: Authenticated user with Basic Authentication
    Given the system has one or more valid users

  Scenario Outline: Validate POST user creation from Excel
    When the user sends a POST request for "<TestCaseName>"
    Then the response status should match the expected from excel for POST user creation

    Examples:
      | TestCaseName                                |
      | ValidUserCreation                           |
      | InvalidNameSpecialChars                     |
      | PhoneNumberTooLong                          |
      | MissingEmailDomain                          |
      | MinimalValidFields                          |
    
    