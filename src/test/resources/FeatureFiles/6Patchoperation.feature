@patchoperation
Feature: PATCH Operation - Update User for Lastname

  @TC58
  Scenario: Check if admin is able to patch user with valid inputs
    Given Admin updates request body for UserLastname
    When Admin sends HTTP PATCH request
    Then user receives Status code 200 OK with response body