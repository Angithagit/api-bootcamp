Feature: PUT operation Create User API Validation

  Background: Authenticated user with Basic Authentication
    Given the system has one or more valid users

  Scenario Outline: Validate PUT user creation from Excel
    When the user sends a PUT request for "<TestCaseName>"
    Then the response status should match the expected from excel for PUT user creation

    Examples:
      | TestCaseName                                |
      | UpdateValidInputs                           |
      | UpdateWithInvalidUserId                     |
      | UpdateWithInvalidContact                    |
      | UpdateWithInvalidEmailId                    |
      | UpdateWithInvalidFirstName                  |
      | UpdateWithInvalidLastName                   |
      | UpdateWithInvalidplotNumber                 |
      | UpdateWithInvalidStreet                     |
      | UpdateWithInvalidState                      |
      | UpdateWithInvalidCountry                    |
      | UpdateWithInvalidZipcode                    |
    
    